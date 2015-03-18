package com.ahammad.androidtest.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class NoteProvider extends ContentProvider {

    // All URIs share these parts
    public static final String AUTHORITY = "net.ahammad.androidtest.notesprovider";
    public static final String SCHEME = "content://";

    // URIs
    // Used for all persons
    public static final String NOTES = SCHEME + AUTHORITY + "/note";
    public static final Uri URI_NOTES = Uri.parse(NOTES);
    // Used for a single person, just add the id to the end
    public static final String NOTE_BASE = NOTES + "/";

    private DataBaseHandler mDataBaseHandler;

    public NoteProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
            SQLiteDatabase db=mDataBaseHandler.getWritableDatabase();

        int count = db.delete(Note.TABLE_NAME,selection ,selectionArgs);
        getContext().getContentResolver().notifyChange(uri,null);
        return  count;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDataBaseHandler.getWritableDatabase();
        long _id = db.insert(Note.TABLE_NAME, null, values);
        Uri _uri = ContentUris.withAppendedId(URI_NOTES, _id);

        getContext().getContentResolver().notifyChange(uri, null);
        return _uri;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        mDataBaseHandler = new DataBaseHandler(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor result = null;
        if (URI_NOTES.equals(uri)) {
            result =mDataBaseHandler
                    .getReadableDatabase()
                    .query(Note.TABLE_NAME, Note.FIELDS, null, null, null,
                            null, null, null);
            result.setNotificationUri(getContext().getContentResolver(), URI_NOTES);
        } else if (uri.toString().startsWith(NOTE_BASE)) {
            final long id = Long.parseLong(uri.getLastPathSegment());
            result =mDataBaseHandler
                    .getReadableDatabase()
                    .query(Note.TABLE_NAME, Note.FIELDS,
                            Note.COL_ID + " IS ?",
                            new String[] { String.valueOf(id) }, null, null,
                            null, null);
            result.setNotificationUri(getContext().getContentResolver(), URI_NOTES);
        } else {
            throw new UnsupportedOperationException("Not yet implemented");
        }
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
