package com.om_tat_sat.grade_ace.newUiFrags

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.om_tat_sat.grade_ace.BSC_AGRICULTURE_OGPA_Calculator
import com.om_tat_sat.grade_ace.Btech_Agriculture_Bsc_Horticulture_OGPA_Calculator
import com.om_tat_sat.grade_ace.R
import com.om_tat_sat.grade_ace.TopperTips
import com.om_tat_sat.grade_ace.databinding.FragmentHomeNewUiBinding
import com.om_tat_sat.grade_ace.newUiActivity.SignInWithGoogle
import com.om_tat_sat.grade_ace.valueSaverNewUi.firebaseSingleton
import java.util.Objects

class HomeFragmentNewUi : Fragment() {
    var firebaseAuth: FirebaseAuth? = null
    var mediaPlayer: MediaPlayer? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var add_opga: AppCompatButton? = null
    var name: EditText? = null
    var spinner: Spinner? = null
    lateinit var arrayList: ArrayList<String>
    var issue: String = ""
    var name_sem_arr1: HashMap<String, String>? = null
    var name_sem_arr2: HashMap<String, String>? = null
    var name_sem_arr3: HashMap<String, String>? = null
    var recyclerView: RecyclerView? = null
    var app_language: SharedPreferences? = null
    var language: Int = 0
    var OldOrNew="Old"
    val binding :FragmentHomeNewUiBinding?=null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      return FragmentHomeNewUiBinding.inflate(inflater, container, false).root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arrayList = java.util.ArrayList()
        name_sem_arr1 = java.util.HashMap()
        name_sem_arr2 = java.util.HashMap()
        name_sem_arr3 = java.util.HashMap()

//        app_language =
//            this.activity!!.getSharedPreferences("app_language", Context.MODE_PRIVATE)
//        language = app_language.getInt("current_language", 0)
//        if (language == 1) {
        arrayList.add(getString(com.om_tat_sat.grade_ace.R.string.ogpa_message_2_fragment))
//        } else if (language == 0) {
//            arrayList!!.add("Select Semester")
//        }
        arrayList.add("1")
        arrayList.add("2")
        arrayList.add("3")
        arrayList.add("4")
        arrayList.add("5")
        arrayList.add("6")
        arrayList.add("7")
        arrayList.add("8")

        firebaseAuth=FirebaseAuth.getInstance()
        if(firebaseAuth?.currentUser==null){
            startActivity(Intent(requireContext(), SignInWithGoogle::class.java))
        }
        firebaseDatabase = FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/")
        databaseReference = firebaseDatabase!!.reference.child(firebaseAuth!!.currentUser!!.uid)

        checkNewSchema()

        refresh()
        val binding = FragmentHomeNewUiBinding.bind(view)
        binding.bscAgricultureFragHomeNewUiLl.setOnClickListener {
            OldOrNew="Old"
            showNameSemDialog(name_sem_arr1!!,"Agriculture")
        }
        binding.btechAgricultureFragHomeNewUiLl.setOnClickListener {
            OldOrNew="Old"
            showNameSemDialog(name_sem_arr3!!,"Btech")
        }
        binding.bscHorticultureFragHomeNewUiLl.setOnClickListener {
            OldOrNew="Old"
            showNameSemDialog(name_sem_arr2!!,"Horticulture")
        }

        binding.bscHorticultureFragHomeNewUiNew.setOnClickListener {
            OldOrNew="New"
            showNameSemDialog(name_sem_arr2!!,"Horticulture")
        }
        binding.btechAgricultureFragHomeNewUiNew.setOnClickListener {
            OldOrNew="New"
            showNameSemDialog(name_sem_arr3!!,"Btech")
        }
        binding.bscAgricultureFragHomeNewUiNew.setOnClickListener {
            OldOrNew="New"
            showNameSemDialog(name_sem_arr1!!,"Agriculture")
        }



