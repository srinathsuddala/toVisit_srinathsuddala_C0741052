package com.example.tovisit_srinathsuddala_c0741052.activities;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tovisit_srinathsuddala_c0741052.FavoritePlace;
import com.example.tovisit_srinathsuddala_c0741052.FavoritePlaceDatabase;
import com.example.tovisit_srinathsuddala_c0741052.R;
import com.example.tovisit_srinathsuddala_c0741052.googleplaces.PlaceDetailsJSONParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;

public class PlaceDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int MY_PERMISSIONS_REQUEST_PHONE_CALLS = 123;

    WebView mWvPlaceDetails;
    String international_phone_number;
    Toolbar toolbar;
    private TextView tvAdd;
    private FavoritePlaceDatabase favoritePlaceDatabase;
    private String selectedPlace = "";

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        tvAdd = findViewById(R.id.tvAdd);
        tvAdd.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Details");
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
        favoritePlaceDatabase = FavoritePlaceDatabase.getInstance(this);
        // Getting reference to WebView ( wv_place_details ) of the layout activity_place_details
        mWvPlaceDetails = (WebView) findViewById(R.id.wv_place_details);

        mWvPlaceDetails.getSettings().setUseWideViewPort(false);

        // Getting place reference from the map
        String reference = getIntent().getStringExtra("reference");


        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        sb.append("reference=" + reference);
        sb.append("&sensor=true");
        sb.append("&key=AIzaSyDk35EI0lk7rSwOp9yokQvNTuuQvcW1amU");


        // Creating a new non-ui thread task to download Google place details
        PlacesTask placesTask = new PlacesTask();

        // Invokes the "doInBackground()" method of the class PlaceTask
        placesTask.execute(sb.toString());

    }

    ;


    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);


            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        } catch (Exception e) {
            Log.d("Excptn while dwnld url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvAdd:
                FavoritePlace favoritePlace = new FavoritePlace(selectedPlace, Calendar.getInstance().getTimeInMillis());
                favoritePlaceDatabase.favoritePlaceDao().insertFavoritePlace(favoritePlace);
                Toast.makeText(PlaceDetailsActivity.this, "Location added to favorites", Toast.LENGTH_LONG).show();
                finish();
                break;
            default:
                break;
        }
    }


    /**
     * A class, to download Google Place Details
     */
    private class PlacesTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google place details in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }
    }


    /**
     * A class to parse the Google Place Details in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, HashMap<String, String>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected HashMap<String, String> doInBackground(String... jsonData) {

            HashMap<String, String> hPlaceDetails = null;
            PlaceDetailsJSONParser placeDetailsJsonParser = new PlaceDetailsJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                // Start parsing Google place details in JSON format
                hPlaceDetails = placeDetailsJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return hPlaceDetails;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(HashMap<String, String> hPlaceDetails) {


            String name = hPlaceDetails.get("name");
            String icon = hPlaceDetails.get("icon");
            String vicinity = hPlaceDetails.get("vicinity");
            String lat = hPlaceDetails.get("lat");
            String lng = hPlaceDetails.get("lng");
            String formatted_address = hPlaceDetails.get("formatted_address");
            selectedPlace = formatted_address;
            String formatted_phone = hPlaceDetails.get("formatted_phone");
            String website = hPlaceDetails.get("website");
            String rating = hPlaceDetails.get("rating");
            international_phone_number = hPlaceDetails.get("international_phone_number");
            String url = hPlaceDetails.get("url");


            String mimeType = "text/html";
            String encoding = "utf-8";

            String data = "<html>" +
                    "<body><img style='float:left' src=" + icon + " /><h1><center>" + name + "</center></h1>" +
                    "<br style='clear:both' />" +
                    "<hr  />" +
                    "<p>Vicinity : " + vicinity + "</p>" +
                    "<p>Location : " + lat + "," + lng + "</p>" +
                    "<p>Address : " + formatted_address + "</p>" +
                    "<p>Phone : " + formatted_phone + "</p>" +
                    "<p>Website : " + website + "</p>" +
                    "<p>Rating : " + rating + "</p>" +
                    "<p>International Phone  : " + international_phone_number + "</p>" +
                    "<p>URL  : <a href='" + url + "'>" + url + "</p>" +
                    "</body></html>";

            // Setting the data in WebView
            mWvPlaceDetails.loadDataWithBaseURL("", data, mimeType, encoding, "");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        MenuItem setting_done = menu.findItem(R.id.setting_done);
        MenuItem done = menu.findItem(R.id.done);
        MenuItem menu_call = menu.findItem(R.id.menu_call);
        setting_done.setVisible(false);
        done.setVisible(false);
        menu_call.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_call:

                if (international_phone_number != null) {
                    //if we do this thing using INTENT then permission is not required
                    boolean result = checkPermission();
                    if (result) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + international_phone_number));
                        startActivity(intent);
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(PlaceDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(PlaceDetailsActivity.this, Manifest.permission.CALL_PHONE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PlaceDetailsActivity.this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("call permission is necessary to call phones!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(PlaceDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_PHONE_CALLS);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(PlaceDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_PHONE_CALLS);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_PHONE_CALLS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + international_phone_number));
                    startActivity(intent);

                } else {
                    //code for deny
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}