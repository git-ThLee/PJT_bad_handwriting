package com.badwriting.badwriting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class Drawing_Activity extends AppCompatActivity {

    LinearLayout draw_Layout;
    MyView m;

    ImageButton backspace_Btn;
    ImageButton save_Btn;
    ImageButton clean_Btn;
    ImageButton re_write_Btn;

    TextView examination_TextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing_);

        draw_Layout = (LinearLayout) findViewById(R.id.draw_Layout);
        m = new MyView(this);
        // setContentView(m);
        examination_TextView = (TextView)findViewById(R.id.examination_TextView);
        Intent intent = getIntent();
        String Recognition_string = intent.getExtras().getString("55");
        examination_TextView.setText(Recognition_string);

        /* 뒤로가기 버튼  */
        backspace_Btn = (ImageButton) findViewById(R.id.backspace_Btn);
        backspace_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Intent intent = new Intent(getApplicationContext(), Slow_Test_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        /* 저장 버튼  */
        save_Btn = (ImageButton) findViewById(R.id.save_Btn);
        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[]  PhotoModels = {"저장","취소"};
                final AlertDialog.Builder alt_bld = new AlertDialog.Builder(Drawing_Activity.this);
                alt_bld.setTitle("저장하시겠습니까");
                alt_bld.setSingleChoiceItems(PhotoModels, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if(item == 0 ){//저장
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            /* 부분 화면 스크린샷 ( Layout ) */
                            draw_Layout.setDrawingCacheEnabled(true);
                            Bitmap layout_bitmap = draw_Layout.getDrawingCache();

                            Bitmap resized2 = Bitmap.createScaledBitmap(layout_bitmap, 250, 250, true);
                            resized2.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byte[] byteArray2 = stream.toByteArray();

                            Intent intent = new Intent(getApplicationContext(), Slow_Test_Activity.class);
                            intent.putExtra("33",byteArray2);
                            startActivity(intent);
                            finish();
                            dialog.cancel();
                        }else if(item ==1){//취소
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
        /* 리셋 버튼 */
        clean_Btn = (ImageButton)findViewById(R.id.clean_Btn);
        clean_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.reset_paint();
            }
        });

        /* 연필 버튼 */
        re_write_Btn = (ImageButton)findViewById(R.id.re_write_Btn);
        re_write_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.re_write_paint();
            }
        });
        draw_Layout.addView(m);
    }


    public class MyView extends View {


        private Path drawPath;
        private Paint drawPaint, canvasPaint;
        private int paintColor = 0xffffffff;
        private Canvas drawCanvas;
        private Bitmap canvasBitmap;

        public MyView(Context context) {

            super(context);
            setupDrawing();


        }
        public  void re_write_paint(){
            drawPaint.setColor(0xffffffff);
            drawPaint.setStrokeWidth(10);
            invalidate(); // 화면을 갱신함 -> onDraw()를 호출
        }
        public void reset_paint() {
            drawPaint.setColor(0xff000000);
            drawPaint.setStrokeWidth(5000);
            invalidate(); // 화면을 갱신함 -> onDraw()를 호출
        }

        private void setupDrawing() {

            drawPath = new Path();
            drawPaint = new Paint();
            drawPaint.setColor(paintColor);
            drawPaint.setAntiAlias(true);
            drawPaint.setStrokeWidth(10); // 선두께
            drawPaint.setStyle(Paint.Style.STROKE);
            drawPaint.setStrokeJoin(Paint.Join.ROUND);
            drawPaint.setStrokeCap(Paint.Cap.ROUND);
            canvasPaint = new Paint(Paint.DITHER_FLAG);
        }

        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            drawCanvas = new Canvas(canvasBitmap);
        }

        protected void onDraw(Canvas canvas) {

            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
            canvas.drawPath(drawPath, drawPaint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float touchX = event.getX();
            float touchY = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    drawPath.moveTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    drawPath.lineTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_UP:
                    drawPath.lineTo(touchX, touchY);
                    drawCanvas.drawPath(drawPath, drawPaint);
                    drawPath.reset();

                    break;
                default:
                    return false;
            }

            invalidate();
            return true;
        }


    }

}