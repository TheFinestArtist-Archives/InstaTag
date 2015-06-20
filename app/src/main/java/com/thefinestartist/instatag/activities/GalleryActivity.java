package com.thefinestartist.instatag.activities;

import android.app.LoaderManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
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
import com.thefinestartist.instatag.helpers.AdHelper;
import com.thefinestartist.instatag.helpers.PhotoAsyncLoader;
import com.thefinestartist.instatag.helpers.PhotoItemHelper;
import com.thefinestartist.instatag.helpers.ShareHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GalleryActivity extends InstaTagActivity
        implements
        SwipeRefreshLayout.OnRefreshListener,
        AdapterView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<List<PhotoItem>> {

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
    private Loader loader;

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

        loader = getLoaderManager().initLoader(0, null, this);
        refresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK)
                || (requestCode == REQUEST_EDIT_PHOTO && resultCode == RESULT_OK)) {
            refresh();
        }
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    private void refresh() {
        swipeLayout.setRefreshing(true);
        loader.forceLoad();
    }

    @Override
    public Loader<List<PhotoItem>> onCreateLoader(int id, Bundle args) {
        return new PhotoAsyncLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<PhotoItem>> loader, List<PhotoItem> data) {
        photoItems.clear();
        for (PhotoItem item : data)
            photoItems.add(item);
        adapter.notifyDataSetChanged();
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<List<PhotoItem>> loader) {
        // do nothing
    }

    /**
     * OnPhotoClicked
     */
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
                    case R.id.delete:
                        PhotoItemHelper.delete(photoItem);
                        refresh();
                        break;
                }
            }
        }).show();
    }

    /**
     * Menu Settings
     */
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
}
