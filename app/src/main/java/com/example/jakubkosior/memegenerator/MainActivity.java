package com.example.jakubkosior.memegenerator;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void pickPhotoFromGallery(View v){
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

        startActivityForResult(photoIntent,);
    }
}
