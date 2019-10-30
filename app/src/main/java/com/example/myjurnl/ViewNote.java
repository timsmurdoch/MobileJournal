package com.example.myjurnl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

public class ViewNote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle from = getIntent().getExtras();
        Bundle to = new Bundle();
        if (from != null) {
            to.putInt("NOTE_ID", from.getInt("NOTE_ID"));
        }

        FragmentManager fm = getSupportFragmentManager();

        //Create new fragment and put it into the view book frame
        Fragment frag = new ViewNoteFragment();
        frag.setArguments(to);
        fm.beginTransaction().replace(R.id.view_note_activity, frag).commit();
    }



}
