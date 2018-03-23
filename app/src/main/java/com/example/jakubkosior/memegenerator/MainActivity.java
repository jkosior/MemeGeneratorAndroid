package com.example.jakubkosior.memegenerator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 10;
    public static final int REQUEST_CODE_STORAGE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button shareButton = (Button) this.findViewById(R.id.share_button);

        shareButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                sharePhoto();
            }
        });

    }

    private void sharePhoto() {
        createComposite();
        createShareIntent();
    }

    private void createShareIntent() {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);

        File image = new File(getCacheDir(), "images/image.png");
        Uri imageUri = FileProvider.getUriForFile(
                    this,
                    "com.mydomain.fileprovider",
                    image);

        sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        sendIntent.setType("image/png");

        startActivity(sendIntent);
    }

    private void createComposite() {
        /**
         * Creation of composite image
         */
        FrameLayout meme = (FrameLayout) findViewById(R.id.meme_layout);
        meme.setDrawingCacheEnabled(true);
        Bitmap bitmap = meme.getDrawingCache();

        File sharedFile = new File(
                getCacheDir(),
                "images"
            );


        sharedFile.mkdirs();

        try {
            FileOutputStream stream = new FileOutputStream(sharedFile + "/image.png");

            bitmap.compress(
                    Bitmap.CompressFormat.PNG,
                    100,
                    stream
                );

            stream.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

        meme.setDrawingCacheEnabled(false);
        meme.destroyDrawingCache();
    }

    public void pickPhotoFromGallery(View v){
        requestPermission();
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
        else{
            photoIntentCreator();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

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

                    Toast.makeText(this,
                            "Permission denied",
                            Toast.LENGTH_SHORT).show();

                }
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            Uri photoUri = data.getData();

            ImageView memePhoto = (ImageView) this.findViewById(R.id.meme_photo_view);


            /**
             * Picasso external image library
             */

            Picasso.with(this).load(photoUri).into(memePhoto);

        }
    }
}
