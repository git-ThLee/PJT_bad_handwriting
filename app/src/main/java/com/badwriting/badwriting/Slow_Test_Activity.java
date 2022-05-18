package com.badwriting.badwriting;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Slow_Test_Activity extends AppCompatActivity {

    private AdView mAdView;


    TextView examination_TextView;
    Button OCR_Btn;
    Button sayong_Btn;
    boolean sayong_boolean=false;


    int list_moongoo_int=0;

    String ocr_word;
    String editText_word;

    ImageButton image_1_Btn;
    ImageButton image_2_Btn;
    ImageButton backspace_Btn;
    ImageButton left_iamgeBtn;
    ImageButton right_iamgeBtn;

    Bitmap bitmap;

    int rotation = 0;

    /* OCR 부분 */
    Bitmap image; //사용되는 이미지
    private TessBaseAPI mTess; //Tess API reference
    String datapath = "" ; //언어데이터가 있는 경로
    private String lang = "";
    private ImageView imageView;
    private Button button;
    private static final int REQUEST_CODE = 0;

    // 이미지 불러오기 관련
    Button Gallery_Btn;
    Button Camera_Btn;
    ImageView Main_imageView;
    private final int GET_GALLERY_IMAGE = 200;
    private File file;
    final int PICTURE_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slow__test);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        final String[] moongoolist = getResources().getStringArray(R.array.moongoolist);
        examination_TextView = (TextView)findViewById(R.id.examination_TextView);
        examination_TextView.setText(moongoolist[list_moongoo_int]);
        Main_imageView = (ImageView)findViewById(R.id.Main_imageView);

        final LinearLayout sayong_layout = (LinearLayout)findViewById(R.id.sayong_layout);
        sayong_layout.setVisibility(View.GONE);

        Intent intent = getIntent();
        byte[] byteArray = getIntent().getByteArrayExtra("33");
        if(byteArray != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            Main_imageView.setImageBitmap(bitmap);
        }

        /* 왼쪽 이미지 버튼 클릭 */
        left_iamgeBtn = (ImageButton)findViewById(R.id.left_iamgeBtn);
        left_iamgeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list_moongoo_int == 0){
                    list_moongoo_int = 4;
                }else{
                    list_moongoo_int -=1;
                }
                examination_TextView.setText(moongoolist[list_moongoo_int]);
            }
        });
        /* 오른쪽 이미지 버튼 클릭 */
        right_iamgeBtn = (ImageButton)findViewById(R.id.right_iamgeBtn);
        right_iamgeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list_moongoo_int == 4){
                    list_moongoo_int = 0;
                }else{
                    list_moongoo_int +=1;
                }
                examination_TextView.setText(moongoolist[list_moongoo_int]);
            }

        });


        /* OCR 부분 */
        //언어파일 경로 or 데이터 경로
        datapath = getFilesDir()+ "/tesseract/";
        //트레이닝데이터가 카피되어 있는지 체크
        checkFile(new File(datapath + "tessdata/"));

        //Tesseract API or 문자 인식을 수행할 tess 객체 생성
        lang = "kor";
        mTess = new TessBaseAPI();
        mTess.init(datapath, lang);
        String OCRresult = null;
       // ocr_result_TextView = (TextView)findViewById(R.id.ocr_result_TextView);

        /* 이미지 클릭시 */
        Main_imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final CharSequence[]  PhotoModels = {"직접쓰기","갤러리","카메라"};
                final AlertDialog.Builder alt_bld = new AlertDialog.Builder(Slow_Test_Activity.this);
                alt_bld.setTitle("사진 가져오기");

                alt_bld.setSingleChoiceItems(PhotoModels, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if(item == 0 ){//직접쓰기
                            Intent intent = new Intent(getApplicationContext(), Drawing_Activity.class);
                            intent.putExtra("55",examination_TextView.getText()); // 11 = 인식 / 22 = 악필 / 33 = 직접쓰기(이미지) / 44 = ocr 결과 텍스트 // 55 = 문구
                            startActivity(intent);
                            finish();
                            dialog.cancel();
                        }else if(item ==1){//갤러리
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"),  PICTURE_REQUEST_CODE);
                            dialog.cancel();
                        }else if(item == 2 ){ //카메라
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, 101);
                            dialog.cancel();
                        }

                    }
                });
                alt_bld.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                final AlertDialog alert = alt_bld.create();
                alert.show();
            }
        });


        /* 검사하기 버튼 */

        OCR_Btn = (Button)findViewById(R.id.OCR_Btn);
        OCR_Btn.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if(Main_imageView.getDrawable() != null) {
                        Drawable temp = Main_imageView.getDrawable();
                        Bitmap tmpBitmap = ((BitmapDrawable) temp).getBitmap();

                        Matrix matrix = new Matrix();
                        matrix.postRotate(rotation); // 회전한 각도 입력
                        bitmap = Bitmap.createBitmap(tmpBitmap, 0, 0, tmpBitmap.getWidth(), tmpBitmap.getHeight(), matrix, true);

                        mTess.setImage(bitmap);
                        String OCRresult = null;
                        OCRresult = mTess.getUTF8Text();
                        if(OCRresult != null || OCRresult != ""){
                            OCR_TEXT_CHANG( OCRresult , examination_TextView.getText().toString() );
                        }else{
                            Toast.makeText(Slow_Test_Activity.this, "분석 사진이 없습니다", Toast.LENGTH_SHORT).show();
                        }

                    }
            }
        });

        /* 회전 버튼  */
        image_1_Btn = (ImageButton)findViewById(R.id.image_1_Btn);
        image_1_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main_imageView.setRotation(Main_imageView.getRotation()+90);
                rotation -= 90;
            }
        });
        /* 회전 버튼  */
        image_2_Btn = (ImageButton)findViewById(R.id.image_2_Btn);
        image_2_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main_imageView.setRotation(Main_imageView.getRotation()-90);
                rotation += 90;
            }
        });
        /* 뒤로가기 버튼  */
        backspace_Btn = (ImageButton)findViewById(R.id.backspace_Btn);
        backspace_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        /* 사용법 버튼  */
        sayong_Btn = (Button)findViewById(R.id.sayong_Btn);
        sayong_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sayong_boolean ==false){
                    sayong_layout.setVisibility(View.VISIBLE);
                    sayong_boolean=true;
                }else{
                    sayong_layout.setVisibility(View.GONE);
                    sayong_boolean= false;
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Main_imageView = (ImageView)findViewById(R.id.Main_imageView);
        if (requestCode == PICTURE_REQUEST_CODE) {
            Uri selectedImageUri = data.getData();
            Main_imageView.setImageURI(selectedImageUri);

        }else if (requestCode == 101 && resultCode == Activity.RESULT_OK && data.hasExtra("data")) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            if (bitmap != null) {
                Main_imageView.setImageBitmap(bitmap);
            }
        }
    }
    /* OCR 부분 */
    //copy file to device
    private void copyFiles() {
        try{
            String filepath = datapath + "/tessdata/kor.traineddata";
            AssetManager assetManager = getAssets();
            InputStream instream = assetManager.open("tessdata/kor.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //check file on the device
    private void checkFile(File dir) {
        //디렉토리가 없으면 디렉토리를 만들고 그후에 파일을 카피
        if(!dir.exists()&& dir.mkdirs()) {
            copyFiles();
        }
        //디렉토리가 있지만 파일이 없으면 파일카피 진행
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/kor.traineddata";
            File datafile = new File(datafilepath);
            if(!datafile.exists()) {
                copyFiles();
            }
        }
    }

    /* **********************************************
     * 자음 모음 분리
     * 설연수 -> ㅅㅓㄹㅇㅕㄴㅅㅜ,    바보 -> ㅂㅏㅂㅗ
     * **********************************************/
    /** 초성 - 가(ㄱ), 날(ㄴ) 닭(ㄷ) */
    /** ㄱ ㄲ ㄴ ㄷ ㄸ ㄹ ㅁ ㅂ ㅃ ㅅ ㅆ ㅇ ㅈ ㅉ ㅊ ㅋ ㅌ ㅍ ㅎ */
    public static char[] arrChoSung = { 0x3131, 0x3132, 0x3134, 0x3137, 0x3138,
            0x3139, 0x3141, 0x3142, 0x3143, 0x3145, 0x3146, 0x3147, 0x3148,
            0x3149, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e };

    /** 중성 - 가(ㅏ), 야(ㅑ), 뺨(ㅑ)*/
    /* ㅏ ㅐ ㅑ ㅒ ㅓ ㅔ ㅕ ㅖ ㅗ ㅘ ㅙ ㅚ ㅛ ㅜ ㅝ ㅞ ㅟ ㅠ ㅡ ㅢ ㅣ */
    public static char[] arrJungSung = { 0x314f, 0x3150, 0x3151, 0x3152,
            0x3153, 0x3154, 0x3155, 0x3156, 0x3157, 0x3158, 0x3159, 0x315a,
            0x315b, 0x315c, 0x315d, 0x315e, 0x315f, 0x3160, 0x3161, 0x3162,
            0x3163 };
    /** 종성 - 가(없음), 갈(ㄹ) 천(ㄴ) */
    /** X ㄱ ㄲ ㄳ ㄴ ㄵ ㄶ ㄷ ㄹ ㄺ ㄻ ㄼ ㄽ ㄾ ㄿ ㅀ ㅁ ㅂ ㅄ ㅅ ㅆ ㅇ ㅈ ㅊ ㅋ ㅌ ㅍ ㅎ */
    public static char[] arrJongSung = { 0x0000, 0x3131, 0x3132, 0x3133,
            0x3134, 0x3135, 0x3136, 0x3137, 0x3139, 0x313a, 0x313b, 0x313c,
            0x313d, 0x313e, 0x313f, 0x3140, 0x3141, 0x3142, 0x3144, 0x3145,
            0x3146, 0x3147, 0x3148, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void OCR_TEXT_CHANG(String ocr_str, String editText_str){

        ocr_word = ocr_str;
        editText_word = editText_str;

        int word_length=0;
        if(ocr_word.length() >= editText_word.length()){
            word_length = ocr_word.length();
        }else{
            word_length = editText_word.length();
        }

        int ocr_word_count=0;
        String[] ocr_chosung= new String[word_length];
        String[] ocr_jungsung= new String[word_length];
        String[] ocr_jongsung= new String[word_length];

        int editText_word_count = 0;
        String[] editText_chosung= new String[word_length];
        String[] editText_jungsung= new String[word_length];
        String[] editText_jongsung= new String[word_length];


            for (int i = 0; i < ocr_word.length(); i++) {
                /*  한글자씩 읽어들인다. */
                char chars = (char) (ocr_word.charAt(i) - 0xAC00); // 한글+영어 한글자씩
                if (chars >= 0 && chars <= 11172) {
                    /* A. 자음과 모음이 합쳐진 글자인경우 */
                    /* A-1. 초/중/종성 분리 */
                    ocr_chosung[i] = Integer.toString(chars / (21 * 28));
                    ocr_jungsung[i] = Integer.toString(chars % (21 * 28) / 28);
                    ocr_jongsung[i] = Integer.toString(chars % (21 * 28) % 28);
                } else {
                    /*  한글이 아니거나 자음만 있을경우 */
                }//if
            }
            for (int i = 0; i < editText_word.length(); i++) {
                /*  한글자씩 읽어들인다. */
                char chars = (char) (editText_word.charAt(i) - 0xAC00); // 한글+영어 한글자씩
                if (chars >= 0 && chars <= 11172) {
                    /* A. 자음과 모음이 합쳐진 글자인경우 */
                    /* A-1. 초/중/종성 분리 */
                    editText_chosung[i] = Integer.toString(chars / (21 * 28));
                    editText_jungsung[i] = Integer.toString(chars % (21 * 28) / 28);
                    editText_jongsung[i] = Integer.toString(chars % (21 * 28) % 28);
                } else {
                    /*  한글이 아니거나 자음만 있을경우 */
                }//if
            }

        int count_Bad_writing = 0 ;
        int count_Good_writing = 0 ;

        for(int i = 0 ; i < word_length; i++){
            if(ocr_chosung[i] == null){
                ocr_chosung[i]=null;
            }if(ocr_jungsung[i] == null){
                ocr_jungsung[i]=null;
            }if( ocr_jongsung[i] == null){
                ocr_jongsung[i]=null;
            }else if(Integer.parseInt(ocr_jongsung[i]) == 0){
                ocr_jongsung[i]=null;
            }

            if(editText_chosung[i] == null){
                editText_chosung[i]=null;
            }if(editText_jungsung[i] == null){
                editText_jungsung[i]=null;
            }if(editText_jongsung[i] == null){
                editText_jongsung[i]=null;
            }else if( Integer.parseInt(editText_jongsung[i]) == 0){
                editText_jongsung[i]=null;
            }

            if(ocr_chosung[i]!= null){
                if(editText_chosung[i] != null){
                    if(ocr_chosung[i] == editText_chosung[i]){
                        count_Good_writing++;
                    }
                    if(ocr_chosung[i] != editText_chosung[i]){
                        count_Bad_writing++;
                    }
                }
            }else if(ocr_chosung[i]== null){
                if(editText_chosung[i] != null){
                    count_Bad_writing++;
                }
            }
            if(ocr_jungsung[i]!= null){
                if(editText_jungsung[i] != null){
                    if(ocr_jungsung[i] == editText_jungsung[i]){
                        count_Good_writing++;
                    }
                    if(ocr_jungsung[i] != editText_jungsung[i]){
                        count_Bad_writing++;
                    }
                }
            }else if(ocr_jungsung[i]== null){
                if(editText_jungsung[i] != null){
                    count_Bad_writing++;
                }
            }
            if(ocr_jongsung[i]!= null){
                if(editText_jongsung[i] != null){
                    if(ocr_jongsung[i] == editText_jongsung[i]){
                        count_Good_writing++;
                    }
                    if(ocr_jongsung[i] != editText_jongsung[i]){
                        count_Bad_writing++;
                    }
                }
            }else if(ocr_jongsung[i]== null){
                if(editText_jongsung[i] != null){
                    count_Bad_writing++;
                }
            }
        }

        if(ocr_word.length() != 0){
            int int1 = (ocr_word.length()*100)/editText_word.length();
            String result1 = String.valueOf(Math.abs((float)(int1)));
            double int2 = (double) ocr_word.length()/(double) editText_word.length();
            double int3 = (double) count_Bad_writing+ (double) count_Good_writing;
            if( count_Bad_writing + count_Good_writing == 0 ){
                count_Bad_writing =1;
                int3 = 1.0;
            }
            double int4 = Math.abs(((count_Bad_writing*100)/int3));
            String result2 = String.format("%.1f",int4);
            Intent intent = new Intent(getApplicationContext(), Result_Activity.class);

            intent.putExtra("11",result1);// 11 = 인식 / 22 = 악필 / 33 = 직접쓰기(이미지) / 44 = ocr 결과 텍스트 // 55 = 문구
            intent.putExtra("22",result2);
            intent.putExtra("44",ocr_word);
            examination_TextView = (TextView)findViewById(R.id.examination_TextView) ;

            intent.putExtra("55",examination_TextView.getText());
            startActivity(intent);
        }else if(ocr_word.length() == 0){
            Toast.makeText(this, "올바른 사진을 넣어주세요", Toast.LENGTH_SHORT).show();
        }

    }

}
