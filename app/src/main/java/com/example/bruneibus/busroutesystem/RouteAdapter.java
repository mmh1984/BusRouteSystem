package com.example.bruneibus.busroutesystem;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mikail on 18/1/2018.
 */

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.CustomViewHolder> {

Context context;
ArrayList<RouteClass> routeClasses;

    public RouteAdapter(Context context, ArrayList<RouteClass> routeClasses) {
        this.context = context;
        this.routeClasses = routeClasses;
    }

    @Override
    public RouteAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.busroute_row,parent,false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RouteAdapter.CustomViewHolder holder, int position) {
    RouteClass routeClass=routeClasses.get(position);
    holder.place.setText(routeClass.getPlace());
    holder.area.setText(routeClass.getArea());
    holder.type.setText(routeClass.getType());
   double distance;

    GPSTracker gpsTracker=new GPSTracker(context);
        Location l=gpsTracker.getLocation();

        if (l != null) {
            double lat=l.getLatitude();
            double lng=l.getLongitude();
            double endlat=Double.parseDouble(routeClass.getLat());
            double endlng=Double.parseDouble(routeClass.getLng());

            Location to=new Location("Point B");
            to.setLatitude(endlat);
            to.setLongitude(endlng);

            distance=l.distanceTo(to)/1000;
            String km=String.format("%.1f",distance);
            holder.distance.setText(km + " km away");
        }else {
            holder.distance.setText("not set");
        }

    }

    @Override
    public int getItemCount() {
        return routeClasses.size();
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
                    RouteClass routeClass=routeClasses.get(pos);
                    final String lat=routeClass.getLat();
                    final String lng=routeClass.getLng();
                    final String place=routeClass.getPlace();


                    Intent i=new Intent(context,MapsActivity.class);
                    i.putExtra("lat",lat);
                    i.putExtra("lng",lng);
                    i.putExtra("place",place);
                    context.startActivity(i);


                }
            });
        }
    }
}
