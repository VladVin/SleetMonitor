package vladvin.sleetmonitor.rest_api;

import java.util.ArrayList;

public interface MapViewerApiListener {
    void onFallLocationsReceived(ArrayList<LocationPoint> fallLocations);
}
