package pl.dominikgaloch.pracalicencjacka;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseUtilities extends SQLiteOpenHelper {
    //Todo remove
    private static final String CREATE_TABLE_PLACES = "CREATE TABLE places (" +
            "ID integer primary key autoincrement," +
            "PLACE_NAME text," +
            "DESCRIPTION text," +
            "LATITUDE real," +
            "LONGITUDE real," +
            "MARKER_COLOR integer," +
            "IS_VISITED integer," +
            "VISIT_DATE text )";

    private static final String CREATE_TABLE_PHOTOS = "CREATE TABLE photos (" +
            "ID integer primary key autoincrement," +
            "IMAGE blob," +
            "PLACE_ID integer," +
            "foreign key (PLACE_ID) references places(ID)";

    public DatabaseUtilities(Context context)
    {
        super(context, context.getResources().getString(R.string.database_name), null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PLACES);
        db.execSQL(CREATE_TABLE_PHOTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //super.onUpgrade(db, oldVersion, newVersion);
    }
}
