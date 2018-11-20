package com.tudinhtu.gallery;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class GalleryPreview extends AppCompatActivity {

    ImageView GalleryPreviewImg;
    String path, pathInDetails;
    String imageName, lastModDate;
    String[] namePart;
    int position;
    int positionShow=0;
    long space;
    static ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> datelist = new ArrayList<HashMap<String, String>>();
    GestureDetector gestureDetector;
    final int SWIPE_THRESHOLD = 200;
    final int SWIPE_VELOCITY = 250;
    final int SWIPE_THRESHOLD_VER = 300;
    final int SWIPE_VELOCITY_VER = 250;
    Handler myHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_preview);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 1);
        String choose = intent.getStringExtra("choose");
        if (choose.equals("1"))
        {
            Thread myBackgroundThread = new Thread( backgroundTask, "backAlias1" );
            Log.d("toast14",choose);
            myBackgroundThread.start();
        }


        path = list.get(position).get(Function.KEY_PATH);
        GalleryPreviewImg = (ImageView) findViewById(R.id.GalleryPreviewImg);
        updateImageInfo(position);


        setTitle(imageName);

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
        });
    }
    private Runnable foregroundRunnable = new Runnable() {
        @Override
        public void run() {
            try {

                path = list.get(positionShow).get(Function.KEY_PATH);
                Log.d("toast15","1");
                updateImageInfo(positionShow);
                setTitle(imageName);
                Glide.with(GalleryPreview.this)
                        .load(new File(list.get(positionShow).get(Function.KEY_PATH)))
                        .apply(new RequestOptions().fitCenter())
                        .into(GalleryPreviewImg);

            } catch (Exception e) {
                Log.e("<<foregroundTask>>", e.getMessage());
            }
        }
    };
    private Runnable backgroundTask = new Runnable() {
        @Override
        public void run() {

            try {
                for (int n = 0; n < list.size(); n++) {

                    Thread.sleep(1500);
                    positionShow++;
                    myHandler.post(foregroundRunnable);
                }

            } catch (InterruptedException e) {
                Log.e("<<foregroundTask>>", e.getMessage());
            }

        }// run
    };// backgroundTask
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;
            case R.id.Detail_menu:
            {
                CharSequence details = "Name: " + imageName + "\nPath: " + pathInDetails
                        + "\nDate modified: " + lastModDate + "\nSize: " + space + " KB";
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(GalleryPreview.this, details, duration).show();
                break;
            }
            case R.id.SetWallpaper_menu:
            {
                setWallpaper();
                break;
            }
            case R.id.delete_menu:
            {
                deleteImage();
                break;
            }
            case R.id.crop_menu:
            {
                cropImage();
                break;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    public void cropImage() {


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gallery_menu, menu);
        return true;
    }

    public void setWallpaper() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        WallpaperManager myWallpaperManager = WallpaperManager
                .getInstance(this);
        Bitmap myBitmap = BitmapFactory.decodeFile(path);
        Bitmap useThisBitmap;
        float originalWidth = myBitmap.getWidth();
        float originalHeight = myBitmap.getHeight();
        if(originalHeight < originalWidth){
            float scale;
            int scaleWidth;
            if(originalHeight > height){
                scale = originalHeight*1.0f / height;
                scaleWidth = (int) (myBitmap.getWidth() / scale);
            }
            else {
                scale = height / originalHeight;
                scaleWidth = (int) (myBitmap.getWidth() * scale);
            }
            useThisBitmap = Bitmap.createScaledBitmap(
                    myBitmap, scaleWidth , height, true);
            myBitmap.recycle();
        } else{
            float scale;
            int scaleHeight;
            if(originalWidth > width){
                scale = originalWidth*1.0f / width;
                scaleHeight = (int) (myBitmap.getHeight() / scale);
            }
            else {
                scale = width / originalWidth;
                scaleHeight = (int) (myBitmap.getHeight() * scale);
            }
            useThisBitmap = Bitmap.createScaledBitmap(
                    myBitmap, width, scaleHeight, true);
            myBitmap.recycle();
        }

        if (myBitmap != null) {
            try {
                myWallpaperManager.setBitmap(useThisBitmap);
                Toast.makeText(this, "Set wallpaper successfully", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "Failed to set Backgroundimage", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Failed to decode image.", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteImage() {
        File file = new File(path);
        file.delete();

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path))));
       /* ContentResolver contentResolver = getContentResolver();
        contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.ImageColumns.DATA + "=?" , new String[]{ path });*/
        list.remove(position);
        if (file.exists()){
            if (file.delete()){
                Toast.makeText(GalleryPreview.this, "Deleted!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(GalleryPreview.this, "This image not deleted.", Toast.LENGTH_SHORT).show();
            }
        }
        AlbumActivity.posDelete=position;

        AlbumActivity.isdelete=1;
        finish();
    }


    public void updateImageInfo(int positionTemp) {
        path = list.get(positionTemp).get(Function.KEY_PATH);
        pathInDetails = "";
        namePart = path.split("/");
        for (int i = 1; i < namePart.length-1; i++) {
            pathInDetails += "/" + namePart[i];
        }
        imageName = namePart[namePart.length - 1];

        File image = new File(path);
        String date;
        String[] dateModParts;
        if (image.exists()) {
            Date lastModified = new Date(image.lastModified());
            date = lastModified.toString();
            dateModParts = date.split(" ");
            lastModDate = dateModParts[2] + " " + dateModParts[1] + " " + dateModParts[5] + " " + dateModParts[3];
            space = image.length() / 1000;
        }
    }

    public class ExifInterface extends GalleryPreview {

    }

    class MyGesture extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //Kéo từ trái sang phải
            if(e2.getX() - e1.getX() > SWIPE_THRESHOLD*0.7  && Math.abs(velocityX) > SWIPE_VELOCITY && 0 < position ){
                position--;
                updateImageInfo(position);
                setTitle(imageName);
                Glide.with(GalleryPreview.this)
                        .load(new File(list.get(position).get(Function.KEY_PATH)))
                        .apply(new RequestOptions().fitCenter().optionalFitCenter())
                        .into(GalleryPreviewImg);
            }
            //Kéo từ phải sang trái
            if(e1.getX() - e2.getX() > SWIPE_THRESHOLD*0.7  && Math.abs(velocityX) > SWIPE_VELOCITY && position < list.size() - 1){
                //Next hình tiếp theo
                position++;
                updateImageInfo(position);
                setTitle(imageName);
                Glide.with(GalleryPreview.this)
                        .load(new File(list.get(position).get(Function.KEY_PATH))) // Uri of the picture
                        .apply(new RequestOptions().fitCenter().optionalFitCenter())
                        .into(GalleryPreviewImg);
            }
            //Kéo từ trên xuống dưới
            if(e2.getY() - e1.getY() > SWIPE_THRESHOLD_VER  && Math.abs(velocityY) > SWIPE_VELOCITY_VER){
                list = null; //Giải phóng dung lượng của biến
                finish();
            }
            //Kéo từ dưới lên trên
            if(e1.getY() - e2.getY() > SWIPE_THRESHOLD_VER  && Math.abs(velocityY) > SWIPE_VELOCITY_VER){
                list = null; //Giải phóng dung lượng của biến
                finish();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }


}
