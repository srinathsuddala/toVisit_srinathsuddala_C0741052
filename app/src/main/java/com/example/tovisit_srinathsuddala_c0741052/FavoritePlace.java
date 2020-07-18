package com.example.tovisit_srinathsuddala_c0741052;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_place")
public class FavoritePlace {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "address")
    private String address;
    @ColumnInfo(name = "dateTime")
    private long dateTime;

    public FavoritePlace(String address, long dateTime){
        this.address = address;
        this.dateTime = dateTime;
    }

    @Ignore
    public FavoritePlace(long dateTime){
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }
}
