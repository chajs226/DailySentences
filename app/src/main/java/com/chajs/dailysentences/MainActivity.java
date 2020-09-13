package com.chajs.dailysentences;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
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
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    static int counter=0;
    DatabaseHelper myDb = null;
    Sentence sentence;

    Button btnInsert;
    //Button btnShow;
    //Button btnTestNoti;
    Button btnList;
    //Button btnAlarmStart;
    //Button btnAlarmStop;
    Button btnSettings;
    TextView txtNotiTime;
    //static TextView txtAlarmStat;
    StringBuilder notiTimes;

    NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Chaneel1";

    private AdView mAdView;

    //private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Log.d("onCreate","MainActivity");
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        myDb = new DatabaseHelper(this);

        btnInsert = (Button) findViewById(R.id.buttonInsert);
        //btnShow = (Button) findViewById(R.id.buttonVeiwAll);
        //btnTestNoti = (Button) findViewById(R.id.buttonNoti);
        //btnTestNoti.setVisibility(View.INVISIBLE);
        btnList = (Button) findViewById(R.id.buttonLoadList);
        //btnAlarmStart = (Button)findViewById(R.id.buttonAlarmStart);
        //btnAlarmStop = (Button)findViewById(R.id.buttonAlarmStop);
        btnSettings = (Button)findViewById(R.id.buttonSettings);

        //txtNotiTime = (TextView) findViewById(R.id.textViewNotiTime);

        //txtAlarmStat = (TextView) findViewById(R.id.textViewAlramStat);

        //alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        //이벤트 함수
        OpenInsertActivity();
        //OpenShowSentenceActivity();
        OpenListActivity();
        OpenSettingsActivity();
        //TestNoti();
        //AlarmStart();
        //AlarmStop();

        //알람 자동 실행
        //AlarmRegister();


    }

    /*
    public void AlarmStart() {
        btnAlarmStart.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlarmRegister();
                    }
                }
        );
    }
*/

    /*
    public void AlarmStop() {
        btnAlarmStop.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlarmUnregister();
                    }
                }
        );
    }
*/

    /*
    public void AlarmRegister() {
        String autoAlarmYN;
        String fromTime;
        String toTime;
        String count;

        autoAlarmYN = "N";
        fromTime = "";
        toTime = "";
        count = "0";

        Cursor res = myDb.getSettingData();
        if(res.getCount() == 0) {
            autoAlarmYN = "N";
            fromTime = "";
            toTime = "";
            count = "0";
        }
        else {
            while (res.moveToNext()) {
                autoAlarmYN = res.getString(1).toString();
                fromTime = res.getString(2).toString();
                toTime = res.getString(3).toString();
                count = res.getString(4).toString();
            }
        }

        Log.d("AlarmRegister",autoAlarmYN);
        Log.d("AlarmRegister",fromTime);
        Log.d("AlarmRegister",toTime);

        if(autoAlarmYN.equals("Y")) {
            AlarmRegisterByAuto(fromTime, toTime, count);
        }
        else {
            AlarmRegisterByManual();
        }
    }

    public void AlarmRegisterByAuto(String fromTime, String toTime, String count) {

        Integer fromTimeMin, toTimeMin;
        Integer countpereach;

        fromTimeMin = Integer.parseInt(fromTime.split(":")[0]) * 60 + Integer.parseInt(fromTime.split(":")[1]);
        toTimeMin = Integer.parseInt(toTime.split(":")[0]) * 60 + Integer.parseInt(toTime.split(":")[1]);

        countpereach = Integer.parseInt(count);

        Random rand = new Random();

        Cursor res = myDb.getNotiTime();
        if (res.getCount() == 0) {
            //showMessage("None","조회내용이 없습니다.");
            return;
        }

        Calendar calendar = Calendar.getInstance();
        notiTimes = new StringBuilder("Noti Times: ");

        while (res.moveToNext()) {
            for (int i = 0; i < countpereach; i++) {
                Intent intent = new Intent(this, Alarm.class);
                intent.putExtra("id", res.getString(0));
                PendingIntent pIntent = PendingIntent.getBroadcast(this, Integer.valueOf(res.getString(0)), intent, 0);

                int randomValue = rand.nextInt(toTimeMin - fromTimeMin) + fromTimeMin;

                Log.d("NotiTime: ", String.valueOf(randomValue));
                Log.d("NotiTime: ", String.valueOf(randomValue / 60));
                Log.d("NotiTime: ", String.valueOf(randomValue % 60));

                calendar.set(Calendar.HOUR_OF_DAY, randomValue / 60);
                calendar.set(Calendar.MINUTE, randomValue % 60);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pIntent);

                notiTimes.append(String.valueOf(randomValue / 60) + ":" + String.valueOf(randomValue % 60) + " ");
            }
        }
        Toast.makeText(this, "Alarm Registered", Toast.LENGTH_LONG).show();
        //txtAlarmStat.setText("Alarm Registered");
    }

    public void AlarmRegisterByManual() {

        Cursor res = myDb.getNotiTime();
        Log.d("AlarmRegister", String.valueOf(res.getCount()));

        if(res.getCount() == 0) {
            //showMessage("None","조회내용이 없습니다.");
            return ;
        }

        Calendar calendar = Calendar.getInstance();
        String[] hourMinute;

        notiTimes = new StringBuilder("Noti Times: ");
        while (res.moveToNext()) {
            Log.d("AlarmRegister ", "start");

            Intent intent = new Intent(this, Alarm.class);
            intent.putExtra("id", res.getString(0));
            PendingIntent pIntent = PendingIntent.getBroadcast(this,Integer.valueOf(res.getString(0)),intent,0);

            Log.d("NotiTime: ", res.getString(0)  + " " + res.getString(1));
            hourMinute = res.getString(1).split(":");
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourMinute[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(hourMinute[1]));
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pIntent);
            notiTimes.append(res.getString(1) + " ");
        }
        //txtAlarmStat.setText("Alarm Registered");
        Toast.makeText(this, "Alarm Registered", Toast.LENGTH_LONG).show();
        //txtAlarmStat.setText("Alarm Registered");
    }


    public void AlarmUnregister() {
        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.cancel(pIntent);
        Toast.makeText(this, "Alarm Unregistered", Toast.LENGTH_LONG).show();
        //txtAlarmStat.setText("Alarm Unregistered");
    }

     */

    /*
    public void TestNoti() {
        btnTestNoti.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoadSentenceForNotiUsingTimerTest();
                    }
                }
        );
    }
     */

    public void showNotificationTest(Sentence sentence) {
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

        builder.setContentTitle("Daily Sentences");
        builder.setContentText(sentence.getKorSentence());
        builder.setSmallIcon(R.drawable.icon);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        notificationManager.notify(1, notification);
    }



/*
    public void LoadSentenceForNotiUsingTimerTest() {
        Cursor res = myDb.getNotiData();

        if(txtNotiTime.getText().length() > 500) {
            txtNotiTime.setText("");
        }
        else {
            Calendar cal = new GregorianCalendar();
            txtNotiTime.append(cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + "-C:" + String.valueOf(res.getCount()) + " | ");
        }

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
                    res.getString(7), res.getString(8), res.getString(9));
            //showMessage("Found",res.getString(1));
            showNotificationTest(sentence);
        }

    }

 */

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

    /*
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

     */

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

    public void OpenSettingsActivity() {
        btnSettings.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        //intent.putExtra("notiTimes", notiTimes.toString());
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
