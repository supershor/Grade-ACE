package com.om_tat_sat.grade_ace.newUiActivity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.rive.runtime.kotlin.RiveAnimationView
import app.rive.runtime.kotlin.core.Rive
import com.airbnb.lottie.LottieAnimationView
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.ScrollBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.view.View
import com.google.firebase.storage.FirebaseStorage
import com.om_tat_sat.grade_ace.R
import java.io.File

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
        Rive.init(this)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_pdf_view_new_ui)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val intent:Intent
        intent=getIntent()
        val keyfileName=intent.getStringExtra("KeyFileName")
        val keySem=intent.getStringExtra("keySem")
        val keyCourse=intent.getStringExtra("KeyCourse")


        firebaseAuth= FirebaseAuth.getInstance()
        if (firebaseAuth?.currentUser ==null){
            startActivity(Intent(this@PdfViewNewUi,FirstLoadingPage::class.java))
        }
        firebaseDatabase = FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/")
        databaseReference = firebaseDatabase!!.reference.child("SemWisePyq").child("$keySem").child(keyCourse!!).child("PYQ").child(keyfileName!!)
        findViewById<TextView>(R.id.downloadPercentage).text="Downloading"

        databaseReference!!.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                fileName=snapshot.child("filename").value.toString()
                namingConventions=snapshot.child("namingConventions").value.toString()
                findViewById<TextView>(R.id.downloadPercentage).text="Download Started"
                findViewById<RiveAnimationView>(R.id.pdfLoadingLottie).controller.setBooleanState("State machine 1","Downloading",true)
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

        findViewById<RiveAnimationView>(R.id.pdfLoadingLottie).controller.setNumberState("State machine 1","Progress",(0).toString().toFloat())


        // Get the download URL
        pdfRef?.downloadUrl?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUrl = task.result
                Log.d("PDFDownload", "Download URL: $downloadUrl")
                val tempFile = File.createTempFile("temp_pdf", ".pdf")
                pdfRef.getFile(tempFile)
                    .addOnProgressListener { progressTask ->
                        val progress = (100.0 * progressTask.bytesTransferred / progressTask.totalByteCount).toInt()
                        if(progress==100){
                            findViewById<RiveAnimationView>(R.id.pdfLoadingLottie).controller.setNumberState("State machine 1","Progress",(progress).toString().toFloat())
                            Handler().postDelayed(Runnable{
                                findViewById<RiveAnimationView>(R.id.pdfLoadingLottie).visibility=android.view.View.GONE
                            },1000)
//                            findViewById<LottieAnimationView>(R.id.pdfLoadingLottie).visibility=android.view.View.GONE
                            findViewById<TextView>(R.id.downloadPercentage).visibility=android.view.View.GONE
                        }else{
                            findViewById<RiveAnimationView>(R.id.pdfLoadingLottie).controller.setNumberState("State machine 1","Progress",(progress).toString().toFloat())
                            Log.d("PDFDownload", "Download progress: ${(progress).toString().toFloat()}%")
                            findViewById<TextView>(R.id.downloadPercentage).text="${progress}%${System.lineSeparator()}Download Completed"
                        }
                        Log.d("PDFDownload", "Download progress: $progress%")
                    }
                    .addOnSuccessListener {
                        Log.d("PDFDownload", "File downloaded successfully.")
                        val byteArray = tempFile.readBytes()
                        pdfView?.fromBytes(byteArray)
                            ?.enableSwipe(true)
                            ?.enableDoubletap(true)
                            ?.swipeVertical(true)
                            ?.defaultPage(1)
                            ?.showMinimap(false)
                            ?.enableAnnotationRendering(true)
                            ?.password(namingConventions)
                            ?.showPageWithAnimation(true)
                            ?.load()
                        val scrollBar = findViewById<ScrollBar>(R.id.scrollBar)
                        pdfView!!.setScrollBar(scrollBar)
                    }
                    .addOnFailureListener { exception ->
                        Log.e("PDFDownload", "Download failed: ${exception.message}")
                        Toast.makeText(this@PdfViewNewUi, "Download failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                        finish()
                    }
            } else {
                Log.e("PDFDownload", "Failed to get download URL: ${task.exception?.message}")
                Toast.makeText(this@PdfViewNewUi, "Download Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }
}