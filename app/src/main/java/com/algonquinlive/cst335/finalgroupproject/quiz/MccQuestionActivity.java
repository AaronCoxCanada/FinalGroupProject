package com.algonquinlive.cst335.finalgroupproject.quiz;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.algonquinlive.cst335.finalgroupproject.R;

public class MccQuestionActivity extends Activity {

    MccFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcc_question_container);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragment = new MccFragment();
            fragment.setArguments(extras);
            fragmentTransaction.replace(R.id.space_replace, fragment);
            fragmentTransaction.commit();
        }
    }


    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        if (checked) {
            switch(view.getId()) {
                case R.id.mcc_question_rad_multiple:
                    fragment.setRadioVisibility("multiple");
                    break;
                case R.id.mcc_question_rad_numeric:
                    fragment.setRadioVisibility("numeric");
                    break;
                case R.id.mcc_question_rad_boolean:
                    fragment.setRadioVisibility("boolean");
                    break;
            }
        }
    }
}
