package com.example.myjurnl;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class makeNoteFragment extends Fragment implements View.OnClickListener{
        DatabaseHelper myDb;
        String timeStamp;
        String myAuthor = "Default";
        static final int REQUEST_IMAGE_CAPTURE = 1;
        public String photoPath;
        public String bitmapPath;
        private Button takePicture;
        private Button submitNote;
        private ImageView image;
        View makeNote;
        Context myContext;
        static final int REQUEST_TAKE_PHOTO = 1;
        EditText myNote;
        EditText myTitle;

        public static makeNoteFragment newInstance(){

        makeNoteFragment f = new makeNoteFragment();
        return f;
    }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.d("MYPHOTOPATHATSTART", "PHOTOPATHATSTARTTT" + myAuthor);

            myContext = getContext();

            makeNote = inflater.inflate(R.layout.make_note_fragment, null);

            takePicture = (Button) makeNote.findViewById(R.id.take_picture);
            image = (ImageView) makeNote.findViewById(R.id.journal_photo);
            submitNote = (Button) makeNote.findViewById(R.id.submit_journal_entry);
            myNote = (EditText) makeNote.findViewById(R.id.journal_entry);
            myTitle = (EditText) makeNote.findViewById(R.id.make_note_title);

            submitNote.setOnClickListener(this);
            takePicture.setOnClickListener(this);

            return makeNote;
        }

        @Override
        public void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);

            if (savedInstanceState != null) {

            }
        }
        @Override
        public void onViewCreated (View v, Bundle savedInstanceState){
            super.onViewCreated(v, savedInstanceState);
            if (savedInstanceState != null) {

                //image = makeNote.findViewById(R.id.journal_photo);
                photoPath = savedInstanceState.getString("photoPath");
                Log.d("tag", "MYPHOTOPATH" +photoPath);
                Log.d("tag", "IMAGE ID VIEW" +image);
                previewImage();
            }
        }
        @Override
        public void onAttach(Context context){
            super.onAttach(context);
            myDb = new DatabaseHelper(context);
        }

    @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.take_picture:
                    dispatchTakePictureIntent();
                    break;
                case R.id.submit_journal_entry:
                    if(photoPath != null ) {
                        galleryAddPic();
                        thumbnailAddPic();
                        sendToDatabase();
                    }
                    else{
                        Toast.makeText(myContext, "Missing Image", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

        public void sendToDatabase(){
            //MATT - Changed author to note title, and added a bitmap field
            Log.d("TAG", "sending started" + " " + myTitle.getText().toString()
                    + " " +myNote.getText().toString() + " " +timeStamp +" " + photoPath);
            if(!myNote.getText().toString().isEmpty()) {
                boolean isInserted = myDb.insertData(myTitle.getText().toString(),
                        myNote.getText().toString(), timeStamp, photoPath, bitmapPath);
                if (isInserted = true)
                    Toast.makeText(myContext,"Inserted",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(myContext,"Fail",Toast.LENGTH_LONG).show();
            }
            else Toast.makeText(myContext, "Missing Data", Toast.LENGTH_SHORT).show();
        }

        public void dispatchTakePictureIntent () {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {

                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(myContext, "com.example.myjurnl.fileprovider", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }

            }
        }

        @Override
        public void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                previewImage();
            }
        }

        public void previewImage () {
            Log.d("tag", "MYPHOTOPATHINPREVIEWIMAGE" +photoPath);
            File imgFile = new File(photoPath);
            if (imgFile.exists()) {
                image.setImageURI(Uri.fromFile(imgFile));
            }
        }

        private File createImageFile () throws IOException {
            // Create an image file name
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            Log.d(timeStamp, "TIMESTAMP");
            //can add more values to file name if needed
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            File image = File.createTempFile(imageFileName, ".jpg", storageDir);
            Log.d(imageFileName, "FILENAME");
            // Save a file: path for use with ACTION_VIEW intents
            photoPath = image.getAbsolutePath();
            return image;
        }
        private void galleryAddPic () {
            Log.d("tag", "GalleryAddPic");
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(photoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            getActivity().sendBroadcast(mediaScanIntent);
        }

        private void thumbnailAddPic() {
            // Get the dimensions of the View
            int targetW = 220;
            int targetH = 220;

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;

            // Determine how much to scale down the image
            //int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
            int scaleFactor = 50;

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap mBitmapImage = BitmapFactory.decodeFile(photoPath, bmOptions);

            bitmapPath = photoPath + "THUMBNAIL.jpg";

            try (FileOutputStream out = new FileOutputStream(bitmapPath)) {
                mBitmapImage.compress(Bitmap.CompressFormat.PNG, 20, out); // bmp is your Bitmap instance
                Log.d("MATT", "Bitmap saved");
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSaveInstanceState (Bundle outState  ){
            super.onSaveInstanceState(outState);
            outState.putString("photoPath", photoPath);
        }
    }

