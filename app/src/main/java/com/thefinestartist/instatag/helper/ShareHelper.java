package com.thefinestartist.instatag.helper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.cocosw.bottomsheet.BottomSheet;
import com.thefinestartist.instatag.R;
import com.thefinestartist.instatag.adapters.items.PhotoItem;

import java.io.File;
import java.util.List;

/**
 * Created by TheFinestArtist on 5/5/15.
 */
public class ShareHelper {

    public static void share(Activity activity, PhotoItem item) {
        getShareActions(activity, item).title("Share Photo").grid().build().show();
    }

    private static BottomSheet.Builder getShareActions(final Activity activity, PhotoItem photoItem) {
        BottomSheet.Builder builder = new BottomSheet.Builder(activity);
        PackageManager pm = activity.getPackageManager();

        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setAction(Intent.ACTION_SEND);
        File imageFileToShare = new File(photoItem.getFilePath());
        Uri uri = Uri.fromFile(imageFileToShare);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        final List<ResolveInfo> list = pm.queryIntentActivities(shareIntent, 0);

        for (int i = 0; i < list.size(); i++) {
            builder.sheet(i, list.get(i).loadIcon(pm), list.get(i).loadLabel(pm));
        }

        builder.listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityInfo activityInfo = list.get(which).activityInfo;
                ComponentName name = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
                Intent newIntent = (Intent) shareIntent.clone();
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                newIntent.setComponent(name);
                activity.startActivity(newIntent);
                activity.overridePendingTransition(R.anim.modal_activity_open_enter, R.anim.modal_activity_open_exit);
            }
        });
        return builder;
    }
}
