package com.wei.btsearch;

import com.wei.btsearch.configurations.AppConfiguration;

/**
 * Created by wei on 17-3-31.
 */
public class test {
    public static void main(String[] args) {
        String content = "context";
        int id = 5;
        String SQL = "DELETE FROM " + AppConfiguration.DATABASE_NAME +
                " WHERE " + AppConfiguration.DATABASE_ID + "=" + id + ";";
        System.out.println(SQL);
        String DATABASE_TABLE_NAME = "db";
        String DATABASE_TABLE_CREATE =
                "CREATE TABLE " + DATABASE_TABLE_NAME + " (" +
                        AppConfiguration.DATABASE_ID + " Integer Primary Key, " +
                        AppConfiguration.DATABASE_CONTENT + " VARCHAR(128), " +
                        AppConfiguration.DATABASE_DATE + " TIMESTAMP NOT NULL DEFAULT NOW());";
        System.out.println(DATABASE_TABLE_CREATE);
    }
}
