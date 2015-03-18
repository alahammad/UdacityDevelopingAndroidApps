package com.ahammad.androidtest.fragments;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ahammad.androidtest.AddNoteActivity;
import com.ahammad.androidtest.R;
import com.ahammad.androidtest.database.Note;
import com.ahammad.androidtest.database.NoteProvider;

/**
 * Created by Ala Hammad on 3/14/15.
 */
public class AddNoteFragment extends Fragment {

    private Button mAddNote;
    private EditText mNoteText;

    public static AddNoteFragment initialize (){
        AddNoteFragment addNoteFragment =  new AddNoteFragment();
        return  addNoteFragment;
    }

    public AddNoteFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_note_fragment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNoteText = (EditText)view.findViewById(R.id.et_note);
        mAddNote = (Button)view.findViewById(R.id.btn_addnote);
        mAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mNoteText.getText().toString())) {
                    ContentValues values = new ContentValues();
                    values.put(Note.COL_NOTE, mNoteText.getText().toString());
                    values.put(Note.COL_NOTE_DATE, System.currentTimeMillis());
                    getActivity().getContentResolver().insert(NoteProvider.URI_NOTES, values);
                    if (getActivity() instanceof AddNoteActivity){// for one pane
                        getActivity().finish();
                    }
                }
            }
        });
    }
}
