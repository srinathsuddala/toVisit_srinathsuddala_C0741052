package com.example.tovisit_srinathsuddala_c0741052;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = FavoritePlace.class, exportSchema = false, version = 1)
public abstract class FavoritePlaceDatabase extends RoomDatabase {
    private static final String DB_NAME = "favorite_places_db";
    private static FavoritePlaceDatabase instance;

    public static synchronized FavoritePlaceDatabase getInstance(Context context){
        if (instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), FavoritePlaceDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        return instance;
    }

    public abstract FavoritePlaceDao favoritePlaceDao();
}
