package com.tudinhtu.gallery;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Guide extends MainActivity {
    String album_name = "Guide";
    String choose;
    ImageView imgGuide;
    Button btnNext,btnBack;
    int select;
    ImageButton imgB1,imgB2,imgB3,imgB4,imgB5,imgB6;
    TextView tw1,tw2,tw3,tw4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(album_name);
        getSupportActionBar().hide();
        setContentView(R.layout.guide);

        Intent intent = getIntent();
        choose=intent.getStringExtra("choose");
        imgGuide=(ImageView) findViewById(R.id.imageGuide);
        btnBack=(Button) findViewById(R.id.prewGuide);
        btnNext=(Button) findViewById(R.id.nextGuide);
        if(choose.equals("main"))
        {
            showMain();
        }
        else if(choose.equals("album"))
        {
            showAlbum();
        }
        else if(choose.equals("gallery"))
        {
            showGallery();
        }
    }
    public void showGallery()
    {
        select=1;
        imgB1=(ImageButton) findViewById(R.id.imgb2);
        imgB2=(ImageButton) findViewById(R.id.imgb3);
        imgB3=(ImageButton) findViewById(R.id.imgb4);
        imgB1.setVisibility(View.VISIBLE);
        imgB2.setVisibility(View.VISIBLE);
        imgB3.setVisibility(View.VISIBLE);
        tw1=(TextView) findViewById(R.id.swipeUpOrDown);
        tw2=(TextView) findViewById(R.id.swipeLeftOrRight);
        getSelectGallery(select);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select>1)
                {
                    select--;
                }
                getSelectGallery(select);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select==3&& btnNext.getText().equals(getString(R.string.gotItGuide)))
                {
                    finish();
                }
                else
                {
                    if(select<3)
                        select++;
                    getSelectGallery(select);
                }

            }
        });
        imgB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select=1;
                getSelectGallery(select);
            }
        });
        imgB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select=2;
                getSelectGallery(select);
            }
        });
        imgB3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select=3;
                getSelectGallery(select);
            }
        });
    }

    public void getSelectGallery(int select) {
        if(select==1)
        {
            imgB2.setBackgroundResource(R.drawable.cycle);
            imgB3.setBackgroundResource(R.drawable.cycle);
            imgB1.setBackgroundResource(R.drawable.cycle2);
            imgGuide.setImageResource(R.drawable.gallery);
            tw1.setVisibility(View.INVISIBLE);
            tw2.setVisibility(View.INVISIBLE);
            btnNext.setText(getString(R.string.nextGuide));


        }
        else if(select==2)
        {
            imgB1.setBackgroundResource(R.drawable.cycle);
            imgB3.setBackgroundResource(R.drawable.cycle);
            imgB2.setBackgroundResource(R.drawable.cycle2);
            imgGuide.setImageResource(R.drawable.gallery2);
            btnNext.setText(getString(R.string.nextGuide));
            tw2.setVisibility(View.INVISIBLE);
            tw1.setVisibility(View.VISIBLE);
        }
        else if(select==3)
        {
            imgB1.setBackgroundResource(R.drawable.cycle);
            imgB2.setBackgroundResource(R.drawable.cycle);
            imgB3.setBackgroundResource(R.drawable.cycle2);
            btnNext.setText(getString(R.string.gotItGuide));
            imgGuide.setImageResource(R.drawable.gallery3);
            imgGuide.setVisibility(View.VISIBLE);
            tw1.setVisibility(View.INVISIBLE);
            tw2.setVisibility(View.VISIBLE);

        }
    }

    public void showAlbum()
    {
        select=1;
        imgB1=(ImageButton) findViewById(R.id.imgb2);
        imgB2=(ImageButton) findViewById(R.id.imgb3);
        imgB3=(ImageButton) findViewById(R.id.imgb4);
        imgB1.setVisibility(View.VISIBLE);
        imgB2.setVisibility(View.VISIBLE);
        imgB3.setVisibility(View.VISIBLE);
        tw1=(TextView) findViewById(R.id.touchSlideShow);
        tw2=(TextView) findViewById(R.id.touchGuide2);
        getSelectAlbum(select);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select>1)
                {
                    select--;
                }
                getSelectAlbum(select);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select==3&& btnNext.getText().equals(getString(R.string.gotItGuide)))
                {
                    finish();
                }
                else
                {
                    if(select<3)
                        select++;
                    getSelectAlbum(select);
                }

            }
        });
        imgB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select=1;
                getSelectAlbum(select);
            }
        });
        imgB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select=2;
                getSelectAlbum(select);
            }
        });
        imgB3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select=3;
                getSelectAlbum(select);
            }
        });
    }

    public void getSelectAlbum(int select) {
        if(select==1)
        {
            imgB2.setBackgroundResource(R.drawable.cycle);
            imgB3.setBackgroundResource(R.drawable.cycle);
            imgB1.setBackgroundResource(R.drawable.cycle2);
            imgGuide.setImageResource(R.drawable.album);
            tw1.setVisibility(View.INVISIBLE);
            tw2.setVisibility(View.INVISIBLE);
            btnNext.setText(getString(R.string.nextGuide));


        }
        else if(select==2)
        {
            imgB1.setBackgroundResource(R.drawable.cycle);
            imgB3.setBackgroundResource(R.drawable.cycle);
            imgB2.setBackgroundResource(R.drawable.cycle2);
            imgGuide.setImageResource(R.drawable.album2);
            btnNext.setText(getString(R.string.nextGuide));
            tw2.setVisibility(View.INVISIBLE);
            tw1.setVisibility(View.VISIBLE);
        }
        else if(select==3)
        {
            imgB1.setBackgroundResource(R.drawable.cycle);
            imgB2.setBackgroundResource(R.drawable.cycle);
            imgB3.setBackgroundResource(R.drawable.cycle2);
            btnNext.setText(getString(R.string.gotItGuide));
            imgGuide.setImageResource(R.drawable.album3);
            imgGuide.setVisibility(View.VISIBLE);
            tw1.setVisibility(View.INVISIBLE);
            tw2.setVisibility(View.VISIBLE);

        }
    }


    public void showMain()
    {
        select=1;
        imgB1=(ImageButton) findViewById(R.id.imgb2);
        imgB2=(ImageButton) findViewById(R.id.imgb3);
        imgB3=(ImageButton) findViewById(R.id.imgb4);
        imgB1.setVisibility(View.VISIBLE);
        imgB2.setVisibility(View.VISIBLE);
        imgB3.setVisibility(View.VISIBLE);
        tw1=(TextView) findViewById(R.id.welcome);
        tw2=(TextView) findViewById(R.id.numberPhoto);
        tw3= (TextView) findViewById(R.id.nameAlbum);
        tw4=(TextView) findViewById(R.id.touchGuide);
        getSelectMain(select);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select>1)
                {
                    select--;
                }
                getSelectMain(select);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select==3&& btnNext.getText().equals(getString(R.string.gotItGuide)))
                {
                    finish();
                }
                else
                {
                    if(select<3)
                    select++;
                    getSelectMain(select);
                }

            }
        });
        imgB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select=1;
                getSelectMain(select);
            }
        });
        imgB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select=2;
                getSelectMain(select);
            }
        });
        imgB3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select=3;
                getSelectMain(select);
            }
        });
    }

    public void getSelectMain(int select) {
       if(select==1)
       {
           imgB2.setBackgroundResource(R.drawable.cycle);
           imgB3.setBackgroundResource(R.drawable.cycle);
           imgB1.setBackgroundResource(R.drawable.cycle2);
           imgGuide.setVisibility(View.INVISIBLE);


           tw2.setVisibility(View.INVISIBLE);
           tw3.setVisibility(View.INVISIBLE);
           tw4.setVisibility(View.INVISIBLE);
           btnNext.setText(getString(R.string.nextGuide));
           tw1.setVisibility(View.VISIBLE);

       }
       else if(select==2)
       {
           imgB1.setBackgroundResource(R.drawable.cycle);
           imgB3.setBackgroundResource(R.drawable.cycle);
           imgB2.setBackgroundResource(R.drawable.cycle2);
           imgGuide.setVisibility(View.VISIBLE);
           imgGuide.setImageResource(R.drawable.main);
           btnNext.setText(getString(R.string.nextGuide));
           tw1.setVisibility(View.INVISIBLE);
           tw4.setVisibility(View.INVISIBLE);
           tw2.setVisibility(View.VISIBLE);
           tw3.setVisibility(View.VISIBLE);
       }
       else if(select==3)
       {
           imgB1.setBackgroundResource(R.drawable.cycle);
           imgB2.setBackgroundResource(R.drawable.cycle);
           imgB3.setBackgroundResource(R.drawable.cycle2);
           btnNext.setText(getString(R.string.gotItGuide));
           imgGuide.setImageResource(R.drawable.main2);
           imgGuide.setVisibility(View.VISIBLE);
           tw1.setVisibility(View.INVISIBLE);
           tw2.setVisibility(View.INVISIBLE);
           tw3.setVisibility(View.INVISIBLE);
           tw4.setVisibility(View.VISIBLE);

       }
    }


}
