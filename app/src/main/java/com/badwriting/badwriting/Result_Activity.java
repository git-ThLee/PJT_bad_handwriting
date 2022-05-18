package com.badwriting.badwriting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class Result_Activity extends AppCompatActivity {

    private AdView mAdView;

    TextView examination_TextView;

    TextView Recognition_TextView;
    ProgressBar Recognition_progressBar;
    TextView Bad_Writing_TextView;
    ProgressBar Bad_Writing_progressBar;

    ImageView emotion_ImageView;

    TextView result_1_textView;//최고
    TextView result_2_textView;//잘함
    TextView result_3_textView;//보통
    TextView result_4_textView;//악필
    TextView result_5_textView;//최악

    TextView result_text_textView;
    Boolean result_text_boolean=false;

    ImageButton backspace_Btn;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button Record_Save_Btn;
    Button record_Go_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();
        final String Recognition_string = intent.getExtras().getString("11");// 11 = 인식률 / 22 = 악필률 / 33 = 직접쓰기(이미지) / 44 = ocr 결과 텍스트 // 55 = 문구
        final String Bad_Writing_String = intent.getExtras().getString("22");
        final String result_String = intent.getExtras().getString("44");
        final String examination_String = intent.getExtras().getString("55");
        examination_TextView= (TextView)findViewById(R.id.examination_TextView);
        if(examination_String != null){
            examination_TextView.setText(examination_String);
        }else if( examination_String == null){
            examination_TextView.setText("null");
        }

        result_text_textView= (TextView)findViewById(R.id.result_text_textView);
        if(result_String != null){
            result_text_textView.setText(result_String);
        }else if( result_String == null){
            examination_TextView.setText("null");
        }

        /* 기록 저장 버튼 */
        Record_Save_Btn = (Button)findViewById(R.id.Record_Save_Btn);
        Record_Save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (examination_String){
                    case "가나다라마바사" :
                        sharedPreferences = getSharedPreferences("record_1", 0);
                        break;
                    case "아자차카타파하" :
                        sharedPreferences = getSharedPreferences("record_2", 0);
                        break;
                    case "바른손글씨" :
                        sharedPreferences = getSharedPreferences("record_3", 0);
                        break;
                    case "악필교정" :
                        sharedPreferences = getSharedPreferences("record_4", 0);
                        break;
                    case "교정기억법" :
                        sharedPreferences = getSharedPreferences("record_5", 0);
                        break;

                        default:
                            Toast.makeText(Result_Activity.this, "문제 발생", Toast.LENGTH_SHORT).show();
                            break;
                }
                editor = sharedPreferences.edit();
                editor.putString("recognition", Recognition_string );
                editor.putString("bad", Bad_Writing_String );
                editor.commit();

                Toast.makeText(Result_Activity.this, "기록 완료!", Toast.LENGTH_SHORT).show();

            }
        });

        /* 기록창 가기 버튼 */
        record_Go_Btn = (Button)findViewById(R.id.record_Go_Btn);
        record_Go_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), record_1_Activity.class);
                startActivity(intent);
            }
        });

        /* 자세히보기 관련 */
        final LinearLayout result_layout = (LinearLayout)findViewById(R.id.result_layout);
        result_layout.setVisibility(View.GONE);
        Button Detail_result_Btn = (Button)findViewById(R.id.Detail_result_Btn);
        Detail_result_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(result_text_boolean == false){
                    result_layout.setVisibility(View.VISIBLE);
                    result_text_boolean=true;
                }else{
                    result_layout.setVisibility(View.GONE);
                    result_text_boolean= false;
                }
            }
        });

        /* 프로그레스바 */
        ProgressBar_code(Recognition_string,Bad_Writing_String);
        /* 상태 표현 */
        State_code((int)Double.parseDouble(Bad_Writing_String));

        /* 뒤로가기 버튼  */
        backspace_Btn = (ImageButton)findViewById(R.id.backspace_Btn);
        backspace_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void ProgressBar_code(String Recognition_string, String Bad_Writing_String){
        Recognition_TextView = (TextView)findViewById(R.id.Recognition_TextView);
        Recognition_progressBar = (ProgressBar)findViewById(R.id.Recognition_progressBar);
        Bad_Writing_TextView = (TextView)findViewById(R.id.Bad_Writing_TextView);
        Bad_Writing_progressBar = (ProgressBar)findViewById(R.id.Bad_Writing_progressBar);

        Recognition_TextView.setText(Recognition_string+ " %");
        Recognition_progressBar.setProgress((int)Double.parseDouble(Recognition_string));
        Bad_Writing_TextView.setText(Bad_Writing_String+ " %");
        Bad_Writing_progressBar.setProgress((int)Double.parseDouble(Bad_Writing_String));
    }

    public void State_code(int state_int){
        result_1_textView = (TextView)findViewById(R.id.result_1_textView);
        result_2_textView = (TextView)findViewById(R.id.result_2_textView);
        result_3_textView = (TextView)findViewById(R.id.result_3_textView);
        result_4_textView = (TextView)findViewById(R.id.result_4_textView);
        result_5_textView = (TextView)findViewById(R.id.result_5_textView);
        emotion_ImageView = (ImageView)findViewById(R.id.emotion_ImageView);

        if(state_int >=70){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                result_5_textView.setBackground(ContextCompat.getDrawable(this,R.drawable.textview_background));
            }
            emotion_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_5));
            result_5_textView.setTextColor(Color.parseColor("#000000"));
            result_4_textView.setBackgroundResource(0);
            result_4_textView.setTextColor(Color.parseColor("#bebebe"));
            result_3_textView.setBackgroundResource(0);
            result_3_textView.setTextColor(Color.parseColor("#bebebe"));
            result_2_textView.setBackgroundResource(0);
            result_2_textView.setTextColor(Color.parseColor("#bebebe"));
            result_1_textView.setBackgroundResource(0);
            result_1_textView.setTextColor(Color.parseColor("#bebebe"));
        }else if(state_int >= 50){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                result_4_textView.setBackground(ContextCompat.getDrawable(this,R.drawable.textview_background));
            }
            emotion_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_4));
            result_4_textView.setTextColor(Color.parseColor("#000000"));
            result_5_textView.setBackgroundResource(0);
            result_5_textView.setTextColor(Color.parseColor("#bebebe"));
            result_3_textView.setBackgroundResource(0);
            result_3_textView.setTextColor(Color.parseColor("#bebebe"));
            result_2_textView.setBackgroundResource(0);
            result_2_textView.setTextColor(Color.parseColor("#bebebe"));
            result_1_textView.setBackgroundResource(0);
            result_1_textView.setTextColor(Color.parseColor("#bebebe"));
        }else if(state_int >= 30){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                result_3_textView.setBackground(ContextCompat.getDrawable(this,R.drawable.textview_background));
            }
            emotion_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_3));
            result_3_textView.setTextColor(Color.parseColor("#000000"));
            result_4_textView.setBackgroundResource(0);
            result_4_textView.setTextColor(Color.parseColor("#bebebe"));
            result_5_textView.setBackgroundResource(0);
            result_5_textView.setTextColor(Color.parseColor("#bebebe"));
            result_2_textView.setBackgroundResource(0);
            result_2_textView.setTextColor(Color.parseColor("#bebebe"));
            result_1_textView.setBackgroundResource(0);
            result_1_textView.setTextColor(Color.parseColor("#bebebe"));
        }else if(state_int >= 10){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                result_2_textView.setBackground(ContextCompat.getDrawable(this,R.drawable.textview_background));
            }
            emotion_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_2));
            result_2_textView.setTextColor(Color.parseColor("#000000"));
            result_4_textView.setBackgroundResource(0);
            result_4_textView.setTextColor(Color.parseColor("#bebebe"));
            result_3_textView.setBackgroundResource(0);
            result_3_textView.setTextColor(Color.parseColor("#bebebe"));
            result_5_textView.setBackgroundResource(0);
            result_5_textView.setTextColor(Color.parseColor("#bebebe"));
            result_1_textView.setBackgroundResource(0);
            result_1_textView.setTextColor(Color.parseColor("#bebebe"));
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                result_1_textView.setBackground(ContextCompat.getDrawable(this,R.drawable.textview_background));
            }
            emotion_ImageView.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.emotion_1));
            result_1_textView.setTextColor(Color.parseColor("#000000"));
            result_4_textView.setBackgroundResource(0);
            result_4_textView.setTextColor(Color.parseColor("#bebebe"));
            result_3_textView.setBackgroundResource(0);
            result_3_textView.setTextColor(Color.parseColor("#bebebe"));
            result_2_textView.setBackgroundResource(0);
            result_2_textView.setTextColor(Color.parseColor("#bebebe"));
            result_5_textView.setBackgroundResource(0);
            result_5_textView.setTextColor(Color.parseColor("#bebebe"));
        }

    }

}
