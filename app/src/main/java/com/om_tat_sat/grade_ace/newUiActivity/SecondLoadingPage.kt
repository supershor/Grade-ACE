package com.om_tat_sat.grade_ace.newUiActivity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.om_tat_sat.grade_ace.R
import com.om_tat_sat.grade_ace.web_policy_view
import java.util.Locale
import java.util.Objects

class SecondLoadingPage : AppCompatActivity() {

    private var issue: String? = null
    var comply: Boolean = false

    val mauth=FirebaseAuth.getInstance()
    val databaseReference=FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/").reference

    private lateinit var alreadyHaveAccountLogIn: TextView
    lateinit var signup_layout: LinearLayout
    lateinit var login_page: LinearLayout
    lateinit var signup_first_page_view_for_name: LinearLayout
    lateinit var signup_second_page_view_for_email_and_password: LinearLayout
    lateinit var name_information_sign_up_page_one: TextInputEditText
    private lateinit var email_information_sign_up_page: TextInputEditText
    private lateinit var password_information_sign_up_page: TextInputEditText
    private lateinit var confirm_password_information_sign_up_page: TextInputEditText
    lateinit var email_information_login_up_page_one: TextInputEditText
    lateinit var password_information_login_up_page_one: TextInputEditText
    lateinit var loading_page_sign_up_name_first_signup_then_save_name: AppCompatButton
    lateinit var save_sign_up_data_email_and_password_and_confirm_password: AppCompatButton
    lateinit var login_at_login_page: AppCompatButton
    lateinit var checkbox_agree_for_terms_and_condition_at_sign_up_page: CheckBox
    lateinit var privacy_policy_signup: TextView
    lateinit var privacy_policy_login: TextView
    lateinit var does_not_have_an_account: TextView
    lateinit var forgot_password_login_page_one: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val language = getSharedPreferences("app_language", MODE_PRIVATE).getInt("current_language", 0)
        if(language==1){
            changeLanguage("hi")
        }
        setContentView(R.layout.activity_second_loading_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        alreadyHaveAccountLogIn = findViewById(R.id.already_have_an_account_sign_up_page)
        does_not_have_an_account = findViewById(R.id.does_not_have_an_account)
        save_sign_up_data_email_and_password_and_confirm_password = findViewById(R.id.save_sign_up_data_email_and_password_and_confirm_password)
        privacy_policy_signup = findViewById(R.id.Privacy_policy_signup)
        privacy_policy_login = findViewById(R.id.privacy_policy_login)
        checkbox_agree_for_terms_and_condition_at_sign_up_page = findViewById(R.id.checkbox_agree_for_terms_and_condition_at_sign_up_page)
        signup_layout = findViewById(R.id.signup_layout)
        login_page = findViewById(R.id.login_page)
        forgot_password_login_page_one = findViewById(R.id.forgot_password_login_page_one)
        password_information_login_up_page_one = findViewById(R.id.password_information_login_up_page_one)
        login_at_login_page = findViewById(R.id.login_at_login_page)
        email_information_login_up_page_one = findViewById(R.id.email_information_login_up_page_one)
        email_information_sign_up_page = findViewById(R.id.email_information_sign_up_page)
        confirm_password_information_sign_up_page = findViewById(R.id.confirm_password_information_sign_up_page)
        password_information_sign_up_page = findViewById(R.id.password_information_sign_up_page)
        signup_first_page_view_for_name = findViewById(R.id.signup_first_page_view_for_name)
        signup_second_page_view_for_email_and_password = findViewById(R.id.signup_second_page_view_for_email_and_password)
        name_information_sign_up_page_one = findViewById(R.id.name_information_sign_up_page_one)
        loading_page_sign_up_name_first_signup_then_save_name = findViewById(R.id.loading_page_sign_up_name_first_signup_then_save_name)

        findViewById<TextView>(R.id.change_language).setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val intent=Intent(this@SecondLoadingPage,ChangeLanguage::class.java)
                intent.putExtra("From","SecondLoadingPage")
                startActivity(intent)
            }
        })

        privacy_policy_login.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(Intent(this@SecondLoadingPage, web_policy_view::class.java))
            }
        })
        privacy_policy_signup.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(Intent(this@SecondLoadingPage, web_policy_view::class.java))
            }
        })

        does_not_have_an_account.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                changeVisibiltiy(login_page,View.GONE)
                changeVisibiltiy(signup_second_page_view_for_email_and_password,View.GONE)
                changeVisibiltiy(signup_layout,View.VISIBLE)
                changeVisibiltiy(signup_first_page_view_for_name,View.VISIBLE)
            }
        })
        alreadyHaveAccountLogIn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                changeVisibiltiy(login_page,View.VISIBLE)
                changeVisibiltiy(signup_second_page_view_for_email_and_password,View.GONE)
                changeVisibiltiy(signup_layout,View.GONE)
                changeVisibiltiy(signup_first_page_view_for_name,View.GONE)
            }

        })
        save_sign_up_data_email_and_password_and_confirm_password.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if (comply) {
                    save()
                } else {
                    val alert = AlertDialog.Builder(this@SecondLoadingPage)
                    alert.setTitle(getString(R.string.I_comply))
                        .setMessage(getString(R.string.I_agree))
                    alert.setPositiveButton(
                        getString(R.string.I_Agree_sign_up)
                    ) { dialog: DialogInterface?, which: Int -> comply = true }.setNegativeButton(
                        getString(R.string.I_not_Agree_sign_up)
                    ) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
                    alert.show()
                }
            }
        })
        loading_page_sign_up_name_first_signup_then_save_name.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if(loading_page_sign_up_name_first_signup_then_save_name.text==getString(R.string.sign_up)){
                    loading_page_sign_up_name_first_signup_then_save_name.text=getString(R.string.save)
                    name_information_sign_up_page_one.visibility=View.VISIBLE
                }else{
                    if(name_information_sign_up_page_one.text?.isEmpty() == false && name_information_sign_up_page_one.text.toString().trim().isNotEmpty()
                    ){
                        changeVisibiltiy(signup_second_page_view_for_email_and_password,View.VISIBLE)
                        changeVisibiltiy(signup_first_page_view_for_name,View.GONE)
                    }else{
                        Toast.makeText(this@SecondLoadingPage,getString(R.string.enter_name),Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })

        login_at_login_page.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val email = email_information_login_up_page_one.text.toString()
                val password = password_information_login_up_page_one.text.toString()
                if(checkFields(email) && checkFields(password) && email.contains("@")){
                    mauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this@SecondLoadingPage) {task->
                        if(task.isSuccessful){
                            if(mauth.currentUser?.isEmailVerified==true){
//                                Log.e("onAuthentication","User email is verified")
                                startActivity(Intent(this@SecondLoadingPage,MainHomeScreen::class.java))
                                val language = getSharedPreferences("app_language", MODE_PRIVATE).getInt("current_language", 0)
                                val hm= HashMap<String, String>()
                                hm["EMAIL"]=email
                                if (language == 0) {
                                    hm["Language"]="English"
                                } else if (language == 1) {
                                    hm["Language"]="Hindi"
                                }
                                databaseReference.child(mauth.currentUser!!.uid.toString())
                                    .child("Personal information")
                                    .updateChildren(hm as Map<String, Any>)
                                finishAffinity()
                            }else{
                                Toast.makeText(this@SecondLoadingPage,getString(R.string.verification_email_sent_with_email_verification_request),Toast.LENGTH_SHORT).show()
//                                Log.e("onAuthentication","User email is not verified")
                                mauth.currentUser?.sendEmailVerification()?.addOnCompleteListener({task:Task<Void?>->
                                    if(task.isSuccessful){
//                                        Log.e("onAuthentication","Email sent")
                                        startActivity(Intent(this@SecondLoadingPage,
                                            EmailVerificationPage::class.java))
                                        finishAffinity()
                                    }else{
//                                        Log.e("onAuthentication","Email not sent")
//                                        Log.e("onAuthentication",task.exception.toString())
                                        Toast.makeText(this@SecondLoadingPage,getString(R.string.verification_email_failed_with_login_again_request),Toast.LENGTH_SHORT).show()
                                        mauth.signOut()
                                    }
                                })
                            }
                        }
                    }
                        .addOnFailureListener(this@SecondLoadingPage){task->
                            if (task.message?.contains("The user account has been disabled by an administrator.") == true) {
                                Toast.makeText(
                                    this@SecondLoadingPage,
                                    getString(R.string.try_with_another_email),
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@SecondLoadingPage,
                                    task.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
//                            Log.e("onComplete: >>>>>>>>>>>>>", task.toString())
                        }
                }else{
                    Toast.makeText(this@SecondLoadingPage,getString(R.string.enter_all_fields),Toast.LENGTH_SHORT).show()
                }
            }
        })
        forgot_password_login_page_one.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val editText = EditText(this@SecondLoadingPage)
                val alertDialog = AlertDialog.Builder(this@SecondLoadingPage)
                alertDialog.setView(editText)
                alertDialog.setCancelable(false)
                alertDialog.setTitle(getString(R.string.reset_password))
                    .setMessage(getString(R.string.enter_full_email_login))
                alertDialog.setPositiveButton(
                    getString(R.string.Send)
                ) { dialog: DialogInterface?, which: Int ->
                    if (editText.text == null || editText.text.toString()
                            .isEmpty() || !editText.text.toString()
                            .contains("@") || editText.text.toString().contains(" ")
                    ) {
                        Toast.makeText(
                            this@SecondLoadingPage,
                            getString(R.string.Invalid_email),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        mauth.sendPasswordResetEmail(editText.text.toString())
                            .addOnCompleteListener( { task: Task<Void?> ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            this@SecondLoadingPage,
                                            getString(R.string.Reset_link_sent),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            this@SecondLoadingPage,
                                            Objects.requireNonNull<Exception?>(task.exception).message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                    }
                }
                alertDialog.setNegativeButton(
                    getString(R.string.cancel)
                ) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
                alertDialog.show()
            }
        })
    }
    private fun checkFields(field: String): Boolean {
        return field.isNotEmpty()
    }
    private fun changeVisibiltiy(layout: LinearLayout,visibility: Int){
        layout.visibility=visibility
    }
    fun save() {
//        Log.d("SaveFunction", "save() called")
        if (check_fields()) {
//            Log.d("SaveFunction", "Field validation failed: $issue")
            Toast.makeText(this@SecondLoadingPage, issue, Toast.LENGTH_SHORT).show()
        } else {
            val email = email_information_sign_up_page.text.toString()
            val password = password_information_sign_up_page.text.toString()
//            Log.d("SaveFunction", "Attempting to create user with email: $email")

            mauth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val currentUser = mauth.currentUser
                        val userId = currentUser?.uid ?: ""
//                        Log.d("SaveFunction", "User created successfully with UID: $userId")

                        val userName = name_information_sign_up_page_one.text.toString()
//                        Log.d("SaveFunction", "Saving user name to database: $userName")

//                        hm["Languge"]="English"
                        val language = getSharedPreferences("app_language", MODE_PRIVATE).getInt("current_language", 0)
                        val hm= HashMap<String, String>()
                        hm["NAME"]=userName
                        hm["EMAIL"]=email
                        if (language == 0) {
                            hm["Language"]="English"
                        } else if (language == 1) {
                            hm["Language"]="Hindi"
                        }
                        databaseReference.child(userId)
                            .child("Personal information")
                            .setValue(hm)
                            .addOnCompleteListener { task1 ->
                                if (task1.isSuccessful) {
//                                    Log.d("SaveFunction", "User name saved successfully")

//                                    Log.d("SaveFunction", "Sending email verification to: $email")
                                    currentUser?.sendEmailVerification()
                                        ?.addOnCompleteListener { task2 ->
                                            if (task2.isSuccessful) {
//                                                Log.d("SaveFunction", "Email verification sent")
                                                Toast.makeText(
                                                    this@SecondLoadingPage,
                                                    getString(R.string.verification_email_sent),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                startActivity(
                                                    Intent(
                                                        this@SecondLoadingPage,
                                                        EmailVerificationPage::class.java
                                                    )
                                                )
                                                finishAffinity()
                                            } else {
//                                                val errorMessage = task2.exception?.message ?: "Unknown error"
//                                                Log.e("SaveFunction", "Failed to send email verification: $errorMessage")
                                                Toast.makeText(
                                                    this@SecondLoadingPage,
                                                    getString(R.string.verification_email_failed_with_login_again_request),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                mauth.signOut()
                                                changeVisibiltiy(signup_second_page_view_for_email_and_password, View.GONE)
                                                changeVisibiltiy(signup_first_page_view_for_name, View.GONE)
                                                changeVisibiltiy(signup_layout, View.GONE)
                                                changeVisibiltiy(login_page, View.VISIBLE)
                                            }
                                        }
                                } else {
                                    val errorMessage = task1.exception?.message ?: "Unknown error"
//                                    Log.e("SaveFunction", "Failed to save user name to database: $errorMessage")
                                    Toast.makeText(
                                        this@SecondLoadingPage,
                                        errorMessage,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        val errorMessage = task.exception?.message ?: "Unknown error"
//                        Log.e("SaveFunction", "User creation failed: $errorMessage")
                        Toast.makeText(
                            this@SecondLoadingPage,
                            errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun check_fields(): Boolean {
        //checking all input fields for valid input
        if (name_information_sign_up_page_one.text == null || confirm_password_information_sign_up_page.getText() == null || email_information_sign_up_page.getText() == null || password_information_sign_up_page.getText() == null || name_information_sign_up_page_one.getText()
                .toString().isEmpty() || email_information_sign_up_page.getText().toString().isEmpty() || password_information_sign_up_page.getText()
                .toString().isEmpty() || confirm_password_information_sign_up_page.getText().toString().isEmpty()
        ) {
            issue = getString(R.string.enter_all_fields)
            return true
        } else if (password_information_sign_up_page.text?.length!! <= 8) {
            issue = getString(R.string.password_greater_then_8)
            return true
        } else if (email_information_sign_up_page.text?.length!! <= 0) {
            issue = getString(R.string.enter_valid_email)
            return true
        } else if (name_information_sign_up_page_one.text?.length!! <= 1) {
            issue = getString(R.string.name_greater_then_1)
            return true
        } else if (confirm_password_information_sign_up_page.text?.length!! <= 8) {
            issue = getString(R.string.password_greater_then_8)
            return true
        } else if (!checkbox_agree_for_terms_and_condition_at_sign_up_page.isChecked) {
            issue = getString(R.string.I_agree_terms_and_condition)
            return true
        } else if (password_information_sign_up_page.text.toString().contains(" ")) {
            issue = getString(R.string.Invalid_password_spaces)
            return true
        } else if (password_information_sign_up_page.text.toString() != confirm_password_information_sign_up_page.getText().toString()) {
            issue = getString(R.string.both_password_must_be_same)
            return true
        } else if (!email_information_sign_up_page.text.toString().contains("@")) {
            issue = getString(R.string.Invalid_email)
            return true
        } else if (email_information_sign_up_page.text.toString().contains(" ")) {
            issue = getString(R.string.Invalid_email_spaces)
            return true
        }
        return false
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