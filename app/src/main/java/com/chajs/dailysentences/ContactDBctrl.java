package com.chajs.dailysentences;

public class ContactDBctrl {
    private ContactDBctrl(){ }

    public static final String TABLE_NAME = "DAILYENG";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "KOR_SENTENCE";
    public static final String COL_3 = "ENG_SENTENCE";
    public static final String COL_4 = "START_DATE";
    public static final String COL_5 = "END_DATE";
    public static final String COL_6 = "POPUP_TIME";
    public static final String COL_7 = "SUCCESS_COUNT";
    public static final String COL_8 = "FAIL_COUNT";
    public static final String COL_9 = "SKIP_COUNT";

    public static final String SQL_CREATE_TB_MYSENTENCE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME  + " " +
            "(" +
            COL_1 +     " INTEGER PRIMARY KEY AUTOINCREMENT"  + ", " +
            COL_2 +     " TEXT"                                  + ", " +
            COL_3 +     " TEXT"                                  + ", " +
            COL_4 +     " TEXT"                                  + ", " +
            COL_5 +     " TEXT"                                  + ", " +
            COL_6 +     " TEXT"                                  + ", " +
            COL_7 +     " INTEGER"                               + ", " +
            COL_8 +     " INTEGER"                               + ", " +
            COL_9 +     " INTEGER"                               +
            ")";

    // DROP TABLE IF EXISTS CONTACT_T
    public static final String SQL_DROP_TB_MYSENTENCE = "DROP TABLE IF EXISTS " + TABLE_NAME ;

    // 테이블 존재 유무 확인
    public static final String SQL_CHECK_EXISTS_TABLE = "SELECT * FROM SQLITE_MASTER WHERE Name = " + TABLE_NAME;

    public static final String SQL_SELECT_ALL_DATA = "SELECT ID, KOR_SENTENCE, ENG_SENTENCE, START_DATE, END_DATE, POPUP_TIME, IFNULL(SUCCESS_COUNT,'0'), IFNULL(FAIL_COUNT,'0'), IFNULL(SKIP_COUNT,'0') FROM " + TABLE_NAME;

    //현재 시간 기준 popup_time 10분 이내의 것들을 조회
    public static final String SQL_SELECT_NOTI_DATA = "SELECT ID, KOR_SENTENCE, ENG_SENTENCE, START_DATE, END_DATE, POPUP_TIME, IFNULL(SUCCESS_COUNT,'0'), IFNULL(FAIL_COUNT,'0'), IFNULL(SKIP_COUNT,'0') FROM " + TABLE_NAME +
            " WHERE (strftime('%H','now','localtime')*60 + strftime('%M','now','localtime')) - (strftime('%H'," + COL_6 + ")*60 + strftime('%M'," + COL_6 + ")) BETWEEN 0 AND 10";

    // SELECT * FROM CONTACT_T
    //public static final String SQL_SELECT_TB_MYSENTENCE = "SELECT * FROM " + TABLE_NAME ;

    // INSERT OR REPLACE INTO CONTACT_T (NO, NAME, PHONE, OVER20) VALUES (x, x, x, x)
    //public static final String SQL_INSERT_TB_MYSENTENCE = "INSERT OR REPLACE INTO " + TABLE_NAME + " " +
    //        "(" + COL_NO + ", " + COL_NAME + ", " + COL_PHONE + ", " + COL_OVER20 + ") VALUES " ;

    // DELETE FROM CONTACT_T
    //public static final String SQL_DELETE = "DELETE FROM " + TBL_CONTACT ;
}
