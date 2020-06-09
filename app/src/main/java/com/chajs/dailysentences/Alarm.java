package com.chajs.dailysentences;

import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;
//import static com.chajs.dailysentences.MainActivity.CHANEL_NAME;
//import static com.chajs.dailysentences.MainActivity.CHANNEL_ID;

public class Alarm extends BroadcastReceiver {

    DatabaseHelper myDb;
    Sentence sentence;
    NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    private static String CHANNEL_ALARM_ID = "channel1";
    private static String CHANEL_ALARM_NAME = "Chaneel1";

    @Override
    public void onReceive(Context context, Intent intent) {

        myDb = new DatabaseHelper(context);
        String [] id = new String[] {intent.getExtras().getString("id")};

        Log.d("onReceive", "ID = " + id);


        LoadSentenceForNotiUsingAlarm(context, id);


    }

    public void LoadSentenceForNotiUsingAlarm(Context context, String[] id) {
        Cursor res = myDb.getNotiDataForAlaram(id);

        if(res.getCount() == 0) {
            //showMessage("None","조회내용이 없습니다.");
            return ;
        }

        while (res.moveToNext()) {
            sentence = new Sentence(res.getString(0),
                    res.getString(1), res.getString(2),
                    res.getString(3), res.getString(4),
                    res.getString(5), res.getString(6),
                    res.getString(7), res.getString(8), res.getString(9));
            //showMessage("Found",res.getString(1));
            showNotification(context, sentence);
        }

    }

    public void showNotification(Context context, final Sentence sentence) {
        builder = null;
        notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        //오레오 버전 이상
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ALARM_ID, CHANEL_ALARM_NAME, notificationManager.IMPORTANCE_DEFAULT)
            );
            builder = new NotificationCompat.Builder(context, CHANNEL_ALARM_ID);
        }
        //하위버전일때
        else {
            builder = new NotificationCompat.Builder(context);
        }

        Intent intent = new Intent(context, ShowSentenceActivity.class);
        intent.putExtra("sentence", sentence);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentTitle("Daily Practice");
        builder.setContentText(sentence.getKorSentence());
        builder.setSmallIcon(R.drawable.main_pic);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        notificationManager.notify(1, notification);

        //시간 지나면 skip 처리
        TimerTask task = new TimerTask() {
            public void run() {
                try {
                    Integer recordUpdate = Integer.valueOf(sentence.getSkipCount()) + 1;
                    boolean isUpdated = myDb.updateRecord(sentence.getId(), "K", recordUpdate);

                    if(isUpdated) {
                        notificationManager.cancel(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 90000);
    }

}
