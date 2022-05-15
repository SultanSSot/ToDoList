package com.example.todolist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todolist.model.Task;

import java.util.Date;

/**
 * Created by rodrigo on 07/09/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "task";
    public static final String _ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DECRTIPTION = "descritption";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DONE = "done";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_TITLE + " VARCHAR(128)," +
                    COLUMN_DECRTIPTION + " TEXT," +
                    COLUMN_DATE + " DATETIME," +
                    COLUMN_DONE + " BOOLEAN" +
                    " )";

    private static final String DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    // Database Information
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Task.db";
    private static DBHelper _dbHelper;

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBHelper getInstance(Context context) {
        if (_dbHelper == null) {
            _dbHelper = new DBHelper(context);
        }
        return _dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersionNumber, int newVersionNumber) {
        database.execSQL(DELETE_ENTRIES);
        onCreate(database);
    }

    public long insertTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, task.getTitle());
        contentValues.put(COLUMN_DECRTIPTION, task.getDescription());
        if (task.getDate() != null) {
            contentValues.put(COLUMN_DATE, task.getDate().getTime());
        }
        contentValues.put(COLUMN_DONE, task.isDone());

        // Insert the new row, returning the primary key value of the new row
        long id = db.insert(TABLE_NAME, null, contentValues);
        return id;
    }

    public Cursor getAllTask() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] tableFields = {
                _ID,
                COLUMN_TITLE,
                COLUMN_DECRTIPTION,
                COLUMN_DATE,
                COLUMN_DONE
        };

        Cursor cursor = db.query(
                TABLE_NAME,          // The table to query
                tableFields,                                     // The columns to return
                null,                                       // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                COLUMN_DATE + " ASC"           // The sort order
        );
        return cursor;
    }

    public Cursor getDoneTask() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] tableFields = {
                _ID,
                COLUMN_TITLE,
                COLUMN_DECRTIPTION,
                COLUMN_DATE,
                COLUMN_DONE
        };

        Cursor cursor = db.query(
                TABLE_NAME,                      // The table to query
                tableFields,                                                 // The columns to return
                COLUMN_DONE + " = '1'",                      // The columns for the WHERE clause
                null,                                                   // The values for the WHERE clause
                null,                                                   // don't group the rows
                null,                                                   // don't filter by row groups
                COLUMN_DATE + " ASC"          // The sort order
        );
        return cursor;
    }

    public Task getTaskbyID(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] tableFields = {
                _ID,
                COLUMN_TITLE,
                COLUMN_DECRTIPTION,
                COLUMN_DATE,
                COLUMN_DONE
        };

        Cursor cursor = db.query(
                TABLE_NAME,                      // The table to query
                tableFields,                                                 // The columns to return
                _ID + " = " + id,                                   // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                                   // don't group the rows
                null,                                                   // don't filter by row groups
                null                                                    // The sort order
        );

        Task task = new Task();
        if (cursor != null && cursor.moveToFirst()) {
            task.set_id(cursor.getInt(cursor.getColumnIndex(_ID)));
            task.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            task.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DECRTIPTION)));
            Long timestamp = cursor.getLong(cursor.getColumnIndex(COLUMN_DATE));
            if (timestamp != 0) {
                task.setDate(new Date(timestamp));
            }
            task.setDone(cursor.getInt(cursor.getColumnIndex(COLUMN_DONE)) > 0);
        }
        return task;
    }

    public Cursor getPendingTask() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] tableFields = {
                _ID,
                COLUMN_TITLE,
                COLUMN_DECRTIPTION,
                COLUMN_DATE,
                COLUMN_DONE
        };

        Cursor cursor = db.query(
                TABLE_NAME,                      // The table to query
                tableFields,                                                 // The columns to return
                COLUMN_DONE + " = '0'",                      // The columns for the WHERE clause
                null,                                                   // The values for the WHERE clause
                null,                                                   // don't group the rows
                null,                                                   // don't filter by row groups
                COLUMN_DATE + " ASC"          // The sort order
        );
        return cursor;
    }

    public void deleteTaskbyID(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Define 'where' part of query.
        String selection = _ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(id)};
        // Issue SQL statement.
        db.delete(TABLE_NAME, selection, selectionArgs);
    }

    public void updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        // New value for one column
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, task.getTitle());
        contentValues.put(COLUMN_DECRTIPTION, task.getDescription());
        if (task.getDate() != null) {
            contentValues.put(COLUMN_DATE, task.getDate().getTime());
        }
        contentValues.put(COLUMN_DONE, task.isDone());

        // Which row to update, based on the ID
        String selection = _ID + " = ?";
        String[] selectionArguments = {String.valueOf(task.get_id())};

        db.update(TABLE_NAME, contentValues, selection, selectionArguments);
    }
}
