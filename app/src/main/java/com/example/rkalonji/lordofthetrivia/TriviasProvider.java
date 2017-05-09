package com.example.rkalonji.lordofthetrivia;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;

/**
 * Created by Rkalonji on 04/21/2017.
 */

public class TriviasProvider extends ContentProvider {
    public static final String PROVIDER_NAME = "com.example.rkalonji.lordofthetrivia";

    // trivia_set table unique columns
    public static final String _ID = "_id";
    public static final String TRIVIA_SET_FIREBASE_ID = "triviaSetfirebaseId";
    public static final String NAME = "name";
    public static final String IMAGE_PATH = "imagePath";

    // option table unique columns
    public static final String TEXT = "text";
    public static final String IS_ANSWER = "isAnswer";
    public static final String FK_QUESTION_FIREBASE_ID = "FKQuestionFirebaseId";

    // best_score table unique columns
    public static final String USERNAME = "username";
    public static final String FK_TRIVIA_SET_FIREBASE_ID = "FKTriviaSetFirebaseId";
    public static final String SCORE = "score";
    public static final String TIMESTAMP = "timestamp";
    public static final String IS_REMOTE = "isRemote";
    public static final String IS_SHARED = "isShared";

    private static HashMap<String, String> TRIVIA_SETS_PROJECTION_MAP;

    public static final int TRIVIA = 1;

    public static final String BASE = "content://" + PROVIDER_NAME;
    public static final String TRIVIA_BASE = "content://" + PROVIDER_NAME + "/trivia";
    public static final Uri TRIVIA_BASE_URI = Uri.parse(TRIVIA_BASE);

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    }

    /**
     * Database specific constant declarations
     */
    private SQLiteDatabase db;
    public static final String DATABASE_NAME = "Trivias.db";
    public static final int DATABASE_VERSION = 1;

    // Tables definition and creation
    public static final String TRIVIA_SET_TABLE_NAME = "trivia_set";
    public static final String CREATE_TRIVIA_SET_DB_TABLE =
            " CREATE TABLE IF NOT EXISTS " + TRIVIA_SET_TABLE_NAME +
                    " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " " + TRIVIA_SET_FIREBASE_ID + " TEXT NOT NULL, " +
                    " " + NAME + "TEXT NOT NULL, " +
                    " " + IMAGE_PATH + " TEXT);";

    public static final String QUESTION_TABLE_NAME = "question";
    public static final String CREATE_QUESTION_DB_TABLE =
            " CREATE TABLE IF NOT EXISTS " + QUESTION_TABLE_NAME +
                    " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " " + TRIVIA_SET_FIREBASE_ID + " TEXT NOT NULL, " +
                    " " + TEXT + "TEXT NOT NULL);";

    public static final String OPTION_TABLE_NAME = "option";
    public static final String CREATE_OPTION_DB_TABLE =
            " CREATE TABLE IF NOT EXISTS " + OPTION_TABLE_NAME +
                    " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " " + TEXT + " TEXT NOT NULL, " +
                    " " + IS_ANSWER + "INTEGER NOT NULL, " +
                    " " + FK_QUESTION_FIREBASE_ID + " TEXT);";

    public static final String BEST_SCORE_TABLE_NAME = "best_score";
    public static final String CREATE_BEST_SCORE_DB_TABLE =
            " CREATE TABLE IF NOT EXISTS " + BEST_SCORE_TABLE_NAME +
                    " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " " + USERNAME + " TEXT NOT NULL, " +
                    " " + FK_TRIVIA_SET_FIREBASE_ID + " TEXT NOT NULL, " +
                    " " + SCORE + " TEXT NOT NULL, " +
                    " " + TIMESTAMP + " TEXT NOT NULL, " +
                    " " + IS_REMOTE + "INTEGER NOT NULL, " +
                    " " + IS_SHARED + " INTEGER NOT NULL);";

    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */

    public static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_TRIVIA_SET_DB_TABLE);
            db.execSQL(CREATE_QUESTION_DB_TABLE);
            db.execSQL(CREATE_OPTION_DB_TABLE);
            db.execSQL(CREATE_BEST_SCORE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + BEST_SCORE_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + OPTION_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + QUESTION_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TRIVIA_SET_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */

        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /**
         * Add a new movie record
         */
        long rowID = 0;

        switch (uriMatcher.match(uri)) {
            case TRIVIA:
                rowID = db.insert(TRIVIA_SET_TABLE_NAME, "", values);
                break;
        }
        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(TRIVIA_BASE_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case TRIVIA:
                qb.setTables(TRIVIA_SET_TABLE_NAME);
                qb.setProjectionMap(TRIVIA_SETS_PROJECTION_MAP);
                break;
        }

        if (sortOrder == null || sortOrder == ""){
            /**
             * By default sort on student names
             */
            sortOrder = "";
        }

        Cursor c = qb.query(db,	projection,	selection,
                selectionArgs,null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case TRIVIA:
                count = db.delete(TRIVIA_SET_TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case TRIVIA:
                count = db.update(TRIVIA_SET_TABLE_NAME, values, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            /**
             * Get all student records
             */
            case TRIVIA:
                return "vnd.android.cursor.dir/vnd.example.students";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}