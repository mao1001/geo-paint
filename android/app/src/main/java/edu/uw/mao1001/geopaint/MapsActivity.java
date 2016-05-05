package edu.uw.mao1001.geopaint;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.android.gms.maps.SupportMapFragment;
import com.thebluealliance.spectrum.SpectrumDialog;

public class MapsActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = "MapsActivity";

    private static Drawer drawer;

    private static SupportMapFragment mapFragment;

    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;

    //-----------------------//
    //   O V E R R I D E S   //
    //-----------------------//
    // AppCompatActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            setup();
        }
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
                showColorPickerDialog();
                break;
            case "Turn on brush":
                //Start drawing and toggle icon and title
                drawer.startDrawing();
                item.setTitle(getString(R.string.action_brush_on_title));
                item.setIcon(R.drawable.ic_smoke_free_white_24dp);
                break;
            case "Turn off brush":
                //Start drawing and toggle icon and title
                drawer.stopDrawing();
                item.setTitle(getString(R.string.action_brush_off_title));
                item.setIcon(R.drawable.ic_smoking_rooms_white_24dp);
                break;
            case "Save drawing":
                drawer.saveDrawing();
                break;
            case "Share drawing":
                ShareActionProvider mySharedProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(item);
                drawer.shareDrawing(mySharedProvider);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        //Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        //Log.d(TAG, "onStop");
        drawer.mGoogleApiClient.disconnect();
        super.onStop();
    }

    //-----------------------//
    //   O V E R R I D E S   //
    //-----------------------//
    // ActivityCompat.OnRequestPermissionsResultCallback

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Log.d(TAG, "onRequestPermissionResult");

        switch (requestCode) {
            case PERMISSION_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Log.d(TAG, "About to run setup");
                    setup();
                } else {
                    finish();
                }
            }
        }
    }

    //-----------------------------------//
    //   P R I V A T E   M E T H O D S   //
    //-----------------------------------//

    /**
     * Requests permission from the user. Only runs for Marshmellow 6.0+
     * @param permission Permission to be asked for.
     * @return True if permission is already granted in the manifest otherwise false and asks for permission
     */
    private boolean requestPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permission)) {
                // Should we show an explanation?
            } else {
                Log.d(TAG, "Requesting Permission");
                ActivityCompat.requestPermissions(this,
                        new String[]{permission},
                        PERMISSION_REQUEST_FINE_LOCATION);
            }

            return false;
        }

        return true;
    }

    /**
     * Private helper that initiates alot of the helper calls that
     * need to wait until the the map or other callbacks are finished first.
     */
    private void setup() {
        //Log.d(TAG, "Running setup");
        drawer = new Drawer(this);
        mapFragment.setRetainInstance(true);
        mapFragment.getMapAsync(drawer);

        drawer.mGoogleApiClient.connect();
    }

    /**
     * Shows a picker dialog that allows the user to pick a color to draw with
     */
    private void showColorPickerDialog() {
        new SpectrumDialog.Builder(this)
            .setColors(R.array.colors)
            .setSelectedColor(R.color.white)
            .setDismissOnColorSelected(false)
            .setOutlineWidth(2)
            .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                @Override
                public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                    if (positiveResult) {
                        drawer.currentColor = color;
                    }

                    //Log.i(TAG, "Color picker: " +  Integer.toHexString(color));
                }
            }).build().show(getSupportFragmentManager(), "color_picker_dialog");

    }
}
