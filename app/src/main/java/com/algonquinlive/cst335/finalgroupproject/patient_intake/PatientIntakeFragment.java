package com.algonquinlive.cst335.finalgroupproject.patient_intake;

/**
 * Created by zmswe on 2018-04-05.
 */

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.algonquinlive.cst335.finalgroupproject.R;

public class PatientIntakeFragment extends Fragment {

    Button deleteButton;

    boolean isTablet;
    long ID;

    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);
        Bundle infoToPass = getArguments();
        isTablet = (Boolean)infoToPass.get("isTablet");
        ID = (long)infoToPass.get("textID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View gui = inflater.inflate(R.layout.activity_patient_intake_form, null);
/*        textMessage = (TextView) gui.findViewById(R.id.textMessage);
        textMessage.setText(text);
        textID = (TextView) gui.findViewById(R.id.textID);
        textID.setText(String.valueOf(ID));*/

/*        deleteButton = (Button) gui.findViewById(R.id.patient_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (isTablet){
                    //((PatientIntakePatients) getActivity()).deleteMessage(ID);
                } else {
                    Intent delIntent = new Intent(getActivity(), PatientIntakePatients.class);
                    delIntent.putExtra("ID", ID);
                    getActivity().setResult(Activity.RESULT_OK, delIntent);
                    getActivity().finish();
                }
            }
        });*/

        return gui;
    }

}