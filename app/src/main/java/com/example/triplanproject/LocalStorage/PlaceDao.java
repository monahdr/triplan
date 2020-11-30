package com.example.triplanproject.LocalStorage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlaceDao {

    @Insert
    void insert(PlaceClass place);

    @Update
    void update(PlaceClass place);

    @Delete
    void delete(PlaceClass place);

    @Query("SELECT * FROM table_place WHERE idTrip = :idTripP")
    LiveData<List<PlaceClass>> getAllPlaces(long idTripP);

    @Query("SELECT * FROM table_place WHERE idPlace = :id")
    LiveData<PlaceClass> getPlace(Long id);

    @Query("DELETE FROM table_place WHERE idPlace = :idTrip")
    void delete(long idTrip);

}
