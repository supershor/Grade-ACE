package com.om_tat_sat.grade_ace.newUiActivity

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.om_tat_sat.grade_ace.R
import com.om_tat_sat.grade_ace.newUiFrags.HomeFragmentNewUi
import com.om_tat_sat.grade_ace.newUiFrags.graphNewUiFrag
import com.om_tat_sat.grade_ace.newUiFrags.ogpaAllThreeNewUiFrag
import com.om_tat_sat.grade_ace.newUiFrags.overAllOgpaNewUiFrag
import com.om_tat_sat.grade_ace.newUiFrags.pyqNewUiFrag

class MainHomeScreen : AppCompatActivity() {
    lateinit var bottomNav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_main_home_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bottomNav = findViewById(R.id.bottom_navigation_main_home)
        supportFragmentManager.beginTransaction().add(
            R.id.fragment_container_main_home,
            HomeFragmentNewUi()
        ).commit()

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container_main_home,
                        HomeFragmentNewUi()
                    ).commit()
                    true
                }
                R.id.overall -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container_main_home,
                        overAllOgpaNewUiFrag()
                    ).commit()
                    true
                }
                R.id.graph -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container_main_home,
                        graphNewUiFrag()
                    ).commit()
                    true
                }
                R.id.pyq -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container_main_home,
                        pyqNewUiFrag()
                    ).commit()
                    true
                }
                R.id.ogpa -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container_main_home,
                        ogpaAllThreeNewUiFrag()
                    ).commit()
                    true
                }
                else -> false
            }
        }
    }
}
