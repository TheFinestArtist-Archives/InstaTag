package com.thefinestartist.instatag.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.aviary.android.feather.sdk.AviaryIntent;
import com.aviary.android.feather.sdk.internal.headless.utils.MegaPixels;
import com.orhanobut.logger.Logger;
import com.thefinestartist.instatag.R;
import com.thefinestartist.instatag.adapters.items.PhotoItem;
import com.thefinestartist.instatag.helpers.AdHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TheFinestArtist on 5/5/15.
 */
public class PhotoEditActivity extends CameraActivity {

    protected static final int REQUEST_EDIT_PHOTO = 4321;

    private final static String EDITING_PHOTO_PATH = "EDITING_PHOTO_PATH";

    private String mEditingPhotoPath;
    private File mEditingPhotoFile;

    protected void editPhoto(PhotoItem photoItem) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                mEditingPhotoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "There was a problem creating the photo...", Toast.LENGTH_SHORT).show();
            }
            if (mEditingPhotoFile != null) {
                Intent newIntent = new AviaryIntent.Builder(this)
                        .setData(Uri.parse(photoItem.getFilePath()))
                        .withOutput(Uri.fromFile(mEditingPhotoFile))
                        .saveWithNoChanges(false)
                        .withOutputFormat(Bitmap.CompressFormat.JPEG)
                        .withOutputSize(MegaPixels.Mp30)
                        .withOutputQuality(100)
                        .withVibrationEnabled(true)
                        .build();

                startActivityForResult(newIntent, REQUEST_EDIT_PHOTO);
                overridePendingTransition(R.anim.modal_activity_open_enter, R.anim.modal_activity_open_exit);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDIT_PHOTO) {
            if (resultCode == RESULT_OK) {
                galleryAddPic();
                AdHelper.popUpAd(this);
            } else {
                if (mEditingPhotoFile != null && mEditingPhotoFile.exists())
                    Logger.d("onActivityResult, mEditingPhotoFile has been deleted? " + mEditingPhotoFile.delete());
                mEditingPhotoPath = null;
                mEditingPhotoFile = null;
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "InstaTag_Edit_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mEditingPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mEditingPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EDITING_PHOTO_PATH, mEditingPhotoPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mEditingPhotoPath = savedInstanceState.getString(EDITING_PHOTO_PATH);
        mEditingPhotoFile = new File(mEditingPhotoPath);
    }
}
