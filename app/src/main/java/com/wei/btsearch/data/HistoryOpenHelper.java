package com.wei.btsearch.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.wei.btsearch.configurations.AppConfiguration;

/**
 * Created by wei on 17-3-31.
 */
public class HistoryOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE_NAME = AppConfiguration.DATABASE_NAME;

    String DATABASE_TABLE_CREATE =
            "CREATE TABLE " + DATABASE_TABLE_NAME + " (" +
                    AppConfiguration.DATABASE_ID + " Integer Primary Key, " +
                    AppConfiguration.DATABASE_CONTENT + " VARCHAR(128));" ;


    public HistoryOpenHelper(Context context) {
        super(context, DATABASE_TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
