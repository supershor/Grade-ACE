package com.om_tat_sat.grade_ace;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.ResourceBusyException;
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
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class MainPage extends AppCompatActivity {
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    AppCompatButton bsc_agriculture;
    SharedPreferences app_language;

    TextView english;
    TextView hindi;
    CheckBox english_checkbox;
    CheckBox hindi_checkbox;
    int language=0;
    AppCompatButton bsc_horticulture;
    AppCompatButton b_tech_agriculture_engineering;
    MediaPlayer mediaPlayer;

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
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        //checking if user is signed in or not
        firebaseAuth= FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()==null){
            startActivity(new Intent(MainPage.this, Loading_Page.class));
            finishAffinity();
        }

        //initializing
        app_language=getSharedPreferences("app_language",MODE_PRIVATE);
        language=app_language.getInt("current_language",0);
        mediaPlayer=MediaPlayer.create(MainPage.this,R.raw.button_tap);
        bsc_agriculture=findViewById(R.id.bsc_agriculture);
        bsc_horticulture=findViewById(R.id.bsc_horticulture);
        b_tech_agriculture_engineering=findViewById(R.id.btech_agriculture);
        bsc_agriculture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                startActivity(new Intent(MainPage.this,Bsc_Agriculture_tab.class));
            }
        });
        bsc_horticulture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                startActivity(new Intent(MainPage.this,Bsc_Horticulture_tab.class));
            }
        });
        b_tech_agriculture_engineering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                startActivity(new Intent(MainPage.this,Btech_Agriculture_Engineering_tab.class));
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        mediaPlayer.start();
        if (item.getItemId()==R.id.logout){
            AlertDialog.Builder builder=new AlertDialog.Builder(MainPage.this);
            builder.setCancelable(false);
            builder.setTitle(getString(R.string.logout))
                    .setMessage(getString(R.string.sure_to_logout))
                    .setPositiveButton(getString(R.string.logout), (dialog, which) -> {
                        mediaPlayer.start();
                        firebaseAuth.signOut();
                        startActivity(new Intent(MainPage.this,Loading_Page.class));
                        finishAffinity();
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mediaPlayer.start();
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }
        else if (item.getItemId()==R.id.report_error){
            Intent intent=new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(MailTo.MAILTO_SCHEME));
            intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"supershor.cp@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT,"Report error on Grade ACE.");
            intent.putExtra(Intent.EXTRA_TEXT,"Hello 👋\n"+"\nIts -\n"+firebaseAuth.getCurrentUser().getUid()+"\n"+"Email:- "+firebaseAuth.getCurrentUser().getEmail()+"\n\nName:-\nPhone Number:-\nError:-");
            startActivity(intent);

        }else if (item.getItemId()==R.id.contact_owner){
            Intent intent=new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(MailTo.MAILTO_SCHEME));
            intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"supershor.cp@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT,"Contact owner of Grade ACE.");
            intent.putExtra(Intent.EXTRA_TEXT,"Hello 👋\n"+"\nIts -\n"+firebaseAuth.getCurrentUser().getUid()+"\n"+"Email:- "+firebaseAuth.getCurrentUser().getEmail()+"\n\nName:-\nPhone Number:-\nReason:-");
            startActivity(intent);
        }else if (item.getItemId()==R.id.refresh){
            startActivity(new Intent(MainPage.this,TopperTips.class));
        }else if (item.getItemId()==R.id.change_language){
            Log.e("onOptionsItemSelected:-------------------","1");
            View view= LayoutInflater.from(MainPage.this).inflate(R.layout.change_language,null);
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
            AlertDialog.Builder alert=new AlertDialog.Builder(MainPage.this);
            alert.setView(view);
            alert.setCancelable(false);
            alert.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
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
                    startActivity(new Intent(MainPage.this,MainPage.class));
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
    }
    public void changeLanguage(String  language){
        Resources resources=this.getResources();
        Configuration configuration=resources.getConfiguration();
        Locale locale=new Locale(language);
        locale.setDefault(locale);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
    }
}