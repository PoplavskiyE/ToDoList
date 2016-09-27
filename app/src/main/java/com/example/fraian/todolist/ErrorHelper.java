package com.example.fraian.todolist;

import android.database.DatabaseErrorHandler;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

/**
 * Created by fraian on 27.09.2016.
 */

public class ErrorHelper implements DatabaseErrorHandler {
    Context context;

    public ErrorHelper(Context c) {
        context = c;
    }

    public void onCorruption(SQLiteDatabase db) {
        Toast.makeText(context, "db crash", Toast.LENGTH_LONG).show();
    }
}
