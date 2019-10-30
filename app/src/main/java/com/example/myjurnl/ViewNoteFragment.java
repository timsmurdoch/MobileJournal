package com.example.myjurnl;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

import static android.view.View.GONE;

public class ViewNoteFragment extends Fragment {
    TextView title;
    TextView author;
    TextView content;
    ImageView image;

    EditText editTitle;
    EditText editAuthor;
    EditText editContent;
    ImageView editImage;

    File imgFile;

    FloatingActionButton editButton;
    FloatingActionButton deleteButton;
    DatabaseHelper myDB;

    Note note;

    private String sTitle;
    private String sContent;

    private boolean edit_mode = false;
    ViewNotesFragment theViewNotesFrag;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Create new View
        final View view = inflater.inflate(R.layout.fragment_view_note, container, false);
        theViewNotesFrag = (ViewNotesFragment) getFragmentManager().findFragmentByTag("VIEWNOTESFRAGTAG");


        title = view.findViewById(R.id.view_note_fragment_title);
        author = view.findViewById(R.id.view_note_fragment_author);
        content = view.findViewById(R.id.view_note_fragment_content);
        image = view.findViewById(R.id.view_note_fragment_image);

        editTitle = view.findViewById(R.id.edit_title);
        editAuthor = view.findViewById(R.id.edit_author);
        editContent = view.findViewById(R.id.edit_content);
        editImage = view.findViewById(R.id.edit_image);
        editButton = view.findViewById(R.id.edit_button);
        deleteButton = view.findViewById(R.id.delete_button);

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                deleteNote();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNote();
            }
        });

        getNote();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
    public void deleteNote(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle("Delete Entry");
        builder.setMessage("Are you sure you want to delete this?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDB = new DatabaseHelper(getActivity());
                        if (myDB.deleteRow(getArguments().getInt("NOTE_ID"))) {
                            Log.d("TIM", "DELETED");
                            Intent i = new Intent(getActivity(), MainActivity.class);
                            startActivity(i);
                        }
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public void getNote() {
        //Get the note
        myDB = new DatabaseHelper(getActivity());

        Cursor res = myDB.getAllData();
        if (res.getCount() == 0) {
            Log.d("MATT", "No records found");
        } else {
            while (res.moveToNext()) {
                //Should make this an exception
                if (Integer.parseInt(res.getString(0)) == getArguments().getInt("NOTE_ID")) {
                    Note n = new Note(Integer.parseInt(res.getString(0)),
                            res.getString(1),
                            res.getString(2),
                            res.getString(3),
                            res.getString(4),
                            res.getString(5));

                    note = n;
                    Log.d("MATT", "Found the note!");
                    Log.d("TEST", "Item: " + res.getString(0));
                    break;
                } else {
                    //Log.d("DEBUG", "Could not find the note");
                }
            }
        }

        editTitle.setVisibility(GONE);
        editAuthor.setVisibility(GONE);
        editContent.setVisibility(GONE);
        editImage.setVisibility(GONE);

        title.setVisibility(View.VISIBLE);
        author.setVisibility(View.VISIBLE);
        content.setVisibility(View.VISIBLE);
        image.setVisibility(View.VISIBLE);

        title.setText(note.getTitle());
        author.setText(note.getTimeStamp());
        content.setText(note.getContent());
        imgFile = new File(note.getImage());

        editTitle.setText(note.getTitle());
        editContent.setText(note.getContent());

        if (imgFile.exists()) {
            image.setImageURI(Uri.fromFile(imgFile));
        }
    }


    public void editNote() {
        if (edit_mode) {
            Log.d("MATT", "Edit note...");
            edit_mode = false;

            editTitle.setText(title.getText());
            editAuthor.setText(author.getText());
            editImage.setImageURI(Uri.fromFile(imgFile));
            editContent.setText(content.getText());

            editTitle.setVisibility(View.VISIBLE);
            editAuthor.setVisibility(View.VISIBLE);
            editContent.setVisibility(View.VISIBLE);
            editImage.setVisibility(View.VISIBLE);

            title.setVisibility(GONE);
            author.setVisibility(GONE);
            content.setVisibility(GONE);
            image.setVisibility(GONE);

            Log.d("MATT", "...Edit note...");

        } else {
            Log.d("MATT", "ID: " + note.getID());
            sTitle = editTitle.getText().toString();
            sContent = editContent.getText().toString();
            myDB.insertDataToRow(note.getID(), sTitle, sContent);

            //Update list if in landscape
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                theViewNotesFrag.updateListOfNotes();
            }
            
            getNote();
            edit_mode = true;
            editTitle.setVisibility(GONE);
            editAuthor.setVisibility(GONE);
            editContent.setVisibility(GONE);
            editImage.setVisibility(GONE);

            title.setVisibility(View.VISIBLE);
            author.setVisibility(View.VISIBLE);
            content.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);
        }
    }
}
