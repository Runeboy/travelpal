package com.rune.travelpal.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.rune.travelpal.data.DatabaseHelper;

/**
 * A content provider for travelpal information
 */
public class MyTodoContentProvider extends ContentProvider {

    // database
    private DatabaseHelper database;

    private static final String AUTHORITY = "rune.travelpal.content"; // "de.vogella.android.todos.contentprovider";

    public enum Path {
        Station(0);

        private final int value;
        private Path(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        for(Path path : Path.values()) {
            sURIMatcher.addURI(AUTHORITY, path.toString(), path.getValue());
        }
    }

    @Override
    public boolean onCreate() {
        database = new DatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int uriType = sURIMatcher.match(uri);
        if (uriType == Path.Station.getValue()) {

            queryBuilder.setTables(StationContract.BASE_PATH);
        }

else
            throw new IllegalArgumentException("Unknown URI: " + uri);

    SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
            throw new IllegalArgumentException("Not allowed");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new IllegalArgumentException("Not allowed");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new IllegalArgumentException("Not allowed");
    }


}