package com.ahammad.androidtest.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;

/**
 * Created by Ala Hammad on 3/14/15.
 */
public class Note implements Serializable{

    public static final String TABLE_NAME="notes";
    public static final String COL_ID= "_id";
    public static final String COL_NOTE = "note";
    public static final String COL_NOTE_DATE = "note_date";

    public static final String[] FIELDS = { COL_ID, COL_NOTE, COL_NOTE_DATE};

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COL_ID + " INTEGER PRIMARY KEY,"
                    + COL_NOTE + " TEXT NOT NULL DEFAULT '',"
                    + COL_NOTE_DATE + " TEXT NOT NULL DEFAULT ''"
                    + ")";


    public long id;
    public String note;
    public long noteDate;

    public Note() {
    }


    public Note(String note, long noteDate) {
        this.note = note;
        this.noteDate = noteDate;
    }

    /**
     * Convert information from the database into a Person object.
     */
    public Note(final Cursor cursor) {
        // Indices expected to match order in FIELDS!
        this.id = cursor.getLong(0);
        this.note = cursor.getString(1);
        this.noteDate = cursor.getLong(2);
    }

    public ContentValues getContent() {
        final ContentValues values = new ContentValues();
        // Note that ID is NOT included here
        values.put(COL_NOTE,note);
        values.put(COL_NOTE_DATE, noteDate);

        return values;
    }

}
