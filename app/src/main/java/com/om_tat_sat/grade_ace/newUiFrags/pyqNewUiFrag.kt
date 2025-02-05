package com.om_tat_sat.grade_ace.newUiFrags

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.om_tat_sat.grade_ace.Interface.RecyclerInterface
import com.om_tat_sat.grade_ace.R
import com.om_tat_sat.grade_ace.Recycler.recycler_PYQ
import com.om_tat_sat.grade_ace.data_holders.PYQ_DataHolder
import com.om_tat_sat.grade_ace.newUiActivity.FirstLoadingPage
import com.om_tat_sat.grade_ace.newUiActivity.PdfViewNewUi
import com.om_tat_sat.grade_ace.valueSaverNewUi.firebasePyqLoader

class pyqNewUiFrag : Fragment(), RecyclerInterface {
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var firebaseAuth: FirebaseAuth? = null
    var recyclerview_overall_PYQ: RecyclerView? = null
    var arrayListPyq = ArrayList<PYQ_DataHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("pyqNewUiFrag", "onCreate called")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("pyqNewUiFrag", "onCreateView called")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pyq_new_ui, container, false)
        recyclerview_overall_PYQ = view.findViewById(R.id.recyclerview_overall_PYQ)
        recyclerview_overall_PYQ?.layoutManager = LinearLayoutManager(activity)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("pyqNewUiFrag", "onViewCreated called")
        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth?.currentUser == null) {
            startActivity(Intent(context, FirstLoadingPage::class.java))
        }
        recyclerview_overall_PYQ = view.findViewById(R.id.recyclerview_overall_PYQ)
        refresh()
    }

    private fun refresh() {
        Log.d("pyqNewUiFrag", "refresh called")
        firebaseDatabase = FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/")
        databaseReference = firebaseDatabase!!.reference.child("PYQ")
        firebasePyqLoader.fetchPYQ(firebaseAuth!!, databaseReference!!, { snapshot ->
            addData(snapshot!!)
        })
    }

    private fun addData(snapshot: DataSnapshot) {
        Log.d("pyqNewUiFrag", "addData called with snapshot: $snapshot")
        arrayListPyq.clear()
        for (dataSnapshot in snapshot.children) {
            Log.d("pyqNewUiFrag", "addData: $dataSnapshot")
            Log.d("pyqNewUiFrag", "addData: ${dataSnapshot.key}")
            Log.d("pyqNewUiFrag", "addData: ${dataSnapshot.child("sem").value}")
            Log.d("pyqNewUiFrag", "addData: ${dataSnapshot.child("message").value}")
            Log.d("pyqNewUiFrag", "addData: ${dataSnapshot.child("targeted").value}")
            Log.d("pyqNewUiFrag", "addData: ${dataSnapshot.child("namingConventions").value}")
            Log.d("pyqNewUiFrag", "addData: ${dataSnapshot.child("filename").value}")
            val name = dataSnapshot.key
            val semester = dataSnapshot.child("sem").value
            val message = dataSnapshot.child("message").value
            val targeted = dataSnapshot.child("targeted").value
            arrayListPyq.add(PYQ_DataHolder(message.toString(), name.toString(), semester.toString(), targeted.toString()))
        }
        Log.d("pyqNewUiFrag", "addData: $arrayListPyq")
        val recyclerPyq = recycler_PYQ(this, requireContext(), arrayListPyq)
        recyclerview_overall_PYQ?.setAdapter(recyclerPyq)
        Log.d("pyqNewUiFrag", "RecyclerView adapter set")
    }

    override fun onClick(i: Int) {
        Log.d("pyqNewUiFrag", "onClick called with index: $i")
        Toast.makeText(context, context?.getString(R.string.pleaseWaitWhileThePdfIsBeingLoaded), Toast.LENGTH_SHORT).show()
        val intent:Intent = Intent(context,PdfViewNewUi::class.java)
        intent.putExtra("KeyFileName",arrayListPyq.get(i).name)
        Log.d("pyqNewUiFrag", "onClick: ${arrayListPyq.get(i).name}")
        startActivity(intent)
    }

    override fun onClick(i: Int, ogpaType: String?) {
        Log.d("pyqNewUiFrag", "onClick called with index:")
    }
}
