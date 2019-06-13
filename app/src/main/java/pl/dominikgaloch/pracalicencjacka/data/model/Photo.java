package pl.dominikgaloch.pracalicencjacka.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Location.class, parentColumns = "id",
        childColumns = "location_id", onDelete = ForeignKey.CASCADE))
public class Photo {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    @ColumnInfo(name = "photo_location")
    private String photoLocation;
    @ColumnInfo(name = "location_id")
    private int locationID;

    public Photo(String photoLocation, int locationID) {
        this.photoLocation = photoLocation;
        this.locationID = locationID;
    }

    public Photo() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhotoLocation() {
        return photoLocation;
    }

    public void setPhotoLocation(String photoLocation) {
        this.photoLocation = photoLocation;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int placeID) {
        this.locationID = placeID;
    }
}
