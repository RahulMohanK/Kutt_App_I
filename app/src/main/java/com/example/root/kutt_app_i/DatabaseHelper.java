package com.example.root.kutt_app_i;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DBNAME="first.db";
    public static final String TBNAME="firstEntry";
    public static final String COL1="ID";
    public static final String COL2="LINK";
    public DatabaseHelper(Context context) {
        super(context,DBNAME , null, 1);

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table " + TBNAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,LINK TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TBNAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData (String link){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();



            contentValues.put(COL2, link);


            long result = sqLiteDatabase.insert(TBNAME, null, contentValues);

            if (result == -1) {
                return false;
            } else {
                return true;
            }


    }

    public Cursor getAllData(){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor res = sqLiteDatabase.rawQuery("select * from " + TBNAME + " order By "+ COL1 +" DESC ",null);

        return  res;
    }






}
