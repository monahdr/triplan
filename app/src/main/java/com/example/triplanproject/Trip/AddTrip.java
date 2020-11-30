package com.example.triplanproject.Trip;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import androidx.fragment.app.FragmentActivity;

import com.example.triplanproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddTrip extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap map;
    SupportMapFragment mapFragment;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip2);

        searchView= findViewById(R.id.searchViewLocation);
        mapFragment= (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            String location = searchView.getQuery().toString();
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = searchView.getQuery().toString();
                geoLocate( location);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);
    }

    private void geoLocate(String location) {
        Geocoder geocoder = new Geocoder(AddTrip.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(location, 1);
        }catch (IOException e){
            Log.e("MAP", "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d("MAP", "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10) );
            mapFragment.getMapAsync(AddTrip.this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;

    }
}