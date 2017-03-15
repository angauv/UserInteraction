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
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    ImageView image = null;
    int nextImage = 0; // go to next or previous image, default image is .a picture
    int i = 0;
    String path = Environment.getExternalStorageDirectory().toString() + "/Pictures/";
    //private String path = "/mnt/sdcard/Pictures/";
    //private File[] files = new File(path).listFiles();
    static final int REQUEST_TAKE_PHOTO =1;
    String photoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView)findViewById(R.id.imageView1);

        File[] files = new File(path).listFiles();
        String imgPath = path + files[0].getName();
        File file = new File(imgPath);
        Uri imgUri = Uri.fromFile(file);
        image.setImageURI(imgUri);

        Button leftButton = (Button) findViewById(R.id.button_left);
        leftButton.setOnClickListener(leftButtonListener);
    }
    private OnClickListener leftButtonListener = new OnClickListener() {
        public void onClick(View v) {
            File[] files = new File(path).listFiles();
            // Determines if image is at beginning of gallery, if not scroll to next image
            if(nextImage == 0)
                nextImage = files.length-1;
            else
                nextImage--;

            String imgPath = path + files[nextImage].getName();
            File file = new File(imgPath);
            Uri imgUri = Uri.fromFile(file);
            image.setImageURI(imgUri);
            Toast.makeText(MainActivity.this, imgPath, Toast.LENGTH_SHORT).show();
        }
    };
    public void rightClick(View v) {
        File[] files = new File(path).listFiles();
        // Determine is at the end of gallery, if not scroll to next image
        if(nextImage == files.length-1)
            nextImage = 0;
        else
            nextImage++;

        String imgPath = path + files[nextImage].getName();
        File file = new File(imgPath);
        Uri imgUri = Uri.fromFile(file);
        image.setImageURI(imgUri);
        Toast.makeText(MainActivity.this, imgPath, Toast.LENGTH_SHORT).show();
    }
    public void goClick(View v) {
        File[] files = new File(path).listFiles();
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra("imagePath", files[nextImage].getAbsolutePath());
        startActivityForResult(intent,2);
    }

    // Resume the scrolling of images where activity B left off in Activty A
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //if(requestCode == REQUEST_TAKE_PHOTO){
        //  String filePath = data.getDataString();
        // File testFile = new File(filePath);
        //int i = 1;
        //}

        if(requestCode == 2){
            String imgPath = data.getStringExtra("imagePath");
            File file = new File(imgPath);
            Uri imgUri = Uri.fromFile(file);
            String[] fileString = new File(file.getParent()).list();
            nextImage = Arrays.binarySearch(fileString,file.getName());
            image.setImageURI(imgUri);
        }
    }

    public void photoClick(View v) {
        dispatchTakePictureIntent();
    }

    @Override
    public void onClick(View v) {
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch (IOException ex){
                Log.d("photoFile", "Error creating image file");
            }
            if (photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(this,"com.example.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = new File(path);
        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File photoImage = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        photoPath = photoImage.getAbsolutePath();
        return photoImage;
    }
}