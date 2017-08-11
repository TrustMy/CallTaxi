package com.trust.shengyu.calltaxidriver.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Trust on 2017/8/9.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String TableName = "Trust";

    private static final String CREATE_TRUST = "create table "+TableName+"(" +
            "id integer primary key autoincrement," +
            "time text" +
            ")";

    public DbHelper(Context context) {
        super(context, "Trust.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TRUST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
