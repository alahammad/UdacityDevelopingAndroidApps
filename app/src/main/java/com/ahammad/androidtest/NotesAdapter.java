package com.ahammad.androidtest;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Created by Ala Hammad on 3/14/15.
 */
public class NotesAdapter extends CursorAdapter {

    class ViewHolder {
        public final TextView note,noteDate;
        ViewHolder(View view) {
            note = (TextView)view.findViewById(R.id.tv_note);
            noteDate=(TextView)view.findViewById(R.id.tv_note_date);
        }
    }

    public NotesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.note, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String note = cursor.getString(1);
        long noteDate=cursor.getLong(2);

        viewHolder.note.setText(note);
        String date = new SimpleDateFormat("MM/dd/yyyy").format(noteDate);

        viewHolder.noteDate.setText(date);
    }
}
