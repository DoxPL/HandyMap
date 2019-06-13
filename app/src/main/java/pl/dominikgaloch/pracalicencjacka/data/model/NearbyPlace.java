package pl.dominikgaloch.pracalicencjacka.data.model;

import androidx.room.ColumnInfo;

public class NearbyPlace {

    @ColumnInfo(name = "location_name")
    private String name;
    @ColumnInfo(name = "distance")
    private double distance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
