package com.example.rm;

import static java.security.AccessController.getContext;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

// SQLite DB 생성
public class SqliteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SQLite_DB.db";  // DB 파일 이름

    public SqliteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  // DB 생성/관리 객체 생성 : DB 경로 찾기, DB 파일(메모리 내 DB이면 null), CursorFactory 객체 생성(기본값 null), DB 번호(1부터 시작)
    }

    @Override
    public void onCreate(SQLiteDatabase db) {   // DB가 처음 생성될 때 호출됨 (=테이블 생성)
        db.execSQL(Table.SQL_CREATE_USER);
        db.execSQL(Table.SQL_CREATE_TRASH);
        db.execSQL(Table.SQL_CREATE_MAP);
        db.execSQL(Table.SQL_CREATE_POST);
        db.execSQL(Table.SQL_CREATE_COMMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  // DB 버전이 업그레이드 되었을 때 실행
        onCreate(db);   // 새 테이블 생성
    }

    // 회원가입 시 사용자의 id/pw/email/name을 SQLite DB에 저장
    public Boolean insertData(String id, String password, String email, String nickname){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase(); // SQLite DB 인스턴스를 쓰기 가능 모드로 열어줌 (-> DB에 데이터 추가/수정/삭제 가능)
        ContentValues values = new ContentValues();     // DB 행에 삽입할 데이터 보관, key-value 구조(DB 열 이름 - 삽입할 데이터)

        values.put(Table.COLUMN_USER_ID, id);
        values.put(Table.COLUMN_USER_PW, password);
        values.put(Table.COLUMN_USER_EMAIL, email);
        values.put(Table.COLUMN_USER_NICKNAME, nickname);

        // 새 행(레코드) 추가 (데이터를 삽입할 테이블명, - , 삽입할 데이터(key-value)), 반환값 long
        long result = sqLiteDatabase.insert("USER", null, values);
        if (result == -1) {
            // 추가 실패
            Log.e("Database", "DB 추가 실패");
            return false;
        }
        else {
            // 추가 성공
            Log.e("Database", "DB 추가 성공 : " + result);
            return true;
        }
    }

    // 회원가입 아이디 중복 확인, 로그인 아이디 확인, 아이디 찾기
    // 사용자 아이디가 맞는지 검사 (USER 테이블에 id가 존재하는지 확인 -> getCount()로 일치한 행의 개수가 1개라도 있으면 true/없으면 false)
    public Boolean checkId(String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from USER where USER_ID=?", new String[]{id});    // SQL 쿼리 결과 출력

        if(cursor.getCount() > 0){
            return true;
        }
        else
            return false;
    }

    // 닉네임 중복 확인
    public Boolean checkNickname(String nickname){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from USER where USER_NICKNAME=?", new String[]{nickname});    // SQL 쿼리 결과 출력

        if(cursor.getCount() > 0){
            return true;
        }
        else
            return false;
    }

    // 사용자 아아디, 비밀번호가 맞는지 검사
    public Boolean checkIdPassword(String id, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from USER where USER_ID=? and USER_PW=?", new String[] {id, password});

        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }

}

// DB 테이블 정보
class Table {

    // 테이블 열 이름 정의
    // 사용자
    public static final String TABLE_USER = "USER";
    public static final String COLUMN_USER_ID = "USER_ID";
    public static final String COLUMN_USER_PW = "USER_PW";
    public static final String COLUMN_USER_NICKNAME = "USER_NICKNAME";
    public static final String COLUMN_USER_EMAIL = "USER_EMAIL";
    public static final String COLUMN_USER_LEVEL = "USER_LEVEL";
    public static final String COLUMN_USER_STATE = "USER_STATE";

    // 쓰레기
    public static final String TABLE_TRASH = "TRASH";
    public static final String COLUMN_TRASH_CATEGORY = "TRASH_CATEGORY";
    public static final String COLUMN_TRASH_NAME = "TRASH_NAME";
    public static final String COLUMN_TRASH_DESCRIPTION = "TRASH_DES";
    public static final String COLUMN_TRASH_IMAGE = "TRASH_IMAGE";

