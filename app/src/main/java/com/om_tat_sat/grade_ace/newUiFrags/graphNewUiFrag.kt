package com.om_tat_sat.grade_ace.newUiFrags

import android.R.attr
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.ValueDependentColor
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.om_tat_sat.grade_ace.Interface.RecyclerInterface
import com.om_tat_sat.grade_ace.Loading_Page
import com.om_tat_sat.grade_ace.R
import com.om_tat_sat.grade_ace.Recycler.recycler_graphview
import com.om_tat_sat.grade_ace.data_holders.ogpa_holder
import com.om_tat_sat.grade_ace.newUiActivity.InsightsGraph
import com.om_tat_sat.grade_ace.valueSaverNewUi.firebaseSingleton


class graphNewUiFrag : Fragment(), RecyclerInterface {
    var firebaseAuth: FirebaseAuth? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var semester_below_graph_frag:TextView?= null

    var hashmap_ogpa1: HashMap<String, ArrayList<ogpa_holder>>? = null
    var name_arr1: ArrayList<String>? = null
    var recyclerView1: RecyclerView? = null
    var scroll_graph_frag:NestedScrollView? = null

    var hashmap_ogpa2: HashMap<String, ArrayList<ogpa_holder>>? = null
    var name_arr2: ArrayList<String>? = null
    var recyclerView2: RecyclerView? = null

    var hashmap_ogpa3: HashMap<String, ArrayList<ogpa_holder>>? = null
    var name_arr3: ArrayList<String>? = null
    var recyclerView3: RecyclerView? = null

    var mediaPlayer: MediaPlayer? = null
    var graph: GraphView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth!!.currentUser == null) {
            startActivity(Intent(context, Loading_Page::class.java))
        }


        //initializing
        mediaPlayer = MediaPlayer.create(context, R.raw.button_tap)
        name_arr1 = ArrayList()
        hashmap_ogpa1 = HashMap()
        name_arr2 = ArrayList()
        hashmap_ogpa2 = HashMap()
        name_arr3 = ArrayList()
        hashmap_ogpa3 = HashMap()
