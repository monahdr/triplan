package com.example.triplanproject.Trip;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.triplanproject.LocalStorage.Trip;
import com.example.triplanproject.LocalStorage.TripViewModel;
import com.example.triplanproject.Place.ListOfPlaces;
import com.example.triplanproject.R;
import com.example.triplanproject.Retrofit.MapTrip;
import com.example.triplanproject.menu.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TripDetails extends AppCompatActivity {
    private TripViewModel tripViewModel;
    TextView tTime, tDate, tCity;
    long id;
    Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        tTime=findViewById(R.id.placeTime);
        tDate=findViewById(R.id.tripDate1);
        tCity =findViewById(R.id.tripCity);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ListOfTrips.class));

                tripViewModel.deleteAllPlaces(id);
                tripViewModel.deleteTrip(trip);
                Toast.makeText(TripDetails.this, trip.getTitle()+" is deleted.", Toast.LENGTH_SHORT).show();
            }
        });

        // Extraction of information from the db
        Intent i = getIntent();
        id= i.getLongExtra("ID",0);
        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);

        tripViewModel.getTrip(id).observe(this, new Observer<Trip>() {
            @Override
            public void onChanged(Trip tripP) {
                trip=tripP;
                //getSupportActionBar().setTitle(trip.getTitle());
                if(tripP != null)
                    tTime.setText(tripP.getTime().toString());
                    tDate.setText(tripP.getDate().toString());
                    tCity.setText(tripP.getLocation().toString());
            }

        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_trip_details,menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editTrip:
                Intent i = new Intent(this, EditTrip.class);
                i.putExtra("ID", id);
                startActivity(i);
                break;
            case R.id.home:
                Intent in =new Intent(this, MainActivity.class);
                startActivity(in);
                finish();

        }
        return super.onOptionsItemSelected(item);
    }

    public void accessToPlaces(View view) {
        Intent i = new Intent(this, ListOfPlaces.class);
        i.putExtra("ID", id);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    public void accessToTripMap(View view) {
        Intent i = new Intent(this, MapTrip.class);
        i.putExtra("ID", id);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

    }
}