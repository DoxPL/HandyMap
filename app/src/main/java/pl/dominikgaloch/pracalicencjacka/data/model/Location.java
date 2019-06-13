package pl.dominikgaloch.pracalicencjacka.data.model;

import org.osmdroid.util.GeoPoint;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "id")}, foreignKeys = @ForeignKey(entity = Category.class, parentColumns = "id",
        childColumns = "category_id", onDelete = ForeignKey.CASCADE))
public class Location {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    @ColumnInfo(name = "location_name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "latitude")
    private double latitude;
    @ColumnInfo(name = "longitude")
    private double longitude;
    @ColumnInfo(name = "marker_color")
    private int markerColor;
    @ColumnInfo(name = "category_id")
    private int categoryID;

    public Location(String name, String description, double latitude, double longitude, int markerColor, int categoryID) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.markerColor = markerColor;
        this.categoryID = categoryID;
    }

    public Location(String name, String description, GeoPoint point, int markerColor, int categoryID) {
        this.name = name;
        this.description = description;
        this.latitude = point.getLatitude();
        this.longitude = point.getLongitude();
        this.markerColor = markerColor;
        this.categoryID = categoryID;
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

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public GeoPoint getGeoPoint() {
        return new GeoPoint(this.latitude, this.longitude);
    }
}