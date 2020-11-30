package com.example.triplanproject.LocalStorage;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName =  "trip_tables")
public class Trip {
    @PrimaryKey(autoGenerate =  true)
    private long ID;
    private String title;
    private String date;
    private String time;
    private double latitude;
    private double longitude;
    private String location;


    public Trip(){}

    public Trip(String title) {
        this.title = title;
    }

    public Trip(String title, String date, String time){
        this.title=title;
        this.date=date;
        this.time=time;
       // this.location=location;
    }

    public Trip(long ID, String title, String date, String time) {
        this.ID = ID;
        this.title = title;
        this.date = date;
        this.time = time;
       // this.location=location;
    }

    public Trip(String title, String date, String time, double latitude, double longitude, String location) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
