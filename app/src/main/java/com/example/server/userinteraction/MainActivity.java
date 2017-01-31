/*
        Modified by: Andy Ngauv
        ID: A00839270
 */

package com.example.server.userinteraction;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import java.io.*;

import static android.R.attr.data;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    ImageView image = null;
    int imageID;
    int imageSelect [] = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d}; // Array of images
    int nextImage = 0; // go to next or previous image, default image is .a picture
    int i = 0;

/*
    public void imageFile(String fname) {
        File folder = new File(fname);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                imageSel[i] = Integer.parseInt(file.getName());
                i++;
            }
        }
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView)findViewById(R.id.imageView1);

        // Display default image at the beginning
        image.setImageResource(imageSelect[nextImage]);
        imageID = imageSelect[nextImage];

        Button leftButton = (Button) findViewById(R.id.button_left);
        leftButton.setOnClickListener(leftButtonListener);
    }
    private OnClickListener leftButtonListener = new OnClickListener() {
        public void onClick(View v) {
            // Determines if image is at beginning of gallery, if not scroll to next image
            if(nextImage == 0)
                nextImage = 3;
            else
                nextImage--;

            image.setImageResource(imageSelect[nextImage]);
            imageID = imageSelect[nextImage];
        }
    };
    public void rightClick(View v) {
        // Determine is at the end of gallery, if not scroll to next image
        if(nextImage == 3)
            nextImage = 0;
        else
            nextImage++;

        image.setImageResource(imageSelect[nextImage]);
        imageID = imageSelect[nextImage];
    }
    public void goClick(View v) {
        Intent intent = new Intent(this, Main2Activity.class);
        Bundle extras = new Bundle();
        extras.putIntArray("imageArray",imageSelect);
        extras.putInt("nextImage",nextImage);
        extras.putInt("imageStart",0);
        extras.putInt("imageEnd",3);
        intent.putExtras(extras);
        startActivityForResult(intent,1);
    }

    // Resume the scrolling of images where activity B left off in Activty A
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            imageID = data.getIntExtra("imageID",0);
            image.setImageResource(imageID);
        }
    }

    @Override
    public void onClick(View v) {

    }
}