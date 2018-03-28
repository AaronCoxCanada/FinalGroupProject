package com.algonquinlive.cst335.finalgroupproject.patient_intake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.algonquinlive.cst335.finalgroupproject.R;

public class PatientIntakeStart extends Activity {

    //activity name
    protected static final String ACTIVITY_NAME = "PatientIntakeStart";

    //class variables
    Button form;
    Button importXml;
    Button patients;
    Button stats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_intake_start);

        form = findViewById(R.id.PatientIntakeStart_form);
        form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent formIntent = new Intent(PatientIntakeStart.this, PatientIntakeForm.class);
                startActivity(formIntent);
                Log.i("PatientIntakeStart", "started patient application form");
            }
        });

        importXml = findViewById(R.id.PatientIntakeStart_import);
        importXml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent formIntent = new Intent(PatientIntakeStart.this, PatientIntakeImport.class);
                startActivity(formIntent);
                Log.i("PatientIntakeStart", "started patient import");
            }
        });

        Button patients = findViewById(R.id.PatientIntakeStart_patients);
        patients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent formIntent = new Intent(PatientIntakeStart.this, PatientIntakePatients.class);
                startActivity(formIntent);
                Log.i("PatientIntakeStart", "patient list viewed");
            }
        });
    }
}
