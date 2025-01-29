package com.om_tat_sat.grade_ace

import android.media.tv.TsResponse
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import java.util.Objects

class FirstLoadingPage : AppCompatActivity() {
    val mauth=FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_first_loading_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        mauth.signInWithEmailAndPassword("supershor.cp@gmail.com","ashish72240").addOnSuccessListener {
//            Log.d("AuthCheck", "User is authenticated and email is verified.")
//            // TODO start the activity
//        }.addOnFailureListener{
//            Log.e("AuthCheck", "Failed to authenticate user.", it)
//        }

        if(mauth.currentUser?.uid != null) {
            mauth.currentUser?.reload()?.addOnSuccessListener {
                Log.e("AuthCheck", "User refresh done")
                Log.d("AuthCheck", "${mauth.currentUser?.uid.toString()}")
                Log.d("AuthCheck", "${mauth.currentUser?.isEmailVerified.toString()}")
                if(mauth.currentUser?.isEmailVerified==true){
                    // TODO start the activity
                } else {
                    mauth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                        Log.d("AuthCheck", "Verification email sent successfully.")
                        Toast.makeText(this, "Verification email sent successfully.", Toast.LENGTH_SHORT).show()
                        Handler().postDelayed(Runnable {
                            Toast.makeText(this, "Please verify your email.", Toast.LENGTH_SHORT).show()
                        },3000)
                    }?.addOnFailureListener {
                        Log.e("AuthCheck", "Failed to send verification email.", it)
                        Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
                    }
                    Log.d("AuthCheck", "User is authenticated but email is not verified.")
                    // TODO go to verification page
                }
            }?.addOnFailureListener{
                Log.e("AuthCheck", "Failed to refresh user.", it)
                // TODO go to login page
            }
        } else {
            Log.d("AuthCheck", "User is not LoggedIn.")
            // TODO go to login page
        }


    }
}
