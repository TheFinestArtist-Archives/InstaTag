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

package com.thefinestartist.instatag.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.thefinestartist.instatag.R;
import com.thefinestartist.instatag.adapters.items.PhotoItem;
import com.thefinestartist.instatag.helper.ScreenHelper;

import java.util.List;

public class PhotoAdapter extends ArrayAdapter<PhotoItem> {

    private Context context;
    private int resourceId;
    private SparseBooleanArray animationChecker = new SparseBooleanArray();

    public PhotoAdapter(Context context, int resourceId, List<PhotoItem> items) {
        super(context, resourceId, items);
        this.context = context;
        this.resourceId = resourceId;
    }

    private class ViewHolder {
        ImageView photo;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        final ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resourceId, null);

            holder = new ViewHolder();
            holder.photo = (ImageView) view.findViewById(R.id.image_view);

            view.setTag(holder);
            view.setLayoutParams(new GridView.LayoutParams(getItemWidth(), getItemHeight()));
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        PhotoItem photoItem = getItem(position);
        Picasso.with(context)
                .load("file://" + photoItem.getThumbnailPath())
                .rotate(photoItem.getOrientation())
                .noFade()
                .into(holder.photo, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (!animationChecker.get(position)) {
                            Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                            fadeIn.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    animationChecker.append(position, true);
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    animationChecker.append(position, false);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                }
                            });
                            holder.photo.startAnimation(fadeIn);
                        }
                    }

                    @Override
                    public void onError() {
                    }
                });

        return view;
    }

    private int itemWidth, itemHeight;

    private int getItemWidth() {
        if (itemWidth == 0) {
            int gridViewPadding = context.getResources().getDimensionPixelSize(R.dimen.gridview_padding);
            int screenWidth = ScreenHelper.getWidth(context);
            itemWidth = (screenWidth - gridViewPadding * 3) / 3;
        }

        return itemWidth;
    }

    private int getItemHeight() {
        if (itemHeight == 0) {
            int extraHeight = context.getResources().getDimensionPixelSize(R.dimen.gridview_item_extra_height);
            itemHeight = getItemWidth() + extraHeight;
        }

        return itemHeight;
    }
}
