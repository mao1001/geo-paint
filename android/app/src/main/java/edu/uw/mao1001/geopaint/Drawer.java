package edu.uw.mao1001.geopaint;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Nick on 5/4/2016.
 */
public class Drawer implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = "Drawer";

    public static GoogleMap mMap;
    public CustomLocationListener locationListener;
    public boolean drawing = false;


    public Drawer() {
        locationListener = new CustomLocationListener();
    }

    //-----------------------//
    //   O V E R R I D E S   //
    //-----------------------//
    // OnMapReadyCallback

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
    }

    //-----------------------//
    //   O V E R R I D E S   //
    //-----------------------//
    // GoogleApiClient.ConnectionCallbacks

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {}

    //-----------------------//
    //   O V E R R I D E S   //
    //-----------------------//
    // GoogleApiClient.OnConnectionFailedListener

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}


    //-------------------------//
    //   I N N E R C L A S S   //
    //-------------------------//

    private static class CustomLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Log.v(TAG, "New Location: " + location.getLatitude() + " " + location.getLongitude());
        }
    }
}
