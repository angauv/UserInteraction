/*
        Modified by: Andy Ngauv
        ID: A00839270
 */

package com.example.server.userinteraction;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.icu.text.StringSearch;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.widget.Toast;
import android.widget.TextView;
import android.view.GestureDetector.OnGestureListener;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import static java.lang.Math.*;

//public class Main2Activity extends AppCompatActivity {

public class Main2Activity extends AppCompatActivity implements OnClickListener {
    //public class Main2Activity extends AppCompatActivity implements OnGestureListener{
    private static final int SWIPE_MIN_DISTANCE = 100;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private static final float SNAP_MIN = (float)5;
    private static final float SNAP_OK = (float)7;
    private static final float SHAKE_OK = (float)18;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    private TextView myTextView;

    private TextView textXaccel;
    private TextView textYaccel;
    private TextView textZaccel;

    private TextView textXgyro;
    private TextView textYgyro;
    private TextView textZgyro;

    private SensorManager mSensorManager;
    private Sensor acceleration;
    private Sensor gyro;

    private String getPath;
    private static float xA;
    private static float yA;
    private static float zA;
    private static float xG;
    private static float yG;
    private static float zG;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    private int nextImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        ImageView image = (ImageView) findViewById(R.id.imageView1);

        String imgPath = intent.getStringExtra("imagePath");
        File file = new File(imgPath);
        Uri imgUri = Uri.fromFile(file);
        image.setImageURI(imgUri);
        getPath = imgPath;

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

        textXaccel = (TextView) findViewById(R.id.TextViewXaccel);
        textYaccel = (TextView) findViewById(R.id.TextViewYaccel);
        textZaccel = (TextView) findViewById(R.id.TextViewZaccel);

        textXgyro = (TextView) findViewById(R.id.TextViewXgyro);
        textYgyro = (TextView) findViewById(R.id.TextViewYgyro);
        textZgyro = (TextView) findViewById(R.id.TextViewZgyro);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        acceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorListener, gyro, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(mSensorListener, acceleration, SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        //gestureScanner = new GestureDetector(getBaseContext(), this);
    }

    // Find file index of current image
    public int imageIndex(String path){
        File file = new File(path);
        File[] files = new File(file.getParent()).listFiles();
        String[] fileString = new File(file.getParent()).list();
        int i;
        int img = -1;

        // Find file index of current image
        for (i = 0; i < files.length; i++){
            if(fileString[i].equals(file.getName())){
                img = i;
            }
        }
        return img;
    }

    class MyGestureDetector extends SimpleOnGestureListener {
        ImageView image = (ImageView) findViewById(R.id.imageView1);

        File file = new File(getPath);
        Uri imgUri = Uri.fromFile(file);
        File[] files = new File(file.getParent()).listFiles();

        @Override
        public boolean onFling(MotionEvent me1, MotionEvent me2, float vX, float vY) {
            try {
                        /*use me1.getX(), me1.getY(), me2.getX() and me2.getY()
                        to determine the length of the swipe action along x and/or y directions.
                        The parameters vx and vy  are the velocities of the swipe action along  x and y directions, respectively.
                        Use these to detect left and right swipes.  Upon detecting that left or right swipe indeed took place,
                        just rotate the images in the image view in the corresponding direction, as in Activity 1*/

                if (Math.abs(me1.getX() - me2.getX()) > SWIPE_MAX_OFF_PATH)
                    return false;

                // swipe left
                if (me1.getX() - me2.getX() > SWIPE_MIN_DISTANCE) {
                    onSwipeLeft();
                }
                // swipe right
                else if (me2.getX() - me1.getX() > SWIPE_MIN_DISTANCE) {
                    onSwipeRight();
                }
                // swipe Up
                else if (me1.getY() - me2.getY() > SWIPE_MIN_DISTANCE) {
                    onSwipeUp();
                }
                // swipe Down
                else if (me2.getY() - me1.getY() > SWIPE_MIN_DISTANCE) {
                    onSwipeDown();
                }

            } catch (Exception e) {
                Log.d("Swipe","Exception: No swipe detected");
            }
            return false;
        }

