package com.om_tat_sat.grade_ace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.om_tat_sat.grade_ace.Recycler.ViewPagerAdapter_horticulture_btech_agriculture;

import java.util.Objects;

public class Bsc_Horticulture_tab extends AppCompatActivity {
    ViewPager2 viewPager2;
    TabLayout tabLayout;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    MediaPlayer mediaPlayer;
    ViewPagerAdapter_horticulture_btech_agriculture viewPagerAdapter;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bsc_horticulture_tab);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(Bsc_Horticulture_tab.this,R.color.black));

        //tool bar setup
        toolbar=findViewById(R.id.toolbar_bsc_agriculture);
        toolbar.setTitle("BSC HORTICULTURE");
        setSupportActionBar(toolbar);

        firebaseAuth= FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()==null){
            startActivity(new Intent(Bsc_Horticulture_tab.this, Loading_Page.class));
            finishAffinity();
        }
        //sharedPreferences
        sharedPreferences=getSharedPreferences("Degree_type",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("current_degree",1);
        editor.apply();
        editor.commit();

        //Tab layout setup
        mediaPlayer=MediaPlayer.create(Bsc_Horticulture_tab.this,R.raw.button_tap);
        tabLayout=findViewById(R.id.tablayout_bsc_agriculture);
        viewPager2=findViewById(R.id.viewpager);
        viewPagerAdapter=new ViewPagerAdapter_horticulture_btech_agriculture(Bsc_Horticulture_tab.this);
        viewPager2.setAdapter(viewPagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(tabLayout.getTabAt(position)).select();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.agriculture_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        mediaPlayer.start();
        if (item.getItemId()==R.id.logout){
            AlertDialog.Builder builder=new AlertDialog.Builder(Bsc_Horticulture_tab.this);
            builder.setCancelable(false);
            builder.setTitle("Logout")
                    .setMessage("Are you sure you want to Logout ?")
                    .setPositiveButton("Logout", (dialog, which) -> {
                        firebaseAuth.signOut();
                        startActivity(new Intent(Bsc_Horticulture_tab.this,Loading_Page.class));
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