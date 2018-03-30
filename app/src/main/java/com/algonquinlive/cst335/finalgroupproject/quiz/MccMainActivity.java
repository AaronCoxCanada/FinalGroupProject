package com.algonquinlive.cst335.finalgroupproject.quiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.algonquinlive.cst335.finalgroupproject.R;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MccMainActivity extends Activity {

    final String INFO = "Multiple Choice Quiz Creator\n\nAuthor name: Truong Giang Vu\n" +
            "Version: 1.0\n\nClick ADD QUESTION button to add a new question\n";
    SQLiteDatabase db;
    Cursor results;
    ArrayList<String> questions;
    QuestionAdapter questionAdapter;
    ListView listView;
    TextView noQuestion;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcc_main);

        questions = new ArrayList<>();
        listView = findViewById(R.id.mcc_list_view);
        questionAdapter = new QuestionAdapter( this );
        listView.setAdapter (questionAdapter);

        noQuestion = findViewById(R.id.mcc_main_no_question);

        MccDatabaseHelper dbHelper = new MccDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        reloadQuestions();
        ImageButton btnAdd = findViewById(R.id.mcc_main_btn_add);

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

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mcc_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mcc_download:
                download();
                break;
            case R.id.mcc_info:
                showDialog("About", INFO);
                break;
            case R.id.mcc_summary:
                showDialog("Statistic", getStatistic());
                break;
        }
        return true;
    }

    private void download() {
        LayoutInflater inflater = MccMainActivity.this.getLayoutInflater();
        View downloadLayout = inflater.inflate(R.layout.activity_mcc_download_layout, null);
        EditText txtUrl = downloadLayout.findViewById(R.id.mcc_url);


        AlertDialog.Builder builder = new AlertDialog.Builder(MccMainActivity.this);
        builder.setMessage("Download Questions from URL")
                .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        url = txtUrl.getText().toString();
                        new DownloadQuestions().execute();
                    }
                })
                .setView(downloadLayout)
                .setNegativeButton(R.string.mcc_question_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });
        builder.create().show();
    }

    private void showDialog(String title, String message) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(MccMainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(MccMainActivity.this);
        }
        builder.setTitle(title).setMessage(message).setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_info).show();
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

    public String getStatistic() {
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

            statistic = "There are total " + numResults + " questions\n\n " +
                    "Longest question: " + longestQuestion.length() + " characters\n" +
                    "Shortest question: " + shortestQuestion.length() + " characters\n" +
                    "Average length: " + totalLength / numResults + " characters\n";
        }
        else {
            statistic = "There is no question";
        }

        return statistic;
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

    private class DownloadQuestions extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                InputStream in = conn.getInputStream();
//                try {
//                    XmlPullParser parser = Xml.newPullParser();
//                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
//                    parser.setInput(in, null);
//
//                    int type;
//                    //While you're not at the end of the document:
//                    while((type = parser.getEventType()) != XmlPullParser.END_DOCUMENT)
//                    {
//                        //Are you currently at a Start Tag?
//                        if(parser.getEventType() == XmlPullParser.START_TAG)
//                        {
//                            if(parser.getName().equals("temperature") )
//                            {
//                                currentTemp = parser.getAttributeValue(null, "value");
//                                publishProgress(25);
//                                minTemp = parser.getAttributeValue(null, "min");
//                                publishProgress(50);
//                                maxTemp = parser.getAttributeValue(null, "max");
//                                publishProgress(75);
//                            }
//                            else if (parser.getName().equals("weather")) {
//                                String iconName = parser.getAttributeValue(null, "icon");
//                                String fileName = iconName + ".png";
//
//                                Log.i(ACTIVITY_NAME,"Looking for file: " + fileName);
//                                if (fileExistance(fileName)) {
//                                    FileInputStream fis = null;
//                                    try {
//                                        fis = openFileInput(fileName);
//
//                                    }
//                                    catch (FileNotFoundException e) {
//                                        e.printStackTrace();
//                                    }
//                                    Log.i(ACTIVITY_NAME,"Found the file locally");
//                                    picture = BitmapFactory.decodeStream(fis);
//                                }
//                                else {
//                                    String iconUrl = "https://openweathermap.org/img/w/" + fileName;
//                                    picture = getImage(new URL(iconUrl));
//
//                                    FileOutputStream outputStream = openFileOutput( fileName, Context.MODE_PRIVATE);
//                                    picture.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
//                                    Log.i(ACTIVITY_NAME,"Downloaded the file from the Internet");
//                                    outputStream.flush();
//                                    outputStream.close();
//                                }
//                                publishProgress(100);
//                            }
//                            else if (parser.getName().equals("wind")) {
//                                parser.nextTag();
//                                if(parser.getName().equals("speed") )
//                                {
//                                    windSpeed = parser.getAttributeValue(null, "value");
//                                }
//                            }
//                        }
//                        // Go to the next XML event
//                        parser.next();
//                    }
//                } finally {
//                    in.close();
//                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }

            return "";

        }

        @Override
        protected void onPostExecute(String a) {
//            progressBar.setVisibility(View.INVISIBLE);
//            imageView.setImageBitmap(picture);
//            current_temp.setText(currentTemp);
//            min_temp.setText(minTemp);
//            max_temp.setText(maxTemp);
//            wind_speed.setText(windSpeed);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
//            progressBar.setProgress(values[0]);
        }


    }
}
