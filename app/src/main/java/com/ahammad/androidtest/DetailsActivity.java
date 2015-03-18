package com.ahammad.androidtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.ahammad.androidtest.fragments.NotesDetailsFragment;
import com.ahammad.androidtest.database.Note;

/**
 * Created by Ala Hammad on 3/14/15.
 */
public class DetailsActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState==null){
            Bundle bundle = getIntent().getExtras();
            Note note = (Note) getIntent().getSerializableExtra("note");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, NotesDetailsFragment.initialize(note)).commit();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
