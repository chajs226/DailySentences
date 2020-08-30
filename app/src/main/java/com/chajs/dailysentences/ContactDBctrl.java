package com.chajs.dailysentences;

public class ContactDBctrl {
    private ContactDBctrl(){ }

    public static final String TABLE_NAME = "DAILYENG";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "KEYWORD";
    public static final String COL_3 = "KOR_SENTENCE";
    public static final String COL_4 = "ENG_SENTENCE";
    public static final String COL_5 = "START_DATE";
    public static final String COL_6 = "END_DATE";
    public static final String COL_7 = "POPUP_TIME";
    public static final String COL_8 = "SUCCESS_COUNT";
    public static final String COL_9 = "FAIL_COUNT";
    public static final String COL_10 = "SKIP_COUNT";

    public static final String SET_TABLE_NAME = "SETTINGS";
    public static final String STAT_HISTORY_TABLE_NAME = "STATHIS";

    public static final String SQL_CREATE_TB_MYSENTENCE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME  + " " +
            "(" +
            COL_1 +     " INTEGER PRIMARY KEY AUTOINCREMENT"  + ", " +
            COL_2 +     " TEXT"                                  + ", " +
            COL_3 +     " TEXT"                                  + ", " +
            COL_4 +     " TEXT"                                  + ", " +
            COL_5 +     " TEXT"                                  + ", " +
            COL_6 +     " TEXT"                                  + ", " +
            COL_7 +     " TEXT"                                  + ", " +
            COL_8 +     " INTEGER"                               + ", " +
            COL_9 +     " INTEGER"                               + ", " +
            COL_10 +     " INTEGER"                               +
            ")";

    public static final String SQL_CREATE_TB_SETTINGS = "CREATE TABLE IF NOT EXISTS " + SET_TABLE_NAME + " " +
            "(" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "AUTOARLAM_YN TEXT, " +
            "FROM_TIME TEXT, " +
            "TO_TIME TEXT, " +
            "DAILY_ARLAM_COUNT INTEGER, " +
            "USING_SERVER_YN TEXT, " +
            "EMAIL TEXT" +
            ")";

    public static final String SQL_CREATE_TB_STATHIS = "CREATE TABLE IF NOT EXISTS " + STAT_HISTORY_TABLE_NAME + " " +
            "(" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "STAT_DATE TEXT NOT NULL, " +
            "SUM_SUCCESS INTEGER, " +
            "SUM_FAIL INTEGER, " +
            "SUM_SKIP INTEGER, " +
            "SUM_POINT INTEGER, " +
            "AVG_POINT INTEGER, " +
            "EMAIL TEXT" +
            ")";

    // DROP TABLE IF EXISTS CONTACT_T
    public static final String SQL_DROP_TB_MYSENTENCE = "DROP TABLE IF EXISTS " + TABLE_NAME ;

    // DROP TABLE IF EXISTS CONTACT_T
    public static final String SQL_DROP_TB_SETTINGS = "DROP TABLE IF EXISTS " + SET_TABLE_NAME ;

    public static final String SQL_DROP_TB_STATHIS = "DROP TABLE IF EXISTS " + STAT_HISTORY_TABLE_NAME;
    // 테이블 존재 유무 확인
    public static final String SQL_CHECK_EXISTS_TABLE = "SELECT * FROM SQLITE_MASTER WHERE Name = " + TABLE_NAME;

    public static final String SQL_SELECT_ALL_DATA = "SELECT ID, KEYWORD, KOR_SENTENCE, ENG_SENTENCE, START_DATE, END_DATE, POPUP_TIME, IFNULL(SUCCESS_COUNT,'0'), IFNULL(FAIL_COUNT,'0'), IFNULL(SKIP_COUNT,'0'), IFNULL(SUCCESS_COUNT,'0')*5 + IFNULL(FAIL_COUNT,'0')*-1 + IFNULL(SKIP_COUNT,'0')*-3 AS 'RATI'  FROM " + TABLE_NAME + " ORDER BY RATI DESC";
    //Noti 시간
    public static final String SQL_SELECT_NOTI_TIME = "SELECT ID, POPUP_TIME FROM " + TABLE_NAME + " WHERE strftime('%Y/%m/%d', 'now', 'localtime') BETWEEN START_DATE AND END_DATE";

