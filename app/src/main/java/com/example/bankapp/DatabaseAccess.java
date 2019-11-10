package com.example.bankapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseAccess {

    public static final String dbName = "bank.db";

    public static void setDefaultDatabase(Context context) {
        try {
            InputStream input = context.getAssets().open(dbName);
            OutputStream output = new FileOutputStream(dbFile(context));
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            output.flush();
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File dbFile(Context context) {
        return context.getDatabasePath(dbName);
    }

    public static SQLiteDatabase openDefaultBase(Context context) {
        return SQLiteDatabase.openDatabase(dbFile(context).getPath(), null, SQLiteDatabase.OPEN_READWRITE);
    }

}
