package com.example.triplanproject.LocalStorage;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_place")
public class PlaceClass {
    @PrimaryKey(autoGenerate =  true)
    private long idPlace;
    private String idDirection;
    private String name;
    private String time;
    private long idTrip;
    private double latitude;
    private double longitude;
    private String address;

    public PlaceClass(String name, Long idtrip) {
        this.name = name;
        this.idTrip = idtrip;
    }


    public PlaceClass(String idDirection, String name, String time, long idTrip, double latitude, double longitude, String address) {
        this.idDirection = idDirection;
        this.name = name;
        this.time = time;
        this.idTrip = idTrip;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public String getIdDirection() {
        return idDirection;
    }

    public void setIdDirection(String idDirection) {
        this.idDirection = idDirection;
    }

    public long getIdPlace() {
        return idPlace;
    }

    public void setIdPlace(long idPlace) {
        this.idPlace = idPlace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getIdTrip() {
        return idTrip;
    }

    public void setIdTrip(long idTrip) {
        this.idTrip = idTrip;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
