/*
        Modified by: Andy Ngauv
        ID: A00839270
 */

package com.example.server.userinteraction;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ImageView;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.widget.Toast;
import android.widget.TextView;
import android.view.GestureDetector.OnGestureListener;


//public class Main2Activity extends AppCompatActivity {

    public class Main2Activity extends AppCompatActivity implements OnClickListener {
        //public class Main2Activity extends AppCompatActivity implements OnGestureListener{
        private static final int SWIPE_MIN_DISTANCE = 10;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;
        private GestureDetector gestureDetector;
        View.OnTouchListener gestureListener;
        private TextView myTextView;
        private static int clickCount = 1;
        private int imageGet = 0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            int nextImage = extras.getInt("image_id", 0);
            int imageSelect[] = extras.getIntArray("imageArray");
           // int imageID = intent.getIntExtra("image_id",0);
            ImageView image = (ImageView) findViewById(R.id.imageView1);
            image.setImageResource(imageSelect[nextImage]);

            myTextView = new TextView(this);
            myTextView.setTextSize(16);
            image.setOnClickListener(this);
            gestureDetector = new GestureDetector(this, new MyGestureDetector());
            gestureListener = new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            };
            image.setOnTouchListener(gestureListener);
            //gestureScanner = new GestureDetector(getBaseContext(), this);
        }
        class MyGestureDetector extends SimpleOnGestureListener {
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            int nextImage = extras.getInt("image_id", 0);
            int imageSelect[] = extras.getIntArray("imageArray");
            int imageStart = extras.getInt("imageStart",0);
            int imageEnd = extras.getInt("imageEnd",0);
            ImageView image = (ImageView) findViewById(R.id.imageView1);
            @Override
            public boolean onFling(MotionEvent me1, MotionEvent me2, float vX, float vY) {
                try {
                        /*use me1.getX(), me1.getY(), me2.getX() and me2.getY()
                        to determine the length of the swipe action along x and/or y directions.
                        The parameters vx and vy  are the velocities of the swipe action along  x and y directions, respectively.
                        Use these to detect left and right swipes.  Upon detecting that left or right swipe indeed took place,
                        just rotate the images in the image view in the corresponding direction, as in Activity 1*/

                    if(Math.abs(me1.getX()-me2.getX()) > SWIPE_MAX_OFF_PATH)
                        return false;

                    // swipe left
                    if (me1.getX()-me2.getX() > SWIPE_MIN_DISTANCE){
                        Toast.makeText(Main2Activity.this, "left", Toast.LENGTH_SHORT).show();

                        if (nextImage == imageStart)
                            nextImage = imageEnd;
                        else
                            nextImage = nextImage -1;
                        imageGet = imageSelect[nextImage];
                        image.setImageResource(imageGet);
                }
                    // swipe right
                    else if(me2.getX()-me1.getX() > SWIPE_MIN_DISTANCE){
                        Toast.makeText(Main2Activity.this, "right", Toast.LENGTH_SHORT).show();

                        if (nextImage == imageEnd)
                            nextImage = imageStart;
                        else
                            nextImage = nextImage + 1;
                        imageGet = imageSelect[nextImage];
                        image.setImageResource(imageGet);
                    }

                } catch (Exception e) {
                    System.out.println("Exception: No swipe detected");
                }
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e){
                Intent intent = new Intent();
                intent.putExtra("image_id", imageGet);
                setResult(1,intent);
                finish();
                return true;
            }
        }

        @Override
        public void onClick(View v) {
          /*  if (clickCount ==2) {
                Intent intent = new Intent();
                intent.putExtra("image_id", 0);
                finish();
                clickCount = 1;
            }
            else
                clickCount++;*/
        }
    }
