package com.algonquinlive.cst335.finalgroupproject.octranspo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DeathForce on 3/28/2018.
 */

public class BusHistoryDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SearchHistory.db";
    public static final String TABLE_NAME  = "BusNumbers";
    public static final String KEY_ID  = "_id";
    public static final String KEY_BUS_NUMBER  = "BusNumber";
    private static final int DATABASE_VERSION = 1;

    public BusHistoryDBHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME  + "( "+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_BUS_NUMBER + " text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
