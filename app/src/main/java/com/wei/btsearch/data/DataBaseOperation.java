package com.wei.btsearch.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wei.btsearch.configurations.AppConfiguration;

/**
 * Created by wei on 17-3-31.
 */
public class DataBaseOperation {
    Context context;
    SQLiteDatabase database;

    public DataBaseOperation(Context context) {
        this.context = context;
        database = new HistoryOpenHelper(context).getWritableDatabase();
    }

    public void insertContent(String content) {
        final String SQL = "INSERT INTO " + AppConfiguration.DATABASE_NAME +
                " (" + AppConfiguration.DATABASE_CONTENT + ") VALUES ('" + content + "');";

        if (AppConfiguration.DEBUG) {
            System.out.println(SQL);
        }

        try {
            database.execSQL(SQL);
        } catch (Exception e) {
            System.out.println("Insert Error");
            e.printStackTrace();
        }
    }

    public Cursor queryAll() {
        Cursor result = database.query(AppConfiguration.DATABASE_NAME,
                null, null, null, null, null, null);

        return result;
    }

    public void deleteItem(int id) {
        String SQL = "DELETE FROM " + AppConfiguration.DATABASE_NAME +
                " WHERE " + AppConfiguration.DATABASE_ID + "=" + id + ";";
        if (AppConfiguration.DEBUG) {
            System.out.println(SQL);
        }
        database.execSQL(SQL);
    }

    public void deleteAll() {
        String SQL = "DELETE FROM " + AppConfiguration.DATABASE_NAME + ";";
        if (AppConfiguration.DEBUG) {
            System.out.println(SQL);
        }
        database.execSQL(SQL);
    }
}
