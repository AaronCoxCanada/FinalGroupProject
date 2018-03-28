package com.algonquinlive.cst335.finalgroupproject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MccQuestionActivity extends Activity {

    SQLiteDatabase db;
    LinearLayout multipleContainer;
    LinearLayout numericContainer;
    LinearLayout booleanContainer;
    TextView txtQuestion;
    Button btnSave;
    Button btnCancel;
    Button btnDelete;
    long Id;
    String question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcc_question);

        txtQuestion = findViewById(R.id.mcc_question_txt_question);
        btnSave = findViewById(R.id.mcc_question_btn_save);
        btnCancel = findViewById(R.id.mcc_question_btn_cancel);
        btnDelete = findViewById(R.id.mcc_question_btn_delete);
        MccDatabaseHelper dbHelper = new MccDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        Bundle extras = getIntent().getExtras();
        Id = extras.getLong(MccDatabaseHelper.KEY_ID);
        if (Id != 0) {
            Cursor cursor = db.query(false, MccDatabaseHelper.TABLE_NAME,
                    new String[] { MccDatabaseHelper.KEY_ID, MccDatabaseHelper.KEY_QUESTION},
                    MccDatabaseHelper.KEY_ID + "=?", new String[] { String.valueOf(Id) },
                    null, null, null, null);
            cursor.moveToFirst();
            question = cursor.getString(cursor.getColumnIndex(MccDatabaseHelper.KEY_QUESTION));
            txtQuestion.setText(question);
        }


        btnSave.setOnClickListener(e -> {
            String question = txtQuestion.getText().toString();
            ContentValues newData = new ContentValues();
            newData.put(MccDatabaseHelper.KEY_QUESTION, question);

            //Insert the data
            db.insert(MccDatabaseHelper.TABLE_NAME, null, newData);

            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
        btnCancel.setOnClickListener(e -> finish());
        btnDelete.setOnClickListener(e -> {
            db.delete(MccDatabaseHelper.TABLE_NAME, MccDatabaseHelper.KEY_ID + "=?",
                    new String[] {String.valueOf(Id)});
            setResult(5, new Intent());
            finish();
        });

        multipleContainer = findViewById(R.id.mcc_question_container_multiple);
        numericContainer = findViewById(R.id.mcc_question_container_numeric);
        booleanContainer = findViewById(R.id.mcc_question_container_boolean);

        RadioButton radMultiple = findViewById(R.id.mcc_question_rad_multiple);
        RadioButton radMNumeric = findViewById(R.id.mcc_question_rad_numeric);
        RadioButton radBoolean = findViewById(R.id.mcc_question_rad_boolean);
        radMultiple.setChecked(true);
        multipleContainer.setVisibility(View.VISIBLE);
        numericContainer.setVisibility(View.GONE);
        booleanContainer.setVisibility(View.GONE);
    }


    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        if (checked) {
            switch(view.getId()) {
                case R.id.mcc_question_rad_multiple:
                    multipleContainer.setVisibility(View.VISIBLE);
                    numericContainer.setVisibility(View.GONE);
                    booleanContainer.setVisibility(View.GONE);
                    break;
                case R.id.mcc_question_rad_numeric:
                    multipleContainer.setVisibility(View.GONE);
                    numericContainer.setVisibility(View.VISIBLE);
                    booleanContainer.setVisibility(View.GONE);
                    break;
                case R.id.mcc_question_rad_boolean:
                    multipleContainer.setVisibility(View.GONE);
                    numericContainer.setVisibility(View.GONE);
                    booleanContainer.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
}
