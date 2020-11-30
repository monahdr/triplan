package com.example.triplanproject.LocalStorage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TripDao {

    @Insert
    void insert(Trip trip);

    @Update
    void update(Trip trip);

    @Delete
    void delete(Trip trip);

    @Query("DELETE FROM trip_tables")
        void deleteAllTrips();

    @Query("SELECT * FROM trip_tables")
    LiveData<List<Trip>> getAllTrips();

    @Query("SELECT * FROM trip_tables WHERE ID=:id")
    LiveData<Trip> getTrip(long id);
}
