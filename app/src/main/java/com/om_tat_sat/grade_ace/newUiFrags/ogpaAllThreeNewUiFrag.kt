package com.om_tat_sat.grade_ace.newUiFrags

import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.om_tat_sat.grade_ace.R
import com.om_tat_sat.grade_ace.Recycler.Item
import com.om_tat_sat.grade_ace.Recycler.Recyclerview_for_OGPA_SHOWING
import com.om_tat_sat.grade_ace.newUiActivity.MainHomeScreen
import com.om_tat_sat.grade_ace.newUiActivity.SignInWithGoogle
import com.om_tat_sat.grade_ace.valueSaverNewUi.firebaseSingleton

class ogpaAllThreeNewUiFrag : Fragment() {

    var firebaseAuth: FirebaseAuth? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var name: EditText? = null
    var arrayList1: java.util.ArrayList<String>? = null
    var arrayList_ogpa1: java.util.ArrayList<Item>? = null
    var name_sem_arr1: java.util.HashMap<String, String>? = null
    var recyclerView1: RecyclerView? = null

    var arrayList2: java.util.ArrayList<String>? = null
    var arrayList_ogpa2: java.util.ArrayList<Item>? = null
    var name_sem_arr2: java.util.HashMap<String, String>? = null
    var recyclerView2: RecyclerView? = null

    var arrayList3: java.util.ArrayList<String>? = null
    var arrayList_ogpa3: java.util.ArrayList<Item>? = null
    var name_sem_arr3: java.util.HashMap<String, String>? = null
    var recyclerView3: RecyclerView? = null

    var app_language: SharedPreferences? = null
    var language: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ogpa_all_three_new_ui, container, false)
        val recyclerView1 = view.findViewById<RecyclerView>(R.id.recyclerview_agriculture_ogpa_all_three)
        recyclerView1.layoutManager = LinearLayoutManager(activity)

        val recyclerView2 = view.findViewById<RecyclerView>(R.id.recyclerview_hoerticulture_ogpa_all_three)
        recyclerView2.layoutManager = LinearLayoutManager(activity)

        val recyclerView3 = view.findViewById<RecyclerView>(R.id.recyclerview_btech_ogpa_all_three)
        recyclerView3.layoutManager = LinearLayoutManager(activity)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(context, MainHomeScreen::class.java))
                requireActivity().finishAffinity()
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth?.getCurrentUser() == null) {
            startActivity(Intent(context, SignInWithGoogle::class.java))
        }
        firebaseDatabase = FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/")
        arrayList1 = ArrayList<String>()
        name_sem_arr1 = HashMap<String, String>()
        arrayList_ogpa1 = ArrayList<Item>()
        arrayList2 = java.util.ArrayList<String>()
        name_sem_arr2 = java.util.HashMap<String, String>()
        arrayList_ogpa2 = java.util.ArrayList<Item>()
        arrayList3 = java.util.ArrayList<String>()
        name_sem_arr3 = java.util.HashMap<String, String>()
        arrayList_ogpa3 = java.util.ArrayList<Item>()
        recyclerView1 = view.findViewById<RecyclerView>(R.id.recyclerview_agriculture_ogpa_all_three)
        recyclerView2 = view.findViewById<RecyclerView>(R.id.recyclerview_hoerticulture_ogpa_all_three)
        recyclerView3 = view.findViewById<RecyclerView>(R.id.recyclerview_btech_ogpa_all_three)
        refresh()
    }
    private fun refresh() {
        firebaseDatabase = FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/")
        databaseReference = firebaseDatabase!!.reference.child(firebaseAuth!!.currentUser!!.uid)
        firebaseSingleton.fetchData(firebaseAuth!!,databaseReference!!){snapshot->
//            Log.e("main onDataChange: ", snapshot.toString())
            if (snapshot?.value != null) {
                arrayList_ogpa1?.clear()
                arrayList_ogpa2?.clear()
                arrayList_ogpa3?.clear()
                addElements(snapshot.child("OGPA"), arrayList1!!,name_sem_arr1!!,arrayList_ogpa1!!,recyclerView1!!,"AGRICULTURE")
                addElements(snapshot.child("OGPA_HORTICULTURE"),arrayList2!!,name_sem_arr2!!,arrayList_ogpa2!!,recyclerView2!!,"HORTICULTURE")
                addElements(snapshot.child("OGPA_BTECH_AGRICULTURE"),arrayList3!!,name_sem_arr3!!,arrayList_ogpa3!!,recyclerView3!!,"BTECH")
            }
        }
    }
    private fun addElements(
        snapshot: DataSnapshot,
        arrayList: java.util.ArrayList<String>,
        name_sem_arr: java.util.HashMap<String, String>,
        arrayList_ogpa: java.util.ArrayList<Item>,
        recyclerView: RecyclerView,
        ogpaType: String
    ) {
//        Log.e("addElements", "Starting to add elements for OGPA type: $ogpaType")
        for (dataSnapshot in snapshot.children) {
//            Log.e("addElements", "DataSnapshot: $dataSnapshot")

            val name = dataSnapshot.child("NAME").value.toString()
            val sem = dataSnapshot.child("SEM").value.toString()
            val ogpa = dataSnapshot.child("OGPA").value.toString()

            name_sem_arr[name] = name_sem_arr.getOrDefault(name, "") + "_$sem"

//            Log.e("addElements", "name_sem_arr updated: $name = ${name_sem_arr[name]}")

            arrayList_ogpa.add(
                Item(
                    name,
                    ogpa,
                    sem,
                    ogpaType
                )
            )

//            Log.e("addElements", "Added to arrayList_ogpa: NAME=$name, OGPA=$ogpa, SEM=$sem, OGPA_TYPE=$ogpaType")
        }

        val recyclerview = Recyclerview_for_OGPA_SHOWING(
            arrayList_ogpa,
            context
        )

        recyclerView.adapter = recyclerview

//        Log.e("addElements", "RecyclerView adapter set for OGPA type: $ogpaType")
//        Log.e("addElements", "Finished adding elements for OGPA type: $ogpaType")
    }
    override fun onResume() {
        super.onResume()
        refresh()
    }
}