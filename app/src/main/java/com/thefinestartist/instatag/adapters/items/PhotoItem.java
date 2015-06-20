/*
 * Copyright (c) 2014 Rex St. John on behalf of AirPair.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.thefinestartist.instatag.adapters.items;

import java.util.Date;

/**
 * Used to represent a photo item.
 * <p/>
 * Created by Rex St. John (on behalf of AirPair.com) on 3/4/14.
 */
public class PhotoItem {

    private int id;
    private Date date;
    private String bucket;
    private String filePath;
    private String thumbnailPath;
    private int orientation;

    public PhotoItem(int id, Date date, String bucket, String filePath, String thumbnailPath, int orientation) {
        this.id = id;
        this.date = date;
        this.bucket = bucket;
        this.filePath = filePath;
        this.thumbnailPath = thumbnailPath;
        this.orientation = orientation;
    }

    /**
     * Getters
     */
    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getBucket() {
        return bucket;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public int getOrientation() {
        return orientation;
    }
}
