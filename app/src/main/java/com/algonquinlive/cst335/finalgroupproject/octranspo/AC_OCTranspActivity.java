package com.algonquinlive.cst335.finalgroupproject.octranspo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.algonquinlive.cst335.finalgroupproject.R;

import java.util.ArrayList;

public class AC_OCTranspActivity extends AppCompatActivity {

    private EditText searchField;
    private Button searchButton;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;

    private ListView searchHistoryListView;
    private ArrayList<String> busHistoryArray;
    private BusHistoryAdapter busHistoryAdapter;
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_octranspo);
        setupViews();

        busHistoryAdapter = new BusHistoryAdapter(this);
        busHistoryArray = new ArrayList<>();
        searchHistoryListView.setAdapter(busHistoryAdapter);

        db = new BusHistoryDBHelper(this).getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + BusHistoryDBHelper.TABLE_NAME, null, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            busHistoryArray.add(cursor.getString(cursor.getColumnIndex(BusHistoryDBHelper.KEY_BUS_NUMBER)));
            cursor.moveToNext();
        }
        busHistoryAdapter.notifyDataSetChanged();

        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    private void setupViews() {
        searchButton            = findViewById(R.id.searchButton);
        searchField             = findViewById(R.id.busSearchText);
        searchHistoryListView   = findViewById(R.id.historyList);
        searchHistoryListView   = findViewById(R.id.historyList);
        progressBar             = findViewById(R.id.progressBar);
        linearLayout            = findViewById(R.id.main_layout);

        searchButton.setOnClickListener((View e)->{
            String str = searchField.getText().toString();
            busHistoryArray.add(str);
            ContentValues cv = new ContentValues();
            cv.put(BusHistoryDBHelper.KEY_BUS_NUMBER, str);
            db.insert(BusHistoryDBHelper.TABLE_NAME, null, cv);
            busHistoryAdapter.notifyDataSetChanged();

            str = "Bus stop #" + str + " added.";
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

            searchField.setText(null);
        });

        searchHistoryListView.setOnItemClickListener((AdapterView<?> parent, View view, int pos, long id) ->{
            String str = "Retrieving Info for Bus Stop #" + busHistoryArray.get(pos) + "...";
            Snackbar snackbar = Snackbar.make(linearLayout, str, Snackbar.LENGTH_SHORT);
            snackbar.show();

        });

        searchHistoryListView.setOnItemLongClickListener((AdapterView<?> parent, View view, int pos, long id) ->{
            String str = "Are you sure you want to remove #" + busHistoryArray.get(pos) + " from List";

            AlertDialog.Builder builder = new AlertDialog.Builder(AC_OCTranspActivity.this);
            builder.setMessage(str)
                    .setPositiveButton("Yes",
                    (dialog, which) -> {
                        Log.i(getApplicationContext().getPackageName(), "Which = " + which);
                        //TODO delete code here
                    })
                    .setNegativeButton("No",
                    (dialog, which) -> Log.i(getApplicationContext().getPackageName(), "Which = " + which))
                    .create().show();

            return true;
        });


        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(null);
            actionBar.setIcon(null);
        }

    }

    private class BusHistoryAdapter extends ArrayAdapter<String> {

        BusHistoryAdapter(Context ctx){
            //context, and layout ID
            super(ctx, android.R.layout.simple_list_item_1);
        }


        @Override
        public int getCount() {
            return busHistoryArray.size();
        }

        @Override
        public String getItem(int position) {
            return busHistoryArray.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }
}
