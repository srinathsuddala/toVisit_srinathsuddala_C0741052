package com.example.tovisit_srinathsuddala_c0741052;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavoritePlaceDao {

    @Query("Select * from favorite_place")
    List<FavoritePlace> getFavoritePlacesList();

    @Insert
    void insertFavoritePlace(FavoritePlace favoritePlace);

    @Update
    void updateFavoritePlace(FavoritePlace favoritePlace);

    @Delete
    void deleteFavoritePlace(FavoritePlace favoritePlace);
}
