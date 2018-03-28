package com.algonquinlive.cst335.finalgroupproject.patient_intake;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.support.design.widget.Snackbar;
import android.support.design.widget.CoordinatorLayout;

import com.algonquinlive.cst335.finalgroupproject.R;

public class PatientIntakeImport extends Activity {

    Button downloadButton;
    Boolean success = false;
    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_intake_import);

        downloadButton = findViewById(R.id.PatientIntakeImport_download);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String snackbarString;

                if (success)
                    snackbarString = "Xml File Downloaded";
                else
                    snackbarString = "Error: XML fild could not be downloaded";

/*               coordinatorLayout = (CoordinatorLayout) findViewById(R.id.PatientIntakeImport_coordinator);
                Snackbar bar = Snackbar.make(coordinatorLayout, snackbarString, Snackbar.LENGTH_LONG);
                bar.show();*/
            }

        });
    }


}
