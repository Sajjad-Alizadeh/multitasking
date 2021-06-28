package com.example.testapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import model.InfoModel;

public class MyDatabase extends SQLiteOpenHelper {
    private static final String TAG = "MyDatabase";

    private static final int DB_VERSION = 1;

    private static final String DB_NAME = "MyMemos";
    private static final String TABLE_NAME = "memo";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_CONTENT = "content";
    Context context;

    public MyDatabase(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( " +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT , " +
                COL_CONTENT + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addMemo(InfoModel memo) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE, memo.getTitle());
        cv.put(COL_CONTENT, memo.getContent());
        long inserted = database.insert(TABLE_NAME, null, cv);
        if (inserted > 0) {
            Log.i(TAG, "addMemo: " + inserted);
        } else {
            Log.e(TAG, "addMemo: " + inserted);
        }
    }

    public void addMemos(List<InfoModel> memos) {
        for (int i = 0; i < memos.size(); i++) {
            addMemo(memos.get(i));
        }
    }

    public List<InfoModel> getMemos() {
        List<InfoModel> memos = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            while (!cursor.isAfterLast()){
                InfoModel memo = new InfoModel();
                memo.setId(cursor.getInt(0));
                memo.setTitle(cursor.getString(1));
                memo.setContent(cursor.getString(2));
                memos.add(memo);
                cursor.moveToNext();
            }

        }
        cursor.close();
        database.close();
        return memos;
    }

    public void updateMemo(InfoModel memo){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_TITLE,memo.getTitle());
        cv.put(COL_TITLE,memo.getTitle());
        int updated = database.update(TABLE_NAME,cv,"id=?",new String[]{String.valueOf(memo.getId())});
        Log.i(TAG, "updateMemo: "+updated);
    }

}
