package pl.dominikgaloch.pracalicencjacka.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

public class LocationIndexName {
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "location_name")
    private String name;

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

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
