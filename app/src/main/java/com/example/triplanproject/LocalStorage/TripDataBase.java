package com.example.triplanproject.LocalStorage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Trip.class, PlaceClass.class} , version=10)
public abstract class TripDataBase extends RoomDatabase {

    private static TripDataBase instance;
    public abstract TripDao tripDao();
    public abstract  PlaceDao placeDao();

    public static synchronized TripDataBase getInstance(Context context){
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TripDataBase.class, "trip_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;

    }
}
