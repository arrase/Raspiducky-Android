package io.github.arrase.raspiducky.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SelectedPayloadsDB extends SQLiteOpenHelper {

    public static final String SELECTED_TABLE_NAME = "selected";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "selected_payloads";

    private static final String SELECTED_TABLE_CREATE =
            "CREATE TABLE " + SELECTED_TABLE_NAME + " (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "weight INTEGER DEFAULT 1, " +
                    "path TEXT );";

    public SelectedPayloadsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SELECTED_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

