package com.cavistapractical.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
 
public class InsertHelper {

 private Context context;
 private SQLiteDatabase mDb;
 private DBHelper dbHelper;

 public InsertHelper(Context context) {
  this.context = context;
 }

 public InsertHelper open() throws SQLException {
  dbHelper = new DBHelper(context);
  mDb = dbHelper.getWritableDatabase();
  return this;
 }

 public void close() {
  dbHelper.close();
 }

 public long insertComment(String img_id, String img_cmt){

  ContentValues values = new ContentValues();
  values.put(DBHelper.IMAGE_ID, img_id);
  values.put(DBHelper.IMAGE_COMMENT, img_cmt);

  Log.w("Position: ", "Inserted Values-->" + values);

  return mDb.insertWithOnConflict(DBHelper.COMMENT_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE) ;

 }
}