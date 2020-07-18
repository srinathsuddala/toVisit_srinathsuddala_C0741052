package com.example.tovisit_srinathsuddala_c0741052.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Interpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

import com.example.tovisit_srinathsuddala_c0741052.FavoritePlacesActivity;
import com.example.tovisit_srinathsuddala_c0741052.R;
import com.example.tovisit_srinathsuddala_c0741052.helper.SessionManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SelectLocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private static final String TAG = "SelectLocationActivity";
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int DEFAULT_RADIUS = 2000;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean isLocationEnabled;
    private double lati, longi;
    private int zoomLevel;
    int circleRadius;
    Circle circle;

    private GoogleMap mMap;
    private AlertDialog dialog;
    ProgressDialog loadingdialog;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private int screenHeight, screenWidth;
    private int MapType;

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        circleRadius = prefs.getInt("radius", DEFAULT_RADIUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                if (!isLocationEnabled()) {
                    locationRequestDialog();
                }
            }
        } else {
            if (!isLocationEnabled()) {
                locationRequestDialog();
            }
        }
        if (mMap != null) {

            MapType = prefs.getInt("map_type", GoogleMap.MAP_TYPE_NORMAL);

            switch (MapType) {

                case 0:
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;
                case 1:
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    break;
                case 2:
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    break;
                case 3:
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    break;

            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        isLocationEnabled = true;
                        if (mMap == null)
                            initMap();
                        else if (mMap != null) {
                            mMap.clear();
                            moveCamera(new LatLng(lati, longi), "My Location", false);
                        }
                        //Request location updates:
                    }

                } else {
                    isLocationEnabled = false;
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        MenuItem settingsDone = menu.findItem(R.id.setting_done);
        MenuItem favorites = menu.findItem(R.id.favorites);
        settingsDone.setVisible(true);
        favorites.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorites:
                session.setLatLngValues(String.valueOf(lati), String.valueOf(longi));
                session.setRadius(circleRadius);
                Intent intent = new Intent(SelectLocationActivity.this, FavoritePlacesActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_done:
                startActivity(new Intent(SelectLocationActivity.this, SettingsActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setMapType(MapType);

        mMap.setOnMapClickListener(this);
        getCurrentLocation();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (mMap != null) {
            mMap.clear();
            try {
                lati = latLng.latitude;
                longi = latLng.longitude;
                moveCamera(new LatLng(lati, longi), "My Location", false);
            } catch (SecurityException e) {
                Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
            }
        }
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                showPermissionDialoge();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            if (mMap == null)
                initMap();
            else if (mMap != null) {
                mMap.clear();
                moveCamera(new LatLng(lati, longi), "My Location", false);
            }
            return true;
        }
    }

    private void locationRequestDialog() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private void showPermissionDialoge() {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Location Enable")
                    .setMessage("Please Turn On Your Location")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(SelectLocationActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isLocationEnabled && mMap == null) {
            initMap();
        } else if (isLocationEnabled && mMap != null) {
            mMap.clear();
            moveCamera(new LatLng(lati, longi), "My Location", false);
        }
        return isLocationEnabled;
    }

    private void initViews() {

        //getting height and width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;

        toolBarSetup();

        // Session manager
        session = new SessionManager(getApplicationContext());

        prefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        editor = prefs.edit();

        circleRadius = prefs.getInt("radius", DEFAULT_RADIUS);
        loadingdialog = new ProgressDialog(this);

        MapType = prefs.getInt("map_type", GoogleMap.MAP_TYPE_NORMAL);
        Log.d("maptype", MapType + "");

        dialog = new AlertDialog.Builder(this).setTitle("Enable Location")
                .setMessage("Location access is required to show your location.")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                }).create();
        dialog.setCanceledOnTouchOutside(false);

    }

    private void toolBarSetup() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        // getSupportActionBar().setHomeButtonEnabled(true);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       /* toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }

    private void initMap() {
        Log.e(TAG, "" + isServiceOk());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (isServiceOk()) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        }
    }

    public boolean isServiceOk() {
        Log.d(TAG, "is ServiceOk: checking Google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(SelectLocationActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "is ServiceOk: checking Google services is success working..");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "is ServiceOk: an error occured..");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(SelectLocationActivity.this, available, 9001);
            dialog.show();

        } else {
            Log.d(TAG, "You can't make map request");
        }
        return false;
    }

    private void getCurrentLocation() {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (isLocationEnabled) {

                if (!loadingdialog.isShowing())
                    loadingdialog.show();
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                            loadingdialog.dismiss();

                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            lati = currentLocation.getLatitude();
                            longi = currentLocation.getLongitude();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), "My Location", true);


                        } else {
                            loadingdialog.dismiss();
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(SelectLocationActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            loadingdialog.dismiss();
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(final LatLng latLng, String title, boolean isDropAnomationEnable) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);

        if (title.equals("My Location")) {

            circle = mMap.addCircle(new CircleOptions().center(latLng).radius(circleRadius).strokeColor(getResources().getColor(R.color.color_circle_radar_dark)).fillColor(getResources().getColor(R.color.color_circle_radar_light)).strokeWidth(5));
            circle.setVisible(false);

            if (isDropAnomationEnable) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, getZoomLevel(circle)), new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        addMarker(latLng, false, true);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            } else {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, getZoomLevel(circle)));
                addMarker(latLng, false, false);
            }

        }

    }

    protected Marker addMarker(LatLng position, boolean draggable, boolean isDropAnomationEnable) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.draggable(draggable);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_admin_red));
        markerOptions.position(position);
        Marker pinnedMarker = mMap.addMarker(markerOptions);
        if (isDropAnomationEnable)
            startDropMarkerAnimation(pinnedMarker);
        else
            circle.setVisible(true);
        return pinnedMarker;
    }

    private void startDropMarkerAnimation(final Marker marker) {

        final LatLng target = marker.getPosition();
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point targetPoint = proj.toScreenLocation(target);
        final long duration = (long) (500 + (targetPoint.y * 0.6));
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        startPoint.y = 0;
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final Interpolator interpolator = new LinearOutSlowInInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * target.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * target.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    // Post again 16ms later == 60 frames per second
                    handler.postDelayed(this, 16);
                } else {

                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // circleRadarAnimation(marker);
                            circle.setVisible(true);
                        }
                    }, 200);

                }
            }
        });
    }

    public int getZoomLevel(Circle circle) {
        if (circle != null) {
            double radius = circle.getRadius();
            double scale = radius / 400;
            zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));
        }
        return zoomLevel;
    }
}
