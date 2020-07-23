package com.chajs.dailysentences;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SettingsActivity extends AppCompatActivity {

    ToggleButton ToBtnAlarmSet;
    Button BtnFromT;
    Button BtnToT;
    Button BtnSaveSettings;
    TextView TextViewFromT;
    TextView TextViewToT;
    TextView TextViewArlamT;
    TextView TextViewCount;
    TextView TextViewHoi;
    EditText EditTextAlarmCount;
    int mHour, mMinute;

    Settings settings;
    DatabaseHelper myDb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        myDb = new DatabaseHelper(this);

        ToBtnAlarmSet = (ToggleButton) findViewById(R.id.toggleButtonAlarm);
        BtnFromT = (Button) findViewById(R.id.buttonFromT);
        BtnToT = (Button) findViewById(R.id.buttonToT);
        BtnSaveSettings = (Button) findViewById(R.id.buttonSave);
        TextViewFromT = (TextView)findViewById(R.id.textViewFromT);
        TextViewToT = (TextView)findViewById(R.id.textViewToT);
        TextViewArlamT = (TextView)findViewById(R.id.textViewArlamTime) ;
        TextViewCount = (TextView)findViewById(R.id.textViewCount);
        TextViewHoi = (TextView)findViewById(R.id.textViewHoi);
        EditTextAlarmCount = (EditText)findViewById(R.id.editTextCount);


        SetAutoArlam();
        SaveSettings();

        //현재 날짜, 시간 가져오기
        Calendar cal = new GregorianCalendar();
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);

        ToBtnAlarmSet.setOnCheckedChangeListener(( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    InitControls(isChecked);
            }
        }));

        LoadSettingsData();

    }

    private void InitControls(Boolean isChecked) {
        Log.d("aaa","initContrlos");
        if(isChecked) {
            BtnFromT.setVisibility(View.VISIBLE);
            TextViewFromT.setVisibility(View.VISIBLE);
            BtnToT.setVisibility(View.VISIBLE);
            TextViewToT.setVisibility(View.VISIBLE);
            TextViewArlamT.setVisibility(View.VISIBLE);
            TextViewCount.setVisibility(View.VISIBLE);
            TextViewHoi.setVisibility(View.VISIBLE);
            EditTextAlarmCount.setVisibility(View.VISIBLE);
        }
        else {
            BtnFromT.setVisibility(View.INVISIBLE);
            TextViewFromT.setVisibility(View.INVISIBLE);
            BtnToT.setVisibility(View.INVISIBLE);
            TextViewToT.setVisibility(View.INVISIBLE);
            TextViewArlamT.setVisibility(View.INVISIBLE);
            TextViewCount.setVisibility(View.INVISIBLE);
            TextViewHoi.setVisibility(View.INVISIBLE);
            EditTextAlarmCount.setVisibility(View.INVISIBLE);

            TextViewFromT.setText(" ");
            TextViewToT.setText(" ");
            EditTextAlarmCount.setText(" ");
        }

    }

    private void SetAutoArlam() {
        BtnFromT.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new TimePickerDialog(SettingsActivity.this, mFromTimeSetListener, mHour,
                                mMinute, false).show();
                    }
                }
        );

        BtnToT.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new TimePickerDialog(SettingsActivity.this, mToTimeSetListener, mHour,
                                mMinute, false).show();
                    }
                }
        );
    }

    TimePickerDialog.OnTimeSetListener mFromTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mHour = hourOfDay;
                    mMinute = minute;

                    UpdateFromTimeToTime("From");
                }
            };

    TimePickerDialog.OnTimeSetListener mToTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    mHour = hourOfDay;
                    mMinute = minute;

                    UpdateFromTimeToTime("To");
                }
            };

    private void UpdateFromTimeToTime(String FromTo) {
        String hour;
        String minute;

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

        if(FromTo.equals("From")) {
            TextViewFromT.setText(String.format("%s:%s", hour, minute));
        }
        else {
            TextViewToT.setText(String.format("%s:%s", hour, minute));
        }

    }

    /*
    ToButtonAlarmSet = (ToggleButton) findViewById(R.id.toggleButtonAlarm);
        ButtonFromT = (Button) findViewById(R.id.buttonFromT);
        ButtonToT = (Button) findViewById(R.id.buttonToT);
        ButtonSaveSettings = (Button) findViewById(R.id.buttonSettings);
        TextViewFromT = (TextView)findViewById(R.id.textViewFromT);
        TextViewToT = (TextView)findViewById(R.id.textViewToT);
        TextViewArlamT = (TextView)findViewById(R.id.textViewArlamTime) ;
     */
    public void SaveSettings() {
        BtnSaveSettings.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String isChecked;
                        String fromT;
                        String toT;
                        Integer count;

                        if (!ToBtnAlarmSet.isChecked()) {
                            isChecked = "N";
                            fromT = "";
                            toT = "";
                            count = 0;
                        } else {
                            isChecked = "Y";
                            if(validateData()) {
                                fromT = TextViewFromT.getText().toString();
                                toT = TextViewToT.getText().toString();
                                count = Integer.valueOf(EditTextAlarmCount.getText().toString().trim());
                            }
                            else {
                                Toast.makeText(SettingsActivity.this, "입력되지 않은 값이 있습니다.", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }


                        if(settings.getId() == null) {
                            boolean isInserted = myDb.saveSettingData(isChecked, fromT, toT, count,"N", " ");
                            if (isInserted == true)
                                Toast.makeText(SettingsActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(SettingsActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                        }
                        else {
                            boolean isUpdated = myDb.updateSettingDataLocal(settings.getId().toString(), isChecked, fromT, toT, count,"N", " ");
                            if (isUpdated == true)
                                Toast.makeText(SettingsActivity.this, "Data Updated", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(SettingsActivity.this, "Data not Updated", Toast.LENGTH_LONG).show();
                        }

                        finish();
                        onBackPressed();
                    }
                }
        );
    }

    private boolean validateData() {

        if(TextViewFromT.getText().toString().trim().length() == 0 || TextViewToT.getText().toString().trim().length() == 0 || EditTextAlarmCount.getText().toString().trim().length() == 0)
            return false;
        else
            return  true;
    }

    private void LoadSettingsData() {

        Log.d("aaaa","startLoadSettings");
        Cursor res = myDb.getSettingData();
        if(res.getCount() == 0) {
            boolean isInserted = myDb.saveSettingData("N", " ", " ", 0,"N", " ");
            if(isInserted) {
                Cursor res1 = myDb.getSettingData();
                while (res1.moveToNext()) {
                    settings = new Settings(res1.getString(0),
                            res1.getString(1), res1.getString(2),
                            res1.getString(3), res1.getString(4),
                            res1.getString(5), res1.getString(6));
                }
            }
        }
        else {
            while (res.moveToNext()) {
                settings = new Settings(res.getString(0),
                        res.getString(1), res.getString(2),
                        res.getString(3), res.getString(4),
                        res.getString(5), res.getString(6));
            }
        }

        Log.d("aaa",settings.getFromTime());

        if(settings.getAutoArlamYn().equals("Y")) {
            ToBtnAlarmSet.setChecked(true);
            InitControls((true));

            TextViewFromT.setText(settings.getFromTime());
            TextViewToT.setText(settings.getToTIme());
            EditTextAlarmCount.setText(settings.getDailyArlamCount());
        }
        else {
            ToBtnAlarmSet.setChecked(false);
            InitControls(false);
        }
        //InitControls(ToBtnAlarmSet.isChecked());
    }


}
