package com.example.bruneibus.busroutesystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;



public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.CustomViewHolder> {
    Context context;
    ArrayList<Places> places;


    public PlacesAdapter(Context context, ArrayList<Places> places) {
        this.context = context;
        this.places = places;

    }

    @Override
    public PlacesAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.busroute_row,parent,false);

        return new CustomViewHolder(view);

    }

    @Override
    public void onBindViewHolder(PlacesAdapter.CustomViewHolder holder, int position) {

        Places pl=places.get(position);
        GPSTracker gpsTracker=new GPSTracker(context);
        Location l=gpsTracker.getLocation();

        if (l != null) {
            double lat=l.getLatitude();
            double lng=l.getLongitude();
            double endlat=Double.parseDouble(pl.getLat());
            double endlng=Double.parseDouble(pl.getLng());

            Location to=new Location("Point B");
            to.setLatitude(endlat);
            to.setLongitude(endlng);
            double distance;
            distance=l.distanceTo(to)/1000;


                String km = String.format("%.1f", distance);


                holder.distance.setText(km + " km away");


                holder.place.setText(pl.getLandmark());
                holder.area.setText(pl.getArea());
                holder.type.setText(pl.getType());
            }


    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        TextView place;
        TextView area;
        TextView type;
        TextView distance;
        public CustomViewHolder(View view) {
            super(view);
            place=view.findViewById(R.id.tvlandmark);
            area=view.findViewById(R.id.tvcommentdate);
            type=view.findViewById(R.id.tvtype);
            distance=view.findViewById(R.id.tvviewedby);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos=getAdapterPosition();
                    Places p=places.get(pos);
                    final String lat=p.getLat();
                    final String lng=p.getLng();
                    final String place=p.getLandmark();
                    final String placeid=p.getId();

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("Choose Operation")
                            .setPositiveButton("View Buses", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // FIRE ZE MISSILES!
                                    Intent i=new Intent(context,BusList.class);
                                    i.putExtra("placeid",placeid);
                                    i.putExtra("name",place);
                                    context.startActivity(i);
                                }
                            })
                            .setNegativeButton("Open Map", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                    Intent i=new Intent(context,MapsActivity.class);
                                    i.putExtra("lat",lat);
                                    i.putExtra("lng",lng);
                                    i.putExtra("place",place);
                                    context.startActivity(i);
                                }
                            });
                    // Create the AlertDialog object and return it
                   builder.create();
                   builder.show();

/*

                    */


                }
            });

        }
    }
}
