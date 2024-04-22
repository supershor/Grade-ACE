package com.om_tat_sat.grade_ace.data_holders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.om_tat_sat.grade_ace.Interface.RecyclerInterface;
import com.om_tat_sat.grade_ace.Loading_Page;
import com.om_tat_sat.grade_ace.R;
import com.om_tat_sat.grade_ace.Recycler.recycler_graphview;

import java.util.ArrayList;
import java.util.HashMap;

public class Graph extends AppCompatActivity implements RecyclerInterface {
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    HashMap<String,ArrayList<ogpa_holder>> hashmap_ogpa;
    ArrayList<String>name_arr;
    RecyclerView recyclerView;
    GraphView graph;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_graph);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //setting status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(Graph.this,R.color.black));

        firebaseAuth= FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()==null){
            startActivity(new Intent(Graph.this, Loading_Page.class));
            finishAffinity();
        }

        //tool bar setup
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("OGPA GRAPH");

        //initializing
        name_arr=new ArrayList<>();
        hashmap_ogpa=new HashMap<>();
        firebaseDatabase= FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference=firebaseDatabase.getReference().child(firebaseAuth.getCurrentUser().getUid()).child("OGPA");
        recyclerView=findViewById(R.id.recycler_graph);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        refresh();

        graph = findViewById(R.id.graph);
    }
    public void setdata(int index){
        HashMap<Integer,Double>hashMap=new HashMap<>();
        for (ogpa_holder ogpa_holder:hashmap_ogpa.get(name_arr.get(index))){
            hashMap.put(Integer.parseInt(ogpa_holder.sem),Double.parseDouble(ogpa_holder.ogpa));
        }
        ArrayList<DataPoint>arrayList=new ArrayList<>();
        if (hashMap.containsKey(1)){
            arrayList.add(new DataPoint(1,hashMap.get(1)));
        }
        if(hashMap.containsKey(2)){
            arrayList.add(new DataPoint(2,hashMap.get(2)));
        }if(hashMap.containsKey(3)){
            arrayList.add(new DataPoint(3,hashMap.get(3)));
        }if(hashMap.containsKey(4)){
            arrayList.add(new DataPoint(4,hashMap.get(4)));
        }if(hashMap.containsKey(5)){
            arrayList.add(new DataPoint(5,hashMap.get(5)));
        }if(hashMap.containsKey(6)){
            arrayList.add(new DataPoint(6,hashMap.get(6)));
        }
        set_sub_details(arrayList);
    }
    public void set_sub_details(ArrayList<DataPoint>arrayList){
        int size=arrayList.size();
        if (size==1){
            graph.removeAllSeries();
            Toast.makeText(this, "Cant set graph for single sem", Toast.LENGTH_SHORT).show();
        } else if (size==2) {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                    new DataPoint(1,arrayList.get(0).getY()),
                    new DataPoint(2,arrayList.get(1).getY())
            });
            series.setColor(R.color.black);
            graph.addSeries(series);
        }else if (size==3) {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                    new DataPoint(1,arrayList.get(0).getY()),
                    new DataPoint(2,arrayList.get(1).getY()),
                    new DataPoint(3,arrayList.get(2).getY())
            });
            series.setColor(R.color.black);
            graph.addSeries(series);
        }else if (size==4) {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                    new DataPoint(1,arrayList.get(0).getY()),
                    new DataPoint(2,arrayList.get(1).getY()),
                    new DataPoint(3,arrayList.get(2).getY()),
                    new DataPoint(4,arrayList.get(3).getY())
            });
            series.setColor(R.color.black);
            graph.addSeries(series);
        }else if (size==5) {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                    new DataPoint(1,arrayList.get(0).getY()),
                    new DataPoint(2,arrayList.get(1).getY()),
                    new DataPoint(3,arrayList.get(2).getY()),
                    new DataPoint(4,arrayList.get(3).getY()),
                    new DataPoint(5,arrayList.get(4).getY())
            });
            series.setColor(R.color.black);
            graph.addSeries(series);
        }else if (size==6) {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                    new DataPoint(1,arrayList.get(0).getY()),
                    new DataPoint(2,arrayList.get(1).getY()),
                    new DataPoint(3,arrayList.get(2).getY()),
                    new DataPoint(4,arrayList.get(3).getY()),
                    new DataPoint(5,arrayList.get(4).getY()),
                    new DataPoint(6,arrayList.get(5).getY())
            });
            series.setColor(R.color.black);
            graph.addSeries(series);
        }
    }
    private void refresh() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("main onDataChange: ",snapshot.toString());
                if (snapshot.getValue()!=null){
                    name_arr.clear();
                    hashmap_ogpa.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Log.e("main onDataChange: ",dataSnapshot.toString());
                        if (hashmap_ogpa.containsKey(dataSnapshot.child("NAME").getValue()+"")){
                            ArrayList<ogpa_holder>arrayList=hashmap_ogpa.get(dataSnapshot.child("NAME").getValue()+"");
                            arrayList.add(new ogpa_holder(dataSnapshot.child("NAME").getValue()+"",dataSnapshot.child("OGPA").getValue()+"",dataSnapshot.child("SEM").getValue()+""));
                            hashmap_ogpa.put(dataSnapshot.child("NAME").getValue()+"",arrayList);
                        }else{
                            ArrayList<ogpa_holder>arrayList=new ArrayList<>();
                            arrayList.add(new ogpa_holder(dataSnapshot.child("NAME").getValue()+"",dataSnapshot.child("OGPA").getValue()+"",dataSnapshot.child("SEM").getValue()+""));
                            hashmap_ogpa.put(dataSnapshot.child("NAME").getValue()+"",arrayList);
                            name_arr.add(dataSnapshot.child("NAME").getValue()+"");
                        }
                    }
                    Log.e( "graph onDataChange+++++++++++++++",name_arr.toString());
                    recycler_graphview recycler_graphview=new recycler_graphview(Graph.this,name_arr,Graph.this::onClick);
                    recyclerView.setAdapter(recycler_graphview);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e( "main onCancelled: ", error.toString());
                Toast.makeText(Graph.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(int i) {
        setdata(i);
        Toast.makeText(this, i+"", Toast.LENGTH_SHORT).show();
    }
}