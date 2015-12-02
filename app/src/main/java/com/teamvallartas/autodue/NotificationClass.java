package com.teamvallartas.autodue;

import android.app.NotificationManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.grokkingandroid.samplesapp.samples.recyclerviewdemo.R;

/**
 * Created by billyandika on 12/1/15.
 */
public class NotificationClass extends Service {

    static String dueTask = "";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Intent resultIntent=new Intent(this, RecyclerViewDemoActivity.class);
        PendingIntent pIntent=PendingIntent.getActivity(this,0,resultIntent,0);
        Notification nBuilder= new Notification.Builder(this)
                .setContentTitle("A task is due!")
                .setContentText("Mark as done or reschedule it")
                .setContentIntent(pIntent)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        nBuilder.flags |=Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(1,nBuilder);
        return START_STICKY;
    }

    @Override
    public void onCreate() {}

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
