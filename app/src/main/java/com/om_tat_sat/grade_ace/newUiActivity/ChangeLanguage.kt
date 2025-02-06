package com.om_tat_sat.grade_ace.newUiActivity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.om_tat_sat.grade_ace.R
import java.util.Locale

class ChangeLanguage : AppCompatActivity() {
    var current_language: TextView?=null
    var from: String? = null
    var language: Int = 0
    var spinner: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_change_language)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val intent = intent
        from = intent.getStringExtra("From")

//        Log.d("ChangeLanguage", "From: $from")

        // initializing
        language = getSharedPreferences("app_language", MODE_PRIVATE).getInt("current_language", 0)
        current_language = findViewById(R.id.current_language)

        if (language == 0) {
            current_language?.append(getString(R.string.english))
        } else if (language == 1) {
            current_language?.append(getString(R.string.hindi))
        }
        // 0=english
        // 1=hindi
//        Log.d("ChangeLanguage", "Current language: $language")

        spinner=findViewById(R.id.language_spinner)
        spinner?.setSelection(language)

        findViewById<AppCompatButton>(R.id.button_save).setOnClickListener {
//            Log.d("ChangeLanguage", "Save button clicked")
            changeLanguage()
            switch()
        }
        findViewById<AppCompatButton>(R.id.button_cancel).setOnClickListener {
//            Log.d("ChangeLanguage", "Cancel button clicked")
            switch_no_change()
        }
    }

    private fun changeLanguage() {
        val newLanguage = spinner?.selectedItemPosition
//        Log.d("ChangeLanguage", "New language selected: $newLanguage")
        if (newLanguage == 0) {
            val editor: SharedPreferences.Editor = getSharedPreferences("app_language", MODE_PRIVATE).edit()
            editor.putInt("current_language", 0)
            editor.apply()
//            Log.d("ChangeLanguage", "Language set to English")
            changeLanguage("en")
        } else if (newLanguage == 1) {
            val editor: SharedPreferences.Editor = getSharedPreferences("app_language", MODE_PRIVATE).edit()
            editor.putInt("current_language", 1)
            editor.apply()
//            Log.d("ChangeLanguage", "Language set to Hindi")
            changeLanguage("hi")
        }
        switch()
    }

    private fun switch_no_change() {
//        Log.d("ChangeLanguage", "No changes made, finishing activity")
        finish()
    }

    private fun switch() {
//        Log.d("ChangeLanguage", "Switching activity from: $from")
        when (from) {
            "MainHomeScreen" -> {
                startActivity(Intent(this@ChangeLanguage, MainHomeScreen::class.java))
                finishAffinity()
            }
            "SecondLoadingPage" -> {
                startActivity(Intent(this@ChangeLanguage, SecondLoadingPage::class.java))
                finishAffinity()
            }
            "FirstLoadingPage" -> {
                startActivity(Intent(this@ChangeLanguage, FirstLoadingPage::class.java))
                finishAffinity()
            }
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
