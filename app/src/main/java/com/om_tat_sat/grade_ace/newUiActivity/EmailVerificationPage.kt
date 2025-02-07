package com.om_tat_sat.grade_ace.newUiActivity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.om_tat_sat.grade_ace.R

class EmailVerificationPage : AppCompatActivity() {
    val mauth= FirebaseAuth.getInstance()
    lateinit var linkVerification:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_email_verification_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<TextView>(R.id.logout_email_verification_page).setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                mauth.signOut()
                startActivity(Intent(this@EmailVerificationPage, SignInWithGoogle::class.java))
                finishAffinity()
            }
        })

        linkVerification=findViewById<TextView>(R.id.resend_link_email_verification_page)

        findViewById<TextView>(R.id.emailAddress).text=mauth.currentUser?.email

        linkVerification.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                if(linkVerification.text==getString(R.string.resend_link)){
                    mauth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                        Toast.makeText(this@EmailVerificationPage, getString(R.string.verification_email_sent), Toast.LENGTH_SHORT).show()
                        blockVerificationLink(30)

                    }?.addOnFailureListener {
                        Toast.makeText(this@EmailVerificationPage, getString(R.string.verification_email_failed), Toast.LENGTH_SHORT).show()
                    }
                }

            }
        })
        keepChecking()
    }
    private fun blockVerificationLink(sec:Int){
        if(sec==0){
            linkVerification.isEnabled=true
            linkVerification.text=getString(R.string.resend_link)
        }
        else {
            linkVerification.isEnabled = false
            linkVerification.text = "${getString(R.string.resend_link)} $sec"
            Handler().postDelayed(Runnable {
                blockVerificationLink(sec - 1)
            }, 1000)
        }
    }
    private fun keepChecking(){
        mauth.currentUser?.reload()?.addOnSuccessListener {
            if(mauth.currentUser?.isEmailVerified==true){
//                Log.d("TAG", "Email Verified")
                startActivity(Intent(this@EmailVerificationPage,MainHomeScreen::class.java))
                finishAffinity()
            } else {
                Handler().postDelayed(Runnable {
                    keepChecking()
                },3000)
            }
        }?.addOnFailureListener{
            Handler().postDelayed(Runnable {
                keepChecking()
            },1500)
        }
    }
}