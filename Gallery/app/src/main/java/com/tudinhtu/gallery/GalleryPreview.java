package com.androstock.galleryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by SHAJIB on 25/12/2015.
 */
public class GalleryPreview extends MainActivity {

    ImageView GalleryPreviewImg;
    Button btnBack;
    String path;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gallery_preview);

        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        GalleryPreviewImg = (ImageView) findViewById(R.id.GalleryPreviewImg);
        GalleryPreviewImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("toast13","1");
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
            }
        });


        btnBack= (Button) findViewById(R.id.Back01);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("toast","1");
                finish();

            }
        });
        Glide.with(GalleryPreview.this)
                .load(new File(path)) // Uri of the picture
                .into(GalleryPreviewImg);
    }
}

