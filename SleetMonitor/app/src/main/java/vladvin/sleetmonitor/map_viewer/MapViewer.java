package vladvin.sleetmonitor.map_viewer;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import java.util.ArrayList;

import vladvin.sleetmonitor.R;
import vladvin.sleetmonitor.data_proc.DataSender;
import vladvin.sleetmonitor.rest_api.LocationPoint;
import vladvin.sleetmonitor.rest_api.MapViewerApiHandler;
import vladvin.sleetmonitor.rest_api.MapViewerApiListener;
import vladvin.sleetmonitor.sensor_tracker.LocationTracker;
import vladvin.sleetmonitor.sensor_tracker.SensorTracker;

public class MapViewer
        extends FragmentActivity
        implements OnMapReadyCallback,
            ActivityCompat.OnRequestPermissionsResultCallback,
            MapViewerApiListener {

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 0;
    private static final LatLng START_ANCHOR = new LatLng(55.75171885, 48.74271118);
    private static final int START_ZOOM_LEVEL = 15;

    private HeatmapTileProvider heatmapTileProvider;
    private TileOverlay heatmapTileOverlay;
    private GoogleMap googleMap;
    private MapViewerApiHandler mvApiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initializeSensorTracker();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mvApiHandler = new MapViewerApiHandler(getApplicationContext());
        mvApiHandler.registerApiListerner(this);
    }

    private void initializeSensorTracker() {
        LocationTracker locationTracker = new LocationTracker(this);
        locationTracker.connectLocationServices();
        DataSender dataSender = new DataSender(getApplicationContext());
        SensorTracker sensorTracker = new SensorTracker(this, locationTracker, dataSender);
        sensorTracker.startTracking();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                mvApiHandler.getUpdatesInArea(getCurrentFrame());
            }
        });

        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(START_ANCHOR, START_ZOOM_LEVEL));
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    sendToast(getString(R.string.your_are_selfish));
                }
                break;
        }
    }

    @Override
    public void onFallLocationsReceived(ArrayList<LocationPoint> fallLocations) {
        try {
            if (heatmapTileProvider == null) {
                initializeHeatMapTileProvider(parseLocationPoints(fallLocations));
            } else {
                heatmapTileProvider.setData(parseLocationPoints(fallLocations));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        heatmapTileOverlay.clearTileCache();
    }

    private LocationPoint[] getCurrentFrame(){
        LatLngBounds bounds = googleMap.getProjection().getVisibleRegion().latLngBounds;
        LocationPoint[] locRect = new LocationPoint[2];
        locRect[0] = new LocationPoint(bounds.southwest.latitude, bounds.southwest.longitude);
        locRect[1] = new LocationPoint(bounds.northeast.latitude, bounds.northeast.longitude);
        return locRect;
    }

    private ArrayList<LatLng> parseLocationPoints(ArrayList<LocationPoint> locPoints) {
        ArrayList<LatLng> parsedList = new ArrayList<>();
        for (LocationPoint locPoint : locPoints) {
            LatLng latLng = new LatLng(locPoint.getLat(), locPoint.getLon());
            parsedList.add(latLng);
        }
        return parsedList;
    }

    private void initializeHeatMapTileProvider(ArrayList<LatLng> points) {
        heatmapTileProvider = new HeatmapTileProvider.Builder()
                .data(points)
                .build();
        heatmapTileOverlay = this.googleMap
                .addTileOverlay(new TileOverlayOptions().tileProvider(heatmapTileProvider));
    }

    private void sendToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG)
                .show();
    }
}
