package com.example.tovisit_srinathsuddala_c0741052.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tovisit_srinathsuddala_c0741052.R;
import com.example.tovisit_srinathsuddala_c0741052.activities.PlaceDetailsActivity;
import com.example.tovisit_srinathsuddala_c0741052.googleplaces.Place;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

public class DisplayNamesAdapter extends RecyclerView.Adapter<DisplayNamesAdapter.ViewHolder> {

    private List<Place> mData;
    private ArrayList<Double> mDataplacesDistance;
    private LayoutInflater mInflater;
    private Context context;

    public DisplayNamesAdapter(Context mapMarkersActivity, List<Place> fetchplaces, ArrayList<Double> fetchplacesDistance) {
        this.context = mapMarkersActivity;
        this.mInflater = LayoutInflater.from(this.context);
        this.mData = fetchplaces;
        this.mDataplacesDistance = fetchplacesDistance;

    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String categoryname = mData.get(position).getName();
        holder.myTextView.setText(categoryname);

        holder.myTextviewDistance.setText(rounds(mDataplacesDistance.get(position),2) + "kms");

        holder.myTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, PlaceDetailsActivity.class);
                i.putExtra("reference", mData.get(position).getReference());
                context.startActivity(i);
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
        TextView myTextView,myTextviewDistance;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = (TextView) itemView.findViewById(R.id.name);
            myTextviewDistance = (TextView) itemView.findViewById(R.id.distance);

        }

    }

    public double rounds(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = round(value);
        return (double) tmp / factor;
    }

}