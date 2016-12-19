package location_service;

import storage.DBManager;
import storage.FallInfo;
import storage.GeoRTree;
import storage.Location;

import java.util.List;

/**
 * Created by VladVin on 02.12.2016.
 */
public class LocationService extends Thread {
    private final GeoRTree geoRTree;
    private final DBManager dbm;
    private long tsOffset = 0;

    public LocationService(GeoRTree geoRTree, DBManager dbm) {
        this.geoRTree = geoRTree;
        this.dbm = dbm;
    }

    public List<Location> search(Location x1y1, Location x2y2) {
        synchronized (geoRTree) {
            return geoRTree.search(x1y1, x2y2);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            List<FallInfo> falls = dbm.getFalls(tsOffset);
            if (falls == null || falls.size() == 0) {
                continue;
            }

            synchronized (geoRTree) {
                Location curLoc = falls.get(0).getLoc();
                geoRTree.insert(falls.get(0).getId(), falls.get(0).getLoc());
                for (FallInfo fall : falls) {
                    if (!fall.getLoc().equals(curLoc)) {
                        curLoc = fall.getLoc();
                        geoRTree.insert(fall.getId(), fall.getLoc());
                    }
                }
            }

            tsOffset = falls.get(0).getId().getTime();
        }
    }
}
