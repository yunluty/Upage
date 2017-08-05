package com.xhh.upage;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

public class UpageContentProvider extends ContentProvider {
    private static final UriMatcher sMatcher;
    private static final HashMap<String, String> sPagesMap;

    private DBHelper mDBHelper;

    static {
        sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sMatcher.addURI(UpageUtils.AUTHORITY, UpageUtils.PATH_PAGES, UpageUtils.PAGES);
        sMatcher.addURI(UpageUtils.AUTHORITY, UpageUtils.PATH_ID_PATTERN, UpageUtils.PAGE_ID);

        sPagesMap = new HashMap<String, String>();
        sPagesMap.put(UpageUtils._ID, UpageUtils._ID);
        sPagesMap.put(UpageUtils.COLUMN_NAME_TITLE, UpageUtils.COLUMN_NAME_TITLE);
        sPagesMap.put(UpageUtils.COLUMN_NAME_PAGE, UpageUtils.COLUMN_NAME_PAGE);
        sPagesMap.put(UpageUtils.COLUMN_NAME_CREATED, UpageUtils.COLUMN_NAME_CREATED);
        sPagesMap.put(UpageUtils.COLUMN_NAME_MODIFIED, UpageUtils.COLUMN_NAME_MODIFIED);
    }

    static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, UpageUtils.DB_NAME, null, UpageUtils.DB_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + UpageUtils.TABLE_NAME + " ("
                    + UpageUtils._ID + " INTEGER PRIMARY KEY,"
                    + UpageUtils.COLUMN_NAME_TITLE + " TEXT,"
                    + UpageUtils.COLUMN_NAME_PAGE + " TEXT,"
                    + UpageUtils.COLUMN_NAME_CREATED + " INTEGER,"
                    + UpageUtils.COLUMN_NAME_MODIFIED + " INTEGER"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + UpageUtils.TABLE_NAME);
            onCreate(db);
        }
    }


    public UpageContentProvider() {}

    @Override
    public boolean onCreate() {
        mDBHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int row = 0;
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        switch (sMatcher.match(uri)) {
            case UpageUtils.PAGES:
                row = db.delete(UpageUtils.TABLE_NAME, selection, selectionArgs);
                break;

            case UpageUtils.PAGE_ID:
                String id = uri.getPathSegments().get(1);
                String where = UpageUtils._ID + "=" + id;

                if (selection != null) {
                    where = where + "AND" + selection;
                }
                row = db.delete(UpageUtils.TABLE_NAME, where, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }

        return row;
    }

    @Override
    public String getType(Uri uri) {
        switch (sMatcher.match(uri)) {
            case UpageUtils.PAGES:
                return UpageUtils.CONTENT_TYPE;

            case UpageUtils.PAGE_ID:
                return UpageUtils.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (sMatcher.match(uri) != UpageUtils.PAGES) {
            throw new UnsupportedOperationException("Unknown Uri");
        }

        ContentValues newValues;

        if (values != null) {
            newValues = new ContentValues(values);
        } else {
            newValues = new ContentValues();
        }

        Long now = System.currentTimeMillis();

        if (!newValues.containsKey(UpageUtils.COLUMN_NAME_CREATED)) {
            newValues.put(UpageUtils.COLUMN_NAME_CREATED, now);
        }

        if (!newValues.containsKey(UpageUtils.COLUMN_NAME_MODIFIED)) {
            newValues.put(UpageUtils.COLUMN_NAME_MODIFIED, now);
        }

        if (!newValues.containsKey(UpageUtils.COLUMN_NAME_TITLE)) {
            newValues.put(UpageUtils.COLUMN_NAME_TITLE, "");
        }

        if (!newValues.containsKey(UpageUtils.COLUMN_NAME_PAGE)) {
            newValues.put(UpageUtils.COLUMN_NAME_PAGE, "");
        }

        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        long rowID = db.insert(UpageUtils.TABLE_NAME, UpageUtils.COLUMN_NAME_PAGE, newValues);


        if (rowID > 0) {
            return ContentUris.withAppendedId(UpageUtils.CONTENT_URI, rowID);
        } else {
            return null;
        }

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        qb.setTables(UpageUtils.TABLE_NAME);
        qb.setProjectionMap(sPagesMap);

        switch (sMatcher.match(uri)) {
            case UpageUtils.PAGES:
                break;

            case UpageUtils.PAGE_ID:
                qb.appendWhere(UpageUtils._ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown uri:" + uri);
        }

        String sortBy;
        if (TextUtils.isEmpty(sortOrder)) {
            sortBy = UpageUtils.DEFAULT_SORT_ORDER;
        } else {
            sortBy = sortOrder;
        }

        SQLiteDatabase db = mDBHelper.getReadableDatabase();

        if (db != null) {
            return qb.query(db, projection, selection, selectionArgs, null, null, sortBy);
        } else {
            return null;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int row;
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        switch (sMatcher.match(uri)) {
            case UpageUtils.PAGES:
                row = db.update(UpageUtils.TABLE_NAME, values, selection, selectionArgs);
                break;

            case UpageUtils.PAGE_ID:
                String id = uri.getPathSegments().get(1);
                String where = UpageUtils._ID + "=" + id;

                if (selection != null) {
                    where = where + "AND" + selection;
                }
                row = db.update(UpageUtils.TABLE_NAME, values, where, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }

        return row;
    }
}
