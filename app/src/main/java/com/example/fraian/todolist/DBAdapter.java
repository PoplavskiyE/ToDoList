package com.example.fraian.todolist;

/**
 * Created by fraian on 27.09.2016.
 */

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class DBAdapter {
    private ArrayList<HashMap<String, Object>> myTasks;
    private static final String NAME = "name";
    public static final String DATABASE_NAME = "TASKS";
    public static final String DATABASE_TABLE = "TASKS_LIST";
    public static final int MYDATABASE_VERSION = 1;
    public static final String TASK_NAME = "Task";
    private static final String SCRIPT_CREATE_DATABASE = "CREATE TABLE "
            + DATABASE_NAME + " (" + TASK_NAME + " text not null);";

    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private Context context;

    public DBAdapter(Context c) {
        context = c;
        myTasks = new ArrayList<HashMap<String, Object>>();
    }

    public long insert(String abName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_NAME, abName);

        return database.insert(DATABASE_NAME, null, contentValues);
    }

    public long updateInfo(String abName, String oldName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TASK_NAME, abName);

        return database.update(DATABASE_NAME, contentValues, TASK_NAME
                + " = ? ", new String[]{oldName});
    }

    public long deleteContact(String oldName) {
        try {
            return database.delete(DATABASE_NAME, TASK_NAME + " = ?",
                    new String[]{oldName});
        } catch (SQLException e) {
        }
        return 1;
    }

    public int deleteAll() {
        return database.delete(DATABASE_TABLE, null, null);
    }

    public DBAdapter openToWrite() {
        dbHelper = new DBHelper(context, DATABASE_NAME, null,
                MYDATABASE_VERSION, new ErrorHelper(context));
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            new ErrorHelper(context);
        }
        return this;
    }

    public DBAdapter openToRead() throws android.database.SQLException {
        dbHelper = new DBHelper(context, DATABASE_NAME, null,
                MYDATABASE_VERSION, new ErrorHelper(context));
        database = dbHelper.getReadableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void refreshContacts(ListView listView) {
        HashMap<String, Object> hm;
        Cursor cursor = database.query(DATABASE_NAME, null, null, null, null,
                null, null);
        int index_NAME = cursor.getColumnIndex(TASK_NAME);
        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            hm = new HashMap<String, Object>();
            hm.put(NAME, cursor.getString(index_NAME));

            myTasks.add(hm);
        }
        SimpleAdapter adapter = new SimpleAdapter(context, myTasks,
                R.layout.list, new String[]{NAME},
                new int[]{R.id.txt_name});
        listView.setAdapter(adapter);

    }

    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, CursorFactory factory,
                        int version, DatabaseErrorHandler errorHandler) {
            super(context, name, factory, version, errorHandler);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SCRIPT_CREATE_DATABASE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        }

    }

}
