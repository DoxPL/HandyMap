package pl.dominikgaloch.pracalicencjacka.data.models;

import org.osmdroid.util.GeoPoint;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "id")})
public class Location {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    @ColumnInfo(name = "location_name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "visit_date")
    private String visitDate;
    @ColumnInfo(name = "latitude")
    private double latitude;
    @ColumnInfo(name = "longitude")
    private double longitude;
    @ColumnInfo(name = "marker_color")
    private int markerColor;
    @ColumnInfo(name = "visit_state")
    private boolean visited;


    public Location(String name, String description, double latitude, double longitude, int markerColor) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.markerColor = markerColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getMarkerColor() {
        return markerColor;
    }

    public void setMarkerColor(int markerColor) {
        this.markerColor = markerColor;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public GeoPoint getGeoPoint()
    {
        return new GeoPoint(this.latitude, this.longitude);
    }
}