package com.thefinestartist.instatag.helpers;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

/**
 * Created by TheFinestArtist on 5/5/15.
 */
public class MediaScanHelper {

    public static void scan(Context context, String path, MediaScannerConnection.OnScanCompletedListener listener) {
        MediaScannerConnection.scanFile(context, new String[]{path}, null, listener);
    }

    public static void delete(final Context context, String path, final MediaScannerConnection.OnScanCompletedListener listener) {
        MediaScannerConnection.scanFile(context, new String[]{path},
                null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        if (uri != null)
                            context.getContentResolver().delete(uri, null, null);
                        listener.onScanCompleted(path, uri);
                    }
                });
    }
}
