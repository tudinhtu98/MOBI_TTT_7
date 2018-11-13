package com.androstock.galleryapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;

public  class CreatePhoto extends FavoriteAlbum {
    String album_name = "Create Photo";
    Button btnChooseImgage, btnFinally;
    ImageView imChooseImage;
    EditText edtName,edtStatus;
    Uri targetUri;
    Intent dataResult=new Intent();
    String choose;
    int i=0;
    int isChooseImage=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        choose=intent.getStringExtra("choose");
        setTitle(album_name);

        setContentView(R.layout.create_photo);
        btnChooseImgage=(Button) findViewById(R.id.chooseImage);
        btnFinally =(Button) findViewById(R.id.Xong);
        edtName =(EditText) findViewById(R.id.name);
        edtStatus=(EditText) findViewById(R.id.status);
        imChooseImage= (ImageView) findViewById(R.id.imageView2);

        if(choose.equals("2"))
        {
            i=0;
            isChooseImage=1;
            edtName.setText(intent.getStringExtra("name"));
            edtStatus.setText(intent.getStringExtra("status"));
            String a=intent.getStringExtra("image");

            targetUri=Uri.parse(a);
            uriToBitmap(targetUri);
            Log.d("toast13",""+i);
        }
        btnChooseImgage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });
        btnFinally.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isChooseImage==1) {
                    dataResult.putExtra("name", edtName.getText().toString());
                    dataResult.putExtra("status", edtStatus.getText().toString());
                    if (i == 1) {    // Truyền data vào intent

                        setResult(Activity.RESULT_OK, dataResult);
                    } else if (i == 0) {

                        dataResult.putExtra("image", targetUri.toString());
                        setResult(Activity.RESULT_FIRST_USER, dataResult);

                    }
                    finish();
                }

                else if(isChooseImage==0)
                {
                    Toast.makeText(CreatePhoto.this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
    public void uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            imChooseImage.setImageBitmap(image);
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
            dataResult=data;
            i=1;
            isChooseImage=1;
            targetUri = data.getData();
            uriToBitmap(targetUri);
        }
        else /*if(resultCode==RESULT_CANCELED)*/
        {
            Toast.makeText(CreatePhoto.this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                dataResult.putExtra("name", "");
                dataResult.putExtra("status", "");
                dataResult.putExtra("image", "");
                setResult(Activity.RESULT_CANCELED, dataResult);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
