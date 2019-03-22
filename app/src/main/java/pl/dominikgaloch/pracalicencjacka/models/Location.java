package pl.dominikgaloch.pracalicencjacka.models;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class Location {
    private String name;
    private String description;
    private String visitDate;
    private LatLng coordinates;
    private int markerColor;
    private boolean visited;

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

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
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
}
