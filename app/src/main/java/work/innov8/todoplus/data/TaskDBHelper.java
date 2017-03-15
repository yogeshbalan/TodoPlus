package work.innov8.todoplus.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import work.innov8.todoplus.model.Task;

/**
 * Created by yogesh on 11/3/17.
 */

public class TaskDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "task.db";
    public static final int DATABASE_VERSION = 1;

    public static final String SQL_CREATE_TABLE_ENTRIES =
            "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME + " (" +
                    TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY," +
                    TaskContract.TaskEntry.COLUMN_NAME_TITLE + " VARCHAR(128)," +
                    TaskContract.TaskEntry.COLUMN_NAME_DESCRTIPTION + " TEXT," +
                    TaskContract.TaskEntry.COLUMN_NAME_DATE + " DATETIME," +
                    TaskContract.TaskEntry.COLUMN_NAME_TASK_DATE_CREATED + " DATETIME," +
                    TaskContract.TaskEntry.COLUMN_NAME_DONE + " BOOLEAN" +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME;

    private static final String SQL_DELETE_DATABASE_ENTRIES = "DELETE FROM " + TaskContract.TaskEntry.TABLE_NAME;

    private static final String TAG = "db";

    private static TaskDBHelper taskDBHelper;

    public TaskDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static TaskDBHelper getTaskDBHelperInstance(Context context) {
        if (taskDBHelper == null) {
            taskDBHelper = new TaskDBHelper(context);
        }
        return taskDBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public void deleteTable() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL(SQL_DELETE_DATABASE_ENTRIES);
        sqLiteDatabase.close();
    }

    public long insert(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_NAME_TITLE, task.getTaskTitle());
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DESCRTIPTION, task.getTaskDescription());
        if (task.getTaskDate() != null) {
            values.put(TaskContract.TaskEntry.COLUMN_NAME_DATE, task.getTaskDate().getTime());
        }
        if (task.getTaskDateCreated() != null) {
            values.put(TaskContract.TaskEntry.COLUMN_NAME_TASK_DATE_CREATED, task.getTaskDateCreated().getTime());
        }
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DONE, task.isTaskDone());

        // Insert the new row, returning the primary key value of the new row
        long id = db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
        return id;
    }

    public Cursor getAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] fields = {
                TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.COLUMN_NAME_TITLE,
                TaskContract.TaskEntry.COLUMN_NAME_DESCRTIPTION,
                TaskContract.TaskEntry.COLUMN_NAME_DATE,
                TaskContract.TaskEntry.COLUMN_NAME_TASK_DATE_CREATED,
                TaskContract.TaskEntry.COLUMN_NAME_DONE
        };

        Cursor cursor = db.query(
                TaskContract.TaskEntry.TABLE_NAME,                       // The table to query
                fields,                                                  // The columns to return
                null,                                                    // The columns for the WHERE clause
                null,                                                    // The values for the WHERE clause
                null,                                                    // don't group the rows
                null,                                                    // don't filter by row groups
                TaskContract.TaskEntry.COLUMN_NAME_DATE + " ASC"         // The sort order
        );
        return cursor;
    }

    public List<Task> getAllTask() {
        List<Task> mTaskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] fields = {
                TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.COLUMN_NAME_TITLE,
                TaskContract.TaskEntry.COLUMN_NAME_DESCRTIPTION,
                TaskContract.TaskEntry.COLUMN_NAME_DATE,
                TaskContract.TaskEntry.COLUMN_NAME_TASK_DATE_CREATED,
                TaskContract.TaskEntry.COLUMN_NAME_DONE
        };

        Cursor cursor = db.query(
                TaskContract.TaskEntry.TABLE_NAME,                       // The table to query
                fields,                                                  // The columns to return
                null,                                                    // The columns for the WHERE clause
                null,                                                    // The values for the WHERE clause
                null,                                                    // don't group the rows
                null,                                                    // don't filter by row groups
                TaskContract.TaskEntry.COLUMN_NAME_DATE + " ASC"         // The sort order
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getColumnIndex(TaskContract.TaskEntry._ID);
                int titleIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_TITLE);
                int descIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_DESCRTIPTION);
                int dateIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_DATE);
                int dateCreatedIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_TASK_DATE_CREATED);
                int statusIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_DONE);
                String title = cursor.getString(titleIndex);
                String desc = cursor.getString(descIndex);
                String date = cursor.getString(dateIndex);
                String dateCreated = cursor.getString(dateCreatedIndex);
                String isDone = cursor.getString(statusIndex);
                boolean done = Boolean.getBoolean(isDone);
                Task task = new Task(id, title, desc, null, null, done);
                mTaskList.add(task);
            }

        } else {
            Log.d(TAG, "getAllTask: RECORD DOES NOT EXIST");
        }

        return mTaskList;
    }

    public Cursor getDone() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] fields = {
                TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.COLUMN_NAME_TITLE,
                TaskContract.TaskEntry.COLUMN_NAME_DESCRTIPTION,
                TaskContract.TaskEntry.COLUMN_NAME_DATE,
                TaskContract.TaskEntry.COLUMN_NAME_TASK_DATE_CREATED,
                TaskContract.TaskEntry.COLUMN_NAME_DONE
        };

        Cursor cursor = db.query(
                TaskContract.TaskEntry.TABLE_NAME,                      // The table to query
                fields,                                                 // The columns to return
                TaskContract.TaskEntry.COLUMN_NAME_DONE + " = '1'",     // The columns for the WHERE clause
                null,                                                   // The values for the WHERE clause
                null,                                                   // don't group the rows
                null,                                                   // don't filter by row groups
                TaskContract.TaskEntry.COLUMN_NAME_DATE + " ASC"        // The sort order
        );
        return cursor;
    }

    public Task getTask(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] fields = {
                TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.COLUMN_NAME_TITLE,
                TaskContract.TaskEntry.COLUMN_NAME_DESCRTIPTION,
                TaskContract.TaskEntry.COLUMN_NAME_DATE,
                TaskContract.TaskEntry.COLUMN_NAME_TASK_DATE_CREATED,
                TaskContract.TaskEntry.COLUMN_NAME_DONE
        };

        Cursor cursor = db.query(
                TaskContract.TaskEntry.TABLE_NAME,                      // The table to query
                fields,                                                 // The columns to return
                TaskContract.TaskEntry._ID + " = " + id,                // The columns for the WHERE clause
                null,                                                   // The values for the WHERE clause
                null,                                                   // don't group the rows
                null,                                                   // don't filter by row groups
                null                                                    // The sort order
        );

        Task task = new Task();
        if (cursor != null && cursor.moveToFirst()) {
            task.set_id(cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry._ID)));
            task.setTaskTitle(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_TITLE)));
            task.setTaskDescription(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_DESCRTIPTION)));
            Long valueTaskDate = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_DATE));
            if (valueTaskDate != 0) {
                task.setTaskDate(new Date(valueTaskDate));
            }
            Long valueTaskDateCreated = cursor.getLong(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_TASK_DATE_CREATED));
            if (valueTaskDateCreated != 0) {
                task.setTaskDateCreated(new Date(valueTaskDateCreated));
            }
            task.setTaskDone(cursor.getInt(cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_NAME_DONE)) > 0);
        }
        return task;
    }

    public Cursor getPending() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] fields = {
                TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.COLUMN_NAME_TITLE,
                TaskContract.TaskEntry.COLUMN_NAME_DESCRTIPTION,
                TaskContract.TaskEntry.COLUMN_NAME_DATE,
                TaskContract.TaskEntry.COLUMN_NAME_TASK_DATE_CREATED,
                TaskContract.TaskEntry.COLUMN_NAME_DONE
        };

        Cursor cursor = db.query(
                TaskContract.TaskEntry.TABLE_NAME,                      // The table to query
                fields,                                                 // The columns to return
                TaskContract.TaskEntry.COLUMN_NAME_DONE + " = '0'",     // The columns for the WHERE clause
                null,                                                   // The values for the WHERE clause
                null,                                                   // don't group the rows
                null,                                                   // don't filter by row groups
                TaskContract.TaskEntry.COLUMN_NAME_DATE + " ASC"        // The sort order
        );
        return cursor;
    }

    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Define 'where' part of query.
        String selection = TaskContract.TaskEntry._ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(id)};
        // Issue SQL statement.
        db.delete(TaskContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
    }

    public void update(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_NAME_TITLE, task.getTaskTitle());
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DESCRTIPTION, task.getTaskDescription());
        if (task.getTaskDate() != null) {
            values.put(TaskContract.TaskEntry.COLUMN_NAME_DATE, task.getTaskDate().getTime());
        }
        if (task.getTaskDateCreated() != null) {
            values.put(TaskContract.TaskEntry.COLUMN_NAME_TASK_DATE_CREATED, task.getTaskDateCreated().getTime());
        }
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DONE, task.isTaskDone());

        // Which row to update, based on the ID
        String selection = TaskContract.TaskEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(task.get_id())};

        db.update(TaskContract.TaskEntry.TABLE_NAME, values, selection, selectionArgs);
    }

}
