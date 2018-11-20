package com.tudinhtu.gallery;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedInputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public  class FavoriteAlbum extends AppCompatActivity {
    String album_name = "Special Photo";
    Button btnAdd,btnChange,btnSee;
    ImageView targetImage;
    TextView twName,twStatus;
    Intent dataResult=new Intent();
    int isCreateNewAlbum=0;
    int i=0;
    Uri targetUri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_album);
        setTitle(album_name);
        btnAdd= (Button) findViewById(R.id.addPhoto);
        twName=(TextView)findViewById(R.id.twName) ;
        twStatus=(TextView)findViewById(R.id.twStatus) ;
        targetImage = (ImageView)findViewById(R.id.targetImage);
        btnChange=(Button) findViewById(R.id.change);
        btnSee=(Button) findViewById(R.id.prew);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FavoriteAlbum.this, CreatePhoto.class);
                intent.putExtra("choose","1");
                startActivityForResult(intent,0);
                /*
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);*/
            }
        });
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCreateNewAlbum==1)
                {
                    Intent intent = new Intent(FavoriteAlbum.this, CreatePhoto.class);
                    intent.putExtra("choose","2");
                    intent.putExtra("image",targetUri.toString());
                    intent.putExtra("name",twName.getText().toString());
                    intent.putExtra("status",twStatus.getText().toString());
                    startActivityForResult(intent,0);
                }
                else
                {
                    Toast.makeText(FavoriteAlbum.this, "Tạo photo trước", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isCreateNewAlbum==1)
                {
                    Intent intent = new Intent(FavoriteAlbum.this, PrewPhoto.class);
                    intent.putExtra("image",targetUri.toString());
                    intent.putExtra("name",twName.getText().toString());
                    intent.putExtra("status",twStatus.getText().toString());
                    startActivityForResult(intent,0);
                }
                else
                {
                    Toast.makeText(FavoriteAlbum.this, "Tạo photo trước", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        //targetUri=restoringPreferences();
       /* Log.d("toast2",targetUri.toString());
        i=0;
        dataResult.putExtra("link",targetUri.toString());
        uriToBitmap(targetUri);*/
    }
    void uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            targetImage.setImageBitmap(image);
            parcelFileDescriptor.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){

            targetUri = data.getData();

            uriToBitmap(targetUri);
            twName.setText(data.getStringExtra("name"));
            twStatus.setText(data.getStringExtra("status"));
            isCreateNewAlbum=1;
            btnAdd.setVisibility(View.INVISIBLE);
        }
        else if(resultCode==RESULT_FIRST_USER)
        {

            String a=data.getStringExtra("image");
            targetUri=Uri.parse(a);
            isCreateNewAlbum=1;
            uriToBitmap(targetUri);
            twName.setText(data.getStringExtra("name"));
            twStatus.setText(data.getStringExtra("status"));
        }
        else if(resultCode==RESULT_CANCELED)
        {

        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    /*@Override
    protected void onResume() {
        super.onResume();
        targetUri= restoringPreferences();

    }

    @Override
    protected void onPause() {
        super.onPause();
        savingPreferences();
    }
    public void savingPreferences()
    {
        SharedPreferences pre=getSharedPreferences(prefnames,MODE_PRIVATE);
        SharedPreferences.Editor editor=pre.edit();
        editor.clear();
        editor.putString("link",targetUri.toString());

        editor.commit();

    }
    public  Uri restoringPreferences()
    {
        SharedPreferences pre=getSharedPreferences(prefnames,MODE_PRIVATE);
        targetUri = Uri.parse(pre.getString("link",""));
        return targetUri;
    }*/
}

