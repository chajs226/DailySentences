package com.chajs.dailysentences;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class InsertActivity extends AppCompatActivity {

    private static final String TAG = "InsertActivity";
    int mFromYear, mFromMonth, mFromDay, mToYear, mToMonth, mToDay, mHour, mMinute;

    DatabaseHelper myDb = null;
    EditText editKor, editEng, editKeyword;
    Button btnAddData;
    Button btnDeleteData;
    boolean tableExists = false;
    String id = null;

    Button btnStartDate;
    TextView txtStartDate;

    Button btnEndDate;
    TextView txtEndDate;

    Button btnSetTime;
    TextView txtTime;

    TextView txtRecord;

    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("문장 등록");

        myDb = new DatabaseHelper(this);

        editKeyword = (EditText)findViewById(R.id.editTextKeyword);

        editKor = (EditText)findViewById(R.id.editTextKor);
        editEng = (EditText)findViewById(R.id.editTextEng);
        btnAddData = (Button)findViewById(R.id.buttonAdd);
        btnDeleteData = (Button)findViewById(R.id.buttonDelete);

        btnStartDate = (Button)findViewById(R.id.buttonStartDate);
        txtStartDate = (TextView)findViewById(R.id.textViewStartDate);

        btnEndDate = (Button)findViewById(R.id.buttonEndDate);
        txtEndDate = (TextView)findViewById(R.id.textViewEndDate);

        btnSetTime = (Button)findViewById(R.id.buttonTime);
        txtTime = (TextView)findViewById(R.id.textViewTime);

        txtRecord = (TextView)findViewById(R.id.textViewRecord);
        AddData();
        DeleteData();
        SetDate();
        SetTime();

        //현재 날짜, 시간 가져오기
        Calendar cal = new GregorianCalendar();
        mFromYear = cal.get(Calendar.YEAR);
        mFromMonth = cal.get(Calendar.MONTH);
        mFromDay = cal.get(Calendar.DAY_OF_MONTH);
        mToYear = 2020;
        mToMonth = 11;
        mToDay = 31;
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

        Intent intent = getIntent();

        onNewIntent(getIntent());

    }

    protected void onNewIntent(Intent intent) {
        Log.d(TAG,"onNewIntent: ");
        if (intent != null )
        {
            processIntent(intent);
        }
        super.onNewIntent(intent);
    }

    private void processIntent(Intent intent) {
        Log.d(TAG,"processIntent: ");
        Sentence sentence = (Sentence)intent.getSerializableExtra("sentence");
        Log.d(TAG,"sentence: " + sentence);
        if(sentence != null) {
            id = sentence.getId();
            txtStartDate.setText(sentence.getStartDate());
            txtEndDate.setText(sentence.getEndDate());
            txtTime.setText(sentence.getPopupTime());
            txtRecord.setText("Success:" + sentence.getSucessCount() + " " +
                    "Fail:" + sentence.getFailCount() + " " +
                    "Skip:" + sentence.getSkipCount());
            editKeyword.setText(sentence.getKeyword());
            editKor.setText(sentence.getKorSentence());
            editEng.setText(sentence.getEngSentence());
        }
        else {
            UpdateNow();
            txtRecord.setText(" ");
        }
    }

    public void SetDate() {
        btnStartDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(InsertActivity.this, mFromDateSetListener, mFromYear,
                                mFromMonth, mFromDay).show();
                    }
                }
        );

        btnEndDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(InsertActivity.this, mToDateSetListener, mToYear,
                                mToMonth, mToDay).show();
                    }
                }
        );
    }


    public void SetTime() {
        btnSetTime.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new TimePickerDialog(InsertActivity.this, mTimeSetListener, mHour,
                                mMinute, false).show();
                    }
                }
        );
    }

    public void AddData() {
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(id == null) {
                            boolean isInserted = myDb.insertData(editKeyword.getText().toString(), editKor.getText().toString(),
                                    editEng.getText().toString(), txtStartDate.getText().toString(), txtEndDate.getText().toString(), txtTime.getText().toString());
                            if (isInserted == true)
                                Toast.makeText(InsertActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(InsertActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                        }
                        else {
                            boolean isUpdated = myDb.updateData(id, editKeyword.getText().toString(), editKor.getText().toString(),
                                    editEng.getText().toString(), txtStartDate.getText().toString(), txtEndDate.getText().toString(), txtTime.getText().toString());
                            if (isUpdated == true)
                                Toast.makeText(InsertActivity.this, "Data Updated", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(InsertActivity.this, "Data not Updated", Toast.LENGTH_LONG).show();

                        }
                        finish();
                        onBackPressed();
                    }
                }
        );
    }

    public void DeleteData() {
        btnDeleteData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(id == null) {
                                Toast.makeText(InsertActivity.this, "Data Nothing", Toast.LENGTH_LONG).show();
                        }
                        else {
                            boolean isDeleted = myDb.deleteData(id);
                            if (isDeleted == true)
                                Toast.makeText(InsertActivity.this, "Data Deleted", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(InsertActivity.this, "Data not Deleted", Toast.LENGTH_LONG).show();
                        }
                        finish();
                        onBackPressed();

                    }
                }
        );
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    DatePickerDialog.OnDateSetListener mFromDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mFromYear = year;
                    mFromMonth = month;
                    mFromDay = dayOfMonth;

                    UpdateNow();
                }
            };

    DatePickerDialog.OnDateSetListener mToDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mToYear = year;
                    mToMonth = month;
                    mToDay = dayOfMonth;

                    UpdateNow();
                }
            };

    TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mHour = hourOfDay;
                    mMinute = minute;

                    UpdateNow();
                }
            };

    void UpdateNow() {
        String hour;
        String minute;
        String fromYear;
        String toYear;
        String fromMonth;
        String toMonth;
        String fromDay;
        String toDay;

        fromYear = Integer.toString(mFromYear);
        toYear = Integer.toString(mToYear);

        if (mFromMonth < 10) {
            fromMonth = "0" + Integer.toString(mFromMonth+1);
        } else {
            fromMonth = Integer.toString(mFromMonth+1);
        }

        if (mToMonth < 10) {
            toMonth = "0" + Integer.toString(mToMonth);
        } else {
            toMonth = Integer.toString(mToMonth);
        }

        if (mFromDay < 10) {
            fromDay = "0" + Integer.toString(mFromDay);
        } else {
            fromDay = Integer.toString(mFromDay);
        }

        if (mToDay < 10) {
            toDay = "0" + Integer.toString(mToDay);
        } else {
            toDay = Integer.toString(mToDay);
        }

        if (mHour < 10) {
            hour = "0" + Integer.toString(mHour);
        } else {
            hour = Integer.toString(mHour);
        }

        if (mMinute < 10) {
            minute = "0" + Integer.toString(mMinute);
        } else {
            minute = Integer.toString(mMinute);
        }

        txtStartDate.setText(String.format("%s/%s/%s", fromYear,
                fromMonth, fromDay));
        txtEndDate.setText(String.format("%s/%s/%s", toYear,
                toMonth, toDay));
        txtTime.setText(String.format("%s:%s", hour, minute));
    }

}
