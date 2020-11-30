package com.example.triplanproject.Trip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.triplanproject.LocalStorage.Trip;
import com.example.triplanproject.LocalStorage.TripViewModel;
import com.example.triplanproject.Map.MapAddTripLocation;
import com.example.triplanproject.R;
import com.example.triplanproject.menu.MainActivity;

import java.util.List;

public class ListOfTrips extends AppCompatActivity implements AdapterTrip.OnListItemClickListener {

    private TripViewModel tripViewModel;
    ListOfTrips context;
    Toolbar toolbar;
    RecyclerView recyclerView;
    AdapterTrip adapter;
    List<Trip> trips;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_trips);
        context=this;

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setTitle("Your Trips");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.hasFixedSize();
        // to display the list

        tripViewModel = new ViewModelProvider(context).get(TripViewModel.class);
        tripViewModel.getAllTrips().observe(context, new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                adapter = new AdapterTrip(trips);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adapter);
                Log.d("TRIPS","Number of trips:  "+ trips.size());
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_trips_list,menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                //startActivity(new Intent(this, AddTrip2.class));
                startActivity(new Intent(this, MapAddTripLocation.class));
                //Toast.makeText(this, "add btn is clicked !", Toast.LENGTH_SHORT).show();
                break;

            case R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                //Toast.makeText(this, "add btn is clicked !", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClickListener(int clickedItemIndex) {

    }
}