package com.example.ajinkyarode.addressfinder;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by ajinkyarode on 3/24/17.
 */
public class CameraAccess extends Activity{

    private static final int CAMERA_REQUEST = 1888;
    private static final int PICTURE_RESULT = 1;
    private ImageView imageView;
    private Button capture;
    private Button exit;
    private Button save;
    private TextView addr;
    private StorageReference mStorage;
    private Bitmap photo;
    private String addrs;
    private String temp2;
    private String temp1;
    ContentValues values;
    Uri imageUri;
    Bitmap thumbnail;
    String imageurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_camera);
        this.imageView = (ImageView)this.findViewById(R.id.imageView);




        capture=(Button) findViewById(R.id.button3);
        exit = (Button) findViewById(R.id.button5);
        addr= (TextView) findViewById(R.id.textView7);
        save= (Button) findViewById(R.id.button4);
        mStorage = FirebaseStorage.getInstance().getReference();
        Intent intent = getIntent();
        final String address = intent.getExtras().getString("address");

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        capture.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, PICTURE_RESULT);



                //startActivityForResult(cameraIntent, CAMERA_REQUEST);
                addrs=address;
                String[] temp = addrs.split(":");
                temp1=temp[0];
                temp2=temp[1];
                addr.setText(temp1);
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String encodedString = BitMapToString(photo);
        String st=mStorage.getPath();
                Log.d("hi",st);
        StorageReference filedata = mStorage.child("Photos").child(temp2).child(temp1.concat(".jpg"));

        byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);

        filedata.putBytes(encodeByte);
                
        //myRef.setValue("Hello, World!");
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.exit(0);
            }
        });




    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case PICTURE_RESULT:
                if (requestCode == PICTURE_RESULT)
                    if (resultCode == Activity.RESULT_OK) {
                        try {
                            photo = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(), imageUri);
                            imageView.setImageBitmap(photo);
                            imageurl = getRealPathFromURI(imageUri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
             photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }*/

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }


}

