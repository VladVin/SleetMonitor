package vladvin.sleetmonitor.sensor_tracker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import vladvin.sleetmonitor.MapViewer;

/**
 * Created by VladVin on 18.09.2016.
 */
public class LocationTracker implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final long LOCATION_UPDATE_INTERVAL = 5000;  // msec

    private Activity activity;
    private GoogleApiClient locationApiClient;

    private Location lastLocation = null;

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
        return lastLocation;
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest locRequest = LocationRequest
                .create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(LOCATION_UPDATE_INTERVAL);

        if (ActivityCompat.checkSelfPermission(
                activity.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    MapViewer.LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        LocationServices.FusedLocationApi
                .requestLocationUpdates(locationApiClient, locRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
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
}
