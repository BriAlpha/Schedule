package com.example.can301.things.Service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import androidx.core.app.NotificationCompat;

import com.example.can301.things.R;
import com.example.can301.things.Today.NoteActivity;

import static com.example.can301.things.MainActivity.todayCount;
import static com.example.can301.things.Setting.SettingActivity.count;

public class SendMessage extends Service {
    public SendMessage() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(count%2 == 0){  //Switch images according to the number of times
                    Intent notificationIntent = new Intent(SendMessage.this,NoteActivity.class);  //Start setting the notification effect
                    PendingIntent pendingIntent = PendingIntent.getActivity(SendMessage.this,0, notificationIntent,0);  //Set PendingIntent
                    NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){  //Check whether the value is greater than 8.0
                        NotificationChannel notificationChannel = new NotificationChannel("channel","channel",NotificationManager.IMPORTANCE_HIGH);
                        manager.createNotificationChannel(notificationChannel);
                        Notification notification = new NotificationCompat.Builder(SendMessage.this,"channel")
                                .setContentTitle("The task list has been delivered")
                                .setContentText("Lord, there's more today" + todayCount + "tasks to complete oh!")
                                .setWhen(System.currentTimeMillis())  //Time to send the notification
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.logo144))  //Set large icon
                                .setContentIntent(pendingIntent)  //Set the click effect of the notification
                                .setAutoCancel(true) //Set to automatically turn off notifications when clicked
                                .build();

                        if(todayCount > 0){
                            manager.notify(1,notification);  //Display notification
                        }
                    }

                    else{
                        Notification notification = new NotificationCompat.Builder(SendMessage.this)
                                .setContentTitle("Today's to-do list")
                                .setContentText("The little master still has today" + todayCount + "tasks to complete oh!")
                                .setWhen(System.currentTimeMillis())  //Time to send the notification
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.logo144))  //Set large icon
                                .setContentIntent(pendingIntent)  //Set the click effect of the notification
                                .setAutoCancel(true)  //Set to automatically turn off notifications when clicked
                                .build();

                        if(todayCount > 0){
                            manager.notify(1,notification);  //Display notification
                        }
                    }
                }

                else{
                }

            }
        }).start();

        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);  //Set timing service
        int fourHour = 4*60*60*1000;  //The unit is milliseconds (4 hours).
        long time = SystemClock.elapsedRealtime() + fourHour;
        Intent i = new Intent(this,SendMessage.class);
        PendingIntent pi = PendingIntent.getService(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,time,pi);
        return super.onStartCommand(intent,flags,startId);
    }
}
