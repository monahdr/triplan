package com.example.triplanproject.Trip;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.triplanproject.LocalStorage.Trip;
import com.example.triplanproject.R;

import java.util.List;

public class AdapterTrip extends RecyclerView.Adapter<AdapterTrip.ViewHolder> {

    private List<Trip> trips;

    AdapterTrip(List<Trip> trips){
        this.trips= trips;

    }

    @NonNull
    @Override
    public AdapterTrip.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view= inflater.inflate(R.layout.trip_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTrip.ViewHolder holder, int position) {
        String title = trips.get(position).getTitle();
         String date=trips.get(position).getDate();
         String time= trips.get(position).getTime();

        holder.tTilte.setText(title);
        holder.tDate.setText(date);


        //holder.tTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public interface OnListItemClickListener{
        void onListItemClickListener(int clickedItemIndex);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tTilte, tDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tTilte=itemView.findViewById(R.id.placeTitle);
            tDate=itemView.findViewById(R.id.tDate);
           // tTime=itemView.findViewById(R.id.tTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i= new Intent(v.getContext(), TripDetails.class);
                    i.putExtra("ID",trips.get(getAdapterPosition()).getID());
                    v.getContext().startActivity(i);
                }
            });
        }
    }




}
