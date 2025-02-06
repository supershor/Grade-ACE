package com.om_tat_sat.grade_ace;

import android.annotation.SuppressLint;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.om_tat_sat.grade_ace.data_holders.input_fields;
import com.om_tat_sat.grade_ace.data_holders.marking;
import com.om_tat_sat.grade_ace.newUiActivity.SecondLoadingPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class BSC_AGRICULTURE_OGPA_Calculator extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    ArrayList<ArrayList<marking>> array;
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
    MediaPlayer mediaPlayer;
    SharedPreferences app_language;
    int language;
    String OldOrNew="NA";
    DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ogpa_calculator);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        //intents
        mediaPlayer=MediaPlayer.create(BSC_AGRICULTURE_OGPA_Calculator.this,R.raw.button_tap);
        intent=getIntent();
        sem=intent.getIntExtra("SEM",1);
        name=intent.getStringExtra("NAME");
        OldOrNew=intent.getStringExtra("OldOrNew") ;
        Log.e( "main sem onCreate:0000000000000000",sem+"");

        if(OldOrNew==null ||  OldOrNew=="NA" || OldOrNew.isEmpty()){
            OldOrNew="Old";
        }
        //tool bar setup
        toolbar=findViewById(R.id.toolbar_calculator);
        toolbar.setTitle(getString(R.string.semester)+sem);

        //checking if user is signed in or not
        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()==null){
            startActivity(new Intent(BSC_AGRICULTURE_OGPA_Calculator.this, SecondLoadingPage.class));
            finishAffinity();
        }

        //initializing
        app_language=getSharedPreferences("app_language",MODE_PRIVATE);
        language=app_language.getInt("current_language",0);
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
        initialize_arrays();
        firebaseDatabase=FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference=firebaseDatabase.getReference().child(firebaseAuth.getCurrentUser().getUid()).child("OGPA");


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
            if(input_fields.getTheory_marks().getVisibility()==View.VISIBLE){
                if (check(input_fields.getTheory_marks())){
                    Log.e( "main return calculate: ", i+"");
                    return;
                }else if (sem==7||sem==8) {
                    if (Double.parseDouble(input_fields.getTheory_marks().getText().toString())>100){
                        Toast.makeText(this,R.string.bsc_agriculture_message_5, Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        theory+=(Double.parseDouble(input_fields.getTheory_marks().getText().toString()) * marking.getTheory_marks());
                        total+=(100*marking.getTheory_marks());
                    }
                }else if (marking.getTheory_marks()!=0 && marking.getPractical_marks()==0){
                    Log.e( "ogpa change  calculate:lllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllllll","change detacted,"+marking.getTheory_marks()+","+marking.getPractical_marks());
                    if (Double.parseDouble(input_fields.getTheory_marks().getText().toString())>100){
                        Toast.makeText(this, R.string.bsc_agriculture_message_6, Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        theory+=(Double.parseDouble(input_fields.getTheory_marks().getText().toString()) * marking.getTheory_marks());
                        total+=(100*marking.getTheory_marks());
                    }
                } else if (Double.parseDouble(input_fields.getTheory_marks().getText().toString())>70) {
                    Toast.makeText(this,R.string.bsc_agriculture_message_7, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(this, R.string.bsc_agriculture_message_8, Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        practical+=(Double.parseDouble(input_fields.getPractical_marks().getText().toString())* marking.getPractical_marks());
                        total+=(50*marking.getPractical_marks());
                    }
                }
                else if (Double.parseDouble(input_fields.getPractical_marks().getText().toString())>30) {
                    Toast.makeText(this, R.string.bsc_agriculture_message_9, Toast.LENGTH_SHORT).show();
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
        HashMap<String,String>hashMap=new HashMap<>();
        hashMap.put("NAME",name);
        hashMap.put("OGPA",(theory+practical)/total*10+"");
        hashMap.put("SEM",sem+"");
        databaseReference.child(name+sem).setValue(hashMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Toast.makeText(BSC_AGRICULTURE_OGPA_Calculator.this, R.string.bsc_agriculture_message_10, Toast.LENGTH_SHORT).show();
                Toast.makeText(BSC_AGRICULTURE_OGPA_Calculator.this,getString(R.string.bsc_agriculture_message_11)+(theory+practical)/total*10, Toast.LENGTH_SHORT).show();
                if(Double.compare((theory+practical)/total*10,8.5)>=0){
                    AlertDialog.Builder builder=new AlertDialog.Builder(BSC_AGRICULTURE_OGPA_Calculator.this);
                    builder.setTitle(R.string.bsc_agriculture_message_1);
                    builder.setMessage(R.string.bsc_agriculture_message_2);
                    builder.setNegativeButton(R.string.bsc_agriculture_message_3_no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton(R.string.bsc_agriculture_message_4_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(Intent.ACTION_SENDTO);
                            intent.setData(Uri.parse(MailTo.MAILTO_SCHEME));
                            intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"supershor.cp@gmail.com"});
                            intent.putExtra(Intent.EXTRA_SUBJECT,"Sharing tips for Grade ACE");
                            intent.putExtra(Intent.EXTRA_TEXT,"Hello 👋\n"+"\nThis is :-\n"+firebaseAuth.getCurrentUser().getUid()+"\n"+"(It's your I'd kindly do not edit)"+"\n\nName:-\nPassing Year:-\nCollege Name:-\nPhone Number:-\nTips:-");
                            startActivity(intent);
                        }
                    });
                    builder.show();
                }
            }else{
                Toast.makeText(BSC_AGRICULTURE_OGPA_Calculator.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                Log.e( "Main error found onComplete:---------",task.getException()+"");
            }
        });
    }
    public boolean check(EditText editText){
        if (editText.getText()==null||editText.getText().toString().isEmpty()){
            Toast.makeText(this,R.string.bsc_agriculture_message_12, Toast.LENGTH_SHORT).show();
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

    private void fetch() {
        SharedPreferences prefs = getSharedPreferences("app_language", MODE_PRIVATE);
        int language = prefs.getInt("current_language", 0);
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;
        firebaseDatabase = FirebaseDatabase.getInstance("https://grade-ace-default-rtdb.asia-southeast1.firebasedatabase.app/");
        Log.e("oldNew--------",OldOrNew.toString());
        databaseReference = firebaseDatabase.getReference().child("Markings").child("Agriculture").child(OldOrNew);
        Log.e("oldNew---------------------------------",OldOrNew.toString());

        if (language == 0) {
            databaseReference = databaseReference.child("English");
        } else {
            databaseReference = databaseReference.child("Hindi");
        }

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot != null) {
                    Log.e("asd--", snapshot.toString());
                    for (DataSnapshot subSnapshot : snapshot.getChildren()) {
                        switch (subSnapshot.getKey()) {
                            case "sub_arr1":
                                addAllInSubArr(sub_arr1, subSnapshot.getValue());
                                break;
                            case "sub_arr2":
                                addAllInSubArr(sub_arr2, subSnapshot.getValue());
                                break;
                            case "sub_arr3":
                                addAllInSubArr(sub_arr3, subSnapshot.getValue());
                                break;
                            case "sub_arr4":
                                addAllInSubArr(sub_arr4, subSnapshot.getValue());
                                break;
                            case "sub_arr5":
                                addAllInSubArr(sub_arr5, subSnapshot.getValue());
                                break;
                            case "sub_arr6":
                                addAllInSubArr(sub_arr6, subSnapshot.getValue());
                                break;
                            case "sub_arr7":
                                addAllInSubArr(sub_arr7, subSnapshot.getValue());
                                break;
                            case "sub_arr8":
                                addAllInSubArr(sub_arr8, subSnapshot.getValue());
                                break;
                        }
                    }
                    Log.e("asd", sub_arr1.toString());
                    Log.e("asd", sub_arr2.toString());
                    Log.e("asd", sub_arr3.toString());
                    Log.e("asd", sub_arr4.toString());
                    Log.e("asd", sub_arr5.toString());
                    Log.e("asd", sub_arr6.toString());
                    Log.e("asd", sub_arr7.toString());
                    Log.e("asd", sub_arr8.toString());
                    addAll();
                    initialize_fields();
                    refresh();
                    calculate.setOnClickListener(v -> calculate());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(BSC_AGRICULTURE_OGPA_Calculator.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addAll() {
        array.add(sub_arr1);
        array.add(sub_arr2);
        array.add(sub_arr3);
        array.add(sub_arr4);
        array.add(sub_arr5);
        array.add(sub_arr6);
        array.add(sub_arr7);
        array.add(sub_arr8);
    }

    private void addAllInSubArr(ArrayList<marking> subarr, Object snapshot) {
        for (Object subSnapshot : (ArrayList<?>) snapshot) {
            HashMap<String, Object> subObj = (HashMap<String, Object>) subSnapshot;
            Log.e("asd", subObj.toString());
            Log.e("asd",getIntValue(subObj.get("theory"))+"" );

            subarr.add(new marking(
                    subObj.getOrDefault("subject", "NA").toString(),
                    getIntValue(subObj.get("theory")),
                    getIntValue(subObj.get("practical"))
            ));
        }
    }

    private int getIntValue(Object value) {
        if (value instanceof Long) {
            return ((Long) value).intValue();
        } else if (value instanceof String) {
            return Integer.parseInt((String) value);
        } else {
            return 0;
        }
    }


    public void initialize_arrays(){

        fetch();

//        if (language==0){
//            sub_arr1.add(new marking("Fundamentals of Agricultural Economics",2,0));
//            sub_arr1.add(new marking("Fundamentals of Entomology-1",1,1));
//            sub_arr1.add(new marking("Rural Sociology and Educational Psychology",2,0));
//            sub_arr1.add(new marking("Introduction to Forestry",1,1));
//            sub_arr1.add(new marking("Fundamentals of Agronomy",3,1));
//            sub_arr1.add(new marking("Agriculture Heritage",1,0));
//            sub_arr1.add(new marking("Human Values and Ethics",0,1));
//            sub_arr1.add(new marking("Fundamentals of Horticulture",1,1));
//            sub_arr1.add(new marking("NSS/NCC/Physical Education and Yoga Practices",0,2));
//            sub_arr1.add(new marking("Fundamentals of Soil Science",2,1));
//
//
//
//            array.add(sub_arr1);
//
//            sub_arr2.add(new marking("Fundamentals of Plant Biochemistry and Biotechnology",2,1));
//            sub_arr2.add(new marking("Fundamentals of Crop Physiology",1,1));
//            sub_arr2.add(new marking("Farm Management, Production and Resource Economics",1,1));
//            sub_arr2.add(new marking("Soil and Water Conservation Engineering",1,1));
//            sub_arr2.add(new marking("Fundamentals of Entomology-2",1,1));
//            sub_arr2.add(new marking("Fundamentals of Agricultural Extension Education",2,1));
//            sub_arr2.add(new marking("Agriculture Water Management",1,1));
//            sub_arr2.add(new marking("Agricultural Microbiology",1,1));
//            sub_arr2.add(new marking("Fundamentals of Genetics",2,1));
//            sub_arr2.add(new marking("Fundamentals of Plant Pathology",3,1));
//
//
//
//
//            array.add(sub_arr2);
//
//            sub_arr3.add(new marking("Food safety and standards",2,1));
//            sub_arr3.add(new marking("Agricultural Finance and Cooperation",2,1));
//            sub_arr3.add(new marking("Comprehension  and Communication Skills in English",1,1));
//            sub_arr3.add(new marking("Farm Machinery and Power",1,1));
//            sub_arr3.add(new marking("Crop Production Technology-1 (kharif crops)",1,1));
//            sub_arr3.add(new marking("Weed Management",2,1));
//            sub_arr3.add(new marking("Production Technology for Vegetables and Spices",1,1));
//            sub_arr3.add(new marking("Fundamentals of Plant Breeding",2,1));
//            sub_arr3.add(new marking("Agri-Informatics and Computer Applications",1,1));
//            sub_arr3.add(new marking("Livestock and Poultry Management",3,1));
//
//
//
//            array.add(sub_arr3);
//
//            sub_arr4.add(new marking("Agri Business Management",2,1));
//            sub_arr4.add(new marking("Agricultural Marketing Trade and Prices",2,1));
//            sub_arr4.add(new marking("Principles of Seed Technology",1,2));
//            sub_arr4.add(new marking("Environmental Studies and Disaster Management",2,1));
//            sub_arr4.add(new marking("Crop Production Technology-2 (Rabi crops)",1,1));
//            sub_arr4.add(new marking("Farming System and Sustainable Agriculture",1,0));
//            sub_arr4.add(new marking("Production Technology for Ornamental Crops, MAP and Landscaping",1,1));
//            sub_arr4.add(new marking("Production Technology for Fruits and Planting Crops",1,1));
//            sub_arr4.add(new marking("Renewable Energy and Green Technology",1,1));
//            sub_arr4.add(new marking("Elementary Statistics and Computer Application",1,1));
//            sub_arr4.add(new marking("Problematic Soils and their Management",2,0));
//
//
//
//
//            array.add(sub_arr4);
//
//            sub_arr5.add(new marking("Geo-informatics and Nano-technology and Precision Farming",1,1));
//            sub_arr5.add(new marking("Pest of Crops and Stored Grain and their Management",2,1));
//            sub_arr5.add(new marking("Principles of Integrated Pest and Disease Management",2,1));
//            sub_arr5.add(new marking("Entrepreneurship Development and Business Management",1,1));
//            sub_arr5.add(new marking("Practical Crop Production-1 (kharif crops)",0,2));
//            sub_arr5.add(new marking("Landscaping",2,1));
//            sub_arr5.add(new marking("Introductory Agro-Meteorology add Climate Change",1,1));
//            sub_arr5.add(new marking("Crop Improvement-1",1,1));
//            sub_arr5.add(new marking("Intellectual Property Rights",1,0));
//            sub_arr5.add(new marking("Disease of Fields and Horticulture Crops and their Management-1",2,1));
//            sub_arr5.add(new marking("Manures, Fertilizers and Soil Fertility Management",2,1));
//
//
//
//            array.add(sub_arr5);
//
//
//
//            sub_arr6.add(new marking("Principles of Food Science and Nutrition",2,0));
//            sub_arr6.add(new marking("Protected Cultivation and Post Harvest Technology",1,1));
//            sub_arr6.add(new marking("Management of Beneficial Insects",1,1));
//            sub_arr6.add(new marking("Communication Skills add Personality Development",1,1));
//            sub_arr6.add(new marking("Rainfed Agriculture and Watershed Management",1,1));
//            sub_arr6.add(new marking("Practical Crop Production-2 (Rabi crops)",0,2));
//            sub_arr6.add(new marking("Post Harvest Management and Value Addition of Fruits and Vegetable",1,1));
//            sub_arr6.add(new marking("Crop Improvement-2 (Rabi crops)",1,1));
//            sub_arr6.add(new marking("Disease of Fields and Horticulture Crops and their Management-2",2,1));
//            sub_arr6.add(new marking("Principles of Organic Farming",1,1));
//
//
//
//            array.add(sub_arr6);
//
//            sub_arr7.add(new marking("Plant Clinic",1,0));
//            sub_arr7.add(new marking("General Orientation & On Campus Training by Different Faculties",2,0));
//            sub_arr7.add(new marking("Village Attachment",7,0));
//            sub_arr7.add(new marking("Unit Attachment in Univ./College/ KVK/Research Station Attachment",4,0));
//            sub_arr7.add(new marking("Agro Industrial/Agri Business Attachment",4,0));
//            sub_arr7.add(new marking("Project Report Preparation, Presentation and Evaluation",2,0));
//
//
//            array.add(sub_arr7);
//
//            sub_arr8.add(new marking("Subject 1",10,0));
//            sub_arr8.add(new marking("Subject 2",10,0));
//
//            array.add(sub_arr8);
//        }
//        else if (language==1) {
//            sub_arr1.add(new marking("फंडामेंटल्स ऑफ़ एग्रीकल्चर इकोनॉमिक्स ",2,0));
//            sub_arr1.add(new marking("फंडामेंटल्स ऑफ़ एंटोमोलॉजी - १ ",1,1));
//            sub_arr1.add(new marking("रूरल सोशियोलॉजी एंड एजुकेशनल प्शिकोलॉजी ",2,0));
//            sub_arr1.add(new marking("इंट्रोडक्शन तो फॉरेस्ट्री",1,1));
//            sub_arr1.add(new marking("फंडामेंटल्स ऑफ़ अग्रोनोमी ",3,1));
//            sub_arr1.add(new marking("ह्यूमन वैल्यूज़  एंड एथिक्स ",0,1));
//            sub_arr1.add(new marking("फंडामेंटल्स  ऑफ़ हार्टिकल्चर ",1,1));
//            sub_arr1.add(new marking("एनएसएस /एनसीसी /फिजिकल एडकेशन एंड योगा प्रैक्टिस ",0,2));
//            sub_arr1.add(new marking("फंडामेंटल्स ऑफ़ सॉइल साइंस ",2,1));
//            sub_arr1.add(new marking("एग्रीकल्चर हेरिटेज ",1,0));
//
//
//
//            array.add(sub_arr1);
//
//            sub_arr2.add(new marking("फंडामेंटल्स  ऑफ़  बायोचमिस्ट्री एंड  बायोटेक्नोलॉजी",2,1));
//            sub_arr2.add(new marking("फंडामेंटल्स  ऑफ़ क्रॉप फिजियोलॉजी ",1,1));
//            sub_arr2.add(new marking("फ़ार्म मैनेजमेंट , प्रोडक्शन एंड  रिसोर्स इकोनॉमिक्स ",1,1));
//            sub_arr2.add(new marking("सॉइल एंड वाटर कांजेर्वेशन इंजीनियरिंग ",1,1));
//            sub_arr2.add(new marking("फंडामेंटल्स  ऑफ़ एंटोमोल्जी -२ ",1,1));
//            sub_arr2.add(new marking("फंडामेंटल्स  ऑफ़ एग्रीक्युचरल एक्सटेंशन  एजुकेशन ",2,1));
//            sub_arr2.add(new marking("एग्रीकल्चर वाटर मैनेजमेंट ",1,1));
//            sub_arr2.add(new marking("एग्रीकल्चर माइक्रोबायोलॉजी ",1,1));
//            sub_arr2.add(new marking("फंडामेंटल्स  ऑफ़ जेनेटिक्स ",2,1));
//            sub_arr2.add(new marking("फंडामेंटल्स  ऑफ़ प्लांट पैथोलॉजी ",3,1));
//
//
//
//
//            array.add(sub_arr2);
//
//            sub_arr3.add(new marking("फ़ूड सेफ्टी एंड स्टेण्डर्ड्स ",2,1));
//            sub_arr3.add(new marking("एग्रीकल्चर फाइनेंस एंड कोऑपरेशन ",2,1));
//            sub_arr3.add(new marking("कॉम्प्रिहेंशन एंड कम्युनिकेशन स्किल्स इन इंगलिश",1,1));
//            sub_arr3.add(new marking("फ़ार्म मशीनरी एंड पॉवर ",1,1));
//            sub_arr3.add(new marking("क्रॉप प्रोटेक्शन टेक्नोलॉजी - १ (ख़रीफ़  क्रैप्स )",1,1));
//            sub_arr3.add(new marking("वीड  मैनेजमेंट ",2,1));
//            sub_arr3.add(new marking("प्रोडक्शन  टेक्नोलॉजी फॉर वेजेटेबल्स एंड स्पाइसेस ",1,1));
//            sub_arr3.add(new marking("फंडामेंटल्स  ऑफ़ प्लांट ब्रीडिंग ",2,1));
//            sub_arr3.add(new marking("आगरी-इंफ़ॉरमैटिक्स एंड कंप्यूटर एप्लिकेशंस  ",1,1));
//            sub_arr3.add(new marking("लाइवस्टॉक  एंड पोल्ट्री मैनेजमेंट ",3,1));
//
//
//
//            array.add(sub_arr3);
//
//            sub_arr4.add(new marking("आगरी  बिज़नेस  मैनेजमेंट ",2,1));
//            sub_arr4.add(new marking("एग्रीकल्चर मार्केटिंग ट्रेड एंड प्राइसेस ",2,1));
//            sub_arr4.add(new marking("प्रिंसिपल्स ऑफ़ सीड टेक्नोलॉजी ",1,2));
//            sub_arr4.add(new marking("एनवायर्नमेंटल स्टडीज़ एंड डिज़ाट्र मैनेजमेंट ",2,1));
//            sub_arr4.add(new marking("क्रॉप प्रोटेक्शन टेक्नोलॉजी- २ (रबी क्रैप्स )",1,1));
//            sub_arr4.add(new marking("फार्मिंग सिस्टम एंड सस्टेनेबल एग्रीकल्चर ",1,0));
//            sub_arr4.add(new marking("प्रोडक्शन टेक्नोलॉजी फॉर ऑर्नामेंटल  क्रैप्स , मैप एंड लैंडस्केपिंग ",1,1));
//            sub_arr4.add(new marking("प्रोडक्शन टेक्नोलॉजी फॉर फ्रूट्स एंड प्लांटेशन क्रैप्स ",1,1));
//            sub_arr4.add(new marking("रिन्यूएबल एनर्जी एंड ग्रीन टेक्नोलॉजी",1,1));
//            sub_arr4.add(new marking("एलिमेंटरी स्टेटिस्टिक्स एंड  कंप्यूटर एप्लिकेशंस ",1,1));
//            sub_arr4.add(new marking("प्रॉब्लेमैटिक्स सॉइल्स एंड देअर मैनेजमेंट ",2,0));
//
//
//
//
//            array.add(sub_arr4);
//
//            sub_arr5.add(new marking("जिओ-इंफ़ॉरमैटिक्स एंड नैनों टेक्नॉलजी एंड प्रिसिशन फार्मिंग",1,1));
//                    sub_arr5.add(new marking("पेस्ट ऑफ़ क्रैप्स एंड स्टोर्ड ग्रेन्स एंड देअर मैनेजमेंट ",2,1));
//            sub_arr5.add(new marking("प्रिंसिपल्स ऑफ़ इंटीग्रेटेड पेस्ट एंड डिजीस मैनेजमेंट ",2,1));
//            sub_arr5.add(new marking("एंट्रीप्रेन्शिप डेवलपमेंट an डे बिज़नेस मैनेजमेंट ",1,1));
//            sub_arr5.add(new marking("प्रैक्टिकल क्रॉप प्रोडक्शन - १ (ख़रीफ़ क्रैप्स )",0,2));
//            sub_arr5.add(new marking("लैंडस्केपिंग ",2,1));
//            sub_arr5.add(new marking("इंट्रोक्यूटर्यज़गरों-मेट्रोलॉजी एंड क्लाइमेट चेंज",1,1));
//                    sub_arr5.add(new marking("क्रॉप इम्प्रूवमेंट -१ ",1,1));
//            sub_arr5.add(new marking("इटेलेक्चुअल प्रॉपर्टी राइट्स ",1,0));
//            sub_arr5.add(new marking("डिजीस ऑफ़ फील्ड एंड हार्टिकल्चर क्रैप्स एंड थिर मैनेजमेंट - १ ",2,1));
//            sub_arr5.add(new marking("मैनर्स, फर्टिलाइजर्स एंड सॉइल फर्टिलिटी मैनेजमेंट ",2,1));
//
//
//
//            array.add(sub_arr5);
//
//
//
//            sub_arr6.add(new marking("प्रिंसिपल्स ऑफ़ फ़ूड साइंस एंड न्यूट्रीशन ",2,0));
//            sub_arr6.add(new marking("प्रोटेक्टेड कल्टीवेशन एंड पोस्ट हार्वेस्ट टेक्नोलॉजी ",1,1));
//            sub_arr6.add(new marking("मैनेजमेंट ऑफ़ बेनिफ़िशियल इंसेक्ट्स ",1,1));
//            sub_arr6.add(new marking("कम्युनिकेशन स्किल्स एंड पर्सनालिटी मैनेजमेंट ",1,1));
//            sub_arr6.add(new marking("रैनफ़ेड एग्रीकल्चर एंड वॉटरशेड मैनेजमेंट ",1,1));
//            sub_arr6.add(new marking("प्रैक्टिकल क्रॉप प्रोडक्शन -२ (रबी क्रैप्स )",0,2));
//            sub_arr6.add(new marking("पोस्ट हार्वेस्ट मैनेजमेंट एंड वैल्यू एडिशन ऑफ़ फ्रूट्स एंड वेजेटेबल्स  ",1,1));
//            sub_arr6.add(new marking("क्रॉप इम्प्रूवमेंट -२  (रबी क्रैप्स )",1,1));
//            sub_arr6.add(new marking("डिजीस ऑफ़ फील्ड एंड हार्टिकल्चर क्रैप्स एंड थिर मैनेजमेंट - २ ",2,1));
//            sub_arr6.add(new marking("प्रिंसिपल्स ऑफ़ आर्गेनिक फार्मिंग ",1,1));
//
//
//
//            array.add(sub_arr6);
//
//            sub_arr7.add(new marking("प्लांट क्लिनिक ",1,0));
//            sub_arr7.add(new marking("जनरल ओरिएंटेशन एंड ऑन कैंपस ट्रेनिंग बाय डिफ़रेंट फैकल्टीज़ ",2,0));
//            sub_arr7.add(new marking("विलेज अटैचमेंट ",7,0));
//            sub_arr7.add(new marking("यूनिट अटैचमेंट इन यूनिवर्सिटी /कॉलेज / केवीके /रिसर्च स्टेशन अटैचमेंट ",4,0));
//            sub_arr7.add(new marking("एग्रो इंडस्ट्रियल /आगरी बिज़नेस अटैचमेंट ",4,0));
//            sub_arr7.add(new marking("प्रोजेक्ट रिपोर्ट प्रिपरेशन , प्रेजेंटेशन एंड एवलेशन ",2,0));
//
//
//            array.add(sub_arr7);
//
//            sub_arr8.add(new marking("सब्जेक्ट  1",10,0));
//            sub_arr8.add(new marking("सब्जेक्ट  2",10,0));
//
//            array.add(sub_arr8);
//        }

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