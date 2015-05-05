package com.thefinestartist.instatag.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.aviary.android.feather.sdk.AviaryIntent;
import com.aviary.android.feather.sdk.internal.headless.utils.MegaPixels;
import com.thefinestartist.instatag.adapters.items.PhotoItem;
import com.thefinestartist.instatag.helper.AdHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by TheFinestArtist on 5/5/15.
 */
public class PhotoEditActivity extends CameraActivity {

    static final int REQUEST_EDIT_PHOTO = 4321;

    protected void editPhoto(PhotoItem photoItem) {


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "There was a problem creating the photo...", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                Intent newIntent = new AviaryIntent.Builder(this)
                        .setData(Uri.parse(photoItem.getFilePath()))
                        .withOutput(Uri.fromFile(photoFile))
                        .saveWithNoChanges(false)
                        .withOutputFormat(Bitmap.CompressFormat.JPEG)
                        .withOutputSize(MegaPixels.Mp30)
                        .withOutputQuality(100)
                        .withVibrationEnabled(true)
                        .build();

                startActivityForResult(newIntent, REQUEST_EDIT_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_EDIT_PHOTO) {
            galleryAddPic();
            AdHelper.popUpAd(this);
        }
    }

    private String mCurrentPhotoPath;

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

        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}
