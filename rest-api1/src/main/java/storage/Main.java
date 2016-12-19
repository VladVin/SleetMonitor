package storage;

import java.util.List;

public class Main {
    public static void main (String args[]){

    }

    public static List<Location> search(Location x1y1, Location x2y2) {
        GeoRTree rTree = new GeoRTree();
        Location p1 = new Location(1.5, 2);
        Location p2 = new Location(1.5, 6.5);
        Location p3 = new Location(6.5, 5.5);
        Location p4 = new Location(7.0, 8.0);
        Location p5 = new Location(8.0, 7.0);
        Location p6 = new Location(4.0, 5.0);
        Location p7 = new Location(4.0, 3.0);
        Location p8 = new Location(3.2, 4.0);
        Location p9 = new Location(7.5, 1.4);
        rTree.insert(new Id("1", 12), p1);
        rTree.insert(new Id("2", 15), p2);
        rTree.insert(new Id("3", 22), p3);
        rTree.insert(new Id("4", 37), p4);
        rTree.insert(new Id("5", 41), p5);
        rTree.insert(new Id("6", 46), p6);
        rTree.insert(new Id("7", 49), p7);
        rTree.insert(new Id("8", 51), p8);
        rTree.insert(new Id("9", 53), p9);

        return rTree.search(x1y1, x2y2);
    }
}
