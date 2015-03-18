package com.ahammad.androidtest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.ahammad.androidtest.fragments.AddNoteFragment;

/**
 * Created by Ala Hammad on 3/14/15.
 */
public class AddNoteActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        if (savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, AddNoteFragment.initialize()).commit();
        }
    }
}
