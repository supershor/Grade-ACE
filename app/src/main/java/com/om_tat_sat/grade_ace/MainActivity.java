package com.om_tat_sat.grade_ace;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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
import com.om_tat_sat.grade_ace.Interface.RecyclerInterface;
import com.om_tat_sat.grade_ace.Recycler.Item;
import com.om_tat_sat.grade_ace.Recycler.Recyclerview_for_OGPA_SHOWING;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements RecyclerInterface{
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    AppCompatButton add_opga;
    EditText name;
    Spinner spinner;
    ArrayList<Integer>arrayList;
    Toolbar toolbar;
    String issue="";
    ArrayList<Item>arrayList_ogpa;
    HashMap<String,String>name_sem_arr;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //setting status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.black));

        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()==null){
            startActivity(new Intent(MainActivity.this, Loading_Page.class));
            finishAffinity();
        }

        //initializing
        arrayList=new ArrayList<>();
        name_sem_arr=new HashMap<>();
        arrayList_ogpa=new ArrayList<>();
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        arrayList.add(4);
        arrayList.add(5);
        arrayList.add(6);
        sharedPreferences=getSharedPreferences("NAME",MODE_PRIVATE);
        firebaseDatabase=FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference=firebaseDatabase.getReference().child(firebaseAuth.getCurrentUser().getUid()).child("OGPA");
        recyclerView=findViewById(R.id.recycler_main_page);
        add_opga=findViewById(R.id.add_opga);

        //tool bar setup
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Grade ACE");
        setSupportActionBar(toolbar);

        refresh();

        //onclick
        add_opga.setOnClickListener(v -> {
            View view=LayoutInflater.from(MainActivity.this).inflate(R.layout.add_new_ogpa_criteria,null);
            name=view.findViewById(R.id.name_at_add_new_ogpa);
            spinner=view.findViewById(R.id.spinner_at_add_new_ogpa);
            AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
            alert.setView(view);
            ArrayAdapter<Integer>arrayAdapter=new ArrayAdapter<>(MainActivity.this,R.layout.text_spinner,arrayList);
            arrayAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
            alert.setTitle("Enter details for OGPA");
            alert.setMessage("You can not change this later.");
            alert.setCancelable(false);
            alert.setPositiveButton("CONTINUE", (dialog, which) -> {
                if (check()){
                    Toast.makeText(MainActivity.this,issue, Toast.LENGTH_SHORT).show();
                }else if(name_sem_arr.containsKey(name.getText().toString()) && Objects.equals(name_sem_arr.get(name.getText().toString()), spinner.getSelectedItem().toString())){
                    Toast.makeText(this, "User with same name and semester already exists.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent=new Intent(MainActivity.this,OGPA_calculator.class);
                    intent.putExtra("NAME",name.getText().toString());
                    Log.e( "main onClick:-------------", spinner.getSelectedItem().toString());
                    intent.putExtra("SEM",Integer.parseInt(spinner.getSelectedItem().toString()));
                    startActivity(intent);
                }
            }).setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss());
            alert.show();
        });
    }

    @Override
    protected void onResume() {
        refresh();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
                        name_sem_arr.put(dataSnapshot.child("NAME").getValue()+"",dataSnapshot.child("SEM").getValue()+"");
                        arrayList_ogpa.add(new Item(dataSnapshot.child("NAME").getValue()+"",dataSnapshot.child("OGPA").getValue()+"",dataSnapshot.child("SEM").getValue()+""));
                    }
                    Recyclerview_for_OGPA_SHOWING recyclerview=new Recyclerview_for_OGPA_SHOWING(arrayList_ogpa,MainActivity.this, MainActivity.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerView.setAdapter(recyclerview);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e( "main onCancelled: ", error.toString());
                Toast.makeText(MainActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean check() {
        if (name.getText()!=null && name.getText().toString().isEmpty()){
            issue="Enter Name";
            return true;
        }
        return false;
    }

    @Override
    public void onClick(int i) {

    }
}