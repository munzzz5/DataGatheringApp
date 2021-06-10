package com.example.datagatheringapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;

public class foregroundService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("OnStartCommand:","Reached");
        Intent intent1=new Intent(this,SensorService.class);
        Intent intentNotif=new Intent(this,MainActivity.class);
        PendingIntent piNotif=PendingIntent.getActivity(this,0,intentNotif,0);
        PendingIntent pi=PendingIntent.getService(this,0,intent1,0);
        Notification notification = new NotificationCompat.Builder(this, "MY SERVICE APP")
                .setContentTitle("Foreground Service")
                .setContentText("MY TEXT NOTIF")
                .setContentIntent(piNotif)
                .build();

        startForeground(1,notification);


        long mills=timeSettingForAlarm(13,25,10);
        AlarmManager alarmManager=(AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,mills+1000,1000,pi);
        return super.onStartCommand(intent, flags, startId);
    }
    public long timeSettingForAlarm(int hour,int minute,int second)
    {
        Calendar calendar=Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.SECOND,second);
        Date d=calendar.getTime();
        long millis=d.getTime();
        long currentMillis=System.currentTimeMillis();
        Log.d("TIME CURRENT MILLIS",Long.toString(currentMillis));
        Log.d("TIME SET TIME MILLIS",Long.toString(millis));

        return millis;
    }
    @Override
    public void onCreate() {
        Log.d("OnCreate:","Reached");
        createNotificationChannel();
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        Log.d("OnDestroy:","Reached");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    "MY SERVICE APP",
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
