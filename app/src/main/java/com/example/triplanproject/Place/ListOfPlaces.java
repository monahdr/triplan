package com.example.triplanproject.Place;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.triplanproject.LocalStorage.PlaceClass;
import com.example.triplanproject.LocalStorage.Trip;
import com.example.triplanproject.LocalStorage.TripViewModel;
import com.example.triplanproject.Map.AddPlace;
import com.example.triplanproject.R;
import com.example.triplanproject.Trip.TripDetails;
import com.example.triplanproject.Retrofit.MapTrip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

public class ListOfPlaces extends AppCompatActivity  {

    private TripViewModel tripViewModel;
    RecyclerView recyclerView;
    AdapterPlace adapter;
    ListOfPlaces context;
    Toolbar toolbar;
    TextView toolbarTitle;
    long idTrip;
    Trip trip;
    List<PlaceClass> placeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_places);
        context=this;

        Intent i = getIntent();
        idTrip = i.getLongExtra("ID",0);
        Log.d("IDTRIP", "ID >>> "+ idTrip);

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.hasFixedSize();

        toolbar=findViewById(R.id.toolbar);
        toolbarTitle= toolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        toolbarTitle.setText("Steps of the trip");
        toolbarTitle.setTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tripViewModel = new ViewModelProvider(context).get(TripViewModel.class);
        tripViewModel.getAllPlace(idTrip).observe(this, new Observer<List<PlaceClass>>() {
            @Override
            public void onChanged(List<PlaceClass> places) {
                placeList=places;
                adapter= new AdapterPlace(places);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(adapter);
                Log.d("PLACES","Number of places:  "+ places.size());
                ///drag and reorder recyclerView
                ItemTouchHelper itemTouchHelper= new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(recyclerView);
                //final int fromPosition = viewHolder.getAdapterPosition();
                //final int toPosition = target.getAdapterPosition();

            }
        });


        FloatingActionButton fabMap = findViewById(R.id.fabMap);
        fabMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), MapTrip.class);
                i.putExtra("ID",idTrip);
                startActivity(i);

            }
        });
        FloatingActionButton fab = findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddPlace.class);
                i.putExtra("ID",idTrip);
                startActivity(i);

            }
        });



    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP |
            ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END,0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            final int fromPosition = viewHolder.getAdapterPosition();
            final int toPosition = target.getAdapterPosition();
            Collections.swap(placeList, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };


    public void goBack(View view) {
        Intent i = new Intent(getApplicationContext(), TripDetails.class);
        i.putExtra("ID", idTrip);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }


    public void exchangePlaceStep(long IdOrigin, long IdDestination){

    }
}