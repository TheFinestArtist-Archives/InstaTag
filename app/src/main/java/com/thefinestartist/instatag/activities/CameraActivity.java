package com.thefinestartist.instatag.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.thefinestartist.instatag.helper.AdHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TheFinestArtist on 5/4/15.
 */
public class CameraActivity extends StatusBarTintActivity {

    private static final int REQUEST_TAKE_PHOTO = 1234;

    private String mTakingPhotoPath;
    private File mTakingPhotoFile;

    protected void dispatchTakePictureIntent() {

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "This device does not have a camera.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                mTakingPhotoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "There was a problem creating the photo...", Toast.LENGTH_SHORT).show();
            }
            if (mTakingPhotoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTakingPhotoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                galleryAddPic();
                AdHelper.popUpAd(this);
            } else {
                if (mTakingPhotoFile != null && mTakingPhotoFile.exists())
                    Logger.d("onActivityResult, mTakingPhotoFile has been deleted? " + mTakingPhotoFile.delete());
                mTakingPhotoPath = null;
                mTakingPhotoFile = null;
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "InstaTag_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mTakingPhotoPath = "file:" + image.getAbsolutePath();
        Logger.d("createImageFile : " + mTakingPhotoPath);
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mTakingPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private final static String TAKING_PHOTO_PATH = "TAKING_PHOTO_PATH";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TAKING_PHOTO_PATH, mTakingPhotoPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTakingPhotoPath = savedInstanceState.getString(TAKING_PHOTO_PATH);
        mTakingPhotoFile = new File(mTakingPhotoPath);
    }
}
