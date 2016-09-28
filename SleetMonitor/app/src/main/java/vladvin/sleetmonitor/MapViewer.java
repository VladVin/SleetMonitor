package vladvin.sleetmonitor;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import vladvin.sleetmonitor.sensor_tracker.LocationTracker;
import vladvin.sleetmonitor.sensor_tracker.SensorTracker;

public class MapViewer extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private LocationTracker locationTracker;
    private SensorTracker sensorTracker;

    // Permission request code list
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_viewer);


        locationTracker = new LocationTracker(this);
        locationTracker.connectLocationServices();
        sensorTracker = new SensorTracker(this, locationTracker, null);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (locationTracker != null) {
                        locationTracker.onPermissionGranted();
                    }
                }
                break;
        }
    }
}
