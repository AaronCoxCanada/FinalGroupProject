package com.algonquinlive.cst335.finalgroupproject.patient_intake;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.algonquinlive.cst335.finalgroupproject.R;

public class PatientIntakeForm extends Activity {

    //activity name
    protected static final String ACTIVITY_NAME = "PatientIntakeForm";

    //class variables
    RadioGroup patientTypeRadio;
    int selectedRadioButton;
    Button submitButton;
    GridLayout doctorGrid;
    GridLayout dentistGrid;
    GridLayout optmoteristGrid;

    PatientIntakeDatabaseHelper dbHelper;
    SQLiteDatabase db;
    ContentValues cv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_intake_form);

        submitButton = findViewById(R.id.PatientIntakeForm_submit);
        doctorGrid = findViewById(R.id.doctor_grid);
        dentistGrid = findViewById(R.id.dentist_grid);;
        optmoteristGrid = findViewById(R.id.optometrist_grid);

        //add listener to radio group
        patientTypeRadio = findViewById(R.id.patient_type);
        patientTypeRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.doctor:
                        doctorGrid.setVisibility(View.VISIBLE);
                        dentistGrid.setVisibility(View.GONE);
                        optmoteristGrid.setVisibility(View.GONE);
                        submitButton.setVisibility(View.VISIBLE);
                        selectedRadioButton=1;
                        break;
                    case R.id.dentist:
                        doctorGrid.setVisibility(View.GONE);
                        dentistGrid.setVisibility(View.VISIBLE);
                        optmoteristGrid.setVisibility(View.GONE);
                        submitButton.setVisibility(View.VISIBLE);
                        selectedRadioButton=2;
                        break;
                    case R.id.optometrist:
                        doctorGrid.setVisibility(View.GONE);
                        dentistGrid.setVisibility(View.GONE);
                        optmoteristGrid.setVisibility(View.VISIBLE);
                        submitButton.setVisibility(View.VISIBLE);
                        selectedRadioButton=3;
                        break;
                }
            }
        });

        dbHelper = new PatientIntakeDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();
        cv = new ContentValues();

        //add listener to next button
        submitButton = findViewById(R.id.PatientIntakeForm_submit);
        submitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = "Error: please add all required information";
                boolean valid = false;

                String name = ((EditText)findViewById(R.id.patient_name)).getText().toString(); cv.put(PatientIntakeDatabaseHelper.NAME, name);
                String address = ((EditText)findViewById(R.id.patient_address)).getText().toString(); cv.put(PatientIntakeDatabaseHelper.ADDRESS, address);
                String birthday = ((EditText)findViewById(R.id.patient_birthday)).getText().toString(); cv.put(PatientIntakeDatabaseHelper.BDAY, birthday);
                String phoneNumber = ((EditText)findViewById(R.id.patient_phone)).getText().toString(); cv.put(PatientIntakeDatabaseHelper.PHONE, phoneNumber);
                String healthCard = ((EditText)findViewById(R.id.patient_card)).getText().toString(); cv.put(PatientIntakeDatabaseHelper.CARD, healthCard);
                String description = ((EditText)findViewById(R.id.patient_description)).getText().toString(); cv.put(PatientIntakeDatabaseHelper.DESCRIPTION, description);

                if (selectedRadioButton == 1) {
                    String previousSurgery = ((EditText)findViewById(R.id.patient_surgeries)).getText().toString(); cv.put(PatientIntakeDatabaseHelper.SURGERIES, previousSurgery);
                    String allergies = ((EditText)findViewById(R.id.patient_allergies)).getText().toString(); cv.put(PatientIntakeDatabaseHelper.ALLERGIES, allergies);
                }
                if (selectedRadioButton == 2) {
                    String glassesBought = ((EditText)findViewById(R.id.patient_glassesdate)).getText().toString(); cv.put(PatientIntakeDatabaseHelper.GLASSES, glassesBought);
                    String glassesStore = ((EditText)findViewById(R.id.patient_store)).getText().toString(); cv.put(PatientIntakeDatabaseHelper.STORE, glassesStore);
                }
                if (selectedRadioButton == 3) {
                    String benefits = ((EditText)findViewById(R.id.patient_benefits)).getText().toString(); cv.put(PatientIntakeDatabaseHelper.BENEFITS, benefits);
                    String hadBraces = ((CheckBox)findViewById(R.id.patient_braces)).isChecked()?"Yes":"No"; cv.put(PatientIntakeDatabaseHelper.BRACES, hadBraces);
                }

                if (valid){

                    try {
                        db.insert(PatientIntakeDatabaseHelper.TABLE_NAME, null, cv);
                        cv.clear();
                        Log.i("Patient Form - added", name);
                        message = name +" successfully added";
                    } catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }

                Toast toast = Toast.makeText(PatientIntakeForm.this, message, Toast.LENGTH_SHORT);
                toast.show();

            }

        });
    }
}
