package com.algonquinlive.cst335.finalgroupproject.quiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.algonquinlive.cst335.finalgroupproject.R;

import java.util.ArrayList;

public class MccMainActivity extends Activity {

    final String INFO = "Multiple Choice Quiz Creator\n\nAuthor name: Truong Giang Vu\n" +
            "Version: 1.0\n\nClick ADD QUESTION button to add a new question\n";
    SQLiteDatabase db;
    Cursor results;
    ArrayList<String> questions;
    QuestionAdapter questionAdapter;
    ListView listView;
    TextView noQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcc_main);

        questions = new ArrayList<>();
        listView = findViewById(R.id.mcc_list_view);
        Button btnHelp = findViewById(R.id.mcc_main_btn_help);
        questionAdapter = new QuestionAdapter( this );
        listView.setAdapter (questionAdapter);

        noQuestion = findViewById(R.id.mcc_main_no_question);

        MccDatabaseHelper dbHelper = new MccDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        reloadQuestions();
        Button btnAdd = findViewById(R.id.mcc_main_btn_add);

        btnHelp.setOnClickListener(e -> {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(MccMainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(MccMainActivity.this);
            }
            builder.setTitle("About").setMessage(INFO).setPositiveButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_info).show();
        });
        btnAdd.setOnClickListener(e -> {
            Intent intent = new Intent(MccMainActivity.this, MccQuestionActivity.class);
            Bundle infoToPass = new Bundle();
            infoToPass.putLong(MccDatabaseHelper.KEY_ID, 0);
            intent.putExtras(infoToPass);
            startActivityForResult(intent, 50);
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle infoToPass = new Bundle();
                infoToPass.putLong(MccDatabaseHelper.KEY_ID, id);
                Intent intent = new Intent(MccMainActivity.this, MccQuestionActivity.class);
                intent.putExtras(infoToPass);
                startActivityForResult(intent, 50);
            }
        });
    }

    public void reloadQuestions() {
        results = db.query(false, MccDatabaseHelper.TABLE_NAME,
                new String[] { MccDatabaseHelper.KEY_ID, MccDatabaseHelper.KEY_QUESTION},
                null, null,
                null, null, null, null);

        questions.clear();

        int numResults = results.getCount();
        if (numResults > 0) {
            results.moveToFirst();

            for(int i = 0; i < numResults; i++) {
                int messageColumnIndex = results.getColumnIndex(MccDatabaseHelper.KEY_QUESTION);
                questions.add((i + 1) + ". "+ results.getString(messageColumnIndex));
                results.moveToNext();
            }

            noQuestion.setVisibility(View.GONE);
        }
        else {
            noQuestion.setVisibility(View.VISIBLE);
        }

        questionAdapter.notifyDataSetChanged();
    }

    public void displayStatistic() {
        Cursor cursor = db.query(false, MccDatabaseHelper.TABLE_NAME,
                new String[] { MccDatabaseHelper.KEY_ID, MccDatabaseHelper.KEY_QUESTION},
                null, null,
                null, null, null, null);

        String statistic;
        String longestQuestion = null;
        String shortestQuestion = null;
        int totalLength = 0;
        int numResults = cursor.getCount();
        if (numResults > 0) {
            cursor.moveToFirst();

            for(int i = 0; i < numResults; i++) {

                int messageColumnIndex = cursor.getColumnIndex(MccDatabaseHelper.KEY_QUESTION);
                String question = cursor.getString(messageColumnIndex);
                totalLength += question.length();
                if (longestQuestion == null) {
                    longestQuestion = question;
                }
                else {
                    if (question.length() > longestQuestion.length()) {
                        longestQuestion = question;
                    }
                }

                if (shortestQuestion == null) {
                    shortestQuestion = question;
                }
                else {
                    if (question.length() < shortestQuestion.length()) {
                        shortestQuestion = question;
                    }
                }

                cursor.moveToNext();
            }

            statistic = "There are total " + numResults + " questions\n " +
                    "Longest question length: " + longestQuestion.length() + " characters\n" +
                    "Shortest question length: " + shortestQuestion.length() + " characters\n" +
                    "Average length: " + totalLength / numResults + " characters\n";
        }
        else {
            statistic = "There is no question";
        }

        Toast toast = Toast.makeText(this , statistic, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onActivityResult( int requestCode, int resultCode, Intent data)
    {
        reloadQuestions();
        if(requestCode == 50) {
            String message;
            if (resultCode == Activity.RESULT_OK) {
                message = "Created a new question!";
            }
            else if (resultCode == 15) {
                message = "Question updated successfully!";
            }
            else if (resultCode == 5) {
                message = "Deleted a question!";
            }
            else {
                message = null;
            }

            if (message != null) {
                Toast toast = Toast.makeText(this , message, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }


    private class QuestionAdapter extends ArrayAdapter {

        public QuestionAdapter(Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount() {
            return questions.size();
        }

        @Override
        public String getItem(int position) {
            return questions.get(position);
        }

        //@Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = MccMainActivity.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.activity_mcc_row_question, null);
            TextView message = result.findViewById(R.id.mcc_row_question);
            message.setText(getItem(position));
            return result;
        }

        @Override
        public long getItemId(int position) {
            results = db.query(false, MccDatabaseHelper.TABLE_NAME,
                    new String[] { MccDatabaseHelper.KEY_ID, MccDatabaseHelper.KEY_QUESTION},
                    null, null,
                    null, null, null, null);

            results.moveToPosition(position);

            return results.getLong(results.getColumnIndex(MccDatabaseHelper.KEY_ID));
        }

    }
}
