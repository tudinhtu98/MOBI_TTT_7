package com.tudinhtu.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SHAJIB on 25/12/2015.
 */
public class GalleryPreview extends AppCompatActivity {

    ImageView GalleryPreviewImg;
    Button btnBack;
    String path;
    int position;
    int i = 0;
    static ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    GestureDetector gestureDetector;
    final int SWIPE_THRESHOLD = 200;
    final int SWIPE_VELOCITY = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.gallery_preview);
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 1);
        path = list.get(position).get(Function.KEY_PATH);
        GalleryPreviewImg = (ImageView) findViewById(R.id.GalleryPreviewImg);
        setTitle("Ảnh " + (position + 1));

        Glide.with(GalleryPreview.this)
                .load(new File(path)) // Uri of the picture
                .into(GalleryPreviewImg);

        gestureDetector = new GestureDetector(this,new MyGesture());
        GalleryPreviewImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return false;
            }

        if(i==0)
            {
                getSupportActionBar().hide();
                i=1;
            }
                else
            {
                getSupportActionBar().show();
                i=0;
            }
        });

    }

    class MyGesture extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //Kéo từ trái sang phải
            if(e2.getX() - e1.getX() > SWIPE_THRESHOLD  && Math.abs(velocityX) > SWIPE_VELOCITY && 0 < position ){
                position--;
                setTitle("Ảnh " + (position + 1));
                Glide.with(GalleryPreview.this)
                        .load(new File(list.get(position).get(Function.KEY_PATH))) // Uri of the picture
                        .into(GalleryPreviewImg);
            }
            //Kéo từ phải sang trái
            if(e1.getX() - e2.getX() > SWIPE_THRESHOLD  && Math.abs(velocityX) > SWIPE_VELOCITY && position < list.size() - 1){
                //Next hình tiếp theo
                position++;
                setTitle("Ảnh " + (position + 1));
                Glide.with(GalleryPreview.this)
                        .load(new File(list.get(position).get(Function.KEY_PATH))) // Uri of the picture
                        .into(GalleryPreviewImg);
            }
            //Kéo từ trên xuống dưới
            if(e2.getY() - e1.getY() > SWIPE_THRESHOLD  && Math.abs(velocityY) > SWIPE_VELOCITY){
                list = null; //Giải phóng dung lượng của biến
                finish();
            }
            //Kéo từ dưới lên trên
            if(e1.getY() - e2.getY() > SWIPE_THRESHOLD  && Math.abs(velocityY) > SWIPE_VELOCITY){
                list = null; //Giải phóng dung lượng của biến
                finish();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}
