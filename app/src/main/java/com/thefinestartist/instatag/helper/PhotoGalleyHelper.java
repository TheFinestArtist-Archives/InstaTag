package com.thefinestartist.instatag.helper;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.orhanobut.logger.Logger;
import com.thefinestartist.instatag.adapters.items.PhotoItem;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by TheFinestArtist on 5/5/15.
 */
public class PhotoGalleyHelper {

    public static void getPhotoItems(Context context, ArrayList<PhotoItem> photoItems) {
        photoItems.clear();

        // which image properties are we querying
        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.ORIENTATION
        };

        // Get the base URI for the People table in the Contacts content provider.
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        // Make the query.
        Cursor cur = context.getContentResolver().query(images,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                MediaStore.Images.Media.DATE_TAKEN + " DESC"        // Ordering
        );

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
                String thumbnailPath = getThumbnailPathForLocalFile(context, id);
                int orientation = cur.getInt(orientationColumn);

                PhotoItem newItem = new PhotoItem(id,
                        new Date(date),
                        bucket,
                        filePath,
                        thumbnailPath,
                        orientation);
                photoItems.add(newItem);

                Logger.i("id : " + id + " date : " + date + " bucket : " + bucket + " fullfile : " + filePath + " thumbnail :" + thumbnailPath+ " orientation : " + orientation);
            } while (cur.moveToNext());
        }

        cur.close();
    }

    private static String getThumbnailPathForLocalFile(Context context, long fileId) {
        Cursor thumbCursor = null;
        try {
            thumbCursor = context.getContentResolver().
                    query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI
                            , null
                            , MediaStore.Images.Thumbnails.IMAGE_ID + " = " + fileId + " AND "
                            + MediaStore.Images.Thumbnails.KIND + " = "
                            + MediaStore.Images.Thumbnails.MINI_KIND, null, null);

            if (thumbCursor.moveToFirst()) {
                int dataIndex = thumbCursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                String thumbnailPath = thumbCursor.getString(dataIndex);
                return thumbnailPath;
            }
        } finally {
            if (thumbCursor != null) {
                thumbCursor.close();
            }
        }

        return null;
    }
}
