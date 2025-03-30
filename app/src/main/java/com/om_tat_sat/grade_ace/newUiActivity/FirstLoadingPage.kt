package com.om_tat_sat.grade_ace.newUiActivity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.om_tat_sat.grade_ace.R
import com.om_tat_sat.grade_ace.valueSaverNewUi.firebaseSingleton
import java.util.Locale

class FirstLoadingPage : AppCompatActivity() {
    val mauth=FirebaseAuth.getInstance()
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val language = getSharedPreferences("app_language", MODE_PRIVATE).getInt("current_language", 0)
        if(language==1){
            changeLanguage("hi")
        }
        setContentView(R.layout.activity_first_loading_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        check()
    }
    private fun check(){
        if(mauth.currentUser?.uid != null) {
            mauth.currentUser?.reload()?.addOnSuccessListener {
//                Log.e("AuthCheck", "User refresh done")
//                Log.d("AuthCheck", "${mauth.currentUser?.uid.toString()}")
//                Log.d("AuthCheck", "${mauth.currentUser?.isEmailVerified.toString()}")
                firebaseDatabase = FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/")
                databaseReference = firebaseDatabase!!.reference.child(mauth.currentUser!!.uid)
                firebaseSingleton.fetchData(mauth,databaseReference!!){ snapshot->
//                        Log.e("main onDataChange: ", snapshot.toString())
                    startActivity(Intent(this, MainHomeScreen::class.java))
                    finishAffinity()
                }
            }?.addOnFailureListener{
//                Log.e("AuthCheck", "Failed to refresh user.", it)
                Toast.makeText(this, getString(R.string.refresh_user_failed), Toast.LENGTH_SHORT).show()
                mauth.signOut()
                startActivity(Intent(this, SignInWithGoogle::class.java))
                finishAffinity()
            }
        } else {
//            Log.d("AuthCheck", "User is not LoggedIn.")
            startActivity(Intent(this, SignInWithGoogle::class.java))
            finishAffinity()
        }
    }
    fun changeLanguage(language: String?) {
//        Log.d("ChangeLanguage", "Changing language to: $language")
        val resources = this.resources
        val configuration = resources.configuration
        val locale = Locale(language)
        Locale.setDefault(locale)
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
//        Log.d("ChangeLanguage", "Language changed successfully")
    }
}
