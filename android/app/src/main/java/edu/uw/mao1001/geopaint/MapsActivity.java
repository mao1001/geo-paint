package edu.uw.mao1001.geopaint;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = "MapsActivity";

    private static Drawer drawer;

    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;

    //-----------------------//
    //   O V E R R I D E S   //
    //-----------------------//
    // AppCompatActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        drawer = new Drawer(this);

        mapFragment.getMapAsync(drawer);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getTitle().toString()) {
            case "Pick color":

                break;
            case "Turn on brush":
                drawer.startDrawing();
                item.setTitle(getString(R.string.action_brush_on_title));
                item.setIcon(R.drawable.ic_smoke_free_white_24dp);
                break;
            case "Turn off brush":
                drawer.stopDrawing();
                item.setTitle(getString(R.string.action_brush_off_title));
                item.setIcon(R.drawable.ic_smoking_rooms_white_24dp);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        drawer.mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        drawer.mGoogleApiClient.disconnect();
        super.onStop();
    }

    //-----------------------//
    //   O V E R R I D E S   //
    //-----------------------//
    // ActivityCompat.OnRequestPermissionsResultCallback

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

                // Show an explanation to the user *asynchronously* -- don't block
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
}
