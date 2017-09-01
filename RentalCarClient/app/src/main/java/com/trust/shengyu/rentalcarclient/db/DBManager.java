package com.trust.shengyu.rentalcarclient.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.trust.shengyu.rentalcarclient.tools.L;

/**
 * Created by Trust on 2017/8/9.
 */

public class DBManager {
    private SQLiteDatabase dbWrit;
    private SQLiteDatabase dbRead;
    private ContentValues contentValues;
    private DbHelper dbHelper;

    public DBManager(Context context) {
        dbHelper = new DbHelper(context);

        contentValues = new ContentValues();
    }

    public void openDB(){
        dbWrit = dbHelper.getWritableDatabase();
        dbRead = dbHelper.getReadableDatabase();
    }

    public void closeDB(){
        dbWrit.close();
        dbRead.close();
    }

    public void addData(String msg){
        openDB();
        contentValues.put("time",msg);
        dbWrit.insert("Trust",null,contentValues);
        contentValues.clear();
        closeDB();
    }

    public void selectAllData(){
        openDB();
        Cursor cursor = dbWrit.query("Trust",null,null,null,null,null,null);

        if(cursor.moveToFirst()){
            do {

                String name = cursor.getString(cursor.getColumnIndex("time"));
                L.d("select status:"+name);
            }while (cursor.moveToNext());
        }
        cursor.close();

        closeDB();
    }

    public void update(String msg){
        openDB();
        contentValues.put("time",msg);
        dbWrit.update("Trust",contentValues,"time = ?",
                new String[]{msg});
        contentValues.clear();
        L.d("update msg:"+msg);
        closeDB();
    }

    public void deleteData(String time){
        openDB();
        contentValues.put("time",time);
        dbWrit.insert("Trust",null,contentValues);
        contentValues.clear();
        closeDB();
    }


    public void deleteAll(){
        L.d("deleteAll");
        openDB();
        dbWrit.execSQL("delete  from "+"Trust" );
        selectAllData();
        closeDB();
    }
}
