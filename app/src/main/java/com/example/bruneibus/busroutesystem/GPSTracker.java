package com.example.bruneibus.busroutesystem;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;


public class GPSTracker implements LocationListener{
    Context context;

    public GPSTracker(Context context) {
        this.context = context;

    }

    public Location getLocation() {


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(context, "Permission not granted", Toast.LENGTH_LONG).show();
            return null;
        }
        else {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            //check if GPS is enabled
            boolean isGPSenabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 10, this);
            if (isGPSenabled) {
                Location l=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                return l;
            } else {
                Toast.makeText(context, "Please enable GPS", Toast.LENGTH_LONG).show();

            }
        }

        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
