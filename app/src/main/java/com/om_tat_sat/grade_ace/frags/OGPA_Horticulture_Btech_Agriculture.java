package com.om_tat_sat.grade_ace.frags;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.om_tat_sat.grade_ace.BSC_AGRICULTURE_OGPA_Calculator;
import com.om_tat_sat.grade_ace.Btech_Agriculture_Bsc_Horticulture_OGPA_Calculator;
import com.om_tat_sat.grade_ace.Interface.RecyclerInterface;
import com.om_tat_sat.grade_ace.R;
import com.om_tat_sat.grade_ace.Recycler.Item;
import com.om_tat_sat.grade_ace.Recycler.Recyclerview_for_OGPA_SHOWING;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class OGPA_Horticulture_Btech_Agriculture extends Fragment implements RecyclerInterface {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    MediaPlayer mediaPlayer;
    AppCompatButton add_opga;
    EditText name;
    Spinner spinner;
    ArrayList<String> arrayList;
    String issue="";
    ArrayList<Item>arrayList_ogpa;
    HashMap<String,String> name_sem_arr;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_o_g_p_a__horticulture__btech__agriculture,container,false);
        RecyclerView recyclerView1=view.findViewById(R.id.recyclerview);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }
    private boolean check() {
        if (name.getText()!=null && name.getText().toString().isEmpty()){
            issue="Enter Name";
            return true;
        }
        return false;
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
                    arrayList_ogpa.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Log.e("main onDataChange: ",dataSnapshot.toString());
                        name_sem_arr.put(dataSnapshot.child("NAME").getValue()+"", name_sem_arr.getOrDefault(dataSnapshot.child("NAME").getValue()+"","")+"_"+dataSnapshot.child("SEM").getValue()+"_");
                        arrayList_ogpa.add(new Item(dataSnapshot.child("NAME").getValue()+"",dataSnapshot.child("OGPA").getValue()+"",dataSnapshot.child("SEM").getValue()+""));
                    }
                    Recyclerview_for_OGPA_SHOWING recyclerview=new Recyclerview_for_OGPA_SHOWING(arrayList_ogpa, getContext(), OGPA_Horticulture_Btech_Agriculture.this::onClick);
                    recyclerView.setAdapter(recyclerview);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e( "main onCancelled: ", error.toString());
                Toast.makeText(getContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(int i) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();

        //initializing
        mediaPlayer=MediaPlayer.create(getContext(),R.raw.button_tap);
        arrayList=new ArrayList<>();
        name_sem_arr=new HashMap<>();
        arrayList_ogpa=new ArrayList<>();
        arrayList.add("Select Semester");
        arrayList.add("1");
        arrayList.add("2");
        arrayList.add("3");
        arrayList.add("4");
        arrayList.add("5");
        arrayList.add("6");
        arrayList.add("7");
        arrayList.add("8");
        //sharedoreferences
        sharedPreferences=this.getActivity().getSharedPreferences("Degree_type",0);
        int i=sharedPreferences.getInt("current_degree",0);
        firebaseDatabase=FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/");
        if (i==1) {
            databaseReference=firebaseDatabase.getReference().child(firebaseAuth.getCurrentUser().getUid()).child("OGPA_HORTICULTURE");
        }else if (i==2) {
            databaseReference=firebaseDatabase.getReference().child(firebaseAuth.getCurrentUser().getUid()).child("OGPA_BTECH_AGRICULTURE");
        }
        recyclerView=view.findViewById(R.id.recyclerview);
        add_opga=view.findViewById(R.id.add_ogpa);
        refresh();
        //onclick
        add_opga.setOnClickListener(v -> {
            mediaPlayer.start();
            View view2=LayoutInflater.from(getContext()).inflate(R.layout.add_new_ogpa_criteria,null);
            name=view2.findViewById(R.id.name_at_add_new_ogpa);
            spinner=view2.findViewById(R.id.spinner_at_add_new_ogpa);
            AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
            alert.setView(view2);
            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getContext(),R.layout.text_spinner,arrayList);
            arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
            alert.setTitle("Enter details for OGPA");
            alert.setMessage("You can not change this later.");
            alert.setCancelable(false);
            alert.setPositiveButton("CONTINUE", (dialog, which) -> {
                mediaPlayer.start();
                if (check()){
                    Toast.makeText(getContext(),issue, Toast.LENGTH_SHORT).show();
                } else if (spinner.getSelectedItemPosition()==0){
                    Toast.makeText(getContext(), "Select Semester", Toast.LENGTH_SHORT).show();
                } else if(name_sem_arr.containsKey(name.getText().toString()) && Objects.requireNonNull(name_sem_arr.get(name.getText().toString())).contains(spinner.getSelectedItem().toString())){
                    Toast.makeText(getContext(), "User with same name and semester already exists.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent=new Intent(getContext(), Btech_Agriculture_Bsc_Horticulture_OGPA_Calculator.class);
                    intent.putExtra("NAME",name.getText().toString());
                    Log.e( "main onClick:-------------", spinner.getSelectedItem().toString());
                    intent.putExtra("SEM",Integer.parseInt(spinner.getSelectedItem().toString()));
                    startActivity(intent);
                }
            }).setNegativeButton("CANCEL", (dialog, which) -> {
                mediaPlayer.start();
                dialog.dismiss();
            });
            alert.show();
        });
    }
}