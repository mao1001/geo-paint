package edu.uw.mao1001.geopaint;

import com.google.android.gms.location.LocationListener;

import android.location.Location;
import android.util.Log;

/**
 * Created by Nick on 5/4/2016.
 */
public class Drawer {

    public static final String TAG = "Drawer";

    private static CustomLocationListener locationListener;

    private static boolean drawing = false;


    public Drawer() {
        locationListener = new CustomLocationListener();
    }


    public LocationListener getLocationListener() {
        return locationListener;
    }

    public boolean getDrawingStatus() {
        return drawing;
    }

    public void setDrawingStatus(boolean newStatus) {
        drawing = newStatus;
    }

    private static class CustomLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Log.v(TAG, "New Location: " + location.getLatitude() + " " + location.getLongitude());
        }
    }
}
