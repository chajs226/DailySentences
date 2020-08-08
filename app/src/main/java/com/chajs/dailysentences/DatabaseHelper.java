package com.chajs.dailysentences;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLInput;

import androidx.annotation.Nullable;

import static com.chajs.dailysentences.ContactDBctrl.COL_1;
import static com.chajs.dailysentences.ContactDBctrl.COL_2;
import static com.chajs.dailysentences.ContactDBctrl.COL_3;
import static com.chajs.dailysentences.ContactDBctrl.COL_4;
import static com.chajs.dailysentences.ContactDBctrl.COL_5;
import static com.chajs.dailysentences.ContactDBctrl.COL_6;
import static com.chajs.dailysentences.ContactDBctrl.COL_7;
import static com.chajs.dailysentences.ContactDBctrl.COL_8;
import static com.chajs.dailysentences.ContactDBctrl.COL_9;
import static com.chajs.dailysentences.ContactDBctrl.COL_10;
import static com.chajs.dailysentences.ContactDBctrl.TABLE_NAME;
import static com.chajs.dailysentences.ContactDBctrl.SET_TABLE_NAME;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Mysentences.db";
    public static final int DB_VERSION = 6;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("DatabaseHelper: ","database open");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("onCreate Before: ","before");
        db.execSQL(ContactDBctrl.SQL_CREATE_TB_MYSENTENCE);
        db.execSQL(ContactDBctrl.SQL_CREATE_TB_SETTINGS);
        Log.d("onCreate After: ","after");
    }

    public boolean CheckExistsTable() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery(ContactDBctrl.SQL_CHECK_EXISTS_TABLE,null);
        if (res.getCount() == 1)
            return true;
        else
            return false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ContactDBctrl.SQL_DROP_TB_MYSENTENCE);
        db.execSQL(ContactDBctrl.SQL_DROP_TB_SETTINGS);
        onCreate(db);
    }

    public boolean insertData(String keyword, String kor_sentence, String eng_sentence, String start_date, String end_date, String popup_time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, keyword);
        contentValues.put(COL_3, kor_sentence);
        contentValues.put(COL_4, eng_sentence);
        contentValues.put(COL_5, start_date);
        contentValues.put(COL_6, end_date);
        contentValues.put(COL_7, popup_time);
        Log.d("insertData Before: ","before");
        long result = db.insert(TABLE_NAME, null, contentValues);
        Log.d("insertData After: ","after");
        Log.d("result: ", String.valueOf(result));
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(ContactDBctrl.SQL_SELECT_ALL_DATA,null);
        return res;
    }

    public Cursor getNotiTime() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(ContactDBctrl.SQL_SELECT_NOTI_TIME, null);
        return res;
    }

    public Cursor getNotiDataForAlaram(String[] id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(ContactDBctrl.SQL_SELECT_DATA_FROM_ID, id);
        return res;
    }

    public Cursor getNotiData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(ContactDBctrl.SQL_SELECT_NOTI_DATA, null);
        return res;
    }

    public boolean updateData(String id, String keyword, String kor_sentence, String eng_sentence, String start_date, String end_date, String popup_time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, keyword);
        contentValues.put(COL_3, kor_sentence);
        contentValues.put(COL_4, eng_sentence);
        contentValues.put(COL_5, start_date);
        contentValues.put(COL_6, end_date);
        contentValues.put(COL_7, popup_time);
        db.update(TABLE_NAME, contentValues, "id = ?", new String[] {id});
        return true;
    }

    public boolean deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?", new String[] {id});
        return true;
    }

    public boolean updateRecord(String id, String recordType, Integer recordUpdate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (recordType == "S") {
            contentValues.put(COL_1, id);
            contentValues.put(COL_8, recordUpdate);
            db.update(TABLE_NAME, contentValues, "id = ?", new String[] {id});
        }
        else if (recordType == "F") {
            contentValues.put(COL_1, id);
            contentValues.put(COL_9, recordUpdate);
            db.update(TABLE_NAME, contentValues, "id = ?", new String[] {id});
        }
        else if (recordType == "K") {
            contentValues.put(COL_1, id);
            contentValues.put(COL_10, recordUpdate);
            db.update(TABLE_NAME, contentValues, "id = ?", new String[] {id});
        }
        return true;
    }

    public boolean saveSettingData(String AutoArlam_yn, String fromTime, String toTime, Integer dailyAlramCount, String usingServer, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("AUTOARLAM_YN", AutoArlam_yn);
        contentValues.put("FROM_TIME", fromTime);
        contentValues.put("TO_TIME", toTime);
        contentValues.put("DAILY_ARLAM_COUNT", dailyAlramCount);
        contentValues.put("USING_SERVER_YN", usingServer);
        contentValues.put("EMAIL", email);
        Log.d("insertData Before: ","before");
        long result = db.insert(SET_TABLE_NAME, null, contentValues);
        Log.d("insertData After: ","after");
        Log.d("result: ", String.valueOf(result));
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean updateSettingDataLocal(String id, String AutoArlam_yn, String fromTime, String toTime, Integer dailyAlramCount, String usingServer, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("AUTOARLAM_YN", AutoArlam_yn);
        contentValues.put("FROM_TIME", fromTime);
        contentValues.put("TO_TIME", toTime);
        contentValues.put("DAILY_ARLAM_COUNT", dailyAlramCount);
        contentValues.put("USING_SERVER_YN", usingServer);
        contentValues.put("EMAIL", email);
        db.update(SET_TABLE_NAME, contentValues, "id = ?", new String[] {id});
        return true;
    }

    public Cursor getSettingData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(ContactDBctrl.SQL_SELECT_SETTINGS_DATA,null);
        return res;
    }
}
