package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class taskDataSource {

    private SQLiteDatabase database;
    private taskDBHelper dbHelper;

    public taskDataSource (Context context) {
        dbHelper = new taskDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    //inserts a new contact to the database
    public boolean insertTask (Task t) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("subject", t.getSubject());
            initialValues.put("description", t.getDescription());
            initialValues.put("priority", t.getPriority());
            initialValues.put("dueDate", String.valueOf(t.getDueDate().getTimeInMillis()));

            didSucceed = database.insert("tasks", null, initialValues) > 0;
        } catch (Exception e){
            // Do nothing, will return false if there is an exemption
        }
        return didSucceed;
    }


    // updates an existing contact from the database
    public boolean updateTask (Task t) {
        boolean didSucceed = false;
        try {
            Long rowId = (long) t.getTaskId();
            ContentValues updateValues = new ContentValues();

            updateValues.put("subject", t.getSubject());
            updateValues.put("description", t.getDescription());
            updateValues.put("priority", t.getPriority());
            updateValues.put("dueDate", String.valueOf(t.getDueDate().getTimeInMillis()));

            didSucceed = database.update("tasks", updateValues, "taskId=" + rowId, null) > 0;

        } catch (Exception e) {
            // Do nothing, will return false if there is an exemption
        }
        return didSucceed;
    }

    public int getLastTaskId() {

        int lastId;
        try {
            String query = "Select Max(taskId) from tasks";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId =cursor.getInt(0);
            cursor.close();
        }  catch (Exception e) {
            lastId = -1;
        }
        return lastId;
    }

    //Putting all names of contacts into ArrayList
    public ArrayList<String> getTaskName() {
        ArrayList<String> taskNames = new ArrayList<>();

        try{

            String query = "Select subject from tasks";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                taskNames.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            taskNames = new ArrayList<String>();
        }
        return taskNames;
    }


    public ArrayList<Task> getTasks(String sortField, String sortOrder) {
        ArrayList<Task> tasks = new ArrayList<Task>();

        try {
            String query = "Select * FROM tasks ORDER BY " + sortField + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);

            Task newTask;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newTask = new Task();
                newTask.setSubject(cursor.getString(0));
                newTask.setDescription(cursor.getString(1));
                newTask.setPriority(cursor.getString(2));
                
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(9)));
                newTask.setDueDate(calendar);
                tasks.add(newTask);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            tasks = new ArrayList<Task>();
        }
        return tasks;
    }

    public Task getSpecificTask (int taskId) {
        Task task = new Task();
        String query = "Select * FROM tasks WHERE taskId =" + taskId;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            task.setSubject(cursor.getString(1));
            task.setDescription(cursor.getString(2));
            task.setPriority(cursor.getString(3));

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(cursor.getString(9)));
            task.setDueDate(calendar);

            cursor.close();
        }
        return task;
    }


    //deleting task from data source
    public boolean deleteTask (int taskId) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("tasks", "taskId=" + taskId, null) > 0;
        } catch (Exception e) {
            //Do Nothing return value that is already set to false
        }
        return didDelete;
    }


}
