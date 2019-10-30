package com.example.myjurnl;


import java.util.ArrayList;
import java.util.List;

public class Note {
    private static List<Note> notes = new ArrayList<Note>();

    private int mID;
    private String mTitle;
    private String mImage;
    private String mBitmapImage;
    private String mContent;
    private String mTimeStamp;

    public Note(int ID, String title, String content, String timestamp, String image,
                String bitmap) {
        mID = ID;
        mTitle = title;
        mTimeStamp = timestamp;
        mBitmapImage = bitmap;
        mImage = image;
        mContent = content;
    }

    public int getID() {
        return mID;
    }
    public String getTitle() {
        if(mTitle != null) {
            return mTitle;
        }
        else
            return "No Data";
    }

    public String getTimeStamp() {
        return mTimeStamp; }
    public String getImage() {
        return mImage;
    }
    public String getContent() {
        return mContent;
    }
    public String getBitmapImage() { return mBitmapImage; }

}
