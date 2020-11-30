package com.example.triplanproject.Retrofit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.triplanproject.LocalStorage.PlaceClass;
import com.example.triplanproject.LocalStorage.Trip;
import com.example.triplanproject.LocalStorage.TripViewModel;
import com.example.triplanproject.R;
import com.example.triplanproject.Trip.TripDetails;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MapTrip extends FragmentActivity implements OnMapReadyCallback {
    private TripViewModel tripViewModel;
    GoogleMap mMap;
    int cursor;
    TextView namePlaceSelected, timePlaceSelected,timeTitle,placeTitle,hour, tripCity, addressPlaceSelected, addressTitle ;
    Button goNext;
    LatLng city;


    long id;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_trip);

        namePlaceSelected=findViewById(R.id.placeName);
        timePlaceSelected=findViewById(R.id.placeTime);
        timeTitle=findViewById(R.id.timeTitle);
        placeTitle=findViewById(R.id.placeTitle);
        goNext =findViewById(R.id.goToPlace);
        hour=findViewById(R.id.hour);
        tripCity=findViewById(R.id.tripCity);
        addressPlaceSelected=findViewById(R.id.placeAddress);
        addressTitle=findViewById(R.id.addressTitle);

        timeTitle.setText("");
        placeTitle.setText("");
        hour.setText("");
        addressTitle.setText("");

        Intent directionR = getIntent();
        id = directionR.getLongExtra("ID", 0);


        //Create the list of location
        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        tripViewModel.getAllPlace(id).observe(this, new Observer<List<PlaceClass>>() {
            @Override
            public void onChanged(List<PlaceClass> places) {
                for (PlaceClass place : places) {
                    if(places.size() !=0 ){
                    //Display path
                    int NbOfPlaces = places.size();
                    String origin = places.get(0).getName();
                    String destination = places.get(NbOfPlaces-1).getName();
                    String apiKey = "AIzaSyCIHkEtMM1vQyaIJ55L-FF7y_S5kUwQdZQ";
                    for(int i=1;i<places.size();i++){
                        tripViewModel.updateDirection(places.get(i-1).getName(), places.get(i).getName(), apiKey);

                    }}else {
                        Toast.makeText(MapTrip.this, "There are no place created yet in your trip", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


        ///DISPLAY THE ROUTES
        tripViewModel.getRoutes().observe(this, new Observer<List<DirectionResponse.Routes>>() {
            @Override
            public void onChanged(List<DirectionResponse.Routes> routes) {
                int count = 0;
                ArrayList<LatLng> points = null;
                setRoutePath(routes);
            }
        });


        //TO GO BACK
        FloatingActionButton fab2 = findViewById(R.id.fab3);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), TripDetails.class);
                i.putExtra("ID", id);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * This method will manipulate the Google Map on the main screen
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Google map setup
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // zoom the map in the city of the trip
        tripViewModel.getTrip(id).observe(this, new Observer<Trip>() {
            @Override
            public void onChanged(Trip trip) {
                if(trip != null){
                tripCity.setText(trip.getLocation().toString());
                city = new LatLng(trip.getLatitude(), trip.getLongitude());
                Log.d("Location city", "Lat : " + trip.getLatitude() + " Long : " + trip.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(city, 11));}
            }
        });

        //show the marker on each place and adjust the zoom level
        tripViewModel.getAllPlace(id).observe(this, new Observer<List<PlaceClass>>() {
            @Override
            public void onChanged(List<PlaceClass> places) {
                for (PlaceClass place : places) {
                    LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()));
                }

            }

        });


        //I initiate here the cursor
        cursor=0;

    }

    private void setRoutePath(List<DirectionResponse.Routes> routes){
        int count = 0;
        ArrayList<LatLng> points = null;

        if (routes.size() > 0) {
            DirectionResponse.Routes route = routes.get(0);
            // Traversing through all the routes
            for (DirectionResponse.Routes.Legs leg : route.getLegs()) {
                PolylineOptions lineOptions = new PolylineOptions();
                points = new ArrayList<LatLng>();

                // Fetching all the points in directionR-th route
                for (int j = 0; j < leg.getSteps().size(); j++) {
                    DirectionResponse.Routes.Legs.Steps step = leg.getSteps().get(j);
                    points.addAll(decodePoly(step.getPolyline().getPoints()));
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(20);
                lineOptions.color(Color.RED);
                Polyline polylineFinal = mMap.addPolyline(lineOptions);
                count++;
            }

        }
    }

    //method from the internet
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }


    public void goToPlace(View view) {
        tripViewModel.getAllPlace(id).observe(this, new Observer<List<PlaceClass>>() {
            @Override

            public void onChanged(List<PlaceClass> placeClasses) {
                if(cursor != placeClasses.size() ) {
                    PlaceClass placeSelected = placeClasses.get(cursor);
                    namePlaceSelected.setText(placeSelected.getName().toString());
                    timePlaceSelected.setText(placeSelected.getTime().toString());
                    addressPlaceSelected.setText(placeSelected.getAddress().toString());
                    placeTitle.setText("Place selected :");
                    timeTitle.setText("Time expected there :");
                    addressTitle.setText("Address :");
                    goNext.setText("Next Step");
                    hour.setText("h");
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(placeSelected.getLatitude(),placeSelected.getLongitude()), 14));
                    cursor+=1;

                } else{
                    cursor=0;
                    goNext.setText("Start");
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(city, 11));
                    namePlaceSelected.setText("");
                    timePlaceSelected.setText("");
                    placeTitle.setText("");
                    timeTitle.setText("");
                    addressTitle.setText("");
                    hour.setText("");
                    addressPlaceSelected.setText("");

                }

            }
        });


    }



}