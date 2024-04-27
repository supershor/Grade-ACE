package com.om_tat_sat.grade_ace.frags;

import android.content.Intent;
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
import com.om_tat_sat.grade_ace.Loading_Page;
import com.om_tat_sat.grade_ace.R;
import com.om_tat_sat.grade_ace.Recycler.Recyclerview_for_overall_OGPA;
import com.om_tat_sat.grade_ace.Recycler.recycler_graphview;
import com.om_tat_sat.grade_ace.data_holders.ogpa_holder;

import java.util.ArrayList;
import java.util.HashMap;
public class Overall_OGPA extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    HashMap<String, ArrayList<ogpa_holder>> hashmap_ogpa;
    ArrayList<ArrayList<ogpa_holder>>arrayList;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_overall_ogpa,container,false);
        RecyclerView recyclerView1=view.findViewById(R.id.recyclerview_overall_ogpa);
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
        hashmap_ogpa=new HashMap<>();
        arrayList=new ArrayList<>();
        firebaseDatabase= FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference=firebaseDatabase.getReference().child(firebaseAuth.getCurrentUser().getUid()).child("OGPA");
        recyclerView=view.findViewById(R.id.recyclerview_overall_ogpa);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refresh();
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
                    hashmap_ogpa.clear();
                    arrayList.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Log.e("main onDataChange:2",dataSnapshot.toString());
                        if (hashmap_ogpa.containsKey(dataSnapshot.child("NAME").getValue()+"")){
                            ArrayList<ogpa_holder>arrayList=hashmap_ogpa.get(dataSnapshot.child("NAME").getValue()+"");
                            arrayList.add(new ogpa_holder(dataSnapshot.child("NAME").getValue()+"",dataSnapshot.child("OGPA").getValue()+"",dataSnapshot.child("SEM").getValue()+""));
                            hashmap_ogpa.put(dataSnapshot.child("NAME").getValue()+"",arrayList);
                        }else{
                            ArrayList<ogpa_holder>arrayList=new ArrayList<>();
                            arrayList.add(new ogpa_holder(dataSnapshot.child("NAME").getValue()+"",dataSnapshot.child("OGPA").getValue()+"",dataSnapshot.child("SEM").getValue()+""));
                            hashmap_ogpa.put(dataSnapshot.child("NAME").getValue()+"",arrayList);
                        }
                    }
                    for (String s:hashmap_ogpa.keySet()){
                        for (ogpa_holder ogpa_holder: hashmap_ogpa.get(s)){
                            Log.e( "overall:------------------------",ogpa_holder.name+"-"+ogpa_holder.sem+"-"+ogpa_holder.ogpa);
                        }
                        arrayList.add(hashmap_ogpa.get(s));
                    }
                    //add new reclycler
                    if (!hashmap_ogpa.isEmpty()){
                        Recyclerview_for_overall_OGPA recycler_graphview=new Recyclerview_for_overall_OGPA(arrayList,getContext());
                        recyclerView.setAdapter(recycler_graphview);
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e( "main onCancelled: ", error.toString());
                Toast.makeText(getContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}