package com.om_tat_sat.grade_ace.newUiActivity

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.ScrollBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.om_tat_sat.grade_ace.R

class PdfViewNewUi : AppCompatActivity() {
    var pdfView: PDFView? =null
    var firebaseStorage: FirebaseStorage?=null
    var firebaseDatabase:FirebaseDatabase?=null
    var databaseReference:DatabaseReference?=null
    var firebaseAuth:FirebaseAuth?=null
    private var fileName:String?=null
    private var namingConventions:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_pdf_view_new_ui)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val intent:Intent
        intent=getIntent()
        val keyfileName=intent.getStringExtra("KeyFileName")


        firebaseAuth= FirebaseAuth.getInstance()
        if (firebaseAuth?.currentUser ==null){
            startActivity(Intent(this@PdfViewNewUi,FirstLoadingPage::class.java))
        }
        firebaseDatabase = FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/")
        databaseReference = firebaseDatabase!!.reference.child("PYQ").child(keyfileName!!)


        databaseReference!!.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                fileName=snapshot.child("filename").value.toString()
                namingConventions=snapshot.child("namingConventions").value.toString()
//                Log.d("PdfViewNewUi", "onDataChange: $fileName")
//                Log.d("PdfViewNewUi", "onDataChange: $namingConventions")
                addPdf()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PdfViewNewUi,error.message,Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun addPdf(){
        pdfView = findViewById(R.id.pdfView)
        firebaseStorage = FirebaseStorage.getInstance()
        val storageRef = firebaseStorage!!.reference
        val pdfRef = fileName?.let { storageRef.child(it) }
        // Get the download URL
        pdfRef?.downloadUrl?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUrl = task.result
//                Log.d("Download URL", "File download URL: $downloadUrl")

                // Now you can use this URL to load the PDF
                pdfRef.getBytes(900000).addOnCompleteListener { byteTask ->
                    if (byteTask.isSuccessful) {
                        val byteArray: ByteArray = byteTask.result
                        pdfView?.fromBytes(byteArray)
//                            ?.pages(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40) // all pages are displayed by default
                            ?.enableSwipe(true)
                            ?.enableDoubletap(true)
                            ?.swipeVertical(true)
                            ?.defaultPage(1)
                            ?.showMinimap(false)
                            ?.enableAnnotationRendering(true)
                            ?.password(namingConventions)
                            ?.showPageWithAnimation(true)
                            ?.load()
                        val scrollBar=findViewById<ScrollBar>(R.id.scrollBar)
                        pdfView!!.setScrollBar(scrollBar)
                    } else {
//                        Log.e( "StorageException","Could not open resulting stream.",byteTask.exception)
                    }
                }
            } else {
//                Log.e("StorageException", "Could not get download URL.", task.exception)
            }
        }
    }
}