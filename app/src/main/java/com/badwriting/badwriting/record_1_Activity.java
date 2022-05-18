package com.badwriting.badwriting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class record_1_Activity extends AppCompatActivity {

    private AdView mAdView;

    SharedPreferences sharedPreferences1;
    SharedPreferences sharedPreferences2;
    SharedPreferences sharedPreferences3;
    SharedPreferences sharedPreferences4;
    SharedPreferences sharedPreferences5;
    TextView record_1_TextView;
    TextView record_2_TextView;
    TextView record_3_TextView;
    TextView record_4_TextView;
    TextView record_5_TextView;

    ImageView emotion_1_ImageView;
    ImageView emotion_2_ImageView;
    ImageView emotion_3_ImageView;
    ImageView emotion_4_ImageView;
    ImageView emotion_5_ImageView;

    ImageButton backspace_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_1_);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        onResume();

        /* 뒤로가기 버튼  */
        backspace_Btn = (ImageButton)findViewById(R.id.backspace_Btn);
        backspace_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        record_1_TextView =(TextView)findViewById(R.id.record_1_TextView);
        record_2_TextView =(TextView)findViewById(R.id.record_2_TextView);
        record_3_TextView =(TextView)findViewById(R.id.record_3_TextView);
        record_4_TextView =(TextView)findViewById(R.id.record_4_TextView);
        record_5_TextView =(TextView)findViewById(R.id.record_5_TextView);
        emotion_1_ImageView = (ImageView)findViewById(R.id.emotion_1_ImageView) ;
        emotion_2_ImageView = (ImageView)findViewById(R.id.emotion_2_ImageView) ;
        emotion_3_ImageView = (ImageView)findViewById(R.id.emotion_3_ImageView) ;
        emotion_4_ImageView = (ImageView)findViewById(R.id.emotion_4_ImageView) ;
        emotion_5_ImageView = (ImageView)findViewById(R.id.emotion_5_ImageView) ;

        sharedPreferences1 = getSharedPreferences("record_1", 0);
        sharedPreferences2 = getSharedPreferences("record_2", 0);
        sharedPreferences3 = getSharedPreferences("record_3", 0);
        sharedPreferences4 = getSharedPreferences("record_4", 0);
        sharedPreferences5 = getSharedPreferences("record_5", 0);

        String str1_1 = sharedPreferences1.getString("recognition", null);
        String str1_2 = sharedPreferences1.getString("bad", null);
        String str2_1 = sharedPreferences2.getString("recognition", null);
        String str2_2 = sharedPreferences2.getString("bad", null);
        String str3_1 = sharedPreferences3.getString("recognition", null);
        String str3_2 = sharedPreferences3.getString("bad", null);
        String str4_1 = sharedPreferences4.getString("recognition", null);
        String str4_2 = sharedPreferences4.getString("bad", null);
        String str5_1 = sharedPreferences5.getString("recognition", null);
        String str5_2 = sharedPreferences5.getString("bad", null);

        if(str1_1 != null && str1_2 != null){
             record_1_TextView.setText("인식률 : "+str1_1+ " %\n" +"악필률 : "+str1_2+ " %");
        }else{
            record_1_TextView.setText("기록없음");
        }
        if(str2_1 != null && str2_2 != null){
            record_2_TextView.setText("인식률 : "+str1_1+ " %\n"+ "악필률 : "+str1_2+ " %");
        }else{
            record_2_TextView.setText("기록없음");
        }
        if(str3_1 != null && str3_2 != null){
            record_3_TextView.setText("인식률 : "+str1_1+ " %\n"+ "악필률 : "+str1_2+ " %");
        }else{
            record_3_TextView.setText("기록없음");
        }
        if(str4_1 != null && str4_2 != null){
            record_4_TextView.setText("인식률 : "+str1_1+ " %\n"+ "악필률 : "+str1_2+ " %");
        }else{
            record_4_TextView.setText("기록없음");
        }
        if(str5_1 != null && str5_2 != null){
            record_5_TextView.setText("인식률 : "+str1_1+ " %\n"+ "악필률 : "+str1_2+ " %");
        }else{
            record_5_TextView.setText("기록없음");
        }

        if(str1_2 != null){
            if(Double.parseDouble(str1_2) >=70){
                emotion_1_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_5));
            }else if(Double.parseDouble(str1_2) >= 50){
                emotion_1_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_4));
            }else if(Double.parseDouble(str1_2) >= 30){
                emotion_1_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_3));
            }else if(Double.parseDouble(str1_2) >= 10){
                emotion_1_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_2));
            }else {
                emotion_1_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_1));
            }
        }
        if(str2_2 != null){
            if(Double.parseDouble(str2_2) >=70){
                emotion_2_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_5));
            }else if(Double.parseDouble(str2_2) >= 50){
                emotion_2_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_4));
            }else if(Double.parseDouble(str2_2) >= 30){
                emotion_2_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_3));
            }else if(Double.parseDouble(str2_2) >= 10){
                emotion_2_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_2));
            }else {
                emotion_2_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_1));
            }
        }
        if(str3_2 != null){
            if(Double.parseDouble(str3_2) >=70){
                emotion_3_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_5));
            }else if(Double.parseDouble(str3_2) >= 50){
                emotion_3_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_4));
            }else if(Double.parseDouble(str3_2) >= 30){
                emotion_3_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_3));
            }else if(Double.parseDouble(str3_2) >= 10){
                emotion_3_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_2));
            }else {
                emotion_3_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_1));
            }
        }
        if(str4_2 != null){
            if(Double.parseDouble(str4_2) >=70){
                emotion_4_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_5));
            }else if(Double.parseDouble(str4_2) >= 50){
                emotion_4_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_4));
            }else if(Double.parseDouble(str4_2) >= 30){
                emotion_4_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_3));
            }else if(Double.parseDouble(str4_2) >= 10){
                emotion_4_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_2));
            }else {
                emotion_4_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_1));
            }
        }
        if(str5_2 != null){
            if(Double.parseDouble(str5_2) >=70){
                emotion_5_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_5));
            }else if(Double.parseDouble(str5_2) >= 50){
                emotion_5_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_4));
            }else if(Double.parseDouble(str5_2) >= 30){
                emotion_5_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_3));
            }else if(Double.parseDouble(str5_2) >= 10){
                emotion_5_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_2));
            }else {
                emotion_5_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_1));
            }
        }


    }
}
