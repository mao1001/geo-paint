package edu.uw.mao1001.geopaint;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = "MapsActivity";

    private static Drawer drawer;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;

    //-----------------------//
    //   O V E R R I D E S   //
    //-----------------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        drawer = new Drawer();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    /**
     * Override for GoogleApiClient.ConnectionCallbacks
     * @param bundle
     */

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");
        //Checks if the permission is already in manifest. Will also request it in 6.0+
        if (requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            //This should only run for under 6.0
//            moveToCurrentLocation();

            startLocationUpdates();
        }

    }

    protected void startLocationUpdates() {
        try {
            LocationRequest locationRequest = LocationRequest.create()
                    .setInterval(10 * 1000)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, drawer.getLocationListener());
        } catch (SecurityException e) {
            Log.e(TAG, "Failed to start location updates " + e.getLocalizedMessage());
        }
    }

    /**
     * Override for GoogleApiClient.ConnectionCallbacks
     * @param connectionResult
     */
    @Override
    public void onConnectionSuspended(int i) {}

    /**
     * Override for GoogleApiClient.OnConnectionFailedListener
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    /**
     * Override for ActivityCompat.OnRequestPermissionsResultCallback
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionResult");

        switch (requestCode) {
            case PERMISSION_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    moveToCurrentLocation();
                } else {

                }
            }
        }
    }

    //-----------------------------------//
    //   P R I V A T E   M E T H O D S   //
    //-----------------------------------//

    private boolean requestPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permission)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{permission},
                        PERMISSION_REQUEST_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

            return false;
        }

        return true;
    }

//    private void moveToCurrentLocation() {
//        Log.d(TAG, "moveToCurrentLocation");
//        try {
//
//            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//            if (mLastLocation != null) {
//
//                Log.d(TAG, "movingToMarker");
//                LatLng userLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//                mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
//            }
//        } catch (SecurityException e) {
//            Log.e(TAG, "Error permission was not granted");
//        }
//    }
}
