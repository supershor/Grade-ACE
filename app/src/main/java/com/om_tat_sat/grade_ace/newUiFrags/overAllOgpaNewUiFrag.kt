package com.om_tat_sat.grade_ace.newUiFrags

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.om_tat_sat.grade_ace.Recycler.Recyclerview_for_overall_OGPA
import com.om_tat_sat.grade_ace.data_holders.ogpa_holder
import com.om_tat_sat.grade_ace.newUiActivity.SecondLoadingPage
import com.om_tat_sat.grade_ace.valueSaverNewUi.firebaseSingleton

class overAllOgpaNewUiFrag : Fragment() {
    var firebaseAuth: FirebaseAuth? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null

    var hashmap_ogpa1: java.util.HashMap<String, java.util.ArrayList<ogpa_holder>>? = null
    var arrayList1: java.util.ArrayList<java.util.ArrayList<ogpa_holder>>? = null
    lateinit var recyclerView1: RecyclerView

    var hashmap_ogpa2: java.util.HashMap<String, java.util.ArrayList<ogpa_holder>>? = null
    var arrayList2: java.util.ArrayList<java.util.ArrayList<ogpa_holder>>? = null
    lateinit var recyclerView2: RecyclerView

    var hashmap_ogpa3: java.util.HashMap<String, java.util.ArrayList<ogpa_holder>>? = null
    var arrayList3: java.util.ArrayList<java.util.ArrayList<ogpa_holder>>? = null
    lateinit var recyclerView3: RecyclerView


    var sharedPreferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = LayoutInflater.from(context).inflate(
            R.layout.fragment_ogpa_new_ui,
            container,
            false
        )
        val recyclerView1 = view.findViewById<RecyclerView>(R.id.recyclerview_agriculture_ogpa)
        recyclerView1.layoutManager = LinearLayoutManager(activity)

        val recyclerView2 = view.findViewById<RecyclerView>(R.id.recyclerview_hoticulture_ogpa)
        recyclerView2.layoutManager = LinearLayoutManager(activity)

        val recyclerView3 = view.findViewById<RecyclerView>(R.id.recyclerview_btech_ogpa)
        recyclerView3.layoutManager = LinearLayoutManager(activity)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth?.getCurrentUser() == null) {
            startActivity(Intent(context, SecondLoadingPage::class.java))
        }


        //initializing
        hashmap_ogpa1 = HashMap<String, ArrayList<ogpa_holder>>()
        arrayList1 = ArrayList<ArrayList<ogpa_holder>>()
        recyclerView1 = view.findViewById<RecyclerView>(R.id.recyclerview_agriculture_ogpa)
        recyclerView1.setLayoutManager(LinearLayoutManager(context))

        hashmap_ogpa2 = HashMap<String, ArrayList<ogpa_holder>>()
        arrayList2 = ArrayList<ArrayList<ogpa_holder>>()
        recyclerView2 = view.findViewById<RecyclerView>(R.id.recyclerview_hoticulture_ogpa)
        recyclerView2.setLayoutManager(LinearLayoutManager(context))

        hashmap_ogpa3 = HashMap<String, ArrayList<ogpa_holder>>()
        arrayList3 = ArrayList<ArrayList<ogpa_holder>>()
        recyclerView3 = view.findViewById<RecyclerView>(R.id.recyclerview_btech_ogpa)
        recyclerView3.setLayoutManager(LinearLayoutManager(context))


        //sharedoreferences
        sharedPreferences = this.activity?.getSharedPreferences("Degree_type", 0)
        val i: Int = sharedPreferences!!.getInt("current_degree", 0)
        firebaseDatabase = FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/")

        refresh()
    }
    private fun refresh() {
        firebaseDatabase = FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/")
        databaseReference = firebaseDatabase!!.reference.child(firebaseAuth!!.currentUser!!.uid)
        firebaseSingleton.fetchData(firebaseAuth!!,databaseReference!!){snapshot->
//            Log.e("main onDataChange: ", snapshot.toString())
            if (snapshot?.value != null) {
                hashmap_ogpa1?.clear()
                arrayList1?.clear()

                hashmap_ogpa2?.clear()
                arrayList2?.clear()

                hashmap_ogpa3?.clear()
                arrayList3?.clear()


                extractOGPA(snapshot.child("OGPA"),"AGRICULTURE",hashmap_ogpa1,arrayList1,recyclerView1)
                extractOGPA(snapshot.child("OGPA_HORTICULTURE"),"HORTICULTURE",hashmap_ogpa2,arrayList2,recyclerView2)
                extractOGPA(snapshot.child("OGPA_BTECH_AGRICULTURE"),"BTECH",hashmap_ogpa3,arrayList3,recyclerView3)
            }
        }
    }

    private fun extractOGPA(snapshot: DataSnapshot, ogpa_type: String, hashmap_ogpa: java.util.HashMap<String, java.util.ArrayList<ogpa_holder>>?, arrayList: java.util.ArrayList<java.util.ArrayList<ogpa_holder>>?, recyclerView: RecyclerView) {
//        Log.e("extractOGPA", "Starting to extract OGPA")
        for (dataSnapshot in snapshot.children) {
//            Log.e("main onDataChange:2", dataSnapshot.toString())
            val name = dataSnapshot.child("NAME").value.toString()
            val ogpa = dataSnapshot.child("OGPA").value.toString()
            val sem = dataSnapshot.child("SEM").value.toString()
            val key = name + ogpa_type

//            Log.e("extractOGPA", "Processing: NAME=$name, OGPA=$ogpa, SEM=$sem, OGPA_TYPE=$ogpa_type")

            if (hashmap_ogpa!!.containsKey(key)) {
                val arrayList = hashmap_ogpa!![key]
                arrayList!!.add(
                    ogpa_holder(
                        name,
                        ogpa,
                        sem,
                        ogpa_type
                    )
                )
                hashmap_ogpa!![key] = arrayList
//                Log.e("extractOGPA", "Updated hashmap_ogpa with key=$key")
            } else {
                val arrayList = java.util.ArrayList<ogpa_holder>()
                arrayList.add(
                    ogpa_holder(
                        name,
                        ogpa,
                        sem,
                        ogpa_type
                    )
                )
                hashmap_ogpa!![key] = arrayList
//                Log.e("extractOGPA", "Added new entry to hashmap_ogpa with key=$key")
            }
        }

//        Log.e("extractOGPA", "Finished processing snapshot children")

        for (s in hashmap_ogpa!!.keys) {
//            Log.e("extractOGPA", "Processing key=$s")
//            for (ogpa_holder in hashmap_ogpa!![s]!!) {
//                Log.e(
//                    "overall:------------------------",
//                    ogpa_holder.name + "-" + ogpa_holder.sem + "-" + ogpa_holder.ogpa + ogpa_type
//                )
//            }
            arrayList!!.add(hashmap_ogpa!![s]!!)
//            Log.e("extractOGPA", "Added to arrayList: key=$s")
        }

        if (!hashmap_ogpa!!.isEmpty()) {
            val recycler_graphview = Recyclerview_for_overall_OGPA(
                arrayList,
                context
            )
            recyclerView.adapter = recycler_graphview
//            Log.e("extractOGPA", "RecyclerView adapter set with Recyclerview_for_overall_OGPA")
        } else {
//            Log.e("extractOGPA", "hashmap_ogpa is empty")
        }

//        Log.e("extractOGPA", "Finished extractOGPA function")
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }
}