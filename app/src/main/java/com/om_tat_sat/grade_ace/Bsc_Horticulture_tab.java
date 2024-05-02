package com.om_tat_sat.grade_ace;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

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

import java.util.Locale;
import java.util.Objects;

public class Bsc_Horticulture_tab extends AppCompatActivity {
    ViewPager2 viewPager2;
    TabLayout tabLayout;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    SharedPreferences app_language;

    TextView english;
    TextView hindi;
    CheckBox english_checkbox;
    CheckBox hindi_checkbox;
    int language=0;
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
        toolbar.setTitle(getString(R.string.bsc_horticulture));
        setSupportActionBar(toolbar);

        firebaseAuth= FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()==null){
            startActivity(new Intent(Bsc_Horticulture_tab.this, Loading_Page.class));
            finishAffinity();
        }
        //sharedPreferences
        app_language=getSharedPreferences("app_language",MODE_PRIVATE);
        language=app_language.getInt("current_language",0);
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
            builder.setTitle(getString(R.string.logout))
                    .setMessage(getString(R.string.sure_to_logout))
                    .setPositiveButton(getString(R.string.logout), (dialog, which) -> {
                        firebaseAuth.signOut();
                        startActivity(new Intent(Bsc_Horticulture_tab.this,Loading_Page.class));
                        finishAffinity();
                    })
                    .setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
            builder.show();
        }
        else if (item.getItemId()==R.id.report_error){
            Intent intent=new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(MailTo.MAILTO_SCHEME));
            intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"supershor.cp@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT,"Report error on Grade ACE.");
            intent.putExtra(Intent.EXTRA_TEXT,"Hello 👋\n"+"\nThis is :-\n"+firebaseAuth.getCurrentUser().getUid()+"\n"+"(It's your I'd kindly do not edit)"+"\n\nName:-\nPhone Number:-\nError:-");
            startActivity(intent);
        }else if (item.getItemId()==R.id.contact_owner){
            Intent intent=new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(MailTo.MAILTO_SCHEME));
            intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"supershor.cp@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT,"Contact owner of Grade ACE.");
            intent.putExtra(Intent.EXTRA_TEXT,"Hello 👋\n"+"\nThis is :-\n"+firebaseAuth.getCurrentUser().getUid()+"\n"+"(It's your I'd kindly do not edit)"+"\n\nName:-\nPhone Number:-\nReason:-");
            startActivity(intent);
        }else if (item.getItemId()==R.id.refresh){
            startActivity(new Intent(Bsc_Horticulture_tab.this,TopperTips.class));
        }
        else if (item.getItemId()==R.id.change_language){
            Log.e("onOptionsItemSelected:-------------------","1");
            View view= LayoutInflater.from(Bsc_Horticulture_tab.this).inflate(R.layout.change_language,null);
            english=view.findViewById(R.id.textview_english);
            hindi=view.findViewById(R.id.textview_hindi);
            english_checkbox=view.findViewById(R.id.checkbox_english);
            hindi_checkbox=view.findViewById(R.id.checkbox_hindi);
            if (language==0){
                english_checkbox.setChecked(true);
            } else if (language==1) {
                hindi_checkbox.setChecked(true);
            }
            Log.e("onOptionsItemSelected:-------------------","2");
            AlertDialog.Builder alert=new AlertDialog.Builder(Bsc_Horticulture_tab.this);
            alert.setView(view);
            alert.setCancelable(false);
            alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (english_checkbox.isChecked()){
                        SharedPreferences.Editor editor=app_language.edit();
                        editor.putInt("current_language",0);
                        editor.apply();
                        changeLanguage("en");
                    } else if (hindi_checkbox.isChecked()) {
                        SharedPreferences.Editor editor=app_language.edit();
                        editor.putInt("current_language",1);
                        editor.apply();
                        changeLanguage("hi");
                    }
                    startActivity(new Intent(Bsc_Horticulture_tab.this,MainPage.class));
                    startActivity(new Intent(Bsc_Horticulture_tab.this,Bsc_Horticulture_tab.class));
                    finishAffinity();
                }
            });
            Log.e("onOptionsItemSelected:-------------------","3");
            alert.show();
            Log.e("onOptionsItemSelected:-------------------","4");
            english.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    english_checkbox.setChecked(true);
                    hindi_checkbox.setChecked(false);
                }
            });
            english_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    english_checkbox.setChecked(true);
                    hindi_checkbox.setChecked(false);
                }
            });
            hindi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hindi_checkbox.setChecked(true);
                    english_checkbox.setChecked(false);
                }
            });
            hindi_checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hindi_checkbox.setChecked(true);
                    english_checkbox.setChecked(false);
                }
            });
            Log.e("onOptionsItemSelected:-------------------","5");
        }
        return super.onOptionsItemSelected(item);
    }public void changeLanguage(String  language){
        Resources resources=this.getResources();
        Configuration configuration=resources.getConfiguration();
        Locale locale=new Locale(language);
        locale.setDefault(locale);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
    }
}