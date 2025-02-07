package com.om_tat_sat.grade_ace;

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
import com.om_tat_sat.grade_ace.Recycler.Recycler_topper_tips;
import com.om_tat_sat.grade_ace.data_holders.topper_tips_holder;
import com.om_tat_sat.grade_ace.newUiActivity.SignInWithGoogle;

import java.util.ArrayList;
import java.util.HashMap;

public class TopperTips extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<topper_tips_holder>arrayList;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_topper_tips);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()==null){
            startActivity(new Intent(TopperTips.this, SignInWithGoogle.class));
            finishAffinity();
        }
        arrayList=new ArrayList<>();
        recyclerView=findViewById(R.id.recycler_topper_message);
        recyclerView.setLayoutManager(new LinearLayoutManager(TopperTips.this));
        toolbar=findViewById(R.id.toolbar_topper_message);
        toolbar.setTitle(getString(R.string.Topper_Tips));
        firebaseDatabase=FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference=firebaseDatabase.getReference("Topper_Tips");
        refresh();
    }
    public void refresh(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("onDataChange:--------------", snapshot+"");
                if (snapshot.getValue()!=null){
                    arrayList.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        arrayList.add(new topper_tips_holder(dataSnapshot.child("NAME").getValue()+"",dataSnapshot.child("PASSING_KEY").getValue()+"",dataSnapshot.child("COLLEGE_NAME").getValue()+"",dataSnapshot.child("MESSAGE").getValue()+""));
                    }
                    Recycler_topper_tips recyclerTopperTips=new Recycler_topper_tips(arrayList,TopperTips.this);
                    recyclerView.setAdapter(recyclerTopperTips);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TopperTips.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}