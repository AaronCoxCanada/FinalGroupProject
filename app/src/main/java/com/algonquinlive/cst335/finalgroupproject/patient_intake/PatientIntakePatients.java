package com.algonquinlive.cst335.finalgroupproject.patient_intake;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.algonquinlive.cst335.finalgroupproject.R;

import java.util.ArrayList;

public class PatientIntakePatients extends Activity {

    //list view parameters
    ArrayList<String> patientList;
    ListView listView;
    Adapter messageAdapter;

    //database parameters
    PatientIntakeDatabaseHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;

    //fragment parameters
    FrameLayout frameLayout;
    boolean isTablet = false;
    PatientIntakeFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_intake_patients);

        patientList = new ArrayList<>();
        messageAdapter = new Adapter( this );
        listView = findViewById(R.id.listView);
        listView.setAdapter (messageAdapter);

        dbHelper = new PatientIntakeDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        cursor = db.rawQuery("select "+dbHelper.KEY_ID+", "+dbHelper.NAME+" from "+dbHelper.TABLE_NAME, new String[] {});
        String name;

        if (cursor.moveToFirst()){
            do {
                name = cursor.getString(cursor.getColumnIndex(dbHelper.NAME));
                patientList.add(name);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }

        handleFragment();
    }

    private void handleFragment(){
        //Check if frame layout is loaded
        frameLayout = findViewById(R.id.patient_frame);
        if (frameLayout != null)
            isTablet = true;

        //Show details of fragment
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Store bundle info
                Bundle infoToPass = new Bundle();
                infoToPass.putBoolean("isTablet", isTablet);
                infoToPass.putLong("textID", messageAdapter.getId(position));


                if (isTablet){//for a tablet
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    fragment  =  new PatientIntakeFragment();
                    fragment.setArguments( infoToPass );
                    ft.addToBackStack("Any name, not used"); //only undo FT on back button
                    ft.replace( R.id.patient_frame , fragment);
                    ft.commit();

                } else {//for a phone
                    Intent phoneIntent = new Intent (PatientIntakePatients.this, PatientIntakeForm.class);
                    phoneIntent.putExtras(infoToPass);
                    startActivityForResult(phoneIntent, 50);
                }
            }
        });
    }

    private class Adapter extends ArrayAdapter<String> {

        public Adapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){
            return patientList.size();
        }

        public String getItem(int position){
            return patientList.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){

            LayoutInflater inflater = PatientIntakePatients.this.getLayoutInflater();

            View result = null ;
            result = inflater.inflate(R.layout.patient_name, null);

            TextView message = (TextView)result.findViewById(R.id.patient_name_lv);
            message.setText(   getItem(position)  ); // get the string at position
            return result;
        }

        public long getId(int position) {
            cursor = db.rawQuery("select "+dbHelper.KEY_ID+", "+dbHelper.NAME+" from "+dbHelper.TABLE_NAME, new String[] {});
            cursor.moveToPosition(position);
            return cursor.getInt(cursor.getColumnIndex(dbHelper.KEY_ID));
        }

    }
}
