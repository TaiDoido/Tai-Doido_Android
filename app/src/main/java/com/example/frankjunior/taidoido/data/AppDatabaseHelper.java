package com.example.frankjunior.taidoido.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.VisibleForTesting;

import com.example.frankjunior.taidoido.app.App;
import com.example.frankjunior.taidoido.util.MyLog;
import com.example.frankjunior.taidoido.util.Util;

/**
 * Created by frankjunior on 31/03/16.
 */
public class AppDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "taidoido_database";
    private static final int VERSION = 1;
    private static final String SQL_CREATE = "create_script_taidoido.sql";

    public AppDatabaseHelper() {
        super(App.getContext(), DATABASE_NAME, null, VERSION);
    }

    @VisibleForTesting
    public AppDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        executeSqlCommand(db, SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Use to execute all sql instructions in sqls into the assets folder
     *
     * @param sqLiteDatabase
     * @param sqlFileName
     */
    private void executeSqlCommand(SQLiteDatabase sqLiteDatabase, String sqlFileName) {
        try {
            //Get all sql instructions from the SQL_NAME_ASSETS file.
            String[] sqlInstructions = Util.getStatementSql(App.getContext(), sqlFileName);

            for (final String sql : sqlInstructions) {
                if (sql != null && !sql.isEmpty()) {
                    sqLiteDatabase.execSQL(sql);
                }
            }
        } catch (IllegalArgumentException e) {
            MyLog.printError("There is a problem on sql", e);
        }
    }
}
