package work.innov8.todoplus.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import work.innov8.todoplus.model.User;

/**
 * Created by yogesh on 14/3/17.
 */

public class LoginDBHelper extends SQLiteOpenHelper {

    // Database Information
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "login.db";
    private static LoginDBHelper loginDBHelper;

    public static final String SQL_CREATE_TABLE_ENTRIES =
            "CREATE TABLE " + LoginContract.LoginEntry.TABLE_NAME + " (" +
                    LoginContract.LoginEntry._ID + " INTEGER PRIMARY KEY," +
                    LoginContract.LoginEntry.COLUMN_NAME_EMAIL + " TEXT," +
                    LoginContract.LoginEntry.COLUMN_NAME_PASSWORD + " TEXT" +
                    " );";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LoginContract.LoginEntry.TABLE_NAME;

    public LoginDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static LoginDBHelper getLoginDBHelperInstance(Context context) {
        if (loginDBHelper == null) {
            loginDBHelper = new LoginDBHelper(context);
        }
        return loginDBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public long insert(User user) {

        SQLiteDatabase db = this.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();
        contentValues.put(LoginContract.LoginEntry.COLUMN_NAME_EMAIL, user.getEmail());
        contentValues.put(LoginContract.LoginEntry.COLUMN_NAME_PASSWORD, user.getPassword());

        // Insert the new row, returning the primary key value of the new row
        long id = db.insert(LoginContract.LoginEntry.TABLE_NAME, null, contentValues);
        return id;
    }

    public int delete(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Define 'where' part of query.
        String selection = LoginContract.LoginEntry.COLUMN_NAME_EMAIL + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(email)};
        // Issue SQL statement.
        return db.delete(LoginContract.LoginEntry.TABLE_NAME, selection, selectionArgs);
    }

    public List<User> getAllUser() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] fields = {
                LoginContract.LoginEntry._ID,
                LoginContract.LoginEntry.COLUMN_NAME_EMAIL,
                LoginContract.LoginEntry.COLUMN_NAME_PASSWORD,
        };

        Cursor cursor = db.query(
                LoginContract.LoginEntry.TABLE_NAME,                     // The table to query
                fields,                                                  // The columns to return
                null,                                                    // The columns for the WHERE clause
                null,                                                    // The values for the WHERE clause
                null,                                                    // don't group the rows
                null,                                                    // don't filter by row groups
                TaskContract.TaskEntry._ID + " ASC"                      // The sort order
        );
        while (cursor.moveToNext()) {
            int id = cursor.getColumnIndex(LoginContract.LoginEntry._ID);
            int emailIndex = cursor.getColumnIndex(LoginContract.LoginEntry.COLUMN_NAME_EMAIL);
            int passwordIndex = cursor.getColumnIndex(LoginContract.LoginEntry.COLUMN_NAME_PASSWORD);
            String email = cursor.getString(emailIndex);
            String password = cursor.getString(passwordIndex);
            User user = new User(id, email, password);
            userList.add(user);
        }
        return userList;
    }

    public String getPasswordEntry(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] selectionArgs = {String.valueOf(email)};

        Cursor cursor = db.query(
                LoginContract.LoginEntry.TABLE_NAME,
                null,
                LoginContract.LoginEntry.COLUMN_NAME_EMAIL + "=?",
                new String[]{email},
                null,
                null,
                LoginContract.LoginEntry._ID);

        if (cursor.getCount() < 1) // Email doesn't Exist
        {
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex(LoginContract.LoginEntry.COLUMN_NAME_PASSWORD));
        cursor.close();
        return password;
    }

    public void update(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Define the updated row content.
        ContentValues updatedValues = new ContentValues();
        // Assign values for each row.
        updatedValues.put(LoginContract.LoginEntry.COLUMN_NAME_EMAIL, user.getEmail());
        updatedValues.put(LoginContract.LoginEntry.COLUMN_NAME_PASSWORD, user.getPassword());

        // Which row to update, based on the ID
        String selection = LoginContract.LoginEntry.COLUMN_NAME_EMAIL + " = ?";
        String[] selectionArgs = {String.valueOf(user.getEmail())};

        db.update(LoginContract.LoginEntry.TABLE_NAME, updatedValues, selection, selectionArgs);
    }

}
