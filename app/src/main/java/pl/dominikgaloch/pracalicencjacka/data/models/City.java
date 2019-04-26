package pl.dominikgaloch.pracalicencjacka.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class City {
    @PrimaryKey
    private int id;
    @ColumnInfo(name = "cityName")
    private String cityName;
    @ColumnInfo(name = "latitude")
    private double latitude;
    @ColumnInfo(name = "longitude")
    private double longitude;
}
