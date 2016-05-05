package edu.uw.mao1001.geopaint;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 5/4/2016.
 */
public class Drawer implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final String TAG = "Drawer";

    public static GoogleMap mMap;
    public GoogleApiClient mGoogleApiClient;
    public Context context;

    public boolean isDrawing = false;
    public Drawing currentDrawing;
    public int currentColor;


    public Drawer(Context context) {
        this.context = context;
        this.currentDrawing = new Drawing();
        this.currentColor = R.color.white;

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    //---------------------------------//
    //   P U B L I C   M E T H O D S   //
    //---------------------------------//
    public void stopDrawing() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        isDrawing = false;
        currentDrawing.endLine();
    }

    public void startDrawing() {
        try {
            LocationRequest locationRequest = LocationRequest.create()
                    .setInterval(10 * 1000)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
            isDrawing = true;
        } catch (SecurityException e) {
            Log.e(TAG, "Failed to start location updates " + e.getLocalizedMessage());
        }
    }

    public File saveDrawing() {
        stopDrawing();
        GeoJsonConverter converter = new GeoJsonConverter();
        String string = converter.convertToGeoJson(currentDrawing.getAllLines());

        File file = new File(context.getExternalFilesDir(null), "drawing.geojson");
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(string.getBytes()); //write the string to the file
            outputStream.close(); //close the stream
            //Log.d(TAG, string);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException");
        } catch (IOException e) {
            Log.e(TAG, "IOException");

        }

        return file;
    }

    public void shareDrawing(ShareActionProvider shareActionProvider) {
        File savedFile = saveDrawing();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM, savedFile.toURI());

        shareActionProvider.setShareIntent(intent);
    }

    //-----------------------//
    //   O V E R R I D E S   //
    //-----------------------//
    // OnMapReadyCallback

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.v(TAG, "onMapReady");
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
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "ConnectionSuspended");
    }

    //-----------------------//
    //   O V E R R I D E S   //
    //-----------------------//
    // GoogleApiClient.OnConnectionFailedListener

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}


    //-----------------------//
    //   O V E R R I D E S   //
    //-----------------------//
    // LocationServices

    @Override
    public void onLocationChanged(Location location) {
        //Log.i(TAG, "New Location: " + location.getLatitude() + " " + location.getLongitude());
        if (isDrawing) {
            currentDrawing.addPoint(new LatLng(location.getLatitude(), location.getLongitude()), currentColor);

            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
        }
    }

    //-------------------------//
    //   I N N E R C L A S S   //
    //-------------------------//

    private static class Drawing {
        private List<Polyline> lines;
        private Polyline currentLine;

        public Drawing() {
            lines = new ArrayList<>();
            currentLine = null;
        }

        public void addPoint(LatLng point, int color) {
            if (currentLine == null) {
                PolylineOptions options = new PolylineOptions()
                        .add(point)
                        .color(color);
                currentLine = mMap.addPolyline(options);
            } else {
                List<LatLng> allPoints = currentLine.getPoints();
                allPoints.add(point);
                currentLine.setPoints(allPoints);
            }
        }

        public void endLine() {
            if (currentLine != null) {
                lines.add(currentLine);
                currentLine = null;
            }
        }

        public List<Polyline> getAllLines() {
            return lines;
        }
    }
}