    //현재 시간 기준 popup_time 10분 이내의 것들을 조회
    public static final String SQL_SELECT_NOTI_DATA = "SELECT ID, KEYWORD, KOR_SENTENCE, ENG_SENTENCE, START_DATE, END_DATE, POPUP_TIME, IFNULL(SUCCESS_COUNT,'0'), IFNULL(FAIL_COUNT,'0'), IFNULL(SKIP_COUNT,'0'), IFNULL(SUCCESS_COUNT,'0')*5 + IFNULL(FAIL_COUNT,'0')*-1 + IFNULL(SKIP_COUNT,'0')*-3 AS 'RATI'  FROM " + TABLE_NAME +
            " WHERE (strftime('%H','now','localtime')*60 + strftime('%M','now','localtime')) - (strftime('%H'," + COL_6 + ")*60 + strftime('%M'," + COL_6 + ")) BETWEEN 0 AND 10 ORDER BY POPUP_TIME ASC";

    //ID 로 문장 조회
    public static final String SQL_SELECT_DATA_FROM_ID = "SELECT ID, KEYWORD, KOR_SENTENCE, ENG_SENTENCE, START_DATE, END_DATE, POPUP_TIME, IFNULL(SUCCESS_COUNT,'0'), IFNULL(FAIL_COUNT,'0'), IFNULL(SKIP_COUNT,'0'), IFNULL(SUCCESS_COUNT,'0')*5 + IFNULL(FAIL_COUNT,'0')*-1 + IFNULL(SKIP_COUNT,'0')*-3 AS 'RATI'  FROM " + TABLE_NAME +
            " WHERE ID = ?";

    //전체 문장 기록, 합계
    public static final String SQL_SELECT_STATICS_SUM = "SELECT IFNULL(SUM(SUCCESS_COUNT), 0), IFNULL(SUM(FAIL_COUNT),0), IFNULL(SUM(SKIP_COUNT),0), " +
            "IFNULL(SUM(SUCCESS_COUNT), 0)*5 + IFNULL(SUM(FAIL_COUNT),0)*-1 + IFNULL(SUM(SKIP_COUNT),0)*-3 AS 'SUM', " +
            "(IFNULL(SUM(SUCCESS_COUNT), 0)*5 + IFNULL(SUM(FAIL_COUNT),0)*-1 + IFNULL(SUM(SKIP_COUNT),0)*-3)/COUNT(*) AS 'AVER' FROM " + TABLE_NAME;

    //통계 데이터가 있는지 확인
    public static final String SQL_SELECT_TODATE_STATICS = "SELECT ID FROM " + STAT_HISTORY_TABLE_NAME +
            " WHERE STAT_DATE = ?";

    //통계 데이터 로드
    public static final String SQL_SELECT_STATICS_FOR_GRAPH = "SELECT STAT_DATE, AVG_POINT FROM " + STAT_HISTORY_TABLE_NAME +
            " WHERE STAT_DATE BETWEEN ? AND strftime('%Y%m%d','now','localtime')";

    public static final String SQL_SELECT_SETTINGS_DATA = "SELECT ID, AUTOARLAM_YN, FROM_TIME, TO_TIME, DAILY_ARLAM_COUNT, USING_SERVER_YN, EMAIL FROM " + SET_TABLE_NAME;
    // SELECT * FROM CONTACT_T
    //public static final String SQL_SELECT_TB_MYSENTENCE = "SELECT * FROM " + TABLE_NAME ;

    // INSERT OR REPLACE INTO CONTACT_T (NO, NAME, PHONE, OVER20) VALUES (x, x, x, x)
    //public static final String SQL_INSERT_TB_MYSENTENCE = "INSERT OR REPLACE INTO " + TABLE_NAME + " " +
    //        "(" + COL_NO + ", " + COL_NAME + ", " + COL_PHONE + ", " + COL_OVER20 + ") VALUES " ;

    // DELETE FROM CONTACT_T
    //public static final String SQL_DELETE = "DELETE FROM " + TBL_CONTACT ;
}
