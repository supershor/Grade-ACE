package com.om_tat_sat.grade_ace;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

import com.google.firebase.auth.FirebaseAuth;
public class MainPage extends AppCompatActivity {
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    AppCompatButton bsc_agriculture;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(MainPage.this,R.color.black));

        //tool bar setup
        toolbar=findViewById(R.id.toolbar_main_page);
        toolbar.setTitle("GRADE ACE");
        setSupportActionBar(toolbar);

        //checking if user is signed in or not
        firebaseAuth= FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()==null){
            startActivity(new Intent(MainPage.this, Loading_Page.class));
            finishAffinity();
        }

        //initializing
        bsc_agriculture=findViewById(R.id.bsc_agriculture);
        bsc_agriculture.setOnClickListener(v -> startActivity(new Intent(MainPage.this,Bsc_Agriculture_tab.class)));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.logout){
            AlertDialog.Builder builder=new AlertDialog.Builder(MainPage.this);
            builder.setCancelable(false);
            builder.setTitle("Logout")
                    .setMessage("Are you sure you want to Logout ?")
                    .setPositiveButton("Logout", (dialog, which) -> {
                        firebaseAuth.signOut();
                        startActivity(new Intent(MainPage.this,Loading_Page.class));
                        finishAffinity();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.show();
        }
        else if (item.getItemId()==R.id.report_error){
            Intent intent=new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(MailTo.MAILTO_SCHEME));
            intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"supershor.cp@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT,"Report error on Grade ACE.");
            startActivity(intent);
        }else if (item.getItemId()==R.id.contact_owner){
            Intent intent=new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(MailTo.MAILTO_SCHEME));
            intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"supershor.cp@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT,"Contact owner of Grade ACE.");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}