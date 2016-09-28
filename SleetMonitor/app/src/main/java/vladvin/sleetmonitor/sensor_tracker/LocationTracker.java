package vladvin.sleetmonitor.sensor_tracker;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import vladvin.sleetmonitor.MapViewer;

/**
 * Created by VladVin on 18.09.2016.
 */
public class LocationTracker implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Activity activity;

    private GoogleApiClient locationApiClient;
    private boolean isWaitPermissionApproval = false;

    public LocationTracker(Activity activity) {
        this.activity = activity;
        this.locationApiClient =
                new GoogleApiClient.Builder(activity.getApplicationContext())
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
    }

    public Location getLastKnownLocation() {
        if (locationApiClient.isConnected() && !isWaitPermissionApproval) {
            if (ActivityCompat.checkSelfPermission(
                    activity.getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                        MapViewer.LOCATION_PERMISSION_REQUEST_CODE);

                isWaitPermissionApproval = true;
                return null;
            }
            return LocationServices.FusedLocationApi.getLastLocation(locationApiClient);
        }

        return null;
    }

    public void onPermissionGranted() {
        isWaitPermissionApproval = false;
    }

    public void connectLocationServices() {
        locationApiClient.connect();
    }

    public void disconnectLocationServices() {
        locationApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(Bundle bundle) {

    }
}
