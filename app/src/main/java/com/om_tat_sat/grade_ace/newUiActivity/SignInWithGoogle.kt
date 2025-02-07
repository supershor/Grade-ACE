package com.om_tat_sat.grade_ace.newUiActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.common.SignInButton
import com.om_tat_sat.grade_ace.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.shobhitpuri.custombuttons.GoogleSignInButton

class SignInWithGoogle : AppCompatActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in_with_google)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<TextView>(R.id.change_language).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(this@SignInWithGoogle, ChangeLanguage::class.java)
                intent.putExtra("From", "SecondLoadingPage")
                startActivity(intent)
            }
        })

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        Log.d(TAG, "FirebaseAuth initialized")

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id_new))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this@SignInWithGoogle, gso)
        Log.d(TAG, "GoogleSignInClient configured")

        findViewById<GoogleSignInButton>(R.id.signInWithGoogle).setOnClickListener {
            Log.d(TAG, "Sign in button clicked")
            signIn()
        }
    }

    private fun signIn() {
        Log.d(TAG, "Starting sign-in intent")
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "Sign-in result received")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "Google Sign-In successful, ID Token: ${account.idToken}")
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.e(TAG, "Google Sign-In failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        Log.d(TAG, "Authenticating with Firebase using Google ID Token")
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Firebase authentication successful")
                    val user = auth.currentUser
                    user?.let {
                        val uid = user.uid
                        val email = user.email
                        val name = user.displayName

                        Log.d(TAG, "User details: UID: $uid, Email: $email, Name: $name")

                        // Save data to Firebase
                        val databaseReference = FirebaseDatabase.getInstance().reference
                        val hm = HashMap<String, String>()
                        hm["EMAIL"] = email.toString()
                        hm["NAME"] = name.toString()
                        val language = getSharedPreferences("app_language", MODE_PRIVATE).getInt("current_language", 0)
                        if (language == 0) {
                            hm["Language"] = "English"
                        } else if (language == 1) {
                            hm["Language"] = "Hindi"
                        }
                        databaseReference.child(uid).child("Personal information").updateChildren(hm as Map<String, Any>)
                        Log.d(TAG, "User data saved to Firebase")

                        // Start the MainHomeScreen activity
                        startActivity(Intent(this@SignInWithGoogle, MainHomeScreen::class.java))
                        Log.d(TAG, "MainHomeScreen activity started")
                        finishAffinity()
                    }
                } else {
                    Log.e(TAG, "Firebase authentication failed", task.exception)
                }
            }
    }

    companion object {
        private const val TAG = "SignInWithGoogle"
        private const val RC_SIGN_IN = 9001
    }
}
