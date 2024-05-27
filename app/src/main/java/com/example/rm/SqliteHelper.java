package com.example.rm;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

// SQLite DB 생성
public class SqliteHelper extends SQLiteOpenHelper {

    // 테이블 열 이름 정의
    // 사용자
    public static final String TABLE_USER = "user";
    public static final String COLUMN_USER_ID = "u_id";
    public static final String COLUMN_USER_PW = "u_pw";
    public static final String COLUMN_USER_NICKNAME = "u_nickname";
    public static final String COLUMN_USER_EMAIL = "u_email";
    public static final String COLUMN_USER_LEVEL = "u_level";
    public static final String COLUMN_USER_STATE = "u_state";

    // 쓰레기
    public static final String TABLE_TRASH = "trash";
    public static final String COLUMN_TRASH_ID = "t_id";
    public static final String COLUMN_TRASH_CATEGORY = "t_type";
    public static final String COLUMN_TRASH_NAME = "t_name";
    public static final String COLUMN_TRASH_DESCRIPTION = "t_info";
    public static final String COLUMN_TRASH_IMAGE = "t_image";

    // 분리수거 위치
    public static final String TABLE_MAP = "map";
    public static final String COLUMN_MAP_NAME = "m_name";
    public static final String COLUMN_MAP_ADDRESS = "m_address";
    public static final String COLUMN_MAP_LATITUDE = "m_lat";   // 위도
    public static final String COLUMN_MAP_LONGITUDE = "m_lon";     // 경도

    // 게시글
    public static final String TABLE_POST = "post";
    public static final String COLUMN_POST_ID = "p_id";
    public static final String COLUMN_POST_TITLE = "p_title";
    public static final String COLUMN_POST_NICKNAME = "p_nickname";
    public static final String COLUMN_POST_CONTENT = "p_content";
    public static final String COLUMN_POST_IMAGE = "p_image";
    public static final String COLUMN_POST_HASH = "p_hash";
    public static final String COLUMN_POST_CREATE_DATE = "p_create";
    public static final String COLUMN_POST_UPDATE_DATE = "p_update";
    public static final String COLUMN_POST_DELETE_DATE = "p_delete";

    // 댓글
    public static final String TABLE_COMMENT = "comment";
    public static final String COLUMN_COMMENT_ID = "c_id";
    public static final String COLUMN_COMMENT_NICKNAME = "c_nickname";
    public static final String COLUMN_COMMENT_CONTENT = "c_content";
    public static final String COLUMN_COMMENT_HASH = "c_hash";
    public static final String COLUMN_COMMENT_CREATE_DATE = "c_create";
    public static final String COLUMN_COMMENT_UPDATE_DATE = "c_update";
    public static final String COLUMN_COMMENT_DELETE_DATE = "c_delete";


    // 테이블 생성 SQL문 (속성 타입, 기본 키 값 확인 필요)
    // 사용자 테이블
    public static final String SQL_CREATE_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + " (" +
            COLUMN_USER_ID + " TEXT PRIMARY KEY, " +
            COLUMN_USER_PW + " TEXT NOT NULL, " +
            COLUMN_USER_NICKNAME + " TEXT NOT NULL, " +
            COLUMN_USER_EMAIL + " TEXT NOT NULL, " +
            COLUMN_USER_LEVEL + " TEXT, " +
            COLUMN_USER_STATE + " TEXT)";

    // 쓰레기 테이블
    public static final String SQL_CREATE_TRASH = "CREATE TABLE IF NOT EXISTS " + TABLE_TRASH + " (" +
            COLUMN_TRASH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TRASH_CATEGORY + " TEXT, " +
            COLUMN_TRASH_NAME + " TEXT, " +
            COLUMN_TRASH_DESCRIPTION + " TEXT, " +
            COLUMN_TRASH_IMAGE + " TEXT)";

    // 분리수거 위치 테이블
    public static final String SQL_CREATE_MAP = "CREATE TABLE IF NOT EXISTS " + TABLE_MAP + " (" +
            COLUMN_MAP_NAME + " TEXT PRIMARY KEY, " +
            COLUMN_MAP_ADDRESS + " TEXT, " +
            COLUMN_MAP_LATITUDE + " REAL, " +
            COLUMN_MAP_LONGITUDE + " REAL)";

    // 게시글 테이블
    public static final String SQL_CREATE_POST = "CREATE TABLE IF NOT EXISTS " + TABLE_POST + " (" +
            COLUMN_POST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_POST_TITLE + " TEXT, " +
            COLUMN_POST_NICKNAME + " TEXT, " +
            COLUMN_POST_CONTENT + " TEXT, " +
            COLUMN_POST_IMAGE + " TEXT, " +
            COLUMN_POST_HASH + " TEXT, " +
            COLUMN_POST_CREATE_DATE + " DATETIME, " +
            COLUMN_POST_UPDATE_DATE + " DATETIME, " +
            COLUMN_POST_DELETE_DATE + " DATETIME)";

    // 댓글 테이블
    public static final String SQL_CREATE_COMMENT = "CREATE TABLE IF NOT EXISTS " + TABLE_COMMENT + " (" +
            COLUMN_COMMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_COMMENT_NICKNAME + " TEXT, " +
            COLUMN_COMMENT_CONTENT + " TEXT, " +
            COLUMN_COMMENT_HASH + " TEXT, " +
            COLUMN_COMMENT_CREATE_DATE + " DATETIME, " +
            COLUMN_COMMENT_UPDATE_DATE + " DATETIME, " +
            COLUMN_COMMENT_DELETE_DATE + " DATETIME)";


    // 테이블 조회 SQL문
    public static final String SQL_SELECT_USER = "SELECT * FROM user";


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SQLite.db";  // DB 파일 이름

