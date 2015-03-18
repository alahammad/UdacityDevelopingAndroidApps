package com.ahammad.androidtest.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ahammad.androidtest.NotesAdapter;
import com.ahammad.androidtest.R;
import com.ahammad.androidtest.database.Note;
import com.ahammad.androidtest.database.NoteProvider;

/**
 * Created by Ala Hammad on 3/14/15.
 */
public class NotesFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int NOTES_LOADER = 0;
    private ListView mNotesList;
    private NotesAdapter mAdapter;
    private int mActivatedPosition = ListView.INVALID_POSITION;
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private Callbacks mCallback;



    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(Note note);
    }


    public NotesFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallback = (Callbacks) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.note_fragment,container,false);
        mAdapter = new NotesAdapter(getActivity(),null,0);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNotesList = (ListView)view.findViewById(R.id.lv_notes);
        mNotesList.setAdapter(mAdapter);
        mNotesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    String noteTxt = cursor.getString(1);
                    long noteDate=cursor.getLong(2);
                    Note note = new Note(noteTxt,noteDate);
                    mCallback.onItemSelected(note);
                }
            }
        });
        mNotesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                   if (cursor!=null) {
                       int Itemid = cursor.getInt(0);
                       deleteNoteDialog(Itemid);
                   }
                return true;
            }
        });
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    private void deleteNoteDialog (final int noteId){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage(R.string.delete_note_msg);
        alertDialog.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        getActivity().getContentResolver().delete(NoteProvider.URI_NOTES,Note.COL_ID +  " = "+noteId ,null);
                    }
                });
        alertDialog.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog dialog = alertDialog.create();
        dialog.show();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(NOTES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                NoteProvider.URI_NOTES,
                Note.FIELDS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount()==0){
            Toast.makeText(getActivity(),"No Data Found",Toast.LENGTH_LONG).show();
        }else {
            mAdapter.swapCursor(data);
            setActivatedPosition(0);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            mNotesList.setItemChecked(mActivatedPosition, false);
        } else {
            mNotesList.setItemChecked(position, true);
        }
        mActivatedPosition = position;
    }

}
