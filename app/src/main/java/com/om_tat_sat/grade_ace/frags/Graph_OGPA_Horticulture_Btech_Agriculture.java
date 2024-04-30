package com.om_tat_sat.grade_ace.frags;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.om_tat_sat.grade_ace.data_holders.ogpa_holder;

import java.util.ArrayList;
import java.util.HashMap;
public class Graph_OGPA_Horticulture_Btech_Agriculture extends Fragment implements RecyclerInterface {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    HashMap<String, ArrayList<ogpa_holder>> hashmap_ogpa;
    ArrayList<String>name_arr;
    RecyclerView recyclerView;
    GraphView graph;
    SharedPreferences sharedPreferences;
    MediaPlayer mediaPlayer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_graph__o_g_p_a__horticulture__btech__agriculture, container, false);
        RecyclerView recyclerView1=view.findViewById(R.id.recycler_graph_frag_bsc_agriculture);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        firebaseAuth= FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()==null){
            startActivity(new Intent(getContext(), Loading_Page.class));
        }

        //initializing
        mediaPlayer=MediaPlayer.create(getContext(),R.raw.button_tap);
        name_arr=new ArrayList<>();
        hashmap_ogpa=new HashMap<>();//sharedoreferences
        sharedPreferences=this.getActivity().getSharedPreferences("Degree_type",0);
        int i=sharedPreferences.getInt("current_degree",0);
        firebaseDatabase=FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/");
        if (i==1) {
            databaseReference=firebaseDatabase.getReference().child(firebaseAuth.getCurrentUser().getUid()).child("OGPA_HORTICULTURE");
        }else if (i==2) {
            databaseReference=firebaseDatabase.getReference().child(firebaseAuth.getCurrentUser().getUid()).child("OGPA_BTECH_AGRICULTURE");
        }
        recyclerView=view.findViewById(R.id.recycler_graph_frag_bsc_agriculture);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refresh();

        graph = view.findViewById(R.id.graph_frag_bsc_agriculture);
    }

    @Override
    public void onResume() {
        refresh();
        super.onResume();
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
                    recycler_graphview recycler_graphview=new recycler_graphview(getContext(),name_arr,Graph_OGPA_Horticulture_Btech_Agriculture.this::onClick);
                    recyclerView.setAdapter(recycler_graphview);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e( "main onCancelled: ", error.toString());
                Toast.makeText(getContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
        }if(hashMap.containsKey(7)){
            arrayList.add(new DataPoint(7,hashMap.get(7)));
        }if(hashMap.containsKey(8)){
            arrayList.add(new DataPoint(8,hashMap.get(8)));
        }
        set_sub_details(arrayList);
    }
    public void set_sub_details(ArrayList<DataPoint>arrayList){
        int size=arrayList.size();
        if (size==1){
            graph.removeAllSeries();
            Toast.makeText(getContext(), R.string.graph_ogpa_error_message_1_fragment, Toast.LENGTH_SHORT).show();
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
        }else if (size==7) {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                    new DataPoint(1,arrayList.get(0).getY()),
                    new DataPoint(2,arrayList.get(1).getY()),
                    new DataPoint(3,arrayList.get(2).getY()),
                    new DataPoint(4,arrayList.get(3).getY()),
                    new DataPoint(5,arrayList.get(4).getY()),
                    new DataPoint(6,arrayList.get(5).getY()),
                    new DataPoint(7,arrayList.get(6).getY())
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
                    new DataPoint(6,arrayList.get(5).getY()),
                    new DataPoint(7,arrayList.get(6).getY()),
                    new DataPoint(8,arrayList.get(7).getY())
            });
            series.setColor(R.color.black);
            graph.addSeries(series);
        }
    }
    @Override
    public void onClick(int i) {
        mediaPlayer.start();
        setdata(i);
    }
}