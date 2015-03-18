package com.ahammad.androidtest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.ahammad.androidtest.database.Note;
import com.ahammad.androidtest.fragments.AddNoteFragment;
import com.ahammad.androidtest.fragments.NotesDetailsFragment;
import com.ahammad.androidtest.fragments.NotesFragment;
import com.ahammad.androidtest.utils.AlarmReceiver;
import com.ahammad.androidtest.utils.Utility;


public class MainActivity extends ActionBarActivity   implements NotesFragment.Callbacks{

    private boolean mTwoPane;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.item_detail_container) != null){
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            if (savedInstanceState==null)
                  getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, new NotesDetailsFragment())
                    .commit();
        }
        activiateReminder ();
    }


    private void activiateReminder (){
        if (!Utility.isAllowedReminder(this)) {// this condition to avoid run when application run
            int min = Integer.parseInt(Utility.getReminderTime(this));
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                    intent, PendingIntent.FLAG_CANCEL_CURRENT);
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                    (1 * 60 * 1000), pendingIntent);
        }

    }

    @Override
    public void onItemSelected(Note note) {
        if(mTwoPane){
            NotesDetailsFragment notesDetailsFragment =  NotesDetailsFragment.initialize(note);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, notesDetailsFragment)
                    .commit();
        }else {
            Intent intent = new Intent(this,DetailsActivity.class);
            intent.putExtra("note",note);
            startActivity(intent);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            if (mTwoPane){
                AddNoteFragment addNoteFragment =  AddNoteFragment.initialize();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.item_detail_container, addNoteFragment)
                        .commit();
            }else{
                Intent intent = new Intent(this,AddNoteActivity.class);
                startActivity(intent);
            }
            return true;
        }else if (id==R.id.action_settings){

            Intent intent  =new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
