package com.thefinestartist.instatag.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.aviary.android.feather.sdk.AviaryIntent;
import com.aviary.android.feather.sdk.internal.headless.utils.MegaPixels;
import com.thefinestartist.instatag.adapters.items.PhotoItem;

/**
 * Created by TheFinestArtist on 5/5/15.
 */
public class PhotoEditActivity extends CameraActivity {

    protected void editPhoto(PhotoItem photoItem) {

        Intent newIntent = new AviaryIntent.Builder(this)
                .setData(Uri.parse(photoItem.getFilePath()))
                .withOutput(Uri.parse("file://" + photoItem.getFilePath()))
                .withOutputFormat(Bitmap.CompressFormat.JPEG)
                .withOutputSize(MegaPixels.Mp5)
                .withOutputQuality(90)
                .build();

        startActivityForResult(newIntent, 1);
    }
}
