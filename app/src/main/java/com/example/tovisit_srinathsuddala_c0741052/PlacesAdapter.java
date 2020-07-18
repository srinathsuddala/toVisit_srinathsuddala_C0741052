package com.example.tovisit_srinathsuddala_c0741052;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.MyViewHolder> {

    private List<FavoritePlace> favoritePlaces;

    public PlacesAdapter(List<FavoritePlace> favoritePlaces) {
        this.favoritePlaces = new ArrayList<>();
        this.favoritePlaces.addAll(favoritePlaces);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_place, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesAdapter.MyViewHolder holder, int position) {
        FavoritePlace favoritePlace = favoritePlaces.get(position);
        holder.tvAddress.setText(favoritePlace.getAddress() != null ? favoritePlace.getAddress() : getCreatedTime(favoritePlace.getDateTime()));
    }

    @SuppressLint("SimpleDateFormat")
    private String getCreatedTime(long dateTime) {
        String createdDate = "Place added on ";
        String dateString = new SimpleDateFormat("dd/MM/yyyy").format(new Date(dateTime));
        String timeString = new SimpleDateFormat("HH:mm").format(new Date(dateTime));
        return createdDate.concat(dateString).concat(" at ").concat(timeString);
    }

    @Override
    public int getItemCount() {
        return favoritePlaces.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tvAddress;

        public MyViewHolder(View v) {
            super(v);
            tvAddress = v.findViewById(R.id.tvAddress);
        }
    }

}