        binding.topperTipsFragHomeNewUi.setOnClickListener {
            startActivity(Intent(requireContext(), TopperTips::class.java))
        }
    }
    private fun checkNewSchema(){
        FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Markings").child("NewStudents").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value!=null){

                    if(context?.getSharedPreferences("app_language", MODE_PRIVATE)?.getInt("current_language", 0) ==0){
                        if(snapshot.child("NewEnabled").value.toString().toBoolean()){
                            view?.findViewById<LinearLayout>(R.id.bsc_agriculture_frag_home_new_ui_new)!!.setVisibility(View.VISIBLE)
                            view?.findViewById<LinearLayout>(R.id.btech_agriculture_frag_home_new_ui_new)!!.setVisibility(View.VISIBLE)
                            view?.findViewById<LinearLayout>(R.id.bsc_horticulture_frag_home_new_ui_new)!!.setVisibility(View.VISIBLE)

                            view?.findViewById<TextView>(R.id.bsc_agriculture_frag_home_new_ui_new_message)!!.setText(snapshot.child("AgricultureMessage").value.toString())
                            view?.findViewById<TextView>(R.id.btech_agriculture_frag_home_new_ui_new_message)!!.setText(snapshot.child("BTechMessage").value.toString())
                            view?.findViewById<TextView>(R.id.bsc_horticulture_frag_home_new_ui_new_message)!!.setText(snapshot.child("HorticultureMessage").value.toString())
//                        Log.e("vbn",snapshot.child("AgricultureMessage").value.toString())
//                        binding?.bscHorticultureFragHomeNewUiNewMessage!!.setText(snapshot.child("AgricultureMessage").value.toString())
//                        binding.btechAgricultureFragHomeNewUiNewMessage.setText(snapshot.child("BTechMessage").value.toString())
//                        binding.bscHorticultureFragHomeNewUiNewMessage.setText(snapshot.child("HorticultureMessage").value.toString())
                        }
                        if(snapshot.child("OldMessageEnabled").value.toString().toBoolean()){
                            view?.findViewById<TextView>(R.id.bsc_agriculture_frag_home_new_ui_old_message)!!.setText(snapshot.child("AgricultureMessageOld").value.toString())
                            view?.findViewById<TextView>(R.id.btech_agriculture_frag_home_new_ui_old_message)!!.setText(snapshot.child("BTechMessageOld").value.toString())
                            view?.findViewById<TextView>(R.id.bsc_horticulture_frag_home_new_ui_old_message)!!.setText(snapshot.child("HorticultureMessageOld").value.toString())
                            view?.findViewById<TextView>(R.id.bsc_agriculture_frag_home_new_ui_old_message)!!.setVisibility(View.VISIBLE)
                            view?.findViewById<TextView>(R.id.btech_agriculture_frag_home_new_ui_old_message)!!.setVisibility(View.VISIBLE)
                            view?.findViewById<TextView>(R.id.bsc_horticulture_frag_home_new_ui_old_message)!!.setVisibility(View.VISIBLE)
                        }
                    }
                    else{
                        if(snapshot.child("NewEnabled").value.toString().toBoolean()){
                            view?.findViewById<LinearLayout>(R.id.bsc_agriculture_frag_home_new_ui_new)!!.setVisibility(View.VISIBLE)
                            view?.findViewById<LinearLayout>(R.id.btech_agriculture_frag_home_new_ui_new)!!.setVisibility(View.VISIBLE)
                            view?.findViewById<LinearLayout>(R.id.bsc_horticulture_frag_home_new_ui_new)!!.setVisibility(View.VISIBLE)

                            view?.findViewById<TextView>(R.id.bsc_agriculture_frag_home_new_ui_new_message)!!.setText(snapshot.child("AgricultureMessageHindi").value.toString())
                            view?.findViewById<TextView>(R.id.btech_agriculture_frag_home_new_ui_new_message)!!.setText(snapshot.child("BTechMessageHindi").value.toString())
                            view?.findViewById<TextView>(R.id.bsc_horticulture_frag_home_new_ui_new_message)!!.setText(snapshot.child("HorticultureMessageHindi").value.toString())
//                        Log.e("vbn",snapshot.child("AgricultureMessage").value.toString())
//                        binding?.bscHorticultureFragHomeNewUiNewMessage!!.setText(snapshot.child("AgricultureMessage").value.toString())
//                        binding.btechAgricultureFragHomeNewUiNewMessage.setText(snapshot.child("BTechMessage").value.toString())
//                        binding.bscHorticultureFragHomeNewUiNewMessage.setText(snapshot.child("HorticultureMessage").value.toString())
                        }
                        if(snapshot.child("OldMessageEnabled").value.toString().toBoolean()){
                            view?.findViewById<TextView>(R.id.bsc_agriculture_frag_home_new_ui_old_message)!!.setText(snapshot.child("AgricultureMessageHindiOld").value.toString())
                            view?.findViewById<TextView>(R.id.btech_agriculture_frag_home_new_ui_old_message)!!.setText(snapshot.child("BTechMessageHindiOld").value.toString())
                            view?.findViewById<TextView>(R.id.bsc_horticulture_frag_home_new_ui_old_message)!!.setText(snapshot.child("HorticultureMessageHindiOld").value.toString())
                            view?.findViewById<TextView>(R.id.bsc_agriculture_frag_home_new_ui_old_message)!!.setVisibility(View.VISIBLE)
                            view?.findViewById<TextView>(R.id.btech_agriculture_frag_home_new_ui_old_message)!!.setVisibility(View.VISIBLE)
                            view?.findViewById<TextView>(R.id.bsc_horticulture_frag_home_new_ui_old_message)!!.setVisibility(View.VISIBLE)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun refresh() {
        firebaseDatabase = FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/")
        databaseReference = firebaseDatabase!!.reference.child(firebaseAuth!!.currentUser!!.uid)

        firebaseSingleton.fetchData(firebaseAuth!!,databaseReference!!){snapshot->
//            Log.e("main onDataChange: ", snapshot.toString())
            if(snapshot==null){
                startActivity(Intent(context,SignInWithGoogle::class.java))
            }else{
                if (snapshot.value != null) {
                    name_sem_arr1?.clear()
                    name_sem_arr2?.clear()
                    name_sem_arr3?.clear()
                    addMultiple(snapshot.child("OGPA"), name_sem_arr1!!)
                    addMultiple(snapshot.child("OGPA_HORTICULTURE"), name_sem_arr2!!)
                    addMultiple(snapshot.child("OGPA_BTECH_AGRICULTURE"), name_sem_arr3!!)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }
    private fun addMultiple(snapshot: DataSnapshot, name_sem_arr: HashMap<String, String>){
        for (dataSnapshot in snapshot.children) {
//            Log.e("main onDataChange: ", dataSnapshot.toString())
            name_sem_arr[dataSnapshot.child("NAME").value.toString() + ""] = name_sem_arr.getOrDefault(dataSnapshot.child("NAME").value.toString() + "", "") + "_" + dataSnapshot.child("SEM").value + "_"
        }
    }
    private fun check(): Boolean {
        if (name!!.text != null && name!!.text.toString().isEmpty()) {
            issue = getString(R.string.enter_name)
            return true
        }
        return false
    }
    private fun showNameSemDialog(name_sem_arr: HashMap<String, String>,type:String) {
        val view2: View = LayoutInflater.from(context).inflate(com.om_tat_sat.grade_ace.R.layout.add_new_ogpa_criteria, null)
        name = view2.findViewById<EditText>(com.om_tat_sat.grade_ace.R.id.name_at_add_new_ogpa)
        spinner = view2.findViewById<Spinner>(com.om_tat_sat.grade_ace.R.id.spinner_at_add_new_ogpa)
        val alert = AlertDialog.Builder(
            requireContext()
        )
        alert.setView(view2)
        val arrayAdapter: ArrayAdapter<String>? = context?.let { ArrayAdapter<String>(it, com.om_tat_sat.grade_ace.R.layout.text_spinner, arrayList) }
        arrayAdapter?.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        spinner?.setAdapter(arrayAdapter)
        alert.setTitle(com.om_tat_sat.grade_ace.R.string.ogpa_message_1_fragment)
        alert.setMessage(com.om_tat_sat.grade_ace.R.string.ogpa_message_2_fragment)
        alert.setCancelable(false)
        alert.setPositiveButton(com.om_tat_sat.grade_ace.R.string.ogpa_message_3_fragment) { dialog, which ->
//            mediaPlayer.start()
            if (check()) {
                Toast.makeText(context, issue, Toast.LENGTH_SHORT).show()
                OldOrNew="Old"
            } else if (spinner?.getSelectedItemPosition() == 0) {
                Toast.makeText(
                    context,
                    R.string.ogpa_message_4_fragment,
                    Toast.LENGTH_SHORT
                ).show()
                OldOrNew="Old"
            } else if (name_sem_arr.containsKey(name?.getText().toString()) && Objects.requireNonNull<String>(name_sem_arr.get(name?.getText().toString())).contains(spinner?.getSelectedItem().toString())) {
                Toast.makeText(
                    context,
                    R.string.ogpa_message_5_fragment,
                    Toast.LENGTH_SHORT
                ).show()
                OldOrNew="Old"
            } else {
                if(type=="Agriculture"){
                    val intent = Intent(
                        context,
                        BSC_AGRICULTURE_OGPA_Calculator::class.java
                    )
                    intent.putExtra("NAME", name?.getText().toString())
                    intent.putExtra("OldOrNew",OldOrNew)
//                    Log.e( "main onClick:-------------",OldOrNew)
//                    Log.e( "main onClick:-------------",spinner?.getSelectedItem().toString())
                    intent.putExtra("SEM", spinner?.getSelectedItem().toString().toInt())
                    startActivity(intent)
                }else if(type=="Horticulture"){
                    val intent = Intent(
                        context,
                        Btech_Agriculture_Bsc_Horticulture_OGPA_Calculator::class.java
                    )
                    intent.putExtra("OldOrNew",OldOrNew)
//                    Log.e( "main onClick:-------------",OldOrNew)
                    intent.putExtra("NAME", name?.getText().toString())
//                    Log.e("main onClick:-------------", spinner?.getSelectedItem().toString())
                    intent.putExtra("SEM", spinner?.getSelectedItem().toString().toInt())
                    intent.putExtra("current_degree", 1)
                    startActivity(intent)
                }else if(type=="Btech"){
                    val intent = Intent(
                        context,
                        Btech_Agriculture_Bsc_Horticulture_OGPA_Calculator::class.java
                    )
                    intent.putExtra("OldOrNew",OldOrNew)
//                    Log.e( "main onClick:-------------",OldOrNew)
                    intent.putExtra("NAME", name?.getText().toString())
//                    Log.e("main onClick:-------------", spinner?.getSelectedItem().toString())
                    intent.putExtra("SEM", spinner?.getSelectedItem().toString().toInt())
                    intent.putExtra("current_degree", 2)
                    startActivity(intent)
                }
            }
        }.setNegativeButton(com.om_tat_sat.grade_ace.R.string.ogpa_message_6_fragment) { dialog, which ->
            OldOrNew="Old"
//            mediaPlayer.start()
            dialog.dismiss()
        }
        alert.show()
    }
}