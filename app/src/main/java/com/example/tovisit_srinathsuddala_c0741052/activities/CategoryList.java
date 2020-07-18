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

public class CategoryList extends AppCompatActivity {

    RecyclerView recyclerViewCategories;
    FetchCategoryRecyclerViewAdapter adapter;

    Toolbar toolbar;

    public ArrayList<String> categorynames = new ArrayList<>();
    public ArrayList<Integer> categoryimages = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorylist);

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

        // put values into map
        categorynames.add("Health");
        categorynames.add("Food");
        categorynames.add("Entertainment");

        categoryimages.add(R.drawable.hospital);
        categoryimages.add(R.drawable.food);
        categoryimages.add(R.drawable.ic_games_24px);


        recyclerViewCategories = (RecyclerView) findViewById(R.id.rvCategories);
        // set up the RecyclerView
        recyclerViewCategories.setLayoutManager(new GridLayoutManager(CategoryList.this, 2));
        adapter = new FetchCategoryRecyclerViewAdapter(CategoryList.this, categorynames,categoryimages);
        recyclerViewCategories.setAdapter(adapter);

    }
}
