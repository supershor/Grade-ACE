package com.om_tat_sat.grade_ace.newUiFrags

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.om_tat_sat.grade_ace.Interface.RecyclerInterface
import com.om_tat_sat.grade_ace.R
import com.om_tat_sat.grade_ace.Recycler.recycler_PYQ
import com.om_tat_sat.grade_ace.Recycler.recycler_PyqSemWise
import com.om_tat_sat.grade_ace.Recycler.recycler_StudyPrepMaterial
import com.om_tat_sat.grade_ace.data_holders.PYQ_DataHolder
import com.om_tat_sat.grade_ace.data_holders.Study_Prep_Material_Holder
import com.om_tat_sat.grade_ace.newUiActivity.FirstLoadingPage
import com.om_tat_sat.grade_ace.newUiActivity.MainHomeScreen
import com.om_tat_sat.grade_ace.newUiActivity.PdfViewNewUi
import com.om_tat_sat.grade_ace.valueSaverNewUi.firebasePyqLoader

class pyqNewUiFrag : Fragment(), RecyclerInterface {
    private val TAG = "pyqNewUiFrag"

    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var firebaseDatabaseStudyPrep: FirebaseDatabase? = null
    var databaseReferenceStudyPrep: DatabaseReference? = null
    var firebaseAuth: FirebaseAuth? = null
    var recyclerview_overall_PYQ: RecyclerView? = null
    var arrayListPyq = ArrayList<PYQ_DataHolder>()
    var arrayListStudyPrepMaterial = ArrayList<Study_Prep_Material_Holder>()
    var arrayListSemWisePyq = ArrayList<Study_Prep_Material_Holder>()
    var currentStack = 0
    var currentParentSem=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Log.d(TAG, "onCreate called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Log.d(TAG, "onCreateView called")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pyq_new_ui, container, false)
        recyclerview_overall_PYQ = view.findViewById(R.id.recyclerview_overall_PYQ)
        recyclerview_overall_PYQ?.layoutManager = LinearLayoutManager(activity)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //Log.d(TAG, "Back button pressed")
                if (currentStack == 0) {
                    //Log.d(TAG, "Navigating back to HomeFragmentNewUi")
                    startActivity(Intent(context, MainHomeScreen::class.java))
                    requireActivity().finishAffinity()
                }
                if(currentStack==1){
                    refreshPyqSemWise()
                }
                if(currentStack==2){
                    refreshStudyPrepMaterials(currentParentSem)
                }
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Log.d(TAG, "onViewCreated called")
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth?.currentUser == null) {
            //Log.d(TAG, "No current user found, navigating to FirstLoadingPage")
            startActivity(Intent(context, FirstLoadingPage::class.java))
        }

