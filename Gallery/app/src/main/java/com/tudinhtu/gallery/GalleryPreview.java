package com.tudinhtu.gallery;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
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
    BottomNavigationView bottomNavigationView;
    //private Bitmap mBitmap;
    Matrix matrix = new Matrix();
    float degrees = 0;

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
        bottomNavigationView=(BottomNavigationView) findViewById(R.id.Bottombar);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.detail_prew:
                                Details();
                                break;
                            case R.id.share_prew:
                                shareImage();
                                break;
                            case R.id.delete_prew:
                                deleteImage();
                                break;
                            case R.id.setHomeScreen:
                                setHomeScreen();
                                break;

                            case R.id.setLockScreen:
                                setLockScreen();
                                break;
                            case R.id.setBoth:
                                setHomeScreen();
                                setLockScreen();
                                break;
                            case R.id.rename_menu:
                                renameImage();
                                break;
                        }
                        return false;
                    }
                });
        updateImageInfo(position);


        setTitle(imageName);

        Glide.with(GalleryPreview.this)
                .load(new File(path)) // Uri of the picture
                .into(GalleryPreviewImg);
        Log.d("toast11",path);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.gallery_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;
            case R.id.Detail_menu:
                Details();
                break;
            case R.id.setHomeScreen:
                setHomeScreen();
                break;
            case R.id.setLockScreen:
                setLockScreen();
                break;
            case R.id.setBoth:
                setHomeScreen();
                setLockScreen();
                break;
            case R.id.delete_menu:
                deleteImage();
                break;
            case R.id.crop_menu:
                cropImage();
                break;
            case R.id.rename_menu:
                renameImage();
                break;
            case R.id.share_menu:
                shareImage();
                break;
            case R.id.leftRotation_menu:
                leftRotation();
                break;
            case R.id.rightRotation_menu:
                rightRotation();
                break;
            case R.id.Sticker_menu:
                AddSticker();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    public void Details(){
        Bitmap myBitmap = BitmapFactory.decodeFile(path);
        int originalWidth = myBitmap.getWidth();
        int originalHeight = myBitmap.getHeight();
        CharSequence details = "Name: " + imageName + "\nPath: " + pathInDetails
                + "\nDate modified: " + lastModDate + "\nSize: " + space + " KB"
                + "\nResolution: "+ originalWidth + " x " + originalHeight;
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(GalleryPreview.this, details, duration).show();
    }
    public void AddSticker()
    {
        Intent intent = new Intent(GalleryPreview.this, RepairImage.class);
        intent.putExtra("path",path);
        startActivity(intent);
    }
    public  void leftRotation(){
        GalleryPreviewImg.setScaleType(ImageView.ScaleType.MATRIX);   //required
        matrix.postRotate( -90f, GalleryPreviewImg.getDrawable().getBounds().width()/2, GalleryPreviewImg.getDrawable().getBounds().height()/2);
        if(degrees <= 0) {
            degrees = 360;
        }
        degrees -= 90;
        GalleryPreviewImg.setImageMatrix(matrix);
    }

    public  void rightRotation(){
        GalleryPreviewImg.setScaleType(ImageView.ScaleType.MATRIX);   //required
        matrix.postRotate( 90f, GalleryPreviewImg.getDrawable().getBounds().width()/2, GalleryPreviewImg.getDrawable().getBounds().height()/2);
        if(degrees >= 360) {
            degrees = 0;
        }
        degrees += 90;
        GalleryPreviewImg.setImageMatrix(matrix);
    }

    public void shareImage() {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        try {
            File image = new File(this.getExternalCacheDir(), imageName);
            FileOutputStream fOut = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            final Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");
            share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(image));
            bitmap.recycle();
            startActivity(Intent.createChooser(share, "Share this photo via..."));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void renameImage() {
        File originalFile = new File("Internal "+pathInDetails+"/"+imageName);
        //File newName = new File(pathInDetails + "/ahihi.jpg");
        File newName = new File("Internal "+pathInDetails+"/ahihi.png");
        //originalFile.mkdirs();
        //ImageIO.write(image, "jpg", originalFile);

        if (originalFile.exists()) {
            if (originalFile.setWritable(true, false)) {
                boolean success = originalFile.renameTo(newName);
                if (success) {
                    Toast.makeText(this, "Name changed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Can't change name", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                boolean success = originalFile.renameTo(newName);
                if (success) {
                    Toast.makeText(this, "Name changed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Can't change name", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "Can't set writable", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "File doesn't not exist", Toast.LENGTH_SHORT).show();
        }

        updateImageInfo(position);
        setTitle(originalFile.getName());
    }

    public void cropImage() {


    }

    public void setHomeScreen() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        WallpaperManager myWallpaperManager = WallpaperManager
                .getInstance(this);
        Bitmap myBitmap = BitmapFactory.decodeFile(path);
        Bitmap useThisBitmap;
        int originalHeight = myBitmap.getHeight();
        int originalWidth = myBitmap.getWidth();
        int scaleWidth;
        if(originalHeight == height && originalWidth == width){
            scaleWidth = originalWidth - 1;
        }
        else{
            scaleWidth = (int) (myBitmap.getWidth() * height*1.0f / originalHeight);
        }
        useThisBitmap = Bitmap.createScaledBitmap(
                myBitmap, scaleWidth , height, true);
        myBitmap.recycle();

        if (useThisBitmap != null) {
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

    public void setLockScreen() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        WallpaperManager myWallpaperManager = WallpaperManager
                .getInstance(this);
        Bitmap myBitmap = BitmapFactory.decodeFile(path);
        Bitmap useThisBitmap;
        int originalHeight = myBitmap.getHeight();
        int originalWidth = myBitmap.getWidth();
        int scaleWidth;
        if(originalHeight == height && originalWidth == width){
            scaleWidth = originalWidth - 1;
        }
        else{
            scaleWidth = (int) (myBitmap.getWidth() * height*1.0f / originalHeight);
        }
        useThisBitmap = Bitmap.createScaledBitmap(
                myBitmap, scaleWidth , height, true);
        myBitmap.recycle();

        if (useThisBitmap != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    myWallpaperManager.setBitmap(useThisBitmap, null, true, WallpaperManager.FLAG_LOCK);
                    Toast.makeText(this, "Set wallpaper successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Your device is not supported for the lock screen", Toast.LENGTH_SHORT).show();
                }
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


    class MyGesture extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //Kéo từ trái sang phải
            if(e2.getX() - e1.getX() > SWIPE_THRESHOLD*0.7  && Math.abs(velocityX) > SWIPE_VELOCITY && 0 < position ){
                //Set lại góc 0 độ cho ảnh
                GalleryPreviewImg.setScaleType(ImageView.ScaleType.MATRIX);   //required
                matrix.postRotate( 360 - degrees, GalleryPreviewImg.getDrawable().getBounds().width()/2, GalleryPreviewImg.getDrawable().getBounds().height()/2);
                degrees = 0;
                GalleryPreviewImg.setImageMatrix(matrix);

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
                //Set lại góc 0 độ cho ảnh
                GalleryPreviewImg.setScaleType(ImageView.ScaleType.MATRIX);   //required
                matrix.postRotate( 360 - degrees, GalleryPreviewImg.getDrawable().getBounds().width()/2, GalleryPreviewImg.getDrawable().getBounds().height()/2);
                degrees = 0;
                GalleryPreviewImg.setImageMatrix(matrix);

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
