package com.seonil.scrollingimageview.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.seonil.scrollingimageview.ScrollingImageView;
import com.seonil.scrollingimageview.util.BitmapUtil;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScrollingImageView scrollingImageView = (ScrollingImageView) findViewById(R.id.scrollingImageView);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample);
        scrollingImageView.setImageBitmap(BitmapUtil.resizeBitmapFullDisplaySize(this, bitmap));


        if (!scrollingImageView.isRunning()) {
            scrollingImageView.start();
        }
    }
}
