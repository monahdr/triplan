package com.example.triplanproject.LocalStorage;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.triplanproject.Retrofit.DirectionResponse;

import java.util.List;

public class TripViewModel extends AndroidViewModel {

    private Repository repository;

    public TripViewModel(@NonNull Application application) {
        super(application);
        repository= Repository.getInstance(application);
    }


    public LiveData<List<Trip>> getAllTrips() {
        return repository.getAllTrips();
    }

    public LiveData<Trip> getTrip(Long id){
        return repository.getTrip(id);
    }

    public LiveData<PlaceClass> getPlace(Long id){ return repository.getPlace(id); }

    public LiveData<Trip> getTripFromPlace(Long id){ return repository.getTripFromPlace(id); }

    public  LiveData<List<PlaceClass>> getAllPlace(Long idTrip){
        return repository.getAllPlaces(idTrip);
    }

    public void insert(final Trip trip){
        repository.insert(trip);
    }
    public void insert(final PlaceClass place){
        repository.insert(place);
    }

  public void deleteAllTrips(){
        repository.deleteAllTrips();
  }

  public  void deleteTrip(Trip trip){
        repository.deleteTrip(trip);
  }

    public  void deletePlace(PlaceClass place){
        repository.deletePlace(place);
    }
    public  void updateTrip(Trip trip){
        repository.updateTrip(trip);
    }

    // Draw routes

    public void updateDirection(String origin, String destination, String key ){
        repository.updateDirection(origin,destination, key);
    }
    public DirectionResponse getDirection(){
        return repository.getDirectionResponse();
    }

    public LiveData<List<DirectionResponse.Routes>> getRoutes(){
        return  repository.getRoutes();
    }

    public void deleteAllPlaces(long id) {
        repository.deletePlaces(id);
    }


}
