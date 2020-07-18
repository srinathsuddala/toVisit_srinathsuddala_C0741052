package com.example.tovisit_srinathsuddala_c0741052.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tovisit_srinathsuddala_c0741052.R;
import com.example.tovisit_srinathsuddala_c0741052.activities.CategoryList;
import com.example.tovisit_srinathsuddala_c0741052.activities.MapMarkersActivity;
import com.example.tovisit_srinathsuddala_c0741052.activities.SubCategoryList;

import java.util.ArrayList;

public class FetchCategoryRecyclerViewAdapter extends RecyclerView.Adapter<FetchCategoryRecyclerViewAdapter.ViewHolder> {

    private ArrayList mData;
    private ArrayList<Integer> mDataImages;

    private LayoutInflater mInflater;
    private Activity context;

    // data is passed into the constructor
    public FetchCategoryRecyclerViewAdapter(Activity context, ArrayList<String> data, ArrayList<Integer> dataImages) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mDataImages = dataImages;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_fetchcategory_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String categoryname = mData.get(position).toString();
        holder.myTextView.setText(categoryname);
        if (context instanceof CategoryList)
        holder.categoryImage.setImageResource(mDataImages.get(position));

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (context instanceof CategoryList) {
                    Intent i = new Intent(context, SubCategoryList.class);
                    i.putExtra("categoryname", categoryname);
                    i.putExtra("categoryposition", position);
                    context.startActivity(i);
                } else if (context instanceof SubCategoryList) {

                    Intent i = new Intent(context, MapMarkersActivity.class);
                    i.putExtra("categoryname", categoryname);
                    i.putExtra("categoryposition", position);
                    context.startActivity(i);
                }

            }
        });

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;
        CardView card_view;
        ImageView categoryImage;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = (TextView) itemView.findViewById(R.id.info_text);
            categoryImage = (ImageView) itemView.findViewById(R.id.Iv_cat);
            card_view = (CardView) itemView.findViewById(R.id.card_view);

            if (context instanceof CategoryList) {
                categoryImage.setVisibility(View.VISIBLE);
            }
            if (context instanceof SubCategoryList) {
                categoryImage.setVisibility(View.GONE);
            }
        }

    }

}