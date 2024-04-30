package com.om_tat_sat.grade_ace;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.om_tat_sat.grade_ace.data_holders.input_fields;
import com.om_tat_sat.grade_ace.data_holders.marking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Btech_Agriculture_Bsc_Horticulture_OGPA_Calculator extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    SharedPreferences app_language;
    int language;
    ArrayList<ArrayList<marking>> array;
    MediaPlayer mediaPlayer;
    ArrayList<marking>sub_arr1;
    ArrayList<marking>sub_arr2;
    ArrayList<marking>sub_arr3;
    ArrayList<marking>sub_arr4;
    ArrayList<marking>sub_arr5;
    ArrayList<marking>sub_arr6;
    ArrayList<marking>sub_arr7;
    ArrayList<marking>sub_arr8;
    ArrayList<input_fields>fields;
    AppCompatButton calculate;
    String name;
    LinearLayout linear1;
    LinearLayout linear2;
    LinearLayout linear3;
    LinearLayout linear4;
    LinearLayout linear5;
    LinearLayout linear6;
    LinearLayout linear7;
    LinearLayout linear8;
    LinearLayout linear9;
    LinearLayout linear10;
    LinearLayout linear11;
    LinearLayout linear12;
    EditText theory_marks_1;
    EditText theory_marks_2;
    EditText theory_marks_3;
    EditText theory_marks_4;
    EditText theory_marks_5;
    EditText theory_marks_6;
    EditText theory_marks_7;
    EditText theory_marks_8;
    EditText theory_marks_9;
    EditText theory_marks_10;
    EditText theory_marks_11;
    EditText theory_marks_12;
    EditText practical_marks_1;
    EditText practical_marks_2;
    EditText practical_marks_3;
    EditText practical_marks_4;
    EditText practical_marks_5;
    EditText practical_marks_6;
    EditText practical_marks_7;
    EditText practical_marks_8;
    EditText practical_marks_9;
    EditText practical_marks_10;
    EditText practical_marks_11;
    EditText practical_marks_12;
    TextView subject_name_1;
    TextView subject_name_2;
    TextView subject_name_3;
    TextView subject_name_4;
    TextView subject_name_5;
    TextView subject_name_6;
    TextView subject_name_7;
    TextView subject_name_8;
    TextView subject_name_9;
    TextView subject_name_10;
    TextView subject_name_11;
    TextView subject_name_12;
    Toolbar toolbar;
    Double theory;
    Double practical;
    Double total;
    Intent intent;
    int sem;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_btech_agriculture_bsc_horticulture_ogpa_calculator);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(Btech_Agriculture_Bsc_Horticulture_OGPA_Calculator.this,R.color.black));

        //intents
        intent=getIntent();
        sem=intent.getIntExtra("SEM",1);
        name=intent.getStringExtra("NAME");
        Log.e( "main sem onCreate:0000000000000000",sem+"");
        //tool bar setup
        toolbar=findViewById(R.id.toolbar_calculator);
        toolbar.setTitle(getString(R.string.semester)+sem);

        //checking if user is signed in or not
        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()==null){
            startActivity(new Intent(Btech_Agriculture_Bsc_Horticulture_OGPA_Calculator.this, Loading_Page.class));
            finishAffinity();
        }

        //initializing
        app_language=getSharedPreferences("app_language",MODE_PRIVATE);
        language=app_language.getInt("current_language",0);
        mediaPlayer=MediaPlayer.create(Btech_Agriculture_Bsc_Horticulture_OGPA_Calculator.this,R.raw.button_tap);
        array=new ArrayList<>();
        sub_arr1=new ArrayList<>();
        sub_arr2=new ArrayList<>();
        sub_arr3=new ArrayList<>();
        sub_arr4=new ArrayList<>();
        sub_arr5=new ArrayList<>();
        sub_arr6=new ArrayList<>();
        sub_arr7=new ArrayList<>();
        sub_arr8=new ArrayList<>();
        fields=new ArrayList<>();
        initialize_fields();
        sharedPreferences=getSharedPreferences("Degree_type",0);
        int i=sharedPreferences.getInt("current_degree",0);
        firebaseDatabase=FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/");
        if (i==1) {
            databaseReference=firebaseDatabase.getReference().child(firebaseAuth.getCurrentUser().getUid()).child("OGPA_HORTICULTURE");
            initialize_arrays1();
        }else if (i==2) {
            databaseReference=firebaseDatabase.getReference().child(firebaseAuth.getCurrentUser().getUid()).child("OGPA_BTECH_AGRICULTURE");
            initialize_arrays2();
        }
        refresh();

        calculate.setOnClickListener(v -> calculate());
    }
    public void calculate(){
        mediaPlayer.start();
        theory=0D;
        practical=0D;
        total=0D;
        for (int i=0;i<array.get(sem-1).size();i++){
            Log.e( "main calculate: 1", i+"theory"+"="+theory);
            Log.e( "main calculate: 1", i+"practical"+"="+practical);
            Log.e( "main calculate: 1", i+"total"+"="+total);
            marking marking=array.get(sem-1).get(i);
            input_fields input_fields=fields.get(i);
            if(input_fields.getTheory_marks().getVisibility()== View.VISIBLE){
                if (check(input_fields.getTheory_marks())){
                    Log.e( "main return calculate: ", i+"");
                    return;
                }else if (sem==7||sem==8) {
                    if (Double.parseDouble(input_fields.getTheory_marks().getText().toString())>100){
                        Toast.makeText(this, getString(R.string.bsc_agriculture_message_5), Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        theory+=(Double.parseDouble(input_fields.getTheory_marks().getText().toString()) * marking.getTheory_marks());
                        total+=(100*marking.getTheory_marks());
                    }
                }else if (marking.getTheory_marks()!=0 && marking.getPractical_marks()==0){
                    Log.e( "ogpa change  calculate:lllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll","change detacted,"+marking.getTheory_marks()+","+marking.getPractical_marks());
                    if (Double.parseDouble(input_fields.getTheory_marks().getText().toString())>100){
                        Toast.makeText(this, getString(R.string.bsc_agriculture_message_6), Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        theory+=(Double.parseDouble(input_fields.getTheory_marks().getText().toString()) * marking.getTheory_marks());
                        total+=(100*marking.getTheory_marks());
                    }
                } else if (Double.parseDouble(input_fields.getTheory_marks().getText().toString())>70) {
                    Toast.makeText(this, getString(R.string.bsc_agriculture_message_7), Toast.LENGTH_SHORT).show();
                    return;
                } else{
                    theory+=(Double.parseDouble(input_fields.getTheory_marks().getText().toString()) * marking.getTheory_marks());
                    total+=(70*marking.getTheory_marks());
                }
            }
            if(input_fields.getPractical_marks().getVisibility()==View.VISIBLE){
                if (check(input_fields.getPractical_marks())){
                    Log.e( "main return calculate: ", i+"");
                    return;
                }else if (marking.getTheory_marks()==0 && marking.getPractical_marks()!=0) {
                    Log.e( "ogpa change  calculate:lllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll","change detacted,"+marking.getTheory_marks()+","+marking.getPractical_marks());
                    if (Double.parseDouble(input_fields.getPractical_marks().getText().toString())>50){
                        Toast.makeText(this, getString(R.string.bsc_agriculture_message_8), Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        practical+=(Double.parseDouble(input_fields.getPractical_marks().getText().toString())* marking.getPractical_marks());
                        total+=(50*marking.getPractical_marks());
                    }
                }
                else if (Double.parseDouble(input_fields.getPractical_marks().getText().toString())>30) {
                    Toast.makeText(this, getString(R.string.bsc_agriculture_message_9), Toast.LENGTH_SHORT).show();
                    return;
                } else{
                    practical+=(Double.parseDouble(input_fields.getPractical_marks().getText().toString())* marking.getPractical_marks());
                    total+=(30*marking.getPractical_marks());
                }
            }
            Log.e( "main calculate: ", i+"theory"+"="+theory);
            Log.e( "main calculate: ", i+"practical"+"="+practical);
            Log.e( "main calculate: ", i+"total"+"="+total);
        }
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("NAME",name);
        hashMap.put("OGPA",(theory+practical)/total*10+"");
        hashMap.put("SEM",sem+"");
        databaseReference.child(name+sem).setValue(hashMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Toast.makeText(Btech_Agriculture_Bsc_Horticulture_OGPA_Calculator.this, getString(R.string.bsc_agriculture_message_10), Toast.LENGTH_SHORT).show();
                Toast.makeText(Btech_Agriculture_Bsc_Horticulture_OGPA_Calculator.this,getString(R.string.bsc_agriculture_message_11)+(theory+practical)/total*10, Toast.LENGTH_SHORT).show();
                if(Double.compare((theory+practical)/total*10,8.5)>=0){
                    AlertDialog.Builder builder=new AlertDialog.Builder(Btech_Agriculture_Bsc_Horticulture_OGPA_Calculator.this);
                    builder.setTitle(getString(R.string.bsc_agriculture_message_1));
                    builder.setMessage(getString(R.string.bsc_agriculture_message_2));
                    builder.setNegativeButton(getString(R.string.bsc_agriculture_message_3_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton(getString(R.string.bsc_agriculture_message_4_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(Intent.ACTION_SENDTO);
                            intent.setData(Uri.parse(MailTo.MAILTO_SCHEME));
                            intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"supershor.cp@gmail.com"});
                            intent.putExtra(Intent.EXTRA_SUBJECT,"Sharing tips for Grade ACE");
                            intent.putExtra(Intent.EXTRA_TEXT,"Hello 👋\n"+"\nIts -\n"+firebaseAuth.getCurrentUser().getUid()+"\n"+"Email:- "+firebaseAuth.getCurrentUser().getEmail()+"\n\nName:-\nPassing Year:-\nCollege Name:-\nPhone Number:-\nTips:-");
                            startActivity(intent);
                        }
                    });
                }
            }else{
                Toast.makeText(Btech_Agriculture_Bsc_Horticulture_OGPA_Calculator.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                Log.e( "Main error found onComplete:---------",task.getException()+"");
            }
        });
    }
    public boolean check(EditText editText){
        if (editText.getText()==null||editText.getText().toString().isEmpty()){
            Toast.makeText(this,getString(R.string.bsc_agriculture_message_12), Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
    public void refresh(){
        Log.e( "main onCreate:++++++++++",array.get(sem-1).size()+"");
        for (int i=0;i<array.get(sem-1).size();i++){
            Log.e( "main onCreate: -----------------",i+"");
            marking marking=array.get(sem-1).get(i);
            input_fields input_fields=fields.get(i);
            Log.e("refresh: ", String.valueOf(Boolean.valueOf(View.VISIBLE==input_fields.getPractical_marks().getVisibility())));
            input_fields.getLayout().setVisibility(View.VISIBLE);
            input_fields.getName().setText(marking.getName());
            if (marking.getTheory_marks()<=0){
                input_fields.getTheory_marks().setVisibility(View.GONE);
            }
            if (marking.getPractical_marks()<=0){
                input_fields.getPractical_marks().setVisibility(View.GONE);
            }
        }
    }
    public void initialize_arrays1(){

        //horticulture

        if (language==0){
            sub_arr1.add(new marking("Introductory Crop Physiology",1,1));
            sub_arr1.add(new marking("Economics and Marketing",2,1));
            sub_arr1.add(new marking("Principle of Landscape Architecture",1,1));
            sub_arr1.add(new marking("Human Value and Ethics",0,1));
            sub_arr1.add(new marking("Introductory Microbiology",1,1));
            sub_arr1.add(new marking("NSS/NCC/Physical Education and Yoga Practices",0,2));
            sub_arr1.add(new marking("Fundamentals of Soil Science",1,1));
            sub_arr1.add(new marking("Elementary Plant Biochemistry",1,1));
            sub_arr1.add(new marking("Fundamentals of Horticulture",1,1));

            array.add(sub_arr1);


            sub_arr2.add(new marking("Growth and Development of Horticultural Crop",1,1));
            sub_arr2.add(new marking("Comprehension and Communication Skills in English",1,1));
            sub_arr2.add(new marking("Water Management in Horticulture Crops",1,1));
            sub_arr2.add(new marking("Fundamentals of Extension Education",1,1));
            sub_arr2.add(new marking("Environmental Studies and Disaster Management",2,1));
            sub_arr2.add(new marking("Tropical and Subtropical Fruits",2,1));
            sub_arr2.add(new marking("Principles of Plant Breeding",2,1));
            sub_arr2.add(new marking("Fundamentals of Plant Pathology",2,1));
            sub_arr2.add(new marking("Soil Fertility and Nutritrient Management",1,1));
            sub_arr2.add(new marking("Tropical and Subtropical Vegetables",2,1));



            array.add(sub_arr2);


            sub_arr3.add(new marking("Weed Management in Horticultural Crops",1,1));
            sub_arr3.add(new marking("Elementary Plant Biotechnology",1,1));
            sub_arr3.add(new marking("Fundamentals of Food Technology",1,1));
            sub_arr3.add(new marking("Fundamentals of Entomology",2,1));
            sub_arr3.add(new marking("Commercial Floriculture",2,1));
            sub_arr3.add(new marking("Introductory Agro-Forestry",1,1));
            sub_arr3.add(new marking("Plant Propogation and Nursery Management",1,1));
            sub_arr3.add(new marking("Temprate Fruit Crops",1,1));
            sub_arr3.add(new marking("Diseases of Fruit Plantation, Medicinal and Aromatic Crops",2,1));
            sub_arr3.add(new marking("Agri Informatics and Computer Application",1,1));
            sub_arr3.add(new marking("Temprate Vegetable Crops",1,1));



            array.add(sub_arr3);


            sub_arr4.add(new marking("Farm Power and Machinery",1,1));
            sub_arr4.add(new marking("Insect Pest of Fruit, Plantation, Medicinals and Aromatic Crops",2,1));
            sub_arr4.add(new marking("Ornamental Horticulture",2,1));
            sub_arr4.add(new marking("Plantation Crops",2,1));
            sub_arr4.add(new marking("Breeding of Fruits and Plantation Crops",2,1));
            sub_arr4.add(new marking("Nematode Pests of Horticultural Crops and their Management",1,1));
            sub_arr4.add(new marking("Soil, Water and Plant Analysis",1,1));
            sub_arr4.add(new marking("Elementary Statistics",2,1));
            sub_arr4.add(new marking("Spices and Condiments",2,1));



            array.add(sub_arr4);


            sub_arr5.add(new marking("Organic Farming",2,1));
            sub_arr5.add(new marking("Introduction to Major Field Crops",1,1));
            sub_arr5.add(new marking("Medicinal and Aromatic Crops",2,1));
            sub_arr5.add(new marking("Entrepreneurship Development and Business Management",1,1));
            sub_arr5.add(new marking("Orchard and Estate Management",1,1));
            sub_arr5.add(new marking("Agro-Meteorology and Climate Change",1,1));
            sub_arr5.add(new marking("Breeding of Vegetable, Tuber and Spice Crops",2,1));
            sub_arr5.add(new marking("Diseases of Vegetable, Ornamentals and Spice Crops",2,1));
            sub_arr5.add(new marking("Potato and Tuber Crops",1,1));
            sub_arr5.add(new marking("Precision Farming and Protected Cultivation",2,1));



            array.add(sub_arr5);


            sub_arr6.add(new marking("HortiBuisness Management",1,1));
            sub_arr6.add(new marking("Post Harvest Management of Horticultural Crops",2,1));
            sub_arr6.add(new marking("Insect Pests of vegetable, Ornamental and Spice Crops",2,1));
            sub_arr6.add(new marking("Apiculture, Sericulture and LAC Culture",1,1));
            sub_arr6.add(new marking("Communication Skills and Personality Development",1,1));
            sub_arr6.add(new marking("Processing of Horticultural Crops",1,2));
            sub_arr6.add(new marking("Dryland Horticulture",1,1));
            sub_arr6.add(new marking("Seed Production of Vegetable, Tuber and Spice Crops",2,1));
            sub_arr6.add(new marking("Breeding and Seed Production of Flower and Ornamental Plants",2,1));



            array.add(sub_arr6);

            sub_arr7.add(new marking("Subject 1",10,0));
            sub_arr7.add(new marking("Subject 2",10,0));




            array.add(sub_arr7);



            sub_arr8.add(new marking("Plant Clinic",1,0));
            sub_arr8.add(new marking("General Orientation & On Campus Training by Different Faculties",2,0));
            sub_arr8.add(new marking("Village Attachment",7,0));
            sub_arr8.add(new marking("Unit Attachment in Univ./College/ KVK/Research Station Attachment",4,0));
            sub_arr8.add(new marking("Agro Industrial/Agri Business Attachment",4,0));
            sub_arr8.add(new marking("Project Report Preparation, Presentation and Evaluation",2,0));

            array.add(sub_arr8);
        } else if (language==1) {
            sub_arr1.add(new marking("इंट्रोडक्टरी क्रॉप फिजियोलॉजी ",1,1));
            sub_arr1.add(new marking("इकोनॉमिक्स एंड मार्केटिंग ",2,1));
            sub_arr1.add(new marking("प्रिंसिपल ऑफ़ लैंडस्केप आर्किटेक्चर ",1,1));
            sub_arr1.add(new marking("ह्यूमन वैल्यूज़ एंड एथिक्स ",0,1));
            sub_arr1.add(new marking("इंट्रोडक्टरी माइक्रोबायोलॉजी ",1,1));
            sub_arr1.add(new marking("एनएसएस /एनसीसी /फ़िज़िशियल एजुकेशन एंड योगा प्रैक्टिस ",0,2));
            sub_arr1.add(new marking("फंडामेंटल्स ऑफ़ सॉइल साइंस",1,1));
            sub_arr1.add(new marking("एलिमेंटरी प्लांट बायोचमिस्ट्री ",1,1));
            sub_arr1.add(new marking("फंडामेंटल्स ऑफ़ हार्टिकल्चर ",1,1));

            array.add(sub_arr1);


            sub_arr2.add(new marking("ग्रोथ एंड डेवलपमेंट ऑफ़ हार्टिकल्चरल क्रैप्स ",1,1));
            sub_arr2.add(new marking("कॉम्प्रिहेंशन एंड कम्युनिकेशन स्किल्स इन इंगलिश ",1,1));
            sub_arr2.add(new marking("वाटर मैनेजमेंट इन हार्टिकल्चरल क्रैप्स",1,1));
            sub_arr2.add(new marking("फंडामेंटल्स ऑफ़ एक्सटेंशन एजुकेशन  ",1,1));
            sub_arr2.add(new marking("एनवायर्नमेंटल स्टडीज़ एंड डिजास्टर मैनेजमेंट ",2,1));
            sub_arr2.add(new marking("ट्रॉपिकल एंड सबट्रोपिकल फ्रूट्स ",2,1));
            sub_arr2.add(new marking("प्रिंसिपल्स ऑफ़ प्लांट ब्रीडिंग ",2,1));
            sub_arr2.add(new marking("फंडामेंटल्स ऑफ़ प्लांट पैथोलॉजी",2,1));
                    sub_arr2.add(new marking("सॉइल फर्टिलिटी एंड न्यूट्रीएंट मैनेजमेंट ",1,1));
            sub_arr2.add(new marking("ट्रॉपिकल एंड सबट्रोपिकल वेजेटेबल्स ",2,1));



            array.add(sub_arr2);


            sub_arr3.add(new marking("वीड मैनेजमेंट इन हार्टिकल्चरल क्रैप्स ",1,1));
            sub_arr3.add(new marking("एलिमेंटरी प्लांट बायोटेक्नोलॉजी ",1,1));
            sub_arr3.add(new marking("फंडामेंटल्स ऑफ़ फ़ूड टेक्नोलॉजी ",1,1));
            sub_arr3.add(new marking("फंडामेंटल्स ऑफ़ एंटोमोलॉजी ",2,1));
            sub_arr3.add(new marking("कमर्शियल फ्लोरिकल्चर ",2,1));
            sub_arr3.add(new marking("इंट्रोडक्टरी एग्रो-फॉरेस्ट्री ",1,1));
            sub_arr3.add(new marking("प्लांट प्रॉपगेशन एंड नर्सरी मैनेजमेंट ",1,1));
            sub_arr3.add(new marking("टेम्प्रेट फ्रूट क्रैप्स ",1,1));
            sub_arr3.add(new marking("डाइसीज़ ऑफ़ फ़्रूट, प्लांटेशन, मेडिसिनल एंड आरोमैटिक क्रैप्स ",2,1));
            sub_arr3.add(new marking("आगरी-इंफ़ॉरमैटिक्स एंड कंप्यूटर एप्लिकेशंस  ",1,1));
            sub_arr3.add(new marking("टेम्प्रेट वेजिटेबल क्रैप्स ",1,1));



            array.add(sub_arr3);


            sub_arr4.add(new marking("फ़ार्म पॉवर एंड मशीनरी ",1,1));
            sub_arr4.add(new marking("इन्सेक्ट पेस्ट ऑफ़  फ़्रूट, प्लांटेशन, मेडिसिनल एंड आरोमैटिक क्रैप्स, ",2,1));
            sub_arr4.add(new marking("ऑर्नामेंटल हार्टिकल्चर ",2,1));
            sub_arr4.add(new marking("प्लांटेशन क्रैप्स ",2,1));
            sub_arr4.add(new marking("ब्रीडिंफ ऑफ़ फ्रूट्स एंड प्लांटेशन क्रैप्स ",2,1));
            sub_arr4.add(new marking("नेमेटोड पेस्ट ऑफ़ हार्टिकल्चरल क्रैप्स एंड देअर मैनेजमेंट",1,1));
            sub_arr4.add(new marking("सॉइल वाटर एंड प्लांट एनालिसिस ",1,1));
            sub_arr4.add(new marking("एलिमेंटरी स्टेटिस्टिक्स ",2,1));
            sub_arr4.add(new marking("स्पाइसेस एंड कंडीमेंट्स ",2,1));



            array.add(sub_arr4);


            sub_arr5.add(new marking("आर्गेनिक फार्मिंग ",2,1));
            sub_arr5.add(new marking("इंट्रोडक्शन तो मेजर फील्ड क्रैप्स ",1,1));
            sub_arr5.add(new marking("मेडिसिनल एंड आरोमैटिक क्रॉस ",2,1));
            sub_arr5.add(new marking("एंट्रीप्रेन्शियों डेवलपमेंट एंड बिज़नेस मैनेजमेंट",1,1));
            sub_arr5.add(new marking("ऑर्चर्ड एनडी एस्टेट मैनेजमेंट ",1,1));
            sub_arr5.add(new marking("एग्रो-मेट्रोलॉजी एंड क्लाइमेट चेंज ",1,1));
            sub_arr5.add(new marking("ब्रीडिंग ऑफ़ वेजिटेबल, ट्यूबर एंड स्पाइस क्रैप्स ",2,1));
            sub_arr5.add(new marking("डाइसीज़ ऑफ़ वेजिटेबल, ऑर्नामेंटल्स्व एंड स्पाइस क्रैप्स",2,1));
            sub_arr5.add(new marking("पोटैटो एंड ट्यूबर क्रैप्स ",1,1));
            sub_arr5.add(new marking("प्रिसिशन फार्मिंग एंड प्रोटेक्टेड कल्टीवेशन ",2,1));



            array.add(sub_arr5);


            sub_arr6.add(new marking("होर्टीबसिंस मैनेजमेंट ",1,1));
            sub_arr6.add(new marking("पोस्ट हार्वेस्ट मैनेजमेंट ऑफ़ हार्टिकल्चरल क्रैप्स ",2,1));
            sub_arr6.add(new marking("इन्सेक्ट पेस्ट्स ऑफ़ वेजिटेबल, ऑर्नामेंटल्स्व एंड स्पाइस क्रैप्स",2,1));
            sub_arr6.add(new marking("एपिकल्चर, सरिकल्चर एंड लैक कल्चर",1,1));
            sub_arr6.add(new marking("कम्युनिकेशन स्किल्स एंड पर्सनालिटी डेवलपमेंट ",1,1));
            sub_arr6.add(new marking("प्रोसेसिंग ऑफ़ हार्टिकल्यूटरल क्रैप्स ",1,2));
            sub_arr6.add(new marking("ड्रायलेंड हार्टिकल्यूचर ",1,1));
            sub_arr6.add(new marking("सीड प्रोडक्शन ऑफ़ वेजिटेबल, ट्यूबर एंड स्पाइस क्रैप्स ",2,1));
            sub_arr6.add(new marking("ब्रीडिंग एंड सीड प्रोडक्शन ऑफ़ फ्लावर एंड ऑर्नामेंटल प्लांट्स ",2,1));



            array.add(sub_arr6);

            sub_arr7.add(new marking("सब्जेक्ट  1",10,0));
            sub_arr7.add(new marking("सब्जेक्ट  2",10,0));




            array.add(sub_arr7);



            sub_arr8.add(new marking("प्लांट क्लिनिक",1,0));
            sub_arr8.add(new marking("जनरल ओरिएंटेशन एंड ऑन कैंपस ट्रेनिंग बाय डिफ़रेंट फैकल्टीज़ ",2,0));
            sub_arr8.add(new marking("विलेज अटैचमेंट",7,0));
            sub_arr8.add(new marking("यूनिट अटैचमेंट इन यूनिवर्सिटी /कॉलेज / केवीके /रिसर्च स्टेशन अटैचमेंट ",4,0));
            sub_arr8.add(new marking("एग्रो इंडस्ट्रियल /आगरी बिज़नेस अटैचमेंट",4,0));
            sub_arr8.add(new marking("प्रोजेक्ट रिपोर्ट प्रिपरेशन , प्रेजेंटेशन एंड एवलेशन ",2,0));

            array.add(sub_arr8);

        }
    }
    public void initialize_arrays2(){

        //btech

        if (language==0){
            sub_arr1.add(new marking("Principles of Argonomy",2,1));
            sub_arr1.add(new marking("Principles of Soil Science",2,1));
            sub_arr1.add(new marking("Engineering Physics",2,1));
            sub_arr1.add(new marking("Engineering Chemistry",2,1));
            sub_arr1.add(new marking("Engineering Mathematics-1",2,1));
            sub_arr1.add(new marking("Communication Skills and Personality Development",1,1));
            sub_arr1.add(new marking("Surveying and Levelling",1,2));
            sub_arr1.add(new marking("Engineering Drawing",0,2));



            array.add(sub_arr1);


            sub_arr2.add(new marking("Engineering Mathematics-2",2,1));
            sub_arr2.add(new marking("Environmental Science and Disaster Management",2,1));
            sub_arr2.add(new marking("Principles of Horticulture Crops and Plant Protection",1,1));
            sub_arr2.add(new marking("Strength of Material",1,1));
            sub_arr2.add(new marking("Engineering Mechanics",2,1));
            sub_arr2.add(new marking("Soil Mechanics",1,1));
            sub_arr2.add(new marking("Theory of Machines",2,0));
            sub_arr2.add(new marking("Theory of Structures",1,1));
            sub_arr2.add(new marking("Human Value and Ethics",0,1));
            sub_arr2.add(new marking("Fundamentals of Renewable Energy Resources",2,1));



            array.add(sub_arr2);


            sub_arr3.add(new marking("Machine Design",2,0));
            sub_arr3.add(new marking("Fluid Mechanics and Open Channel Hydraulics",2,1));
            sub_arr3.add(new marking("Thermodynamics, Refrigeration and Air Conditioning",2,1));
            sub_arr3.add(new marking("Heat and Mass Transfer",2,0));
            sub_arr3.add(new marking("Farm Machinery and Eqipments-1",2,1));
            sub_arr3.add(new marking("Irrigation Engineering",2,1));
            sub_arr3.add(new marking("Engineering Properties of Agricultural Produce",1,1));
            sub_arr3.add(new marking("NSS/NCC/Physical Education and Yoga Practices",0,2));
            sub_arr3.add(new marking("Watershed Hydrology",1,1));



            array.add(sub_arr3);


            sub_arr4.add(new marking("Engineering Mathematics-3",2,1));
            sub_arr4.add(new marking("Workshop Technology and Practices",1,2));
            sub_arr4.add(new marking("Electrical Machines and Power Utilization",2,1));
            sub_arr4.add(new marking("Tractor and Automotive Engines",2,1));
            sub_arr4.add(new marking("GroundWater Wells and Pumps",2,1));
            sub_arr4.add(new marking("Sprinkler and Micro Irrigation Systems",1,1));
            sub_arr4.add(new marking("Dairy and Food Engineering",2,1));
            sub_arr4.add(new marking("Renewable Power Sources",2,1));



            array.add(sub_arr4);


            sub_arr5.add(new marking("Auto Cad Application",0,2));
            sub_arr5.add(new marking("Building Construction and Cost Estimation",2,0));
            sub_arr5.add(new marking("Computer Programming and Data Structures",1,2));
            sub_arr5.add(new marking("Applied Electronics and Instrumentations",2,1));
            sub_arr5.add(new marking("Tractor System and Controls",2,1));
            sub_arr5.add(new marking("Drainage Engineering",1,1));
            sub_arr5.add(new marking("Post Harvest Engineering of Horticultural Crops",1,1));
            sub_arr5.add(new marking("Post Harvest Engineering of Cereals, Pulses and Oil Seeds",2,1));
            sub_arr5.add(new marking("Soil and Water Conservation Engineering",2,1));



            array.add(sub_arr5);


            sub_arr6.add(new marking("Entrepreneurship Development and Business Management",2,1));
            sub_arr6.add(new marking("Web Design and Applications",1,1));
            sub_arr6.add(new marking("Farm Machinery and Eqipments-2",2,1));
            sub_arr6.add(new marking("Tractor and Farm Machinery Operation and Maintenance",0,2));
            sub_arr6.add(new marking("Agriculture Structures and Environmental Control",2,1));
            sub_arr6.add(new marking("Bio-Energy Systems Design and Applications",2,1));
            sub_arr6.add(new marking("Watershed Planning and Management",1,1));
            sub_arr6.add(new marking("Water Harvesting and Soil Conservation Structures",2,1));



            array.add(sub_arr6);

            sub_arr7.add(new marking("10 Weeks Industrial Attachment/Internship(Student Ready)",0,10));


            array.add(sub_arr7);

            sub_arr8.add(new marking("Project Planning and Report Writing(Student Ready)",0,10));
            sub_arr8.add(new marking("Subject 1",2,1));
            sub_arr8.add(new marking("Subject 2",2,1));
            sub_arr8.add(new marking("Subject 3",2,1));

            array.add(sub_arr8);
        } else if (language==1) {

            sub_arr1.add(new marking("प्रिंसिपल्स ऑफ़ अग्रोनोमी ",2,1));
            sub_arr1.add(new marking("प्रिंसिपल्स ऑफ़ सॉइल साइंस  ",2,1));
            sub_arr1.add(new marking("इंजीनियरिंग फिजिक्स ",2,1));
            sub_arr1.add(new marking("इंजीनियरिंग केमिस्ट्री ",2,1));
            sub_arr1.add(new marking("इंजीनियरिंग मैथमेटिक्स -१",2,1));
            sub_arr1.add(new marking("कम्युनिकेशन स्किल्स एंड पर्सनालिटी डेवलपमेंट ",1,1));
            sub_arr1.add(new marking("सर्वियिंग एंड लेवलिंग ",1,2));
            sub_arr1.add(new marking("इंजीनियरिंग ड्राइंग ",0,2));



            array.add(sub_arr1);


            sub_arr2.add(new marking("इंजीनियरिंग मैथमेटिक्स -२",2,1));
            sub_arr2.add(new marking("एनवायर्नमेंटल साइंस  एंड  डिज़ास्टर मैनेजमेंट ",2,1));
            sub_arr2.add(new marking("प्रिंसिपल्स  ऑफ़  हार्टिकल्चर  क्रैप्स  एंड  प्लांट  प्रोटेक्शन ",1,1));
            sub_arr2.add(new marking("स्ट्रेंथ  ऑफ़  मटेरियल ",1,1));
            sub_arr2.add(new marking("इंजीनियरिंग मैकेनिक्स ",2,1));
            sub_arr2.add(new marking("सॉइल  मैकेनिक्स ",1,1));
            sub_arr2.add(new marking("थ्योरी  ऑफ़  मशीनेज ",2,0));
            sub_arr2.add(new marking("थ्योरी  ऑफ़  स्ट्रक्चर्स ",1,1));
            sub_arr2.add(new marking("ह्यूमन  वैल्यूज़  एंड  एथिक्स ",0,1));
            sub_arr2.add(new marking("फंडामेंटल्स  ऑफ़  रिन्यूएबल  एनर्जी  रिसोर्सेज़ ",2,1));



            array.add(sub_arr2);


            sub_arr3.add(new marking("मशीन  डिज़ाइन ",2,0));
            sub_arr3.add(new marking("फ्लूइड  मैकेनिक्स  एंड  ओपन  चैनल  हाइड्रॉलिक्स ",2,1));
            sub_arr3.add(new marking("थर्मोडीनामिक्स , रजिस्ट्रेशन  एंड  एयर  कंडीशनिंग ",2,1));
            sub_arr3.add(new marking("हीट एंड मास ट्रांसफ़र ",2,0));
            sub_arr3.add(new marking("फ़ार्म मशीनरी एंड  इक्विपमेंट - १ ",2,1));
            sub_arr3.add(new marking("इरीगेशन  इंजीनियरिंग",2,1));
            sub_arr3.add(new marking("इंजीनियरिंग प्रॉपर्टीज़  ऑफ़ एग्रीकल्चर प्रोड्यूस ",1,1));
            sub_arr3.add(new marking("एनएसएस /एनसीसी /फिजिकल  एजुकेशन  एंड  योगा प्रैक्टिस ",0,2));
            sub_arr3.add(new marking("वॉटरशेड  हाइड्रोलॉजी ",1,1));



            array.add(sub_arr3);


            sub_arr4.add(new marking("इंजीनियरिंग मैथेमैटिक्स -३ ",2,1));
            sub_arr4.add(new marking("वर्कशॉप  टेक्नोलॉजी  एंड प्रैक्टिस ",1,2));
            sub_arr4.add(new marking("इलेक्ट्रिकल  मशीनेज  एंड  पॉवर  उतिलिज़ेशन ",2,1));
            sub_arr4.add(new marking("ट्रेक्टर एंड  ऑटोमोटिव्स  इंजिन्स ",2,1));
            sub_arr4.add(new marking("ग्राउंडवेटर  वेल्स  एंड  पंप्स ",2,1));
            sub_arr4.add(new marking("स्प्रिंकलर  एंड  माइक्रो इरीगेशन  सिस्टम्स ",1,1));
            sub_arr4.add(new marking("डेरी  एंड  फ़ूड  इंजीनियरिंग ",2,1));
            sub_arr4.add(new marking("रिन्यूएबल  पॉवर  सोर्सेज़ ",2,1));



            array.add(sub_arr4);


            sub_arr5.add(new marking("ऑटो  कैड  एप्लीकेशन ",0,2));
            sub_arr5.add(new marking("बिल्डिंग  कंस्ट्रक्शन  एंड  कॉस्ट  एस्टीमेशन ",2,0));
            sub_arr5.add(new marking("कंप्यूटर  प्रोग्रामिंग  एंड  डेटा  स्ट्रक्चर्स ",1,2));
            sub_arr5.add(new marking("एप्लाइड  इलेक्ट्रॉनिक्स  एंड  इंस्ट्रुमेंटेशन ",2,1));
            sub_arr5.add(new marking("ट्रैक्टर  सिस्टम  एंड  कंट्रोल्स ",2,1));
            sub_arr5.add(new marking("ड्रेनेज  इंजीनियरिंग",1,1));
            sub_arr5.add(new marking("पोस्ट हार्वेस्ट इंजीनियरिंग ऑफ़  हार्टिकल्चरल  क्रैप्स ",1,1));
            sub_arr5.add(new marking("पोस्ट  हार्वेस्ट  इंजीनियरिंग ऑफ़  सीरियल्स , पल्स  एंड  आयल  सेंएड्स ",2,1));
            sub_arr5.add(new marking("सॉइल एंड वाटर कांजेर्वेशन  इंजीनियरिंग",2,1));



            array.add(sub_arr5);


            sub_arr6.add(new marking("एंट्रीप्रेन्शिप डेवलपमेंट एंड बिज़नेस मैनेजमेंट",2,1));
            sub_arr6.add(new marking("वेब डिज़ाइन  एंड एप्लिकेशंस ",1,1));
            sub_arr6.add(new marking("फ़ार्म  मशीनरी एंड  एकिपमेंट्स - २ ",2,1));
            sub_arr6.add(new marking("ट्रेक्टर  एंड  फ़ार्म  मशीनरी  ऑपरेशन  एंड  मेंटेनेंस ",0,2));
            sub_arr6.add(new marking("एग्रीकल्चर  स्ट्रक्चर्स  एंड  एनवायर्नमेंटल  कंट्रोल ",2,1));
            sub_arr6.add(new marking("बायो -एनर्जी  सिस्टम्स  डिज़ाइन  एंड  एप्लिकेशंस ",2,1));
            sub_arr6.add(new marking("वॉटरशेड  प्लानिंग  एंड  मैनेजमेंट ",1,1));
            sub_arr6.add(new marking("वाटर  हार्वेस्टिंग  एंड सॉइल कांजेर्वेशन स्ट्रक्चर्स ",2,1));



                    array.add(sub_arr6);

            sub_arr7.add(new marking("१० वीक्स इंडस्ट्रियल अटैचमेंट /इंटर्नशिप (स्टूडेंट  रेडी )",0,10));


            array.add(sub_arr7);

            sub_arr8.add(new marking("प्रोजेक्ट प्लानिंग एंड रिपोर्ट राइटिंग (स्टूडेंट रेडी )",0,10));
            sub_arr8.add(new marking("सब्जेक्ट  1",2,1));
            sub_arr8.add(new marking("सब्जेक्ट  2",2,1));
            sub_arr8.add(new marking("सब्जेक्ट  3",2,1));

            array.add(sub_arr8);


        }


    }
    public void initialize_fields(){
        calculate=findViewById(R.id.calculate);
        linear1=findViewById(R.id.linear1);
        theory_marks_1=findViewById(R.id.subject_theory_marks_name_1);
        practical_marks_1=findViewById(R.id.subject_practical_marks_name_1);
        subject_name_1=findViewById(R.id.subject_name_1);
        linear2=findViewById(R.id.linear2);
        theory_marks_2=findViewById(R.id.subject_theory_marks_name_2);
        practical_marks_2=findViewById(R.id.subject_practical_marks_name_2);
        subject_name_2=findViewById(R.id.subject_name_2);
        linear3=findViewById(R.id.linear3);
        theory_marks_3=findViewById(R.id.subject_theory_marks_name_3);
        practical_marks_3=findViewById(R.id.subject_practical_marks_name_3);
        subject_name_3=findViewById(R.id.subject_name_3);
        linear4=findViewById(R.id.linear4);
        theory_marks_4=findViewById(R.id.subject_theory_marks_name_4);
        practical_marks_4=findViewById(R.id.subject_practical_marks_name_4);
        subject_name_4=findViewById(R.id.subject_name_4);
        linear5=findViewById(R.id.linear5);
        theory_marks_5=findViewById(R.id.subject_theory_marks_name_5);
        practical_marks_5=findViewById(R.id.subject_practical_marks_name_5);
        subject_name_5=findViewById(R.id.subject_name_5);
        linear6=findViewById(R.id.linear6);
        theory_marks_6=findViewById(R.id.subject_theory_marks_name_6);
        practical_marks_6=findViewById(R.id.subject_practical_marks_name_6);
        subject_name_6=findViewById(R.id.subject_name_6);
        linear7=findViewById(R.id.linear7);
        theory_marks_7=findViewById(R.id.subject_theory_marks_name_7);
        practical_marks_7=findViewById(R.id.subject_practical_marks_name_7);
        subject_name_7=findViewById(R.id.subject_name_7);
        linear8=findViewById(R.id.linear8);
        theory_marks_8=findViewById(R.id.subject_theory_marks_name_8);
        practical_marks_8=findViewById(R.id.subject_practical_marks_name_8);
        subject_name_8=findViewById(R.id.subject_name_8);
        linear9=findViewById(R.id.linear9);
        theory_marks_9=findViewById(R.id.subject_theory_marks_name_9);
        practical_marks_9=findViewById(R.id.subject_practical_marks_name_9);
        subject_name_9=findViewById(R.id.subject_name_9);
        linear10=findViewById(R.id.linear10);
        theory_marks_10=findViewById(R.id.subject_theory_marks_name_10);
        practical_marks_10=findViewById(R.id.subject_practical_marks_name_10);
        subject_name_10=findViewById(R.id.subject_name_10);
        linear11=findViewById(R.id.linear11);
        theory_marks_11=findViewById(R.id.subject_theory_marks_name_11);
        practical_marks_11=findViewById(R.id.subject_practical_marks_name_11);
        subject_name_11=findViewById(R.id.subject_name_11);
        linear12=findViewById(R.id.linear12);
        theory_marks_12=findViewById(R.id.subject_theory_marks_name_12);
        practical_marks_12=findViewById(R.id.subject_practical_marks_name_12);
        subject_name_12=findViewById(R.id.subject_name_12);


        fields.add(new input_fields(linear1,theory_marks_1,practical_marks_1,subject_name_1));
        fields.add(new input_fields(linear2,theory_marks_2,practical_marks_2,subject_name_2));
        fields.add(new input_fields(linear3,theory_marks_3,practical_marks_3,subject_name_3));
        fields.add(new input_fields(linear4,theory_marks_4,practical_marks_4,subject_name_4));
        fields.add(new input_fields(linear5,theory_marks_5,practical_marks_5,subject_name_5));
        fields.add(new input_fields(linear6,theory_marks_6,practical_marks_6,subject_name_6));
        fields.add(new input_fields(linear7,theory_marks_7,practical_marks_7,subject_name_7));
        fields.add(new input_fields(linear8,theory_marks_8,practical_marks_8,subject_name_8));
        fields.add(new input_fields(linear9,theory_marks_9,practical_marks_9,subject_name_9));
        fields.add(new input_fields(linear10,theory_marks_10,practical_marks_10,subject_name_10));
        fields.add(new input_fields(linear11,theory_marks_11,practical_marks_11,subject_name_11));
        fields.add(new input_fields(linear12,theory_marks_12,practical_marks_12,subject_name_12));
    }
}