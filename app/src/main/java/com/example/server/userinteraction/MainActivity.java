package com.example.server.userinteraction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity implements OnClickListener {
    ImageView image = null;
    int imageID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView)findViewById(R.id.imageView1);

 /*       Button aButton = (Button) findViewById(R.id.button_a);
        aButton.setOnClickListener(this);

        Button bButton = (Button) findViewById(R.id.button_b);
        bButton.setOnClickListener(this);
*/
        Button cButton = (Button) findViewById(R.id.button_left);
        leftButton.setOnClickListener(leftButtonListener);

    }
    private OnClickListener leftButtonListener = new OnClickListener() {
        public void onClick(View v) {
            image.setImageResource(R.drawable.c);
            imageID = R.drawable.c;
        }
    };
    public void rightClick(View v) {
        image.setImageResource(R.drawable.d);
        imageID = R.drawable.d;
    }
    public void goClick(View v) {
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra("image_id", imageID);
        startActivity(intent);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_left:
                image.setImageResource(R.drawable.a);
                imageID = R.drawable.a;
                break;
            case R.id.button_b:
                image.setImageResource(R.drawable.b);
                break;
        }
    }
}
