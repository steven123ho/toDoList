package com.example.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class taskDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mytasks.db";
    private static final int DATABASE_VERSION = 1;


    //Database creation sql statement
    private static final String Create_Table_Task =
            "create table tasks (taskId integer primary key autoincrement, " +
                    "subject text not null, description text, " +
                    "priority text, dueDate text);";

    public taskDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL(Create_Table_Task);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(taskDBHelper.class.getName(), "upgrading database from version " + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS tasks");
        onCreate(db);
    }



}