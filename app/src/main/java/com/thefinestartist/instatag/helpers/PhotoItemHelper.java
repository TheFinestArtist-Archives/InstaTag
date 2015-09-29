package com.thefinestartist.instatag.helpers;

import com.thefinestartist.instatag.adapters.items.PhotoItem;

import java.io.File;

/**
 * Created by TheFinestArtist on 5/6/15.
 */
public class PhotoItemHelper {

    public static void delete(PhotoItem item) {
        try {
            File file = new File(item.getFilePath());
            if (file.exists())
                file.delete();
        } catch (Exception e) {
        }
    }
}
