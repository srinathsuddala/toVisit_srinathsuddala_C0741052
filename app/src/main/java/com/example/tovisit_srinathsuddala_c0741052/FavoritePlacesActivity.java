package com.example.tovisit_srinathsuddala_c0741052;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tovisit_srinathsuddala_c0741052.activities.CategoryList;

public class FavoritePlacesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView textView;
    private PlacesAdapter placesAdapter;
    FavoritePlaceDatabase favoritePlaceDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_places);
        toolBarSetup();
        recyclerView = findViewById(R.id.rvPlaces);
        textView = findViewById(R.id.tvNoData);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        favoritePlaceDatabase = FavoritePlaceDatabase.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
    }

    private void setAdapter() {
        if (favoritePlaceDatabase.favoritePlaceDao().getFavoritePlacesList().size() > 0) {
            placesAdapter = new PlacesAdapter(favoritePlaceDatabase.favoritePlaceDao().getFavoritePlacesList());
            recyclerView.setAdapter(placesAdapter);
            recyclerView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
    }

    void toolBarSetup() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Favorite Places");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        MenuItem add = menu.findItem(R.id.add);
        add.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                startActivity(new Intent(FavoritePlacesActivity.this, CategoryList.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