    // 분리수거 위치 (지도에 마커로 표시하려면 위도와 경도 필요, 분리수거 열 이름은 DB 명세서 제끼고 일단 내 맘대로 써봄)
    public static final String TABLE_MAP = "MAP";
    public static final String COLUMN_MAP_NAME = "MAP_NAME";
    public static final String COLUMN_MAP_ADDRESS = "MAP_ADDRESS";
    public static final String COLUMN_MAP_LATITUDE = "MAP_LATITUDE";   // 위도
    public static final String COLUMN_MAP_LONGITUDE = "MAP_LONGITUDE";     // 경도

    // 게시글
    public static final String TABLE_POST = "POST";
    public static final String COLUMN_POST_TITLE = "POST_TITLE";
    public static final String COLUMN_POST_NICKNAME = "POST_NICKNAME";
    public static final String COLUMN_POST_CONTENT = "POST_CONTENT";
    public static final String COLUMN_POST_IMAGE = "POST_IMAGE";
    public static final String COLUMN_POST_HASH = "POST_HASH";
    public static final String COLUMN_POST_CREATE_DATE = "POST_CREATE_DATE";
    public static final String COLUMN_POST_UPDATE_DATE = "POST_UPDATE_DATE";
    public static final String COLUMN_POST_DELETE_DATE = "POST_DELETE_DATE";

    // 댓글
    public static final String TABLE_COMMENT = "COMMENT";
    public static final String COLUMN_COMMENT_NICKNAME = "COMMENT_NICKNAME";
    public static final String COLUMN_COMMENT_CONTENT = "COMMENT_CONTENT";
    public static final String COLUMN_COMMENT_HASH = "COMMENT_HASH";
    public static final String COLUMN_COMMENT_CREATE_DATE = "COMMENT_CREATE_DATE";
    public static final String COLUMN_COMMENT_UPDATE_DATE = "COMMENT_UPDATE_DATE";
    public static final String COLUMN_COMMENT_DELETE_DATE = "COMMENT_DELETE_DATE";


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
            COLUMN_TRASH_CATEGORY + " TEXT, " +
            COLUMN_TRASH_NAME + " TEXT, " +
            COLUMN_TRASH_DESCRIPTION + " TEXT, " +
            COLUMN_TRASH_IMAGE + " TEXT)";

    // 분리수거 위치 테이블
    public static final String SQL_CREATE_MAP = "CREATE TABLE IF NOT EXISTS " + TABLE_MAP + " (" +
            COLUMN_MAP_NAME + " TEXT, " +
            COLUMN_MAP_ADDRESS + " TEXT, " +
            COLUMN_MAP_LATITUDE + " REAL, " +
            COLUMN_MAP_LONGITUDE + " REAL)";

    // 게시글 테이블
    public static final String SQL_CREATE_POST = "CREATE TABLE IF NOT EXISTS " + TABLE_POST + " (" +
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
            COLUMN_COMMENT_NICKNAME + " TEXT, " +
            COLUMN_COMMENT_CONTENT + " TEXT, " +
            COLUMN_COMMENT_HASH + " TEXT, " +
            COLUMN_COMMENT_CREATE_DATE + " DATETIME, " +
            COLUMN_COMMENT_UPDATE_DATE + " DATETIME, " +
            COLUMN_COMMENT_DELETE_DATE + " DATETIME)";

    // 테이블 조회 SQL문
    public static final String SQL_SELECT_USER = "SELECT * FROM " + TABLE_USER;


    // 테이블 삭제 SQL문
    public static final String SQL_DROP_USER = "DROP TABLE IF EXISTS " + TABLE_USER;
    public static final String SQL_DROP_TRASH = "DROP TABLE IF EXISTS " + TABLE_TRASH;
    public static final String SQL_DROP_MAP = "DROP TABLE IF EXISTS " + TABLE_MAP;
    public static final String SQL_DROP_POST = "DROP TABLE IF EXISTS " + TABLE_POST;
    public static final String SQL_DROP_COMMENT = "DROP TABLE IF EXISTS " + TABLE_COMMENT;
}