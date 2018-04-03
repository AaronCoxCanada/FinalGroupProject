package com.algonquinlive.cst335.finalgroupproject.quiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.algonquinlive.cst335.finalgroupproject.R;

/**
 * Created by giang on 4/3/18.
 */

public class MccFragment extends Fragment {

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

    public void onCreate(Bundle b)
    {
        super.onCreate(b);
        Id = getArguments().getLong(MccDatabaseHelper.KEY_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View gui = inflater.inflate(R.layout.activity_mcc_question, null);

        multipleContainer = gui.findViewById(R.id.mcc_question_container_multiple);
        numericContainer = gui.findViewById(R.id.mcc_question_container_numeric);
        booleanContainer = gui.findViewById(R.id.mcc_question_container_boolean);

        radMultiple = gui.findViewById(R.id.mcc_question_rad_multiple);
        radMNumeric = gui.findViewById(R.id.mcc_question_rad_numeric);
        radBoolean = gui.findViewById(R.id.mcc_question_rad_boolean);

        radA = gui.findViewById(R.id.mcc_question_rad_A);
        radB = gui.findViewById(R.id.mcc_question_rad_B);
        radC = gui.findViewById(R.id.mcc_question_rad_C);
        radD = gui.findViewById(R.id.mcc_question_rad_D);

        radTrue = gui.findViewById(R.id.mcc_question_rad_true);
        radFalse = gui.findViewById(R.id.mcc_question_rad_false);

        txtQuestion = gui.findViewById(R.id.mcc_question_txt_question);
        btnSave = gui.findViewById(R.id.mcc_question_btn_save);
        btnCancel = gui.findViewById(R.id.mcc_question_btn_cancel);
        btnDelete = gui.findViewById(R.id.mcc_question_btn_delete);
        numericAnswer = gui.findViewById(R.id.mcc_question_txt_numeric_answer);
        numericDelta = gui.findViewById(R.id.mcc_question_txt_delta);

        txtOptionA = gui.findViewById(R.id.txt_option_A);
        txtOptionB = gui.findViewById(R.id.txt_option_B);
        txtOptionC = gui.findViewById(R.id.txt_option_C);
        txtOptionD = gui.findViewById(R.id.txt_option_D);

        MccDatabaseHelper dbHelper = new MccDatabaseHelper(getActivity());
        db = dbHelper.getWritableDatabase();


        if (Id == 0) {
            radMultiple.setChecked(true);
            setRadioVisibility("multiple");
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
                setRadioVisibility("multiple");
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
                setRadioVisibility("numeric");
                numericAnswer.setText(cursor.getString(cursor.getColumnIndex(MccDatabaseHelper.KEY_NUMERIC_ANSWER)));
                numericDelta.setText(cursor.getString(cursor.getColumnIndex(MccDatabaseHelper.KEY_NUMERIC_DELTA)));
            }
            else {
                radBoolean.setChecked(true);
                setRadioVisibility("boolean");
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
                getActivity().setResult(Activity.RESULT_OK, resultIntent);
            }
            else {
                db.update(MccDatabaseHelper.TABLE_NAME, newData,
                        MccDatabaseHelper.KEY_ID + "=?", new String[] {String.valueOf(Id)} );
                getActivity().setResult(15, resultIntent);
            }

            getActivity().finish();
        });

        btnCancel.setOnClickListener(e -> getActivity().finish());

        btnDelete.setOnClickListener(e -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Are you sure you want to delete this question?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    db.delete(MccDatabaseHelper.TABLE_NAME, MccDatabaseHelper.KEY_ID + "=?",
                            new String[] {String.valueOf(Id)});
                    getActivity().setResult(5, new Intent());
                    getActivity().finish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) { }
            });

            builder.create().show();
        });

        return gui;
    }

    public void setRadioVisibility(String name) {
        if(name.equals("multiple")) {
            multipleContainer.setVisibility(View.VISIBLE);
            numericContainer.setVisibility(View.GONE);
            booleanContainer.setVisibility(View.GONE);
        }
        else if (name.equals("numeric")) {
            multipleContainer.setVisibility(View.GONE);
            numericContainer.setVisibility(View.VISIBLE);
            booleanContainer.setVisibility(View.GONE);
        }
        else {
            multipleContainer.setVisibility(View.GONE);
            numericContainer.setVisibility(View.GONE);
            booleanContainer.setVisibility(View.VISIBLE);
        }
    }


}
