package com.example.triplanproject.Map;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.triplanproject.LocalStorage.PlaceClass;
import com.example.triplanproject.LocalStorage.Trip;
import com.example.triplanproject.LocalStorage.TripViewModel;
import com.example.triplanproject.Place.ListOfPlaces;
import com.example.triplanproject.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;

public class AddPlace extends FragmentActivity implements OnMapReadyCallback {

        private GoogleMap mMap;
        String address;
        Address addressCity;
        String location;
        String locationTrip;
        String idDirection;
        Place place;
        EditText placeTime ;
        private TripViewModel tripViewModel;
        SearchView searchView;
        SupportMapFragment mapFragment;
        long idTrip;
        Trip trip;





        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_place);

            placeTime=findViewById(R.id.placeTime);
            searchView=findViewById(R.id.searchViewPlace);


            Intent i =getIntent();
            idTrip =i.getExtras().getLong("ID");
            Log.d("ID","ID >>> "+ idTrip);
            tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);


            //DISPLAY THE MAP
            mapFragment= (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapPlace);

            mapFragment.getMapAsync(this);





            //delete
            FloatingActionButton fab2 = findViewById(R.id.fab2);
            fab2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(AddPlace.this, ListOfPlaces.class);
                    i.putExtra("ID",idTrip);
                    startActivity(i);

                }
            });

            ///PLACE AUTOCOMPLETE
            //Initialize SDK
            String apiKey = getResources().getString(R.string.google_api_key);
            Places.initialize(getApplicationContext(), "AIzaSyBedF9FhTLC3WG_KwACNMNpLJNfZArr1ys");

            //Create a new places client instance
            PlacesClient placesClient = Places.createClient(this);

            //Initialize the AutocompleteSupportFragment
            final AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                    getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
            autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);

            autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);

            tripViewModel.getTrip(idTrip).observe(this, new Observer<Trip>() {
                @Override
                public void onChanged(Trip trip) {
                    double tripLat=trip.getLatitude();
                    double tripLong=trip.getLongitude();
                    LatLng latLngR = new LatLng(tripLat-1,tripLong-1);
                    LatLng latLngL = new LatLng(tripLat+1,tripLong+1);
                    RectangularBounds bounds = RectangularBounds.newInstance(latLngR,latLngL);
                    autocompleteFragment.setLocationRestriction(bounds);

                    ///Types of place data to return
                    AutocompleteSupportFragment autocompleteSupportFragment;
                    autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));


                    autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                        @Override
                        public void onPlaceSelected(@NonNull Place placeP) {
                            //Get info about the selected place
                            Log.i("Place auto", "Place: "+placeP.getName()+", "+placeP.getId()+" address :" +placeP.getAddressComponents());
                            location= placeP.getName();
                            place=placeP;
                            idDirection=placeP.getId();
                            address=placeP.getAddress();
                            LatLng latLng = new LatLng(placeP.getLatLng().latitude,placeP.getLatLng().longitude);
                            mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng ,13));}


                        @Override
                        public void onError(@NonNull Status status) {
                            Log.i("Place auto"," An error occured: "+status);
                        }
                    });
                }
            });
            //autocompleteFragment.setLocationBias(RectangularBounds.newInstance(new LatLng(-33.88,151.184), new LatLng(-33.8,151.22)));
            //autocompleteFragment.setCountry("IN");


            //Add place
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(place != null & location != null & address !=null & placeTime != null ) {
                        PlaceClass newPlace = new PlaceClass( idDirection, location, placeTime.getText().toString(),idTrip,place.getLatLng().latitude,place.getLatLng().longitude,address);
                        tripViewModel.insert(newPlace);
                        Log.d("IDPLACE", "address : "+newPlace.getAddress()+" iddi "+newPlace.getIdDirection());
                        Log.d("IDPLACE ","ID created >>> "+ newPlace.getIdPlace());
                        Log.d("IDT ","ID TRIP of the place created >>> "+ newPlace.getIdTrip());
                        Toast.makeText(AddPlace.this, "A new place is added to your trip !", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(AddPlace.this, ListOfPlaces.class);
                        i.putExtra("ID",idTrip);
                        startActivity(i);}

                    else {
                        Toast.makeText(AddPlace.this, "Information is missing, you can't add the place yet ", Toast.LENGTH_SHORT).show();
                    }


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
            tripViewModel.getTrip(idTrip).observe(this, new Observer<Trip>() {
                @Override
                public void onChanged(Trip tripP) {

                        trip =tripP;
                        LatLng city = new LatLng(trip.getLatitude(),trip.getLongitude());
                        Log.d("Location city","Lat : "+trip.getLatitude()+" Long : "+trip.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(city,11));


                }
            });

        }
}