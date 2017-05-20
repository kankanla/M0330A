package com.kankanla.e560.m0330a.Function;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by E560 on 2017/04/27.
 */

public class GBMFileinfo {
    private final String db_name = "BGM.db";
    private final int db_ver = 1;
    private Context context;
    private String x;

    public GBMFileinfo(Context context) {
        this.context = context;
        File_db file_db = new File_db(context);
        file_db.getWritableDatabase();
        file_db.close();
        Toast.makeText(context, x, Toast.LENGTH_SHORT).show();
    }


    private class File_db extends SQLiteOpenHelper {

        public File_db(Context context) {
            super(context, db_name, null, db_ver);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
