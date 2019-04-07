package pl.dominikgaloch.pracalicencjacka.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class Photo {
    @PrimaryKey(autoGenerate = true)
    private int id;
    //@ForeignKey()
    private int placeId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }
}
