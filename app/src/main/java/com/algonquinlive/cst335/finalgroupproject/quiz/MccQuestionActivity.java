package com.algonquinlive.cst335.finalgroupproject.quiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.algonquinlive.cst335.finalgroupproject.R;
import com.algonquinlive.cst335.finalgroupproject.quiz.MccDatabaseHelper;

public class MccQuestionActivity extends Activity {

    private SQLiteDatabase db;
    private LinearLayout multipleContainer;
    private LinearLayout numericContainer;
    private LinearLayout booleanContainer;
    private TextView txtQuestion;
    private Button btnSave;
    private Button btnCancel;
    private Button btnDelete;
    private RadioButton radMultiple;
    private RadioButton radMNumeric;
    private RadioButton radBoolean;
    private RadioButton radA;
    private RadioButton radB;
    private RadioButton radC;
    private RadioButton radD;
    private RadioButton radTrue;
    private RadioButton radFalse;
    private EditText numericAnswer;
    private EditText numericDelta;
    private EditText txtOptionA;
    private EditText txtOptionB;
    private EditText txtOptionC;
    private EditText txtOptionD;

    private final static String TRUE = "true";
    private final static String FALSE = "false";

    private final static String MULTIPLE = "multiple";
    private final static String NUMERIC = "numeric";
    private final static String BOOLEAN = "boolean";

    private final static String REQUIRED = "Required";

    private final static String A = "A";
    private final static String B = "B";
    private final static String C = "C";
    private final static String D = "D";