        recyclerview_overall_PYQ = view.findViewById(R.id.recyclerview_overall_PYQ)
        refreshPyqSemWise()
    }
    private fun refreshPyqSemWise(){
            Log.d(TAG, "Starting refreshPyqSemWise...")

            // Update stack functionality
            currentStack = 0
            Log.d(TAG, "Current stack set to 0.")

            // Initialize Firebase database reference
            firebaseDatabaseStudyPrep = FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/")
            databaseReferenceStudyPrep = firebaseDatabaseStudyPrep!!.reference.child("SemWisePyq")
            Log.d(TAG, "Firebase database initialized and reference set.")

            // Add a listener to database reference
            databaseReferenceStudyPrep?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(TAG, "Data change detected. Processing snapshot...")
                    arrayListSemWisePyq.clear()
                    for (snap in snapshot.children) {
                        if (snap.child("messageName").exists()) {
                            arrayListSemWisePyq.add(Study_Prep_Material_Holder(snap.key, snap.child("messageName").value.toString()))
                            Log.d(TAG, "Added item with messageName: ${snap.child("messageName").value}")
                        } else {
                            arrayListSemWisePyq.add(Study_Prep_Material_Holder(snap.key, ""))
                            Log.d(TAG, "Added item with no messageName.")
                        }
                    }
                    Log.d(TAG, "Snapshot processed. Updating RecyclerView adapter.")
                    val recyclerPyq = recycler_PyqSemWise(this@pyqNewUiFrag, requireContext(), arrayListSemWisePyq)
                    recyclerview_overall_PYQ?.adapter = recyclerPyq
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Database operation cancelled: ${error.message}")
                    Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
                }
            })
            Log.d(TAG, "refreshPyqSemWise completed.")

    }
    private fun refreshStudyPrepMaterials(sem : String) {
        //Log.d(TAG, "refreshStudyPrepMaterials called")
        currentStack = 1
        firebaseDatabaseStudyPrep = FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/")
        databaseReferenceStudyPrep = firebaseDatabaseStudyPrep!!.reference.child("SemWisePyq").child("${sem}")
        databaseReferenceStudyPrep?.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "Fetched study prep materials successfully")
                Log.d(TAG, "$sem")
                Log.d(TAG, snapshot.toString())
                addStudyPrepMaterialData(snapshot,sem)
            }

            override fun onCancelled(error: DatabaseError) {
                currentStack=0
                Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun addStudyPrepMaterialData(snapshot: DataSnapshot, semester: String) {
        //Log.d(TAG, "addStudyPrepMaterialData called")
        arrayListStudyPrepMaterial.clear()

        for (dataSnapshot in snapshot.children) {
            if(dataSnapshot.key!="messageName"){
                val name = dataSnapshot.key
                val message1 = dataSnapshot.child("message1").value
                val message1Name = dataSnapshot.child("message1Name").value
                val message2 = dataSnapshot.child("message2").value
                val message2Name = dataSnapshot.child("message2Name").value
                val message3 = dataSnapshot.child("message3").value
                val message3Name = dataSnapshot.child("message3Name").value

                //Log.d(TAG, "Adding study prep material: $name")
                arrayListStudyPrepMaterial.add(Study_Prep_Material_Holder(message1.toString(), message1Name.toString(), message2.toString(), message2Name.toString(), message3.toString(), message3Name.toString(), name.toString(), semester))
            }
        }

        val recyclerPyq = recycler_StudyPrepMaterial(this, requireContext(), arrayListStudyPrepMaterial)
        recyclerview_overall_PYQ?.adapter = recyclerPyq
    }

    private fun refresh(parentType: String, semester: String) {
        //Log.d(TAG, "refresh called with parentType: $parentType")
        currentStack = 2
        firebaseDatabase = FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/")
        databaseReference = firebaseDatabase!!.reference.child("SemWisePyq").child("${semester}").child(parentType).child("PYQ")

        databaseReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "Data fetched successfully for $parentType $semester")
                Log.d(TAG, "${snapshot.toString()}")
                addData(snapshot,parentType,semester)
            }

            override fun onCancelled(error: DatabaseError) {
                currentStack=1
                //Log.e(TAG, "Data fetch cancelled or failed: ${error.message}")
                Toast.makeText(context, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addData(snapshot: DataSnapshot, parentName: String, parentSemester:String) {
        //Log.d(TAG, "addData called")
        arrayListPyq.clear()

        for (dataSnapshot in snapshot.children) {
            val name = dataSnapshot.key
            val semester = dataSnapshot.child("sem").value
            val message = dataSnapshot.child("message").value
            val targeted = dataSnapshot.child("targeted").value

            //Log.d(TAG, "Adding PYQ data: $name")
            arrayListPyq.add(PYQ_DataHolder(message.toString(), name.toString(), semester.toString(), targeted.toString(), parentName, parentSemester))
        }

        val recyclerPyq = recycler_PYQ(this, requireContext(), arrayListPyq)
        recyclerview_overall_PYQ?.adapter = recyclerPyq
    }

    override fun onClick(i: Int) {
        //Log.d(TAG, "onClick called for index: $i")
    }

    override fun onClick(i: Int, ogpaType: String?) {
        //Log.d(TAG, "onClick called with index: $i, ogpaType: $ogpaType")
        if(ogpaType=="PyqSemWise"){
            currentParentSem=arrayListSemWisePyq[i].name
            refreshStudyPrepMaterials(arrayListSemWisePyq[i].name)
        }
        if (ogpaType == "StudyPrepMaterial") {
            //Log.d(TAG, "Refreshing study prep material with name: ${arrayListStudyPrepMaterial[i].name}")
            refresh(arrayListStudyPrepMaterial[i].name,arrayListStudyPrepMaterial[i].semester)
        } else if (ogpaType == "PYQ") {
            //Log.d(TAG, "Loading PDF for PYQ with name: ${arrayListPyq[i].name}")
            Toast.makeText(context, context?.getString(R.string.pleaseWaitWhileThePdfIsBeingLoaded), Toast.LENGTH_SHORT).show()
            val intent = Intent(context, PdfViewNewUi::class.java)
            intent.putExtra("KeyFileName", arrayListPyq[i].name)
            intent.putExtra("KeyCourse", arrayListPyq[i].parentName)
            intent.putExtra("keySem",arrayListPyq[i].parentSemester)
            startActivity(intent)
        }
    }
}
