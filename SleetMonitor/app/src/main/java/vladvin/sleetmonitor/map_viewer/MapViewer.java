package vladvin.sleetmonitor.map_viewer;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import vladvin.sleetmonitor.R;
import vladvin.sleetmonitor.data_proc.DataSender;
import vladvin.sleetmonitor.sensor_tracker.LocationTracker;
import vladvin.sleetmonitor.sensor_tracker.SensorTracker;

public class MapViewer
        extends FragmentActivity
        implements OnMapReadyCallback,
            ActivityCompat.OnRequestPermissionsResultCallback{

    private LocationTracker locationTracker;
    private SensorTracker sensorTracker;

    // Permission request code list
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 0;

    // injected code start
    private HeatmapTileProvider mHeatmapTileProvider;
    private TileOverlay mHeatmapTileOverlay;
    // injected code end
    private GoogleMap mMap;
    private AsyncHttpClient httpClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initializeSensorTracker();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        httpClient = new AsyncHttpClient();
    }

    private void initializeSensorTracker() {
        locationTracker = new LocationTracker(this);
        locationTracker.connectLocationServices();
        DataSender dataSender = new DataSender(getApplicationContext());
        sensorTracker = new SensorTracker(this, locationTracker, dataSender);
    }

    private ArrayList<LatLng> readItems(int resource) throws JSONException {
        InputStream inputStream = getResources().openRawResource(resource);
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        return parse(json);
    }

    private ArrayList<LatLng> parse(String json) throws JSONException {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lon");
            list.add(new LatLng(lat, lng));
        }
        return list;
    }

    private String getCurrentFrame(){
        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        LocationPoint[] locRect = new LocationPoint[2];
        locRect[0] = new LocationPoint(bounds.southwest.latitude, bounds.southwest.longitude);
        locRect[1] = new LocationPoint(bounds.northeast.latitude, bounds.northeast.longitude);

        Gson gson = new Gson();
        return gson.toJson(locRect);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                Log.d("1",getCurrentFrame());
                httpClient.get(getApplicationContext(),
                        "http://40.85.131.53:4567/searchLocations/rect=" +
                        getCurrentFrame(),
                        new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String json = new String(responseBody);
                        try {
                            mHeatmapTileProvider.setData(parse(json));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mHeatmapTileOverlay.clearTileCache();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
            }
        });

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(55.75271885, 48.74771118), 15));
        //mMap.moveCamera(CameraUpdateFactory.zoomTo());

        //mMap
        // Add a marker in fall1 and move the camera

        ArrayList<LatLng> list = new ArrayList<LatLng>();

        try {
            //list.add( new DataSet(readItems(R.raw.locations)));
            list=readItems(R.raw.locations);
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
        }


        mHeatmapTileProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .build();

        mHeatmapTileOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mHeatmapTileProvider));
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

    private void sendToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG)
                .show();
    }
}
