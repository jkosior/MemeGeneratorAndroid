package com.example.jakubkosior.memegenerator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 10;
    public static final int REQUEST_CODE_STORAGE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void pickPhotoFromGallery(View v){


    }

    private void photoIntentCreator() {
        Intent photoIntent = new Intent(Intent.ACTION_PICK);

        File photoDirectory = Environment
                .getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                );

        Uri photoUri = Uri.parse(photoDirectory.getPath());

        /**
         * URI: photoUri
         * type: whatever
         */

        photoIntent.setDataAndType(photoUri,"image/*");

        startActivityForResult(photoIntent, REQUEST_CODE);
    }

    private void requestPermission(){
        if(ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_STORAGE);

        }
    }

    @Override
    private void onRequestPermissionResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults){

        switch(requestCode){

            case REQUEST_CODE_STORAGE: {
                if(grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    /**
                     * If permission granted, get image
                     */

                    this.photoIntentCreator();

                } else {

                    /**
                     * If permission denied, show toast
                     */

                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();

                }
            }
        }

    }
}
