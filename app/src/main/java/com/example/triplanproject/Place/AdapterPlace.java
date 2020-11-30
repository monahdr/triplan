package com.example.triplanproject.Place;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.triplanproject.LocalStorage.PlaceClass;
import com.example.triplanproject.R;

import java.util.List;


public class AdapterPlace extends RecyclerView.Adapter<AdapterPlace.ViewHolder>{

    private List<PlaceClass> places;

    public AdapterPlace(List<PlaceClass> places) {
        this.places = places;
    }

    @NonNull
    @Override
    public AdapterPlace.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view= inflater.inflate(R.layout.place_item_view, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull AdapterPlace.ViewHolder holder, int position) {
        String name = places.get(position).getName();
        String id = String.valueOf(places.get(position).getIdPlace());

        holder.placeName.setText(name);
        
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView placeName;
        public final ImageView handleView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.placeTitle);
            handleView=(ImageView)itemView.findViewById(R.id.handle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i= new Intent(v.getContext(), PlaceDetail.class);
                    i.putExtra("IDPlace",places.get(getAdapterPosition()).getIdPlace());
                    i.putExtra("IDTrip",places.get(getAdapterPosition()).getIdTrip());
                    v.getContext().startActivity(i);
                }
            });
            
        }


}
}



