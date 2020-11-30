package com.example.triplanproject.Trip;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.triplanproject.LocalStorage.Trip;
import com.example.triplanproject.LocalStorage.TripViewModel;
import com.example.triplanproject.R;

public class EditTrip extends AppCompatActivity {

    Toolbar toolbar;
    EditText tripTitle, tripDate, tripTime;
    private TripViewModel tripViewModel;
    Long id;
    Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);

        Intent i = getIntent();
        id= i.getLongExtra("ID",0);

        tripTime=findViewById(R.id.placeTime);
        tripDate=findViewById(R.id.dateTrip1);
        tripTitle=findViewById(R.id.placeTitle);

        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);

        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        tripViewModel.getTrip(id).observe(this, new Observer<Trip>() {
            @Override
            public void onChanged(Trip tripP) {
                getSupportActionBar().setTitle(tripP.getTitle());
                tripTitle.setText(tripP.getTitle().toString());
                tripDate.setText(tripP.getDate().toString());
                tripTime.setText(tripP.getTime().toString());
                trip=tripP;
            }
        });

        tripTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() !=0){
                    getSupportActionBar().setTitle(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_trip_menu,menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                trip.setTitle(tripTitle.getText().toString());
                trip.setDate(tripDate.getText().toString());
                trip.setTime(tripTime.getText().toString());
                tripViewModel.updateTrip(trip);
                Toast.makeText(this, "Trip updated", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getApplicationContext(), TripDetails.class);
                i.putExtra("ID", id);
                startActivity(i);

                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToMain() {
        startActivity(new Intent(this, ListOfTrips.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}