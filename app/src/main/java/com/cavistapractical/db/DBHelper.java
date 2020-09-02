package com.cavistapractical.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    static DBHelper dbhelper;
    static final String DATABASE_NAME = "cavistaPractical";
    static final int DATABASE_VERSION = 1;
    public static final String COMMENT_TABLE = "cmt_table";
    public static final String IMAGE_ID = "image_id";
    public static final String IMAGE_COMMENT = "image_cmt";

    public static final String COMMENT = "CREATE TABLE " + COMMENT_TABLE + " (" + IMAGE_ID + " VARCHAR(15) DEFAULT NULL," + IMAGE_COMMENT + " VARCHAR(100) DEFAULT NULL);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(COMMENT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ". Old data will be destroyed");
        db.execSQL("DROP TABLE IF EXISTS" + COMMENT_TABLE);
    }

}