package com.example.myjurnl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class NoteListAdapter extends BaseAdapter {
    private ArrayList<Note> notes;
    private LayoutInflater layoutInflater;
    private Context context;

    public NoteListAdapter(Context context, ArrayList<Note> notes) {
        this.notes = notes;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void deleteItem(int position) {
        Log.d("DEBUG", "Postion: " + position);
        //dons.remove(position);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.fragment_view_notes_row_layout, null);
            holder = new ViewHolder();
            final int id = notes.get(position).getID();
            holder.ID = id;

            holder.title = (TextView) convertView.findViewById(R.id.title_text);
            holder.author = (TextView) convertView.findViewById(R.id.author_text);
            holder.image = (ImageView) convertView.findViewById(R.id.note_list_image);
            holder.row = (LinearLayout) convertView.findViewById(R.id.fragment_book_list_row_layout_row);

            holder.row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Start the new viewNote
                    MainActivity act = (MainActivity) context;
                    act.viewNote(holder.ID);
                }
            });

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }




        String concat_content;
        if (notes.get(position).getContent().length() > 20) {
            concat_content = notes.get(position).getContent().substring(0, 20) + "...";
        } else {
            concat_content = notes.get(position).getContent();
        }

        holder.author.setText(concat_content);
        holder.title.setText(notes.get(position).getTitle());
        holder.ID = notes.get(position).getID();
        //holder.image.setImageDrawable(R.drawable.ic_launcher_background);

        File imgFile = new File(notes.get(position).getBitmapImage());
        if (imgFile.exists()) {
            holder.image.setImageURI(Uri.fromFile(imgFile));
        }


        return convertView;
    }


    static class ViewHolder {
        int ID;
        TextView title;
        TextView author;
        ImageView image;
        LinearLayout row;
    }
}