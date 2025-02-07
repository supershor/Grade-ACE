package com.om_tat_sat.grade_ace.newUiActivity

import android.content.DialogInterface
import android.content.Intent
import android.net.MailTo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.om_tat_sat.grade_ace.R
import com.om_tat_sat.grade_ace.newUiFrags.HomeFragmentNewUi
import com.om_tat_sat.grade_ace.newUiFrags.graphNewUiFrag
import com.om_tat_sat.grade_ace.newUiFrags.ogpaAllThreeNewUiFrag
import com.om_tat_sat.grade_ace.newUiFrags.overAllOgpaNewUiFrag
import com.om_tat_sat.grade_ace.newUiFrags.pyqNewUiFrag
import java.util.Locale
import java.util.Objects

class MainHomeScreen : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var bottomNav: BottomNavigationView
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val language = getSharedPreferences("app_language", MODE_PRIVATE).getInt("current_language", 0)
        if(language==1){
            changeLanguage("hi")
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_main_home_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bottomNav = findViewById(R.id.bottom_navigation_main_home)
        drawerLayout = findViewById(R.id.main)
        navigationView = findViewById(R.id.navigation_view)
        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser == null) {
            startActivity(Intent(this, SignInWithGoogle::class.java))
            finish()
        }
        val hm= HashMap<String, String>()
        hm["EMAIL"]=firebaseAuth.currentUser?.email.toString()
        if (language == 0) {
            hm["Language"]="English"
        } else if (language == 1) {
            hm["Language"]="Hindi"
        }
        FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(firebaseAuth.currentUser?.uid.toString())
            .child("Personal information")
            .updateChildren(hm as Map<String, Any>)
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_bug -> {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.setData(Uri.parse(MailTo.MAILTO_SCHEME))
                    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("supershor.cp@gmail.com"))
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Report bug on Grade ACE.")
                    intent.putExtra(
                        Intent.EXTRA_TEXT, """
     Hello 👋
     
     This is :-
     
     """.trimIndent() + firebaseAuth.currentUser!!
                            .uid + "\n" + "(It's your I'd kindly do not edit)" + "\n\nName:-\nPhone Number:-\nError:-"
                    )
                    startActivity(intent)
                }
                R.id.menu_change_email -> {
                    startActivity(Intent(this@MainHomeScreen, ChangeEmailActivity::class.java))
                }
                R.id.menu_change_password -> {
                    firebaseAuth.sendPasswordResetEmail(firebaseAuth.currentUser?.email.toString())
                        .addOnCompleteListener( { task: Task<Void?> ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this@MainHomeScreen,
                                    getString(R.string.Reset_link_sent),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@MainHomeScreen,
                                    Objects.requireNonNull<Exception?>(task.exception).message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                }
                R.id.menu_contact_owner -> {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.setData(Uri.parse(MailTo.MAILTO_SCHEME))
                    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("supershor.cp@gmail.com"))
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Contact owner of Grade ACE.")
                    intent.putExtra(
                        Intent.EXTRA_TEXT, """
     Hello 👋
     
     This is :-
     
     """.trimIndent() + firebaseAuth.currentUser!!
                            .uid + "\n" + "(It's your I'd kindly do not edit)" + "\n\nName:-\nPhone Number:-\nReason:-"
                    )
                    startActivity(intent)
                }
                R.id.menu_change_language -> {
                    val intent=Intent(this@MainHomeScreen,ChangeLanguage::class.java)
                    intent.putExtra("From","MainHomeScreen")
                    startActivity(intent)
                }
                R.id.menu_feedback -> {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.setData(Uri.parse(MailTo.MAILTO_SCHEME))
                    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("supershor.cp@gmail.com"))
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Contact owner of Grade ACE.")
                    intent.putExtra(
                        Intent.EXTRA_TEXT, """
     Hello 👋
     
     This is :-
     
     """.trimIndent() + firebaseAuth.currentUser!!
                            .uid + "\n" + "(It's your I'd kindly do not edit)" + "\n\nName:-\nPhone Number:-\nReason:-"
                    )
                    startActivity(intent)
                }
                R.id.menu_logout -> {
                    val builder = AlertDialog.Builder(this@MainHomeScreen)
                    builder.setCancelable(false)
                    builder.setTitle(getString(R.string.logout))
                        .setMessage(getString(R.string.sure_to_logout))
                        .setPositiveButton(getString(R.string.logout)) { dialog: DialogInterface?, which: Int ->
                            firebaseAuth.signOut()
                            startActivity(Intent(this@MainHomeScreen, SignInWithGoogle::class.java))
                            finishAffinity()
                        }
                        .setNegativeButton(
                            getString(R.string.cancel)
                        ) { dialog, which ->
                            dialog.dismiss()
                        }
                    builder.show()
                }

            }
            drawerLayout.closeDrawer(GravityCompat.START)
            false
        }


        navigationView.getHeaderView(0).findViewById<TextView>(R.id.nav_header_email).text=firebaseAuth.currentUser?.email
        refresh(navigationView.getHeaderView(0).findViewById<TextView>(R.id.nav_header_name))

        findViewById<ImageButton>(R.id.settings_button_main_home).setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        })

        drawerLayout.closeDrawer(GravityCompat.START)

        supportFragmentManager.beginTransaction().add(
            R.id.fragment_container_main_home,
            HomeFragmentNewUi()
        ).commit()



        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    findViewById<ImageButton>(R.id.settings_button_main_home).visibility = View.VISIBLE
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container_main_home,
                        HomeFragmentNewUi()
                    ).commit()
                    true
                }
                R.id.overall -> {
                    findViewById<ImageButton>(R.id.settings_button_main_home).visibility = View.GONE
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container_main_home,
                        overAllOgpaNewUiFrag()
                    ).commit()
                    true
                }
                R.id.graph -> {
                    findViewById<ImageButton>(R.id.settings_button_main_home).visibility = View.GONE
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container_main_home,
                        graphNewUiFrag()
                    ).commit()
                    true
                }
                R.id.pyq -> {
                    findViewById<ImageButton>(R.id.settings_button_main_home).visibility = View.GONE
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container_main_home,
                        pyqNewUiFrag()
                    ).commit()
                    true
                }
                R.id.ogpa -> {
                    findViewById<ImageButton>(R.id.settings_button_main_home).visibility = View.GONE
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fragment_container_main_home,
                        ogpaAllThreeNewUiFrag()
                    ).commit()
                    true
                }
                else -> false
            }
        }

        FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("VERSION").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //Log.e("main onDataChange: 295", snapshot.toString())
                val versionCode = snapshot.child("versionCode").value.toString()
                val versionName = snapshot.child("versionName").value.toString()
                if(versionCode!="20000005"){
                    AlertDialog.Builder(this@MainHomeScreen)
                        .setTitle(getString(R.string.new_version_available))
                        .setMessage(getString(R.string.new_version_available_message))
                        .setPositiveButton(getString(R.string.update)) { dialog, which ->
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse("https://play.google.com/store/apps/details?id=com.om_tat_sat.grade_ace")
                            startActivity(intent)
                            firebaseAuth.signOut()
                        }
                        .setNegativeButton(getString(R.string.cancel)) { dialog, which ->
                            dialog.dismiss()
                            firebaseAuth.signOut()
                            finishAffinity()
                        }
                        .show()

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun refresh(toset:TextView) {

        FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference(firebaseAuth.currentUser!!.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
//                    Log.e("main onDataChange: 329", snapshot.toString())
                    toset.text= snapshot.child("Personal information").child("NAME").value.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }
    fun changeLanguage(language: String?) {
        //Log.d("ChangeLanguage", "Changing language to: $language")
        val resources = this.resources
        val configuration = resources.configuration
        val locale = Locale(language)
        Locale.setDefault(locale)
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        //Log.d("ChangeLanguage", "Language changed successfully")
    }
}
