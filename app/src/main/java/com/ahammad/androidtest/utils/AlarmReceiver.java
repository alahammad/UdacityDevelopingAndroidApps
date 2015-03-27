package com.ahammad.androidtest.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;

import com.ahammad.androidtest.MainActivity;
import com.ahammad.androidtest.R;
import com.ahammad.androidtest.database.Note;
import com.ahammad.androidtest.database.NoteProvider;

import java.text.SimpleDateFormat;

/**
 * Created by Ala Hammad on 3/17/15.
 */
public class AlarmReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        if (Utility.isAllowedReminder(context)) {
            String msg = getMessage(context);
            if (!TextUtils.isEmpty(msg)) {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.attention_icon)
                                .setContentTitle(context.getString(R.string.app_name))
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(msg))
                                .setContentText(msg);
                Intent resultIntent = new Intent(context, MainActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);

                NotificationManager mNotificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, mBuilder.build());
            }
        }
    }


    private String getMessage(Context context) {
        String string = null;
        Cursor cursor = context.getContentResolver().query(NoteProvider.URI_NOTES, Note.FIELDS, null, null, null);
        if (cursor != null
                && cursor.getCount() > 0) {
            if (cursor.moveToLast()) {
                String date = new SimpleDateFormat("MM/dd/yyyy").format(cursor.getLong(2));
                string = String.format("%s\n%s", cursor.getString(1), date);
            }
        }
        return string;
    }
}