package com.algonquinlive.cst335.finalgroupproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by giang on 3/23/18.
 */

public class MccDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Messages.db";
    public static final int VERSION_NUM = 3;
    public static final String TABLE_NAME = "Questions";

    public static final String KEY_ID = "Id";
    public static final String KEY_QUESTION = "Question";
    public static final String KEY_TYPE = "Type";
    public static final String KEY_MULTIPLE_OPTION_A = "OptionA";
    public static final String KEY_MULTIPLE_OPTION_B = "OptionB";
    public static final String KEY_MULTIPLE_OPTION_C = "OptionC";
    public static final String KEY_MULTIPLE_OPTION_D = "OptionD";
    public static final String KEY_MULTIPLE_ANSWER = "MultipleAnswer";
    public static final String KEY_NUMERIC_ANSWER = "NumericAnswer";
    public static final String KEY_NUMERIC_DELTA = "NumericDelta";
    public static final String KEY_BOOLEAN_ANSWER = "BooleanAnswer";

    public MccDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_QUESTION + " text," + KEY_TYPE + " text,"
                + KEY_MULTIPLE_OPTION_A + " TEXT," + KEY_MULTIPLE_OPTION_B + " TEXT,"
                + KEY_MULTIPLE_OPTION_C + " TEXT," + KEY_MULTIPLE_OPTION_D + " TEXT,"
                + KEY_MULTIPLE_ANSWER + " TEXT," + KEY_NUMERIC_ANSWER + " TEXT,"
                + KEY_NUMERIC_DELTA + " INTEGER," + KEY_BOOLEAN_ANSWER + "TEXT);");
        Log.i("MccDatabaseHelper", "Calling onCreate");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i("MccDatabaseHelper", "Calling onUpgrade, oldVersion=" + i + " newVersion=" + i1);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}