package storage;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Geometry;

import java.util.ArrayList;
import java.util.List;

public class GeoRTree {

    private RTree<Id,Geometry> tree = RTree.maxChildren(4).create();

    public void insert (Id loc, Location coordinates){
        tree = tree.add(loc, Geometries.point(coordinates.getLat(), coordinates.getLon()));
    }

    public void remove (Id loc, Location coordinates){
        tree = tree.delete(loc, Geometries.point(coordinates.getLat(), coordinates.getLon()));
    }

    public List<Location> search (Location x1y1, Location x2y2){
        List<Location> searchRes = new ArrayList<>();
        tree.search(Geometries.rectangle(x1y1.getLat(), x1y1.getLon(),
                x2y2.getLat(), x2y2.getLon()))
                .toBlocking().forEach((Entry<Id, Geometry> entry) ->
                        searchRes.add(new Location(entry.geometry().mbr().x1(),
                                                    entry.geometry().mbr().y1())));
        return searchRes;
    }

}