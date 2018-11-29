package com.tudinhtu.gallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

public class RepairImage extends MainActivity {
    String album_name = "Add Sticker";
    ImageView imgView1,imgView2,imgView3,imgView4,imgView5;
    ImageView Gallery;
    ImageButton imbSticker,imbSad,imbSmile,imbAngry;
    int slSticker=0;
    int stickerSize=0;
    String path;
    float[] lastEvent = null;
    float d = 0f;
    float newRot = 0f;
    private boolean isZoomAndRotate;
    private boolean isOutSide;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    float oldDist = 1f;
    private float xCoOrdinate, yCoOrdinate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(album_name);

        setContentView(R.layout.repair_image);

        Intent intent = getIntent();
        path=intent.getStringExtra("path");
        Log.d("toast11",path);
        Gallery=(ImageView) findViewById(R.id.Gallery);
        imgView1=(ImageView) findViewById(R.id.imgView1);
        imgView2=(ImageView) findViewById(R.id.imgView2);
        imgView3=(ImageView) findViewById(R.id.imgView3);
        imgView4=(ImageView) findViewById(R.id.imgView4);
        imgView5=(ImageView) findViewById(R.id.imgView5);
        imbSticker=(ImageButton) findViewById(R.id.imbSticker);
        imbSad=(ImageButton) findViewById(R.id.imbSad);
        imbSmile=(ImageButton) findViewById(R.id.imbSmile);
        imbAngry=(ImageButton) findViewById(R.id.imbAngry);
        Glide.with(RepairImage.this)
                .load(new File(path)) // Uri of the picture
                .into(Gallery);
        imbSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(slSticker<5)
                {
                    imbSad.setVisibility(View.VISIBLE);
                    imbSmile.setVisibility(View.VISIBLE);
                    imbAngry.setVisibility(View.VISIBLE);
                    imbSad.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            slSticker++;
                            stickerSize=1;

                            getImage();
                            imbSad.setVisibility(View.INVISIBLE);
                            imbSmile.setVisibility(View.INVISIBLE);
                            imbAngry.setVisibility(View.INVISIBLE);
                        }
                    });
                    imbSmile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            slSticker++;
                            stickerSize=2;
                            getImage();
                            imbSad.setVisibility(View.INVISIBLE);
                            imbSmile.setVisibility(View.INVISIBLE);
                            imbAngry.setVisibility(View.INVISIBLE);
                        }
                    });
                    imbAngry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            slSticker++;
                            stickerSize=3;
                            getImage();
                            imbSad.setVisibility(View.INVISIBLE);
                            imbSmile.setVisibility(View.INVISIBLE);
                            imbAngry.setVisibility(View.INVISIBLE);
                        }
                    });

                }
                else
                {
                    Toast.makeText(RepairImage.this, "Không thể thêm Sticker nữa!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView view = (ImageView) v;
                view.bringToFront();
                viewTransformation(view, event);
                return true;
            }
        });
        imgView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView view = (ImageView) v;
                view.bringToFront();
                viewTransformation(view, event);
                return true;
            }
        });
        imgView3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView view = (ImageView) v;
                view.bringToFront();
                viewTransformation(view, event);
                return true;
            }
        });
        imgView4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView view = (ImageView) v;
                view.bringToFront();
                viewTransformation(view, event);
                return true;
            }
        });
        imgView5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ImageView view = (ImageView) v;
                view.bringToFront();
                viewTransformation(view, event);
                return true;
            }
        });

    }
    private void getImage()
    {
        if(slSticker==1)
        {
            getSticker(imgView1);

        }
        else if(slSticker==2)
        {
            getSticker(imgView2);
        }
        else if(slSticker==3)
        {
            getSticker(imgView3);
        }
        else if(slSticker==4)
        {
            getSticker(imgView4);
        }
        else if(slSticker==5)
        {
            getSticker(imgView5);
        }
    }
    private void getSticker(ImageView view)
    {
        if(stickerSize==1)
        {
            view.setImageResource(R.drawable.sad);
        }
        else if(stickerSize==2)
        {
            view.setImageResource(R.drawable.happy);
        }
        else if(stickerSize==3)
        {
            view.setImageResource(R.drawable.angry);
        }
        view.setVisibility(View.VISIBLE);
        view.bringToFront();
    }
    private void viewTransformation(View view, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                xCoOrdinate = view.getX() - event.getRawX();
                yCoOrdinate = view.getY() - event.getRawY();

                start.set(event.getX(), event.getY());
                isOutSide = false;
                mode = DRAG;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    midPoint(mid, event);
                    mode = ZOOM;
                }

                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);
                break;
            case MotionEvent.ACTION_UP:
                isZoomAndRotate = false;
                if (mode == DRAG) {
                    float x = event.getX();
                    float y = event.getY();
                }
            case MotionEvent.ACTION_OUTSIDE:
                isOutSide = true;
                mode = NONE;
                lastEvent = null;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isOutSide) {
                    if (mode == DRAG) {
                        isZoomAndRotate = false;
                        view.animate().x(event.getRawX() + xCoOrdinate).y(event.getRawY() + yCoOrdinate).setDuration(0).start();
                    }
                    if (mode == ZOOM && event.getPointerCount() == 2) {
                        float newDist1 = spacing(event);
                        if (newDist1 > 10f) {
                            float scale = newDist1 / oldDist * view.getScaleX();
                            view.setScaleX(scale);
                            view.setScaleY(scale);
                        }
                        if (lastEvent != null) {
                            newRot = rotation(event);
                            view.setRotation((float) (view.getRotation() + (newRot - d)));
                        }
                    }
                }
                break;
        }
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (int) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.Remove:
                Remove();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.repair_menu, menu);
        return true;
    }
    private void Remove()
    {
        imgView1.setVisibility(View.INVISIBLE);
        imgView2.setVisibility(View.INVISIBLE);
        imgView3.setVisibility(View.INVISIBLE);
        imgView4.setVisibility(View.INVISIBLE);
        imgView5.setVisibility(View.INVISIBLE);
        slSticker=0;
    }
}
