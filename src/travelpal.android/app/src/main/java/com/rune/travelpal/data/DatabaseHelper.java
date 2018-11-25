package com.rune.travelpal.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.rune.travelpal.R;
import rune.logging.Logger;
import com.rune.travelpal.data.dto.Travel;

// Database helper
public class DatabaseHelper extends SQLiteOpenHelper {

    // Fields

    private Context mContext;

    private static Logger log = new Logger(DatabaseHelper.class);

    private static class TravelTable {
        public static final String NAME = "Travel";
        public static final String[] COLUMNS = new String[] {"_id", "fromLocation", "toLocation", "cost"};
        public static final class COLUMN_NAMES {
            public static final String ID = COLUMNS[0];
            public static final String FROM = COLUMNS[1];
            public static final String TO = COLUMNS[2];
            public static final String COST = COLUMNS[3];
        }
    }

    // Constructor

    public DatabaseHelper(Context context) {
        super(
                context,
                context.getResources().getString(R.string.database_name),
                null,
                context.getResources().getInteger(R.integer.database_version)
        );

        mContext = context;
    }

    // Overrides

    @Override
    public void onCreate(SQLiteDatabase db) {
        log.d("onCreate");

        recreateDb(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        log.d("onDowngrade");

        super.onDowngrade(db, oldVersion, newVersion);

        recreateDb(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        log.d("onUpgrade");

        recreateDb(db);
    }

    // Methods

    private void recreateDb(SQLiteDatabase db) {
        String deleteSql = readRawResourceSql(R.raw.delete);
        log.d("Removing db using sql: " + deleteSql);
        db.execSQL(deleteSql);

        String createSql = readRawResourceSql(R.raw.create);
        log.d("Creating db using sql: " + createSql);
        db.execSQL(createSql);
    }

    private String readRawResourceSql(int resId) {
        return readRawResource(resId).replace("\n", "");
    }

    private String readRawResource(int resId) {
        try {
            InputStream stream = mContext.getResources().openRawResource(resId);
            byte[] b = new byte[stream.available()];
            stream.read(b);
            return new String(b);
        } catch (Exception e) {
            throw new RuntimeException("Could not read requested raw resource.", e);
        }
    }

    public List<Travel> getTravels() {
        List<Travel> travels = new ArrayList<>();

        final Cursor cursor = getReadableDatabase().query(
            TravelTable.NAME,
            TravelTable.COLUMNS, null, null, null, null, null
            );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Travel travel = new Travel() {{
                from = cursor.getString(cursor.getColumnIndex(TravelTable.COLUMN_NAMES.FROM));
                to   = cursor.getString(cursor.getColumnIndex(TravelTable.COLUMN_NAMES.TO));
                cost = cursor.getDouble(cursor.getColumnIndex(TravelTable.COLUMN_NAMES.COST));
            }};
            travels.add(travel);
            cursor.moveToNext();
        }

        cursor.close();
        return travels;
    }

    public boolean createTravel(String from, String to, int cost) {
        ContentValues values = new ContentValues();
        values.put(TravelTable.COLUMN_NAMES.FROM, from);
        values.put(TravelTable.COLUMN_NAMES.TO, to);
        values.put(TravelTable.COLUMN_NAMES.COST, cost);

        return getWritableDatabase().insert(TravelTable.NAME, null, values) >= 0 ;
    }

}