    public SqliteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  // DB 생성/관리 객체 생성 : DB 경로 찾기, DB 파일(메모리 내 DB이면 null), CursorFactory 객체 생성(기본값 null), DB 번호(1부터 시작)
    }

    @Override
    public void onCreate(SQLiteDatabase db) {   // DB가 처음 생성될 때 호출됨 (=테이블 생성)
        db.execSQL(SQL_CREATE_USER);
        db.execSQL(SQL_CREATE_TRASH);
        db.execSQL(SQL_CREATE_MAP);
        db.execSQL(SQL_CREATE_POST);
        db.execSQL(SQL_CREATE_COMMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  // DB 버전이 업그레이드 되었을 때 실행
        onCreate(db);   // 새 테이블 생성
    }

    // 회원가입 시 사용자의 id/pw/email/name을 SQLite DB에 저장
    public Boolean insertData(String id, String password, String email, String nickname){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase(); // SQLite DB 인스턴스를 쓰기 가능 모드로 열어줌 (-> DB에 데이터 추가/수정/삭제 가능)
        ContentValues values = new ContentValues();     // DB 행에 삽입할 데이터 보관, key-value 구조(DB 열 이름 - 삽입할 데이터)

        values.put(COLUMN_USER_ID, id);
        values.put(COLUMN_USER_PW, password);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_NICKNAME, nickname);
        values.put(COLUMN_USER_LEVEL, "초급자");


        // 새 행(레코드) 추가 (데이터를 삽입할 테이블명, - , 삽입할 데이터(key-value)), 반환값 long
        long result = sqLiteDatabase.insert("user", null, values);
        if (result == -1) {
            // 추가 실패
            Log.e("sqliteHelper.insertData", "회원정보 추가 실패");
            sqLiteDatabase.close();
            return false;
        }
        else {
            // 추가 성공
            Log.i("sqliteHelper.insertData", "user 테이블에 회원정보 추가 성공, 아이디 : " + id + ", 비밀번호: " + password + ", 닉네임: " + nickname + ", 이메일: " + email);
            sqLiteDatabase.close();
            return true;
        }
    }

    // 닉네임 중복 확인, 탈퇴 시 비번 조회, 회원가입 아이디 중복 확인, 로그인 아이디 확인, 아이디 찾기
    // 사용자 아이디가 맞는지 검사 (USER 테이블에 id가 존재하는지 확인 -> getCount()로 일치한 행의 개수가 1개라도 있으면 true/없으면 false)
    public Boolean findAccount(String userinfo, String key){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = null;

        // key에 저장된 문자열이 nickname일 때 실행
        if (key.equals("nickname")) {
            cursor = sqLiteDatabase.rawQuery("Select * from user where u_nickname=?", new String[]{userinfo});
        }
        else if (key.equals("password")) {
            cursor = sqLiteDatabase.rawQuery("Select * from user where u_pw=?", new String[]{userinfo});
        }
        else if (key.equals("id")) {
            cursor = sqLiteDatabase.rawQuery("Select * from user where u_id=?", new String[]{userinfo});
        }
        else
            return false;


        if(cursor.getCount() > 0){
            sqLiteDatabase.close();
            cursor.close();
            return true;
        }
        else {
            sqLiteDatabase.close();
            cursor.close();
            return false;
        }
    }

    // 사용자 아아디, 비밀번호가 맞는지 검사
    public Boolean checkIdPassword(String id, String password) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from user where u_id=? and u_pw=?", new String[] {id, password});

        if (cursor.getCount()>0) {
            sqLiteDatabase.close();
            cursor.close();
            return true;
        } else {
            sqLiteDatabase.close();
            cursor.close();
            return false;
        }
    }

    // 이메일을 이용해 아이디 찾기
    public String checkEmail(String email){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select u_id from user where u_email=?", new String[]{email});

        if(cursor.moveToFirst()){
            @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID));
            sqLiteDatabase.close();
            cursor.close();
            return id;
        }else {
            sqLiteDatabase.close();
            cursor.close();
            return null;
        }
    }

    // 임시 비번 저장
    public void updatePassword(String id, String tempPw){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("update user set u_pw=? where u_id=?", new String[]{tempPw, id});

        Log.i("Sqlite.updatePw", "임시 비번 해시값 user 테이블에 저장됨 : " +  tempPw);
        sqLiteDatabase.close();
    }

    // DB에 저장된 계정 삭제하기
    public void delAccount(String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from user where u_id=?", new String[]{id});

        Log.i("Sqlite.delAccount", "계정 삭제됨 : " +  id);
        sqLiteDatabase.close();
    }

    // 아이디를 기반으로 사용자의 닉네임과 등급 가져오기
    public Map<String, String> getUserInfo(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Map<String, String> userInfo = new HashMap<>();
        Cursor cursor = db.rawQuery("select u_nickname, u_level from user where u_id = ?", new String[]{userId}); // 입력받은 userId값이 ?으로 전달됨

        // 쿼리 결과 확인
        if (cursor.moveToFirst()) {     // moveToFirst()는 쿼리 결과 집합 중 1번째 레코드(튜플)를 가리키는 포인터임, 계속 이동할 레코드가 있으면 true/없으면 false 반환
            @SuppressLint("Range") String nickname = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NICKNAME));
            @SuppressLint("Range") String level = cursor.getString(cursor.getColumnIndex(COLUMN_USER_LEVEL));
            userInfo.put("nickname", nickname);
            userInfo.put("level", level);

            Log.i("Sqlite.getUserInfo", "저장된 로그인 정보 가져오기      닉네임: " + nickname + ", 등급: " + level);
        }

        // 리소스 해제
        cursor.close();
        db.close();

        return userInfo;
    }




}