//        scroll_graph_frag = view.findViewById(R.id.scroll_graph_frag)
        recyclerView1 = view.findViewById(R.id.recycler_graph_frag_agriculture)
        recyclerView2 = view.findViewById(R.id.recycler_graph_frag_horticulture)
        recyclerView3 = view.findViewById(R.id.recycler_graph_frag_btech)
        semester_below_graph_frag = view.findViewById(R.id.semester_below_graph_frag)

        refresh()

        graph = view.findViewById(R.id.graph_frag_bsc_agriculture)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_graph_new_ui_frga, container, false)
        val recyclerView1 =
            view.findViewById<RecyclerView>(R.id.recycler_graph_frag_agriculture)
        recyclerView1.layoutManager = LinearLayoutManager(activity)
        val recyclerView2 =
            view.findViewById<RecyclerView>(R.id.recycler_graph_frag_horticulture)
        recyclerView2.layoutManager = LinearLayoutManager(activity)
        val recyclerView3 =
            view.findViewById<RecyclerView>(R.id.recycler_graph_frag_btech)
        recyclerView3.layoutManager = LinearLayoutManager(activity)
        return view
    }
    private fun addDifferent(snapshot: DataSnapshot, name_arr: ArrayList<String>, hashmap_ogpa: HashMap<String, ArrayList<ogpa_holder>>, recyclerView: RecyclerView,ogpaType:String) {
        if (snapshot.value != null) {
            name_arr.clear()
            hashmap_ogpa.clear()
            for (dataSnapshot in snapshot.children) {
                Log.e("main onDataChange: ", dataSnapshot.toString())
                if (hashmap_ogpa.containsKey(dataSnapshot.child("NAME").value.toString() + "")) {
                    val arrayList: java.util.ArrayList<ogpa_holder>? = hashmap_ogpa.get(dataSnapshot.child("NAME").value.toString() + "")
                    arrayList?.add(
                        ogpa_holder(
                            dataSnapshot.child("NAME").value.toString() + "",
                            dataSnapshot.child("OGPA").value.toString() + "",
                            dataSnapshot.child("SEM").value.toString() + "",
                            ogpaType
                        )
                    )
                    if (arrayList != null) {
                        hashmap_ogpa.put(
                            dataSnapshot.child("NAME").value.toString() + "",
                            arrayList
                        )
                    }
                } else {
                    val arrayList = java.util.ArrayList<ogpa_holder>()
                    arrayList.add(
                        ogpa_holder(
                            dataSnapshot.child("NAME").value.toString() + "",
                            dataSnapshot.child("OGPA").value.toString() + "",
                            dataSnapshot.child("SEM").value.toString() + "",
                            ogpaType
                        )
                    )
                    hashmap_ogpa.put(
                        dataSnapshot.child("NAME").value.toString() + "",
                        arrayList
                    )
                    name_arr.add(dataSnapshot.child("NAME").value.toString() + "")
                }
            }
            Log.e("graph onDataChange+++++++++++++++", name_arr.toString())
            val recycler_graphview = recycler_graphview(context, name_arr, this@graphNewUiFrag,ogpaType)
            recyclerView.setAdapter(recycler_graphview)
        }
    }
    private fun refresh() {
        firebaseDatabase =
            FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/")
        databaseReference = firebaseDatabase!!.reference.child(firebaseAuth!!.currentUser!!.uid)
        firebaseSingleton.fetchData(FirebaseAuth.getInstance(), databaseReference!!,{ snapshot->
            if(snapshot!=null){
                addDifferent(snapshot.child("OGPA"), name_arr1!!, hashmap_ogpa1!!, recyclerView1!!,"AGRICULTURE")
                addDifferent(snapshot.child("OGPA_HORTICULTURE"), name_arr2!!, hashmap_ogpa2!!, recyclerView2!!,"HORTICULTURE")
                addDifferent(snapshot.child("OGPA_BTECH_AGRICULTURE"), name_arr3!!, hashmap_ogpa3!!, recyclerView3!!,"BTECH")
            }

        })
    }
    override fun onClick(i: Int) {

    }

    override fun onClick(i: Int, ogpaType: String?) {
        Log.e("onClick: ",i.toString()+"=="+ogpaType)
        if(ogpaType=="AGRICULTURE") {
            setdata(i, hashmap_ogpa1!!, name_arr1!!)
        }else if(ogpaType=="HORTICULTURE") {
            setdata(i, hashmap_ogpa2!!, name_arr2!!)
        }
        else if(ogpaType=="BTECH") {
            setdata(i, hashmap_ogpa3!!, name_arr3!!)
        }else{
            Toast.makeText(context,"Clicked Insights ${i}",Toast.LENGTH_SHORT).show()
            val insightsFor=ogpaType?.replace("=Insights","")
            val intent=Intent(context, InsightsGraph::class.java)
            intent.putExtra("ogpaType",insightsFor)
            if(insightsFor=="AGRICULTURE"){
                jumpToInsights(name_arr1!!, hashmap_ogpa1!!,i,intent)
            }else if(insightsFor=="HORTICULTURE"){
                jumpToInsights(name_arr2!!, hashmap_ogpa2!!,i,intent)
            }else if(insightsFor=="BTECH"){
                jumpToInsights(name_arr3!!, hashmap_ogpa3!!,i,intent)
            }
        }
//        scroll_graph_frag?.scrollTo(0,0)
    }
    fun jumpToInsights(name_arr: ArrayList<String>, hashmap_ogpa: HashMap<String, ArrayList<ogpa_holder>>, index: Int, intent: Intent){
        Log.e("onClick: ","AGRICULTURE--${name_arr.get(index)}")
        Log.e("onClick: ",hashmap_ogpa.get(name_arr.get(index)).toString())
        intent.putExtra("name",name_arr.get(index))
        val hashMap = java.util.HashMap<Int, Double>()
        for (ogpa_holder in hashmap_ogpa.get(name_arr.get(index))!!) {
            hashMap[ogpa_holder.sem.toInt()] = ogpa_holder.ogpa.toDouble()
        }
        intent.putExtra("sem1",hashMap.getOrDefault(1,0))
        intent.putExtra("sem2",hashMap.getOrDefault(2,0))
        intent.putExtra("sem3",hashMap.getOrDefault(3,0))
        intent.putExtra("sem4",hashMap.getOrDefault(4,0))
        intent.putExtra("sem5",hashMap.getOrDefault(5,0))
        intent.putExtra("sem6",hashMap.getOrDefault(6,0))
        intent.putExtra("sem7",hashMap.getOrDefault(7,0))
        intent.putExtra("sem8",hashMap.getOrDefault(8,0))

        Log.e("clicked--",hashMap.toString())

        startActivity(intent)
    }

    fun setdata(index: Int, hashmap_ogpa: HashMap<String, ArrayList<ogpa_holder>>, name_arr: ArrayList<String>) {
        val hashMap = java.util.HashMap<Int, Double>()
        for (ogpa_holder in hashmap_ogpa.get(name_arr.get(index))!!) {
            hashMap[ogpa_holder.sem.toInt()] = ogpa_holder.ogpa.toDouble()
        }
        graph!!.removeAllSeries()
        val series = BarGraphSeries(arrayOf<DataPoint>())
        if (hashMap.containsKey(1)) {
            series.appendData(DataPoint(1.0, hashMap[1]!!), false, 100)
        } else {
            series.appendData(DataPoint(1.0, 0.0), false, 100)
        }


        if (hashMap.containsKey(2)) {
            series.appendData(DataPoint(2.0, hashMap[2]!!), false, 100)
        } else {
            series.appendData(DataPoint(2.0, 0.0), false, 100)
        }

        if (hashMap.containsKey(3)) {
            series.appendData(DataPoint(3.0, hashMap[3]!!), false, 100)
        } else {
            series.appendData(DataPoint(3.0, 0.0), false, 100)
        }

        if (hashMap.containsKey(4)) {
            series.appendData(DataPoint(4.0, hashMap[4]!!), false, 100)
        } else {
            series.appendData(DataPoint(4.0, 0.0), false, 100)
        }

        if (hashMap.containsKey(5)) {
            series.appendData(DataPoint(5.0, hashMap[5]!!), false, 100)
        } else {
            series.appendData(DataPoint(5.0, 0.0), false, 100)
        }

        if (hashMap.containsKey(6)) {
            series.appendData(DataPoint(6.0, hashMap[6]!!), false, 100)
        } else {
            series.appendData(DataPoint(6.0, 0.0), false, 100)
        }

        if (hashMap.containsKey(7)) {
            series.appendData(DataPoint(7.0, hashMap[7]!!), false, 100)
        } else {
            series.appendData(DataPoint(7.0, 0.0), false, 100)
        }

        if (hashMap.containsKey(8)) {
            series.appendData(DataPoint(8.0, hashMap[8]!!), false, 100)
        } else {
            series.appendData(DataPoint(8.0, 0.0), false, 100)
        }


//        series.color = R.color.black
//        graph!!.title="GPA"
//        graph!!.titleColor=R.color.black
//        graph!!.legendRenderer.isVisible=true
//        graph!!.legendRenderer.align= LegendRenderer.LegendAlign.TOP
//        graph!!.legendRenderer.textColor=R.color.black
        series.spacing=20
        series.isDrawValuesOnTop=true
        series.valuesOnTopColor=R.color.black
        series.title="OGPA"
//        series.color=R.color.black
//        series.color = Color.BLACK;
        series.setValueDependentColor (object : ValueDependentColor<DataPoint>{
            override fun get(data: DataPoint?): Int {
                return Color.rgb(  ((data?.x!! * 255/4).toInt()), (  Math.abs(data.y *255/6).toInt()  ), 100  )
            }

        })

        graph!!.gridLabelRenderer.gridColor=Color.BLACK
//        val staticLabelsFormatter = StaticLabelsFormatter(graph)
//        staticLabelsFormatter.setHorizontalLabels(arrayOf("old", "middle", "new"))
//        staticLabelsFormatter.setVerticalLabels(arrayOf("low", "middle", "high"))
//        graph!!.gridLabelRenderer.labelFormatter = staticLabelsFormatter
        graph!!.addSeries(series)
        graph!!.visibility=View.VISIBLE



        semester_below_graph_frag?.visibility=View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        refresh()
    }
}