package com.example.tovisit_srinathsuddala_c0741052.activities;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tovisit_srinathsuddala_c0741052.R;
import com.example.tovisit_srinathsuddala_c0741052.adapters.DisplayNamesAdapter;
import com.example.tovisit_srinathsuddala_c0741052.googleplaces.NRPlaces;
import com.example.tovisit_srinathsuddala_c0741052.googleplaces.Place;
import com.example.tovisit_srinathsuddala_c0741052.googleplaces.PlacesException;
import com.example.tovisit_srinathsuddala_c0741052.googleplaces.PlacesListener;
import com.example.tovisit_srinathsuddala_c0741052.helper.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapMarkersActivity extends AppCompatActivity implements OnMapReadyCallback, PlacesListener {

    private GoogleMap mMap;
    String subcategoryname;
    Toolbar toolbar;
    SessionManager sessionManager;

    private RecyclerView recyclerView;
    private DisplayNamesAdapter adapter;

    ArrayList<Double> distanceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapmarkers);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            subcategoryname = extras.getString("categoryname");
        }
        sessionManager = new SessionManager(MapMarkersActivity.this);
        initViews();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng adminplace = new LatLng(Double.parseDouble(sessionManager.getLatitiude()), Double.parseDouble(sessionManager.getLongitude()));
        addMarker(adminplace, false);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(adminplace, 15));

        new NRPlaces.Builder()
                .listener(this)
                .key("AIzaSyDk35EI0lk7rSwOp9yokQvNTuuQvcW1amU")
                .latlng(Double.parseDouble(sessionManager.getLatitiude()), Double.parseDouble(sessionManager.getLongitude()))
                .radius(sessionManager.getRadius())
                .type(subcategoryname.toLowerCase())
                .build()
                .execute();
    }

    @Override
    public void onPlacesFailure(PlacesException e) {
        Log.i("PlacesAPI", "onPlacesFailure()");
    }

    @Override
    public void onPlacesStart() {
        Log.i("PlacesAPI", "onPlacesStart()");
    }

    @Override
    public void onPlacesSuccess(final List<Place> places) {
        Log.i("PlacesAPI", "onPlacesSuccess()");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Place place : places) {

                    LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng)
                            .title(place.getName()).snippet(place.getVicinity()));
                    distanceList.add(distance(Double.parseDouble(sessionManager.getLatitiude()),
                            Double.parseDouble(sessionManager.getLongitude()), latLng.latitude, latLng.longitude));
                    Log.e("reference", place.getReference());
                }

                adapter = new DisplayNamesAdapter(MapMarkersActivity.this, places, distanceList);
                recyclerView.setAdapter(adapter);

                if (places.size() == 0)
                    Toast.makeText(MapMarkersActivity.this, "No places available within the Radius", Toast.LENGTH_SHORT).show();
                Log.e("size", "" + places.size());

            }
        });
    }

    @Override
    public void onPlacesFinished() {
        Log.i("PlacesAPI", "onPlacesFinished()");
    }

    private void initViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Near By Places");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        recyclerView = (RecyclerView) findViewById(R.id.rvNames);
        // set up the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(MapMarkersActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecoration(MapMarkersActivity.this, DividerItemDecoration.VERTICAL));

    }

    protected Marker addMarker(LatLng position, boolean draggable) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.draggable(draggable);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_admin_red));
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(position.latitude, position.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0);
        markerOptions.title(address);
        markerOptions.position(position);
        Marker pinnedMarker = mMap.addMarker(markerOptions);
        return pinnedMarker;
    }


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
