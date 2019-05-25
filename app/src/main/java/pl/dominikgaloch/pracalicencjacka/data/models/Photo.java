package pl.dominikgaloch.pracalicencjacka.data.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Location.class, parentColumns = "id",
        childColumns = "placeID", onDelete = ForeignKey.CASCADE))
public class Photo {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    @ColumnInfo(name = "photoLocation")
    private String photoLocation;
    @ColumnInfo(name = "placeID")
    private int placeID;

    public Photo(String photoLocation, int placeID) {
        this.photoLocation = photoLocation;
        this.placeID = placeID;
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

    public int getPlaceID() {
        return placeID;
    }

    public void setPlaceID(int placeID) {
        this.placeID = placeID;
    }
}
