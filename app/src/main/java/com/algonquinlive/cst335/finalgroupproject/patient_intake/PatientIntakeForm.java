package com.algonquinlive.cst335.finalgroupproject.patient_intake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
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
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_intake_form);

        //add listener to radio group
        patientTypeRadio = findViewById(R.id.patient_type);
        patientTypeRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case -1:
                        selectedRadioButton=0; break;
                    case R.id.doctor:
                        selectedRadioButton=1; break;
                    case R.id.dentist:
                        selectedRadioButton=2; break;
                    case R.id.optometrist:
                        selectedRadioButton=3; break;
                }
            }
        });

        //add listener to next button
        nextButton = findViewById(R.id.PatientIntakeForm_next);
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedRadioButton == 0) {
                    Toast toast = Toast.makeText(PatientIntakeForm.this, "Please select the type of form", Toast.LENGTH_SHORT);
                    toast.show();
                }
                if (selectedRadioButton == 1)
                    startActivity(new Intent(PatientIntakeForm.this, PatientIntakeFormDoctor.class));
                if (selectedRadioButton == 2)
                    startActivity(new Intent(PatientIntakeForm.this, PatientIntakeFormDentist.class));
                if (selectedRadioButton == 3)
                    startActivity(new Intent(PatientIntakeForm.this, PatientIntakeFormOptometrist.class));
            }

        });
    }
}
