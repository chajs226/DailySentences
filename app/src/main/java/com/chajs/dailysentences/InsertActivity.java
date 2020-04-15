package com.chajs.dailysentences;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class InsertActivity extends AppCompatActivity {

    int mFromYear, mFromMonth, mFromDay, mToYear, mToMonth, mToDay, mHour, mMinute;

    DatabaseHelper myDb = null;
    EditText editKor, editEng;
    Button btnAddData;
    boolean tableExists = false;
    String id = null;

    Button btnStartDate;
    TextView txtStartDate;

    Button btnEndDate;
    TextView txtEndDate;

    Button btnSetTime;
    TextView txtTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        myDb = new DatabaseHelper(this);

        editKor = (EditText)findViewById(R.id.editTextKor);
        editEng = (EditText)findViewById(R.id.editTextEng);
        btnAddData = (Button)findViewById(R.id.buttonAdd);

        btnStartDate = (Button)findViewById(R.id.buttonStartDate);
        txtStartDate = (TextView)findViewById(R.id.textViewStartDate);

        btnEndDate = (Button)findViewById(R.id.buttonEndDate);
        txtEndDate = (TextView)findViewById(R.id.textViewEndDate);

        btnSetTime = (Button)findViewById(R.id.buttonTime);
        txtTime = (TextView)findViewById(R.id.textViewTime);
        AddData();
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

        UpdateNow();
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
                            boolean isInserted = myDb.insertData(editKor.getText().toString(),
                                    editEng.getText().toString(), txtStartDate.getText().toString(), txtEndDate.getText().toString(), txtTime.getText().toString());
                            if (isInserted == true)
                                Toast.makeText(InsertActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(InsertActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                        }
                        else {
                            boolean isUpdated = myDb.updateData(id, editKor.getText().toString(),
                                    editEng.getText().toString(), txtStartDate.getText().toString(), txtEndDate.getText().toString(), txtTime.getText().toString());
                            if (isUpdated == true)
                                Toast.makeText(InsertActivity.this, "Data Updated", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(InsertActivity.this, "Data not Updated", Toast.LENGTH_LONG).show();

                        }
                        finish();
                    }
                }
        );
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
