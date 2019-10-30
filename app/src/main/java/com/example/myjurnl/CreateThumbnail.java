package com.example.myjurnl;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;

public class CreateThumbnail extends Service {
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        CreateThumbnail getService() {
            return CreateThumbnail.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public String getBitmap() {
        //Here do the work
        return "This is from the bounded service";
    }
}
