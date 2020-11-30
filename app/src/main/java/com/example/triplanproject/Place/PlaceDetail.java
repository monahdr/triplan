package com.example.triplanproject.Place;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.triplanproject.LocalStorage.PlaceClass;
import com.example.triplanproject.LocalStorage.TripViewModel;
import com.example.triplanproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PlaceDetail extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private UiSettings mUiSettings;
    Address addressPlace;
    String locationPlace;
    TextView placeTitle, placeTime, placeAddress;
    private TripViewModel tripViewModel;
    SearchView searchView;
    SupportMapFragment mapFragment;
    PlaceClass place;
    long idPlace;
    long idTrip;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        placeTime = findViewById(R.id.placeTime);
        placeTitle = findViewById(R.id.placeTitle);
        searchView = findViewById(R.id.searchViewPlace);
        placeAddress=findViewById(R.id.placeAddress);


        Intent i = getIntent();
        idTrip = i.getLongExtra("IDTrip",0 );
        idPlace = i.getLongExtra("IDPlace", 0);
        Log.d("IDPlace", "ID of the place >>> "+idPlace);
        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        tripViewModel.getPlace(idPlace).observe(this, new Observer<PlaceClass>() {
            @Override
            public void onChanged(PlaceClass placeP) {
                if (placeP != null){
                place = placeP;
                placeTitle.setText(place.getName().toString());
                placeTime.setText(place.getTime().toString());
                placeAddress.setText(place.getAddress().toString());}
            }
        });

    //TO DELETE THE PLACE
        FloatingActionButton fab = findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tripViewModel.deletePlace(place);
                Intent i = new Intent(getApplicationContext(), ListOfPlaces.class);
                i.putExtra("ID",idTrip);
                startActivity(i);

            }
        });

    ///DISPLAY THE LOCATION ON THE MAP
        mapFragment= (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapPlace);
        mapFragment.getMapAsync(this);

    //TO GO BACK
        FloatingActionButton fab2 = findViewById(R.id.fab3);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ListOfPlaces.class);
                i.putExtra("ID",idTrip);
                startActivity(i);

            }
        });




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
        tripViewModel.getPlace(idPlace).observe(this, new Observer<PlaceClass>() {
            @Override
            public void onChanged(PlaceClass place) {
                if(place != null){
                double cityLat =place.getLatitude();
                double cityLong=place.getLongitude();
                LatLng latLng = new LatLng(cityLat,cityLong);
                mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));}
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_place_detail,menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.delete:
                tripViewModel.deletePlace(place);
                Intent places = new Intent(this, ListOfPlaces.class);
                places.putExtra("ID", place.getIdTrip());
                startActivity(places);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goBack(View view) {
        Intent i = new Intent(this, ListOfPlaces.class);
        i.putExtra("ID",place.getIdTrip());
        startActivity(i);
        finish();
    }
}
