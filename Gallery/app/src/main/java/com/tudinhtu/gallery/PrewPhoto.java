package com.tudinhtu.gallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class PrewPhoto extends FavoriteAlbum {
    ImageView imageTarget;
    TextView tvName,tvStatus;
    ImageButton imbCamera;
    static final int PICTURE_RESULT = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prew_photo);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        tvName=(TextView)findViewById(R.id.tvName);
        tvStatus=(TextView)findViewById(R.id.tvStatus);
        imageTarget=(ImageView) findViewById(R.id.imageTarget);
        tvName.setText(intent.getStringExtra("name"));
        tvStatus.setText(intent.getStringExtra("status"));
        String a=intent.getStringExtra("image");

        targetUri= Uri.parse(a);
        uriToBitmap(targetUri);
        imbCamera=(ImageButton) findViewById(R.id.imageButton3);
        imbCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               imbCamera.setVisibility(View.INVISIBLE);
               takeScreenshot();
               imbCamera.setVisibility(View.VISIBLE);
            }
        });
    }
    public void uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            imageTarget.setImageBitmap(image);
            parcelFileDescriptor.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            imageFile.setReadable(true,false);

            imageFile.setWritable(true,false);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            MediaStore.Images.Media.insertImage(getContentResolver(),imageFile.getAbsolutePath(),imageFile.getName(),imageFile.getName());
            Log.d("toast14","1");
            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }

    }
    private void openScreenshot(File imageFile) {

        Uri uri = Uri.fromFile(imageFile);
        uriToBitmap(uri);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);

        if(intent.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(intent, PICTURE_RESULT);
        }
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);

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
}
