package com.chajs.dailysentences;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    static int counter=0;
    DatabaseHelper myDb = null;
    Sentence sentence;

    Button btnInsert;
    Button btnShow;
    Button btnTestNoti;
    Button btnList;
    TextView txtNotiTime;

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
        btnList = (Button) findViewById(R.id.buttonLoadList);
        txtNotiTime = (TextView) findViewById(R.id.textViewNotiTime);

        OpenInsertActivity();
        OpenShowSentenceActivity();
        OpenListActivity();
        TestNoti();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("timer: %d", String.valueOf(counter));
                counter++;


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Calendar cal = new GregorianCalendar();

                                txtNotiTime.setText(cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + "/ counter: " + String.valueOf(counter));

                                LoadSentenceForNoti();
                            }
                        });
                    }
                }).start();

            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 0, 540000);

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

    public void showNotification(Sentence sentence) {
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
        intent.putExtra("sentence", sentence);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentTitle("Daily Practice");
        builder.setContentText(sentence.getKorSentence());
        builder.setSmallIcon(R.drawable.main_pic);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        notificationManager.notify(1, notification);
    }

    public void LoadSentenceForNoti() {
        Cursor res = myDb.getNotiData();
        if(res.getCount() == 0) {
            //showMessage("None","조회내용이 없습니다.");
            return ;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            sentence = new Sentence(res.getString(0),
                    res.getString(1), res.getString(2),
                    res.getString(3), res.getString(4),
                    res.getString(5), res.getString(6),
                    res.getString(7), res.getString(8));
            //showMessage("Found",res.getString(1));
            showNotification(sentence);
        }

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

    public void OpenListActivity() {
        btnList.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ListActivity.class);
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