    private long Id;
    private String question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcc_question);

        multipleContainer = findViewById(R.id.mcc_question_container_multiple);
        numericContainer = findViewById(R.id.mcc_question_container_numeric);
        booleanContainer = findViewById(R.id.mcc_question_container_boolean);

        radMultiple = findViewById(R.id.mcc_question_rad_multiple);
        radMNumeric = findViewById(R.id.mcc_question_rad_numeric);
        radBoolean = findViewById(R.id.mcc_question_rad_boolean);

        radA = findViewById(R.id.mcc_question_rad_A);
        radB = findViewById(R.id.mcc_question_rad_B);
        radC = findViewById(R.id.mcc_question_rad_C);
        radD = findViewById(R.id.mcc_question_rad_D);

        radTrue = findViewById(R.id.mcc_question_rad_true);
        radFalse = findViewById(R.id.mcc_question_rad_false);

        txtQuestion = findViewById(R.id.mcc_question_txt_question);
        btnSave = findViewById(R.id.mcc_question_btn_save);
        btnCancel = findViewById(R.id.mcc_question_btn_cancel);
        btnDelete = findViewById(R.id.mcc_question_btn_delete);
        numericAnswer = findViewById(R.id.mcc_question_txt_numeric_answer);
        numericDelta = findViewById(R.id.mcc_question_txt_delta);

        txtOptionA = findViewById(R.id.txt_option_A);
        txtOptionB = findViewById(R.id.txt_option_B);
        txtOptionC = findViewById(R.id.txt_option_C);
        txtOptionD = findViewById(R.id.txt_option_D);

        MccDatabaseHelper dbHelper = new MccDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        Bundle extras = getIntent().getExtras();
        Id = extras.getLong(MccDatabaseHelper.KEY_ID);

        if (Id == 0) {
            radMultiple.setChecked(true);
            onRadioButtonClicked(radMultiple);
            btnDelete.setVisibility(View.GONE);
        }
        else {
            btnDelete.setVisibility(View.VISIBLE);
            Cursor cursor = db.query(false, MccDatabaseHelper.TABLE_NAME,
                    new String[] { MccDatabaseHelper.KEY_ID, MccDatabaseHelper.KEY_QUESTION , MccDatabaseHelper.KEY_TYPE,
                    MccDatabaseHelper.KEY_BOOLEAN_ANSWER, MccDatabaseHelper.KEY_MULTIPLE_ANSWER, MccDatabaseHelper.KEY_NUMERIC_ANSWER,
                    MccDatabaseHelper.KEY_NUMERIC_DELTA, MccDatabaseHelper.KEY_MULTIPLE_OPTION_A, MccDatabaseHelper.KEY_MULTIPLE_OPTION_B,
                    MccDatabaseHelper.KEY_MULTIPLE_OPTION_C, MccDatabaseHelper.KEY_MULTIPLE_OPTION_D},
                    MccDatabaseHelper.KEY_ID + "=?", new String[] { String.valueOf(Id) },
                    null, null, null, null);
            cursor.moveToFirst();
            question = cursor.getString(cursor.getColumnIndex(MccDatabaseHelper.KEY_QUESTION));
            txtQuestion.setText(question);

            String type = cursor.getString(cursor.getColumnIndex(MccDatabaseHelper.KEY_TYPE));
            if (type.equals(MULTIPLE)) {
                radMultiple.setChecked(true);
                onRadioButtonClicked(radMultiple);
                txtOptionA.setText(cursor.getString(cursor.getColumnIndex(MccDatabaseHelper.KEY_MULTIPLE_OPTION_A)));
                txtOptionB.setText(cursor.getString(cursor.getColumnIndex(MccDatabaseHelper.KEY_MULTIPLE_OPTION_B)));
                txtOptionC.setText(cursor.getString(cursor.getColumnIndex(MccDatabaseHelper.KEY_MULTIPLE_OPTION_C)));
                txtOptionD.setText(cursor.getString(cursor.getColumnIndex(MccDatabaseHelper.KEY_MULTIPLE_OPTION_D)));

                String answer = cursor.getString(cursor.getColumnIndex(MccDatabaseHelper.KEY_MULTIPLE_ANSWER));
                if (answer.equals(A)) {
                    radA.setChecked(true);
                }
                else if (answer.equals(B)) {
                    radB.setChecked(true);
                }
                else if (answer.equals(C)) {
                    radC.setChecked(true);
                }
                else {
                    radD.setChecked(true);
                }
            }
            else if (type.equals(NUMERIC)) {
                radMNumeric.setChecked(true);
                onRadioButtonClicked(radMNumeric);
                numericAnswer.setText(cursor.getString(cursor.getColumnIndex(MccDatabaseHelper.KEY_NUMERIC_ANSWER)));
                numericDelta.setText(cursor.getString(cursor.getColumnIndex(MccDatabaseHelper.KEY_NUMERIC_DELTA)));
            }
            else {
                radBoolean.setChecked(true);
                onRadioButtonClicked(radBoolean);
                String answer = cursor.getString(cursor.getColumnIndex(MccDatabaseHelper.KEY_BOOLEAN_ANSWER));
                if (answer.equals(TRUE)) {
                    radTrue.setChecked(true);
                }
                else {
                    radFalse.setChecked(true);
                }
            }
        }


        btnSave.setOnClickListener(e -> {
            String question = txtQuestion.getText().toString();
            if (question.trim().isEmpty()) {
                txtQuestion.setError("Required");
                return;
            }

            ContentValues newData = new ContentValues();
            newData.put(MccDatabaseHelper.KEY_QUESTION, question);

            if (radMultiple.isChecked()) {
                String optionA = txtOptionA.getText().toString();
                String optionB = txtOptionB.getText().toString();
                String optionC = txtOptionC.getText().toString();
                String optionD = txtOptionD.getText().toString();
                if (optionA.trim().isEmpty()) {
                    txtOptionA.setError(REQUIRED);
                    return;
                }
                if (optionB.trim().isEmpty()) {
                    txtOptionB.setError(REQUIRED);
                    return;
                }
                if (optionC.trim().isEmpty()) {
                    txtOptionC.setError(REQUIRED);
                    return;
                }
                if (optionD.trim().isEmpty()) {
                    txtOptionD.setError(REQUIRED);
                    return;
                }

                newData.put(MccDatabaseHelper.KEY_TYPE, MULTIPLE);
                newData.put(MccDatabaseHelper.KEY_MULTIPLE_OPTION_A, optionA);
                newData.put(MccDatabaseHelper.KEY_MULTIPLE_OPTION_B, optionB);
                newData.put(MccDatabaseHelper.KEY_MULTIPLE_OPTION_C, optionC);
                newData.put(MccDatabaseHelper.KEY_MULTIPLE_OPTION_D, optionD);

                String answer;
                if (radA.isChecked()) {
                    answer = A;
                }
                else if (radB.isChecked()) {
                    answer = B;
                }
                else if (radC.isChecked()) {
                    answer = C;
                }
                else {
                    answer = D;
                }
                newData.put(MccDatabaseHelper.KEY_MULTIPLE_ANSWER, answer);
            }
            else if (radMNumeric.isChecked()) {
                String numAnswer = numericAnswer.getText().toString();
                String numDelta = numericDelta.getText().toString();
                if (numAnswer.trim().isEmpty()) {
                    numericAnswer.setError(REQUIRED);
                    return;
                }

                if (numDelta.trim().isEmpty()) {
                    numericDelta.setError(REQUIRED);
                    return;
                }
                newData.put(MccDatabaseHelper.KEY_TYPE, NUMERIC);
                newData.put(MccDatabaseHelper.KEY_NUMERIC_ANSWER, numAnswer);
                newData.put(MccDatabaseHelper.KEY_NUMERIC_DELTA, numDelta);
            }
            else {
                newData.put(MccDatabaseHelper.KEY_TYPE, BOOLEAN);
                newData.put(MccDatabaseHelper.KEY_BOOLEAN_ANSWER, radTrue.isChecked() ? TRUE : FALSE);
            }

            Intent resultIntent = new Intent();
            if (Id == 0) {
                db.insert(MccDatabaseHelper.TABLE_NAME, null, newData);
                setResult(Activity.RESULT_OK, resultIntent);
            }
            else {
                db.update(MccDatabaseHelper.TABLE_NAME, newData,
                        MccDatabaseHelper.KEY_ID + "=?", new String[] {String.valueOf(Id)} );
                setResult(15, resultIntent);
            }

            finish();
        });

        btnCancel.setOnClickListener(e -> finish());

        btnDelete.setOnClickListener(e -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MccQuestionActivity.this);
            builder.setTitle("Are you sure you want to delete this question?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    db.delete(MccDatabaseHelper.TABLE_NAME, MccDatabaseHelper.KEY_ID + "=?",
                            new String[] {String.valueOf(Id)});
                    setResult(5, new Intent());
                    finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) { }
            });

            builder.create().show();
        });
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
