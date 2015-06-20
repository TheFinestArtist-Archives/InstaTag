package com.thefinestartist.instatag.helpers;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.SparseArray;

import com.orhanobut.logger.Logger;
import com.thefinestartist.instatag.adapters.items.PhotoItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by TheFinestArtist on 5/6/15.
 */
public class PhotoAsyncLoader extends AsyncTaskLoader<List<PhotoItem>> {

    public PhotoAsyncLoader(Context context) {
        super(context);
    }

    @Override
    public List<PhotoItem> loadInBackground() {
        return getPhotoItems(getContext());
    }

    private static List<PhotoItem> getPhotoItems(Context context) {

        SparseArray<String> thumbnails = getThumbnails(context);

        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.ORIENTATION
        };

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor cur = context.getContentResolver().query(images,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                MediaStore.Images.Media.DATE_TAKEN + " DESC"        // Ordering
        );

        List<PhotoItem> photoItems = new ArrayList<>();
        if (cur.moveToFirst()) {
            int idColumn = cur.getColumnIndex(MediaStore.Images.Media._ID);
            int dateColumn = cur.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);
            int bucketColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int dataColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA);
            int orientationColumn = cur.getColumnIndex(MediaStore.Images.Media.ORIENTATION);

            do {
                int id = cur.getInt(idColumn);
                long date = cur.getLong(dateColumn);
                String bucket = cur.getString(bucketColumn);
                String filePath = cur.getString(dataColumn);
                String thumbnailPath = thumbnails.get(id);
                int orientation = cur.getInt(orientationColumn);

                PhotoItem newItem = new PhotoItem(id,
                        new Date(date),
                        bucket,
                        filePath,
                        thumbnailPath,
                        orientation);
                photoItems.add(newItem);

                Logger.i("id : " + id + " date : " + date + " bucket : " + bucket + " fullfile : " + filePath + " thumbnail :" + thumbnailPath + " orientation : " + orientation);
            } while (cur.moveToNext());
        }

        cur.close();
        return photoItems;
    }

    private static SparseArray<String> getThumbnails(Context context) {

        final String[] projection = {
                MediaStore.Images.Thumbnails.IMAGE_ID,
                MediaStore.Images.Thumbnails.DATA
        };

        Uri images = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;

        Cursor cur = context.getContentResolver().query(images,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                null        // Ordering
        );

        SparseArray<String> thumbnails = new SparseArray<>();
        if (cur.moveToFirst()) {
            int idColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
            int dataColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
            do {
                int id = cur.getInt(idColumn);
                String filePath = cur.getString(dataColumn);
                thumbnails.append(id, filePath);

            } while (cur.moveToNext());
        }

        cur.close();
        return thumbnails;
    }
}
