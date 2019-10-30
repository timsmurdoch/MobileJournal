package com.example.myjurnl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private static final String tag = "VIEWNOTESFRAGTAG";
    private static final String right_tag = "VIEWNOTETAG";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MATT", "MainAct onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Create new fragment and put it into the appropriate frame

        //Check the orientation and change the frame occordingly
        int orientation = getResources().getConfiguration().orientation;
        int frame;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            frame = R.id.main_activity_land_left;
        } else {
            frame = R.id.main_activity;
        }

        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentByTag(tag);
        Fragment right_frag = fm.findFragmentByTag(right_tag);

        if (right_frag != null) {
            fm.beginTransaction().remove(right_frag).commit();
        }

        //frag = getSupportFragmentManager().findFragmentById(R.id.main_activity);

        if (frag == null) {
            Log.d("MATT", "Frag == null");
            frag = new ViewNotesFragment();
            fm.beginTransaction().add(frame, frag, tag).commit();
        } else {
            Log.d("MATT", "landscape");
            fm.beginTransaction().remove(frag).commit();
            Log.d("MATT", "landscape");
            frag = new ViewNotesFragment();
            fm.beginTransaction().add(frame, frag, tag).commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("MATT", "MainAct onStart()");

        Log.d(tag, "onStart() called");
    }


    public void viewNote(int noteID) {
        //Depending on what orientation the device is in launch a new activity or new fragment
        int orientation = getResources().getConfiguration().orientation;
        int frame;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            FragmentManager fm = getSupportFragmentManager();
            Fragment frag = fm.findFragmentByTag(right_tag);
            frame = R.id.main_activity_land_right;

            //frag = getSupportFragmentManager().findFragmentById(R.id.main_activity);
            if (frag == null) {
                Log.d("MATT", "Frag == null");
                frag = new ViewNoteFragment();
                Bundle to = new Bundle();
                to.putInt("NOTE_ID", noteID);
                frag.setArguments(to);
                fm.beginTransaction().add(frame, frag, right_tag).commit();
            } else {
                fm.beginTransaction().remove(frag).commit();
                Bundle to = new Bundle();
                to.putInt("NOTE_ID", noteID);
                frag = new ViewNoteFragment();
                frag.setArguments(to);
                fm.beginTransaction().add(frame, frag, right_tag).commit();
            }

        } else {
            Intent i = new Intent(MainActivity.this, ViewNote.class);
            //Pass the details of the book
            i.putExtra("NOTE_ID", noteID);
            startActivity(i);
        }

    }
}
