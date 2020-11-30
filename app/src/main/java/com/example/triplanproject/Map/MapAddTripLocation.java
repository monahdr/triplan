package com.example.triplanproject.Map;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.triplanproject.Trip.ListOfTrips;
import com.example.triplanproject.LocalStorage.Trip;
import com.example.triplanproject.LocalStorage.TripViewModel;
import com.example.triplanproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapAddTripLocation extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private UiSettings mUiSettings;
    Address address;
    String location;
    EditText tripTitle, tripTime, tripDate;
    private TripViewModel tripViewModel;
    Toolbar toolbar;
    SearchView searchView;
    SupportMapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        tripTime = findViewById(R.id.placeTime);
        tripDate=findViewById(R.id.dateTrip1);
        tripTitle=findViewById(R.id.placeTitle);
        searchView=findViewById(R.id.searchViewTrip);


        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);

        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New trip");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       // DISPLAY THE MAP
        mapFragment= (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                location =searchView.getQuery().toString();
                List<Address> addressList = null;

                if(location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapAddTripLocation.this);

                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng ,10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MapAddTripLocation.class));
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    private void goToMain() {
        startActivity(new Intent(this, ListOfTrips.class));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }



    public void SaveNewTrip(View view) {
        double latitude =0;
        double longitude=0;
        if (address != null)
        { latitude = address.getLatitude();
            longitude = address.getLongitude();
        }
        Trip newTrip= new Trip(tripTitle.getText().toString(),tripDate.getText().toString(),tripTime.getText().toString(), latitude, longitude,location);
        tripViewModel.insert(newTrip);
        Log.d("latitude","lat  : "+ latitude+" longitude "+longitude);
        Log.d("ID ","ID created >>> "+ newTrip.getID());
        Toast.makeText(this, "Your trip is created", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(MapAddTripLocation.this, ListOfTrips.class);
        i.putExtra("ID", newTrip.getID());
        startActivity(i);

    }
}


