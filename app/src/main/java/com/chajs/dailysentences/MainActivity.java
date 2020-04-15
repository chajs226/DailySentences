package com.chajs.dailysentences;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    static int counter=0;
    DatabaseHelper myDb = null;

    Button btnInsert;
    Button btnShow;
    Button btnTestNoti;

    NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Chaneel1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

        btnInsert = (Button) findViewById(R.id.buttonInsert);
        btnShow = (Button) findViewById(R.id.buttonVeiwAll);
        btnTestNoti = (Button) findViewById(R.id.buttonNoti);
        OpenInsertActivity();
        OpenShowSentenceActivity();
        TestNoti();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("timer: %d", String.valueOf(counter));
                counter++;
                LoadSentenceForNoti();
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 0, 600000);

    }

    public void TestNoti() {
        btnTestNoti.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoadSentenceForNoti();
                    }
                }
        );
    }

    public void showNotification(String id, String kor, String eng) {
        builder = null;
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        //오레오 버전 이상
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANEL_NAME, notificationManager.IMPORTANCE_DEFAULT)
            );
            builder = new NotificationCompat.Builder(this,CHANNEL_ID);
        }
        //하위버전일때
        else {
            builder = new NotificationCompat.Builder(this);
        }

        Intent intent = new Intent(this, ShowSentenceActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("kor", kor);
        intent.putExtra("eng", eng);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentTitle("Daily Practice");
        builder.setContentText(kor);
        builder.setSmallIcon(R.drawable.main_pic);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        notificationManager.notify(1, notification);
    }

    public void LoadSentenceForNoti() {
        Cursor res = myDb.getNotiData();
        if(res.getCount() == 0) {
            return ;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            //buffer.append("Kor: " + res.getString(1) + "\n");
            //buffer.append("Eng: " + res.getString(2) + "\n");
            showNotification(res.getString(0), res.getString(1), res.getString(2));
        }


        //showMessage("Get Data", buffer.toString());

    }

    public void OpenInsertActivity() {
        btnInsert.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), InsertActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }

    public void OpenShowSentenceActivity() {
        btnShow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ShowSentenceActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
