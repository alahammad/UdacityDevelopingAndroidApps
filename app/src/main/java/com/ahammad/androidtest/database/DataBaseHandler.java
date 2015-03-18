package com.ahammad.androidtest.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ala Hammad on 3/14/15.
 */
public class DataBaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NotesExample";

    private final Context context;

    private static DataBaseHandler singleton;



    public static DataBaseHandler getInstance(final Context context) {
        if (singleton == null) {
            singleton = new DataBaseHandler(context);
        }
        return singleton;
    }


    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Note.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public synchronized Note getNote(final long id) {
        final SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.query(Note.TABLE_NAME,
                Note.FIELDS, Note.COL_ID + " IS ?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor == null || cursor.isAfterLast()) {
            return null;
        }

        Note item = null;
        if (cursor.moveToFirst()) {
            item = new Note(cursor);
        }
        cursor.close();

        return item;
    }

    public synchronized int removePerson(final Note note) {
        final SQLiteDatabase db = this.getWritableDatabase();
        final int result = db.delete(Note.TABLE_NAME,
                Note.COL_ID + " IS ?",
                new String[] { Long.toString(note.id) });

        if (result > 0) {
            notifyProviderOnPersonChange();
        }
        return result;
    }


    public synchronized boolean putNote(final Note note) {
        boolean success = false;
        int result = 0;
        final SQLiteDatabase db = this.getWritableDatabase();

        if (note.id > -1) {
            result += db.update(Note.TABLE_NAME, note.getContent(),
                    Note.COL_ID + " IS ?",
                    new String[] { String.valueOf(note.id) });
        }

        if (result > 0) {
            success = true;
        } else {
            // Update failed or wasn't possible, insert instead
            final long id = db.insert(Note.TABLE_NAME, null,
                    note.getContent());

            if (id > -1) {
                note.id = id;
                success = true;
            }
        }

        if (success) {
            notifyProviderOnPersonChange();
        }
        return success;
    }

    private void notifyProviderOnPersonChange() {
        context.getContentResolver().notifyChange(
                NoteProvider.URI_NOTES, null, false);
    }

}
