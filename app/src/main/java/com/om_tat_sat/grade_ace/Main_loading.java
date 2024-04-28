package com.om_tat_sat.grade_ace;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main_loading extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Intent intent;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    int versionCode=4;
    String versionName="4.0";
    boolean update_going_on=false;
    boolean handler_run_complete=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_loading);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!=null){
            firebaseUser=firebaseAuth.getCurrentUser();
            firebaseUser.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        intent=new Intent(Main_loading.this, MainPage.class);
                    }else {
                        Toast.makeText(Main_loading.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        intent=new Intent(Main_loading.this, Loading_Page.class);
                    }
                }
            });
        }else{
            intent=new Intent(Main_loading.this, Loading_Page.class);
        }
        firebaseDatabase=FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference=firebaseDatabase.getReference("VERSION");
        refresh();
    }
    public void refresh(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue()!=null){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        if (dataSnapshot.getKey().equals("versionCode")){
                            if (versionCode!=Integer.parseInt(dataSnapshot.getValue()+"")){
                                update_going_on=true;
                                update();
                            }
                        } else if (dataSnapshot.getKey().equals("versionName")) {
                            if (!versionName.equals(dataSnapshot.getValue()+"")){
                                update_going_on=true;
                                update();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Main_loading.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handler_run_complete=true;
                if (!update_going_on){
                    startActivity(intent);
                    finishAffinity();
                }
            }
        },6100);
    }
    public void update(){
        AlertDialog.Builder alert=new AlertDialog.Builder(Main_loading.this);
        alert.setTitle("Update Available")
                .setMessage("New version of the app is available we recommend updating the app.");
        alert.setCancelable(false);
        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                update_going_on=false;
                if (handler_run_complete){
                    startActivity(intent);
                    finishAffinity();
                }
                dialog.dismiss();
            }
        });
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
}