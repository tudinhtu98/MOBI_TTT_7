package com.tudinhtu.gallery;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MergeCursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;


public class AlbumActivity extends AppCompatActivity {
    GridView galleryGridView;
    int isdateDecrease;
    ArrayList<HashMap<String, String>> imageList = new ArrayList<HashMap<String, String>>();
    static  ArrayList<HashMap<String, String>> dateList = new ArrayList<HashMap<String, String>>();
    static int isdelete =0;
    static int posDelete =0;
    boolean firstTimeAlbum;
    String album_name = "";
    LoadAlbumImages loadAlbumTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        album_name = intent.getStringExtra("name");
        setTitle(album_name);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        int theme = sharedPref.getInt("key", 1);

        galleryGridView = findViewById(R.id.galleryGridView);
        int iDisplayWidth = getResources().getDisplayMetrics().widthPixels ;
        Resources resources = getApplicationContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = iDisplayWidth / (metrics.densityDpi / 160f);

        if(dp < 360)
        {
            dp = (dp - 17) / 2;
            float px = Function.convertDpToPixel(dp, getApplicationContext());
            galleryGridView.setColumnWidth(Math.round(px));
        }

        // set background color at daylight, at night
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour < 18) {
            galleryGridView.setBackgroundColor(Color.WHITE);
        }
        else {
            galleryGridView.setBackgroundColor(Color.BLACK);
        }

        setBackground(theme);
    }

    @Override
    protected void onPause() {
        super.onPause();
        savingPreferences();
        savingPreferencesAlbum();
    }
    public void savingPreferencesAlbum() {
        SharedPreferences pre=getSharedPreferences(MainActivity.appFirstTime,MODE_PRIVATE);
        SharedPreferences.Editor editor=pre.edit();
        if(pre.getBoolean("album",true)==true) {
            //editor.remove("main");
            editor.putBoolean("album", false);
        }
        editor.commit();
    }
    public void restoringPreferencesMainAlbum() {
        SharedPreferences pre=getSharedPreferences(MainActivity.appFirstTime,MODE_PRIVATE);
        firstTimeAlbum=pre.getBoolean("album",true);
    }
    public void savingPreferences() {
        SharedPreferences pre=getSharedPreferences(album_name,MODE_PRIVATE);
        SharedPreferences.Editor editor=pre.edit();
        editor.clear();
        editor.putInt(album_name,isdateDecrease);
        editor.commit();
    }

    private void setBackground(int theme) {
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        switch (theme) {
            case 0:
                galleryGridView.setBackgroundColor(Color.WHITE);
                break;
            case 1:
                galleryGridView.setBackgroundColor(Color.BLACK);
                break;
            case 2:
                if (hour >= 6 && hour < 18) {
                    galleryGridView.setBackgroundColor(Color.WHITE);
                } else {
                    galleryGridView.setBackgroundColor(Color.BLACK);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        restoringPreferences();
        restoringPreferencesMainAlbum();
        if(firstTimeAlbum==true)
        {
            Intent intent = new Intent(AlbumActivity.this, Guide.class);
            intent.putExtra("choose","album" );
            startActivity(intent);
        }
        if(isdelete==0) {
            loadAlbumTask = new LoadAlbumImages();
            loadAlbumTask.execute();
        }
        else if(isdelete==1) {
            Toast.makeText(AlbumActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
            imageList = GalleryPreview.list;
            SingleAlbumAdapter adapter = new SingleAlbumAdapter(AlbumActivity.this, imageList);

            galleryGridView.setAdapter(adapter);
            isdelete=0;
        }
    }

    public void restoringPreferences() {
        SharedPreferences pre=getSharedPreferences(album_name,MODE_PRIVATE);
        isdateDecrease=pre.getInt(album_name,1);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;
            case R.id.slideShow:
            {
                Intent intent = new Intent(AlbumActivity.this, GalleryPreview.class);
                intent.putExtra("position", 0);
                intent.putExtra("choose", "1");
                GalleryPreview.list = imageList;
                startActivity(intent);
                break;
            }
            case R.id.decending:
            {

                isdateDecrease=1;
                loadAlbumTask = new LoadAlbumImages();
                loadAlbumTask.execute();
                break;

            }
            case  R.id.timeIncreasing :
            {
                isdateDecrease=0;
                loadAlbumTask = new LoadAlbumImages();
                loadAlbumTask.execute();
                break;
            }
            case  R.id.guildeAlbum_menu :
            {
                Intent intent = new Intent(AlbumActivity.this, Guide.class);
                intent.putExtra("choose","album" );
                startActivity(intent);
                break;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_menu, menu);
        return true;
    }

    class LoadAlbumImages extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imageList.clear();
        }

        protected String doInBackground(String... args) {
            String xml = "";

            String path;
            String album;
            String timestamp;
            Uri uriExternal = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uriInternal = android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI;

            String[] projection = { MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED };

            Cursor cursorExternal = getContentResolver().query(uriExternal, projection, "bucket_display_name = \""+album_name+"\"", null, null);
            Cursor cursorInternal = getContentResolver().query(uriInternal, projection, "bucket_display_name = \""+album_name+"\"", null, null);
            Cursor cursor = new MergeCursor(new Cursor[]{cursorExternal,cursorInternal});
            while (cursor.moveToNext()) {

                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                timestamp = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED));

                imageList.add(Function.mappingInbox(album, path, timestamp, Function.converToTime(timestamp), null));
            }
            cursor.close();

            if(isdateDecrease==0)
                Collections.sort(imageList, new MapComparator(Function.KEY_TIMESTAMP, "asc"));
            if(isdateDecrease==1)
                Collections.sort(imageList, new MapComparator(Function.KEY_TIMESTAMP, "dsc")); // Arranging photo album by timestamp decending
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {

            SingleAlbumAdapter adapter = new SingleAlbumAdapter(AlbumActivity.this, imageList);
            galleryGridView.setAdapter(adapter);
            galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {
                    Intent intent = new Intent(AlbumActivity.this, GalleryPreview.class);
                    intent.putExtra("position", position);
                    intent.putExtra("choose", "0");
                    GalleryPreview.list = imageList;
                    startActivity(intent);
                }
            });
        }
    }
}



class SingleAlbumAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap< String, String >> data;


    public SingleAlbumAdapter(Activity a, ArrayList < HashMap < String, String >> d) {
        activity = a;
        data = d;
    }

    public int getCount() {
        return data.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        SingleAlbumViewHolder holder;
        if (convertView == null) {
            holder = new SingleAlbumViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.single_album_row, parent, false);

            holder.galleryImage = convertView.findViewById(R.id.galleryImage);

            convertView.setTag(holder);
        } else {
            holder = (SingleAlbumViewHolder) convertView.getTag();
        }
        holder.galleryImage.setId(position);

        HashMap < String, String > song ;
        song = data.get(position);
        try {

            Glide.with(activity)
                    .load(new File(song.get(Function.KEY_PATH))) // Uri of the picture
                    .into(holder.galleryImage);


        } catch (Exception e) {}
        return convertView;
    }
}


class SingleAlbumViewHolder {
    ImageView galleryImage;
}

