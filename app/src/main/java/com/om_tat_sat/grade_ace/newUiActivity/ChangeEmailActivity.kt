package com.om_tat_sat.grade_ace.newUiActivity

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.om_tat_sat.grade_ace.R

class ChangeEmailActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var currentPasswordEditText: TextInputEditText
    private lateinit var newEmailEditText: TextInputEditText
    private lateinit var current_email: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_email)

        firebaseAuth = FirebaseAuth.getInstance()
        currentPasswordEditText = findViewById(R.id.edit_text_current_password)
        newEmailEditText = findViewById(R.id.edit_text_new_email)
        val changeEmailButton: AppCompatButton = findViewById(R.id.button_change_email)
        val cancelButton: AppCompatButton = findViewById(R.id.button_cancel)

        current_email = findViewById(R.id.current_email)
        current_email.append(firebaseAuth.currentUser?.email.toString())

        val user: FirebaseUser? = firebaseAuth.currentUser
        user?.let {
//            Log.d("ChangeEmailActivity", "Current email: ${it.email}")
        }

        changeEmailButton.setOnClickListener {
            val newEmail = newEmailEditText.text.toString().trim()
            val currentPassword = currentPasswordEditText.text.toString().trim()
//            Log.d("ChangeEmailActivity", "Change button clicked with new email: $newEmail and current password")
            if (newEmail.isNotEmpty() && currentPassword.isNotEmpty()) {
                reauthenticateAndChangeEmail(newEmail, currentPassword)
            } else {
                Toast.makeText(this, "Please enter your current password and new email address", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
//            Log.d("ChangeEmailActivity", "Cancel button clicked")
            finish()
        }
    }

    private fun reauthenticateAndChangeEmail(newEmail: String, currentPassword: String) {
        val user: FirebaseUser? = firebaseAuth.currentUser
        user?.let {
            val credential = EmailAuthProvider.getCredential(firebaseAuth.currentUser?.email.toString(), currentPassword)
//            Log.d("ChangeEmailActivity", "Reauthenticating with current email: ${firebaseAuth.currentUser?.email.toString()} and current password")

            it.reauthenticate(credential)
                .addOnCompleteListener { authTask ->
                    if (authTask.isSuccessful) {
//                        Log.d("ChangeEmailActivity", "Reauthentication successful")
                        user.updateEmail(newEmail)
                            .addOnCompleteListener { emailTask ->
                                if (emailTask.isSuccessful) {
//                                    Log.d("ChangeEmailActivity", "Email update successful to: $newEmail")
                                    Toast.makeText(
                                        this@ChangeEmailActivity,
                                        getString(R.string.email_updated),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    startActivity(Intent(this@ChangeEmailActivity, MainHomeScreen::class.java))
                                    finishAffinity()
                                } else {
//                                    Log.e("ChangeEmailActivity", "Email update failed: ${emailTask.exception?.message}")
                                    Toast.makeText(
                                        this@ChangeEmailActivity,
                                        emailTask.exception?.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
//                        Log.e("ChangeEmailActivity", "Reauthentication failed: ${authTask.exception?.message}")
                        Toast.makeText(
                            this@ChangeEmailActivity,
                            authTask.exception?.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } ?: run {
//            Log.e("ChangeEmailActivity", "No authenticated user found")
            Toast.makeText(this, "No authenticated user found", Toast.LENGTH_SHORT).show()
        }
    }
}
