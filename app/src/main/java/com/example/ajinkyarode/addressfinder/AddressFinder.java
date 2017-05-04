package com.example.ajinkyarode.addressfinder;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//43.1094612
//-77.66465746


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddressFinder extends FragmentActivity implements OnMapReadyCallback {
    Geocoder geocoder;
    private Button button;
    private Button camera;
    private TextView latlon;
    private TextView display;
    public Double lat;
    public Double lon;
    public String TAG = "LocationApp";
    private LocationManager mlocManager;
    public LocationListener mlocListener;
    private String address=null;
    private static String locale=null;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_finder);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        button = (Button) findViewById(R.id.button);
        camera = (Button) findViewById(R.id.button2);
        latlon = (TextView) findViewById(R.id.textView4);

        display = (TextView) findViewById(R.id.textView2);



        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        mlocListener = new MyLocation1();
        //Toast.makeText(getApplicationContext(), "HI", Toast.LENGTH_SHORT).show();

        if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //buildAlertMessageNoGps();
            Log.d(TAG,"No provider");
        }

        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, mlocListener);

            button.setEnabled(true);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //lat = Double.parseDouble(latitude.getText().toString());
                    //lon = Double.parseDouble(longitude.getText().toString());

                    //Log.d(TAG,lat.toString());
                    //Log.d(TAG,lon.toString());


                    if (lon != null & lat != null) {
                        latlon.setText("Latitude: "+lat.toString()+"\n"+"Longitude: "+lon.toString());
                        address = getLoc(lat, lon);
                        display.setText(address);
                    }
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            mMap = googleMap;
                            if (lon != null & lat != null) {
                                LatLng loc = new LatLng(lat, lon);
                                mMap.addMarker(new MarkerOptions().position(loc).title(address));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
                            }
                        }
                    });
                }
            });


        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AddressFinder.this, CameraAccess.class);
                intent.putExtra("address", address+":"+locale);
                startActivity(intent);
            }
        });
    }


    public String getLoc(Double lat, Double lon) {

        List<Address> addresses;

        geocoder = new Geocoder(this, Locale.getDefault());
        String city = null;
        String state = null;
        String country = null;
        String postalCode = null;
        String locality = null;
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);

            locality = addresses.get(0).getFeatureName();
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            postalCode = addresses.get(0).getPostalCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        locale = city + " " + state;
        final String address = locality + " " + city + " " + state + " " + country + " " + postalCode;
        return address;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (lon != null & lat != null) {
            LatLng loc = new LatLng(lat, lon);
            mMap.addMarker(new MarkerOptions().position(loc).title("ABC"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,15));

        }
    }

    private class MyLocation1 implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {

        String msg = "New Latitude: " + location.getLatitude()
                + "New Longitude: " + location.getLongitude();
        lat=location.getLatitude();
        lon=location.getLongitude();
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {


        Toast.makeText(getBaseContext(), "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {

        Toast.makeText(getBaseContext(), "Gps is turned on!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
    }

}

