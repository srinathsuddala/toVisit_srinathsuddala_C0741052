package com.example.tovisit_srinathsuddala_c0741052.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tovisit_srinathsuddala_c0741052.R;
import com.example.tovisit_srinathsuddala_c0741052.adapters.FetchCategoryRecyclerViewAdapter;

import java.util.ArrayList;

public class SubCategoryList extends AppCompatActivity {

    RecyclerView recyclerViewCategories;
    FetchCategoryRecyclerViewAdapter adapter;

    Toolbar toolbar;

    public ArrayList<Integer> categoryimages = new ArrayList<>();

    public ArrayList<String> valSubcategories;
    String categoryname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorylist);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            categoryname = extras.getString("categoryname");
        }

        initViews();
    }

    private void initViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Category List");
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

        switch(categoryname) {

            case "Health":
                // create list one and store values
                valSubcategories = new ArrayList<String>();
                valSubcategories.add("Hospital");
                valSubcategories.add("Dentist");
                valSubcategories.add("Pharmacy");
                valSubcategories.add("Physiotherapist");
                break;

            case "Food":
                // create list one and store values
                valSubcategories = new ArrayList<String>();
                valSubcategories.add("Restaurant");
                valSubcategories.add("Cafe");
                valSubcategories.add("Bakery");
                break;
            case "Entertainment":
                // create list one and store values
                valSubcategories = new ArrayList<String>();
                valSubcategories.add("museum");
                valSubcategories.add("night_club");
                valSubcategories.add("movie_theater");
                break;
        }

        categoryimages.add(R.drawable.hospital);
        categoryimages.add(R.drawable.food);

        recyclerViewCategories = (RecyclerView) findViewById(R.id.rvCategories);
        // set up the RecyclerView
        recyclerViewCategories.setLayoutManager(new GridLayoutManager(SubCategoryList.this, 2));
        adapter = new FetchCategoryRecyclerViewAdapter(SubCategoryList.this, valSubcategories,categoryimages);
        recyclerViewCategories.setAdapter(adapter);

    }
}