        // Return to Activity A when user double taps screen and set current image in Activity B
        // to Activity A imageview.
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Intent intent = new Intent();
            //intent.putExtra("imagePath", files[nextImage].getAbsolutePath());
            intent.putExtra("imagePath", getPath);
            setResult(2, intent);
            finish();
            return true;
        }

        public void onSwipeUp(){
            Toast.makeText(Main2Activity.this, "up", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("imagePath", getPath);
            setResult(2, intent);
            finish();
        }
        public void onSwipeDown(){
            Toast.makeText(Main2Activity.this, "down", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("imagePath", getPath);
            setResult(2, intent);
            finish();
        }

        public void onSwipeLeft() {
            Toast.makeText(Main2Activity.this, "left", Toast.LENGTH_SHORT).show();

            // Find file index of current image
            nextImage = imageIndex(getPath);

            if (nextImage == 0)
                nextImage = files.length - 1;
            else
                nextImage--;

            imgUri = Uri.fromFile(files[nextImage]);
            image.setImageURI(imgUri);
            getPath = files[nextImage].getAbsolutePath();
        }

        public void onSwipeRight() {
            Toast.makeText(Main2Activity.this, "right", Toast.LENGTH_SHORT).show();

            // Find file index of current image
            nextImage = imageIndex(getPath);

            if (nextImage == (files.length - 1))
                nextImage = 0;
            else
                nextImage++;

            imgUri = Uri.fromFile(files[nextImage]);
            image.setImageURI(imgUri);
            getPath = files[nextImage].getAbsolutePath();
        }
    }
    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
                getGyro(event);
            }
            else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                getAccelerometer(event);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy){}

        private void getGyro(SensorEvent event){
            ImageView image = (ImageView) findViewById(R.id.imageView1);
            File file = new File(getPath);
            Uri imgUri = Uri.fromFile(file);
            File[] files = new File(file.getParent()).listFiles();

            // Find file index of current image
            nextImage = imageIndex(getPath);

            xG = event.values[0];
            yG = event.values[1];
            zG = event.values[2];

            textXgyro.setText((int)xG + " rad/s");
            textYgyro.setText((int)yG + " rad/s");
            textZgyro.setText((int)zG + " rad/s");

            float sumG = xG + yG + zG;

            if (sumG > SNAP_MIN && mAccel < SNAP_OK){
                if (nextImage == 0)
                    nextImage = files.length -1;
                else
                    nextImage--;

                imgUri = Uri.fromFile(files[nextImage]);
                image.setImageURI(imgUri);
                getPath = files[nextImage].getAbsolutePath();
            }
            else if(sumG < -SNAP_MIN && mAccel < SNAP_OK){
                if (nextImage == files.length -1)
                    nextImage = 0;
                else
                    nextImage++;

                imgUri = Uri.fromFile(files[nextImage]);
                image.setImageURI(imgUri);
                getPath = files[nextImage].getAbsolutePath();
            }
        }
        private void getAccelerometer(SensorEvent event){
            xA = event.values[0];
            yA = event.values[1];
            zA = event.values[2];

            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (xA*xA + yA*yA + zA*zA));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;

            DecimalFormat dF = new DecimalFormat("#.##");
            xA = Float.parseFloat(dF.format(xA));
            yA = Float.parseFloat(dF.format(yA));
            zA = Float.parseFloat(dF.format(zA));

            textXaccel.setText(String.valueOf(xA) + " m/s2");
            textYaccel.setText(String.valueOf(yA) + " m/s2");
            textZaccel.setText(String.valueOf(zA) + " m/s2");

            if (mAccel > SHAKE_OK) {
                Intent intent = new Intent();
                intent.putExtra("imagePath", getPath);
                setResult(2, intent);
                finish();
            }
        }

        private void onResume(){
            Main2Activity.super.onResume();
            mSensorManager.registerListener(this,acceleration,SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(this,gyro,SensorManager.SENSOR_DELAY_NORMAL);
        }

        protected void onPause(){
            Main2Activity.super.onPause();
            mSensorManager.unregisterListener(this);
        }
    };

    @Override
    public void onClick(View v) {}

}
