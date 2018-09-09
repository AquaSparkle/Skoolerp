package erp.skoolerp.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import erp.skoolerp.R;

/**
 * Created by shalu on 10/07/17.
 */

public class NotifyManager
{
    public int NOTIFY_ID = 3;
    private Context context;

    public NotifyManager(Context context)
    {
        this.context = context;
    }

    public void showNotification(String title, String message, Intent intent, int notfy_id) {

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        notfy_id,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        Notification notification;
        notification = mBuilder.setSmallIcon(R.mipmap.notify).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.notify)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.notify))
                .setContentText(message)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);
    }
}
