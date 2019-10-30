package com.example.myjurnl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ViewNotesFragment extends Fragment {
    private List<Note> notes = new ArrayList<Note>();
    ListView mListView;
    FloatingActionButton addButton;
    TextView errorText;
    ProgressBar spinner;

    DatabaseHelper myDB;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Create new View
        final View view = inflater.inflate(R.layout.fragment_view_notes, container, false);

        Log.d("MATT", "LIST OF NOTES in onCrreateView()");

        mListView = (ListView) view.findViewById(R.id.view_notes_list);
        errorText = (TextView) view.findViewById(R.id.error_text_view);
        addButton = view.findViewById(R.id.add_button);

        //Log.d("MATT", "in ViewNotes onCreateView()");

        //Go to the make new note
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), StartingActivity.class);
                startActivity(i);
            }
        });

        updateListOfNotes();

        return view;
    }

    public void updateListOfNotes() {

        if(notes.removeAll(notes)){
            Log.d("TIM", "REMOVED NOTES ARRAY");
        }else{
            Log.d("TIM", "HAVE NOT REMOVED NOTES ARRAY");
        }

        myDB = new DatabaseHelper(getActivity());
        Cursor res = myDB.getAllData();

        if (res.getCount() == 0) {
            Log.d("MATT", "No records found");
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(R.string.no_notes_yet);
        } else {
            Log.d("MATT", "Going through records now...");
            while (res.moveToNext()) {
                boolean flag = false;
                int id = Integer.parseInt(res.getString(0));
                for (int i = 0; i < notes.size(); i++) {
                    if (notes.get(i).getID() == id) { flag = true; }
                }
                if (!flag) {
                    Note n = new Note(Integer.parseInt(res.getString(0)),
                            res.getString(1),
                            res.getString(2),
                            res.getString(3),
                            res.getString(4),
                            res.getString(5));

                    notes.add(n);
                }
                flag = false;

            }

            NoteListAdapter a = new NoteListAdapter(getActivity(), (ArrayList<Note>) notes);
            mListView.setAdapter(a);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        updateListOfNotes();
    }
    @Override
    public void onStart() {
        super.onStart();

        Log.d("MATT", "LIST OF NOTES in onStart()");
        updateListOfNotes();
    }

}
