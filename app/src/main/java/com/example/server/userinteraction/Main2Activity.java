package com.example.server.userinteraction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ImageView;


public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        int imageID = intent.getIntExtra("image_id", 0);
        ImageView image = (ImageView)findViewById(R.id.imageView1);
        image.setImageResource(imageID);
    }
}
