package com.ahammad.androidtest.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahammad.androidtest.R;
import com.ahammad.androidtest.database.Note;
import com.ahammad.androidtest.database.NoteProvider;

import java.text.SimpleDateFormat;

/**
 * Created by Ala Hammad on 3/14/15.
 */
public class NotesDetailsFragment extends Fragment {

    private static final String SHARE_HASHTAG = "#NoteAPP";

    private TextView mNoteText,mNoteDate;

    private Note mNote;
    private ShareActionProvider mShareActionProvider;

    private String mShareNote;



    public NotesDetailsFragment() {
        setHasOptionsMenu(true);
    }

    public static NotesDetailsFragment initialize (Note note){
        NotesDetailsFragment notesDetailsFragment = new NotesDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("note", note);
        notesDetailsFragment.setArguments(bundle);
        return notesDetailsFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes_details,container,false);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        mNote = getArguments()!=null ? (Note) getArguments().getSerializable("note"):null;
        mNoteText = (TextView) view.findViewById(R.id.tv_note);

        mNoteDate = (TextView)view.findViewById(R.id.tv_note_date);
        if (mNote!=null)
            fillViews(mNote);
        else
            setNote ();
    }

    private void fillViews (Note note){
        mNoteText.setText(note.note);
        String date = new SimpleDateFormat("MM/dd/yyyy").format(note.noteDate);
        mNoteDate.setText(date);
        mShareNote = String.format("%s ,\n%s",note.note,date);
    }

    private void setNote (){
        Cursor cursor =getActivity().getContentResolver().query(NoteProvider.URI_NOTES,Note.FIELDS,null,null,null );
        if (cursor!=null
                 && cursor.getCount()>0){
            if (cursor.moveToFirst()){
                Note note = new Note(cursor.getString(1),cursor.getLong(2));
                fillViews(note);
            }
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

//        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mShareNote != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mShareNote+"\n" +SHARE_HASHTAG);
        return shareIntent;
    }



}
