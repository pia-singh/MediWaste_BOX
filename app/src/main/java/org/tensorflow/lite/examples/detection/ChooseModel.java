package org.tensorflow.lite.examples.detection;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChooseModel extends AppCompatActivity {

    // for permission requests
    public static final int REQUEST_PERMISSION = 300;

    // request code for permission requests to the os for image
    public static final int REQUEST_IMAGE = 100;

    // will hold uri of image obtained from camera
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.img_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            //Toast.makeText(getApplicationContext(), "getSupportActionBar", Toast.LENGTH_LONG).show();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // request permission to use the camera on the user's phone
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, REQUEST_PERMISSION);
        }

        // request permission to write data (aka images) to the user's external storage of their phone
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }

        // request permission to read data (aka images) from the user's external storage of their phone
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }


                // open camera
                openCameraIntent();


    }

    // opens camera for user
    private void openCameraIntent(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        // tell camera where to store the resulting picture
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // start camera, and wait for it to finish
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    // checks that the user has allowed all the required permission of read and write and camera. If not, notify the user and close the application
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(getApplicationContext(),"This application needs read, write, and camera permissions to run. Application now closing.",Toast.LENGTH_LONG);
                System.exit(0);
            }
        }
    }
    // dictates what to do after the user takes an image, selects and image, or crops an image
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  @NonNull Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {

            //final ImageView imageView = findViewById(R.id.image_view);
            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            Bitmap bitmap=null;
            List<Uri> uriList = new ArrayList<Uri>();

            uriList.add(imageUri);
            Intent intent = new Intent(ChooseModel.this,ExpansionPanelSampleActivityProgrammatically.class);
            intent.putParcelableArrayListExtra("uriList", (ArrayList<? extends Parcelable>) uriList);
            startActivity(intent);
            finish();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//                imageView.setImageBitmap(bitmap);
//
//                Bitmap b2 = ma.getInference(getApplicationContext(),imageUri);
//                if(b2!=null)
//                {
//                    imageView.setImageBitmap(b2);
//                }
//
//
//                // not sure why this happens, but without this the image appears on its side
//                //imageView.setRotation(imageView.getRotation() + 90);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

}
