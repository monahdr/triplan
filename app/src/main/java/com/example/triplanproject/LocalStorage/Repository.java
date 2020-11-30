package com.example.triplanproject.LocalStorage;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.triplanproject.Retrofit.DirectionApi;
import com.example.triplanproject.Retrofit.DirectionResponse;
import com.example.triplanproject.Retrofit.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    private TripDao tripDao;
    private PlaceDao placeDao;

    private static Repository instance;
    private LiveData<List<Trip>> allTrips;
    private DirectionResponse directionResponse;
    private MutableLiveData<List<DirectionResponse.Routes>> routes;


    private Repository(Application application) {
        TripDataBase database = TripDataBase.getInstance(application);
        tripDao = database.tripDao();
        placeDao = database.placeDao();
        allTrips = tripDao.getAllTrips();
        routes= new MutableLiveData<>();
    }

    public static synchronized Repository getInstance(Application application) {
        if (instance == null)
            instance = new Repository(application);
        return instance;
    }

    public LiveData<List<Trip>> getAllTrips() {
        return allTrips;
    }

    public LiveData<Trip> getTrip(long id){
        return tripDao.getTrip(id);

    }

    public void insert(Trip trip) {
        new InsertTripAsync(tripDao).execute(trip);
    }

    public void insert(PlaceClass place) {
        new InsertPlaceAsync(placeDao).execute(place);
    }

    public void deleteTrip(Trip trip){
        new DeleteTripAsync(tripDao).execute(trip);
        //tripDao.delete(trip);
    }
    public void deletePlaces(long idTrip) {
       new DeleteAllPlacesAsync(placeDao,idTrip).execute();

    }
    public void deletePlace(PlaceClass place) {
        new DeletePlaceAsync(placeDao).execute(place);
    }

    public void deleteAllTrips() {
        new DeleteAllTripsAsync(tripDao).execute();
    }

    public void updateTrip(Trip trip) {
        new UpdateTripAsync(tripDao).execute(trip);
    }

    public LiveData<List<PlaceClass>> getAllPlaces(Long idTrip){
        return placeDao.getAllPlaces(idTrip);
    }

    public LiveData<PlaceClass> getPlace(Long id) {return placeDao.getPlace(id);
    }

    public LiveData<Trip> getTripFromPlace(Long id) {
        PlaceClass place=placeDao.getPlace(id).getValue();
        return tripDao.getTrip(place.getIdTrip());
    }


    private static class InsertTripAsync extends AsyncTask<Trip, Void, Void> {
        private TripDao tripDao;

        private InsertTripAsync(TripDao tripDao) {
            this.tripDao = tripDao;
        }

        @Override
        protected Void doInBackground(Trip... trips) {
            tripDao.insert(trips[0]);
            return null;
        }
    }

    private static class InsertPlaceAsync extends AsyncTask<PlaceClass, Void, Void> {
        private PlaceDao placeDao;

        private InsertPlaceAsync(PlaceDao placeDao) {
            this.placeDao= placeDao;
        }

        @Override
        protected Void doInBackground(PlaceClass... places) {
            placeDao.insert(places[0]);
            return null;
        }
    }

    private static class DeleteAllTripsAsync extends AsyncTask<Void,Void,Void> {
        private TripDao tripDao;

        private DeleteAllTripsAsync(TripDao tripDao) {
            this.tripDao = tripDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            tripDao.deleteAllTrips();
            return null;
        }
    }

    private static class DeleteTripAsync extends AsyncTask<Trip,Void,Void> {
        private TripDao tripDao;

        private DeleteTripAsync(TripDao tripDao) {
            this.tripDao = tripDao;
        }

        @Override
        protected Void doInBackground(Trip... trips) {
            tripDao.delete(trips[0]);
            return null;
        }
    }

    private static class UpdateTripAsync extends AsyncTask<Trip,Void,Void> {
        private TripDao tripDao;

        public UpdateTripAsync(TripDao tripDao) {
            this.tripDao = tripDao;
        }

        @Override
        protected Void doInBackground(Trip... trips) {
            tripDao.update(trips[0]);
            return null;
        }
    }

    private static class DeletePlaceAsync extends AsyncTask<PlaceClass,Void,Void> {
        private PlaceDao placeDao;

        public DeletePlaceAsync(PlaceDao placeDao) { this.placeDao = placeDao;}

        @Override
        protected Void doInBackground(PlaceClass... places) {
            placeDao.delete(places[0]);
            return null;
        }
    }

    private static class DeleteAllPlacesAsync extends AsyncTask<Void,Void,Void> {
        private PlaceDao placeDao;
        private long idTrip;

        public DeleteAllPlacesAsync(PlaceDao placeDao, long idTrip) { this.placeDao = placeDao;
        this.idTrip =idTrip;}

        @Override
        protected Void doInBackground(Void... voids) {
            placeDao.delete(idTrip);
            return null;
        }
    }
///DIRECTION TO DRAW ROUTE IN THE TRIP MAP

    public void updateDirection(String origin, String destination, String key){
        DirectionApi directionApi = ServiceGenerator.getDirectionApiApi();
        Call<DirectionResponse> call = directionApi.getJson(origin,destination, key);
        call.enqueue(new Callback<DirectionResponse>() {
            @Override
            public void onResponse(Call<DirectionResponse> call, Response<DirectionResponse> response) {
                Log.i("onResponse",response.code()+"");
                if (response.code() == 200){
                    routes.setValue(response.body().getRoutes());
                    Log.i("Retrofit", response.body().getRoutes().size()+"");

                }
            }

            @Override
            public void onFailure(Call<DirectionResponse> call, Throwable t) {
                Log.i("Retrofit", t.getMessage());
            }
        });

    }

    public LiveData<List<DirectionResponse.Routes>> getRoutes(){
        return routes;
    }



    public DirectionResponse getDirectionResponse() {
        return directionResponse;
    }
}
