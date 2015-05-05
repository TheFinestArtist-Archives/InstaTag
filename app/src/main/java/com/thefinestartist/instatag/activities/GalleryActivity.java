package com.thefinestartist.instatag.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.cocosw.bottomsheet.BottomSheet;
import com.google.android.gms.ads.AdView;
import com.melnykov.fab.FloatingActionButton;
import com.thefinestartist.instatag.R;
import com.thefinestartist.instatag.adapters.PhotoAdapter;
import com.thefinestartist.instatag.adapters.items.PhotoItem;
import com.thefinestartist.instatag.helper.AdHelper;
import com.thefinestartist.instatag.helper.PhotoGalleyHelper;
import com.thefinestartist.instatag.helper.ShareHelper;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class GalleryActivity extends PhotoEditActivity implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.adView)
    AdView adView;
    @InjectView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @InjectView(android.R.id.list)
    GridView listView;
    @InjectView(R.id.fab)
    FloatingActionButton fab;

    private PhotoAdapter adapter;
    private ArrayList<PhotoItem> photoItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);
        AdHelper.loadBannerAd(adView);

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(R.color.primary, R.color.primary_dark);

        adapter = new PhotoAdapter(this, R.layout.photo_item, photoItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        PhotoGalleyHelper.getPhotoItems(this, photoItems);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
                AdHelper.popUpAd(GalleryActivity.this);
            }
        }, 3000);
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
        new BottomSheet.Builder(this).sheet(R.menu.menu_photo).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PhotoItem photoItem = (PhotoItem) parent.getItemAtPosition(position);
                switch (which) {
                    case R.id.instatag:
                        break;
                    case R.id.edit:
                        editPhoto(photoItem);
                        break;
                    case R.id.share:
                        ShareHelper.share(GalleryActivity.this, photoItem);
                        break;
                }
            }
        }).show();
    }
}
