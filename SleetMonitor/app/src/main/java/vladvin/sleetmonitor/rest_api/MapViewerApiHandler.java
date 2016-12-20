package vladvin.sleetmonitor.rest_api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MapViewerApiHandler {
    private static final String REST_API_URL = "http://40.85.131.53:4567";

    private Context context;
    private AsyncHttpClient httpClient;
    private MapViewerApiListener apiListener;

    public MapViewerApiHandler(Context context) {
        this.context = context;
        httpClient = new AsyncHttpClient();
    }

    public void getUpdatesInArea(LocationPoint[] locRect) {
        if (locRect == null || locRect.length < 2) {
            return;
        }

        String json = new Gson().toJson(locRect);

        httpClient.get(context,
            REST_API_URL + "/searchLocations/rect=" + json,
            new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    ArrayList<LocationPoint> fallLocations =
                            new Gson().fromJson(response, new TypeToken<ArrayList<LocationPoint>>(){}.getType());
                    if (apiListener != null) {
                        apiListener.onFallLocationsReceived(fallLocations);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
    }

    public void registerApiListerner(MapViewerApiListener apiListener) {
        this.apiListener = apiListener;
    }
}
