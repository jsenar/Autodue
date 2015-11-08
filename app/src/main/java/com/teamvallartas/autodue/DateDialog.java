package com.teamvallartas.autodue;

import android.annotation.SuppressLint;
import android.app.*;
import android.app.Dialog;
import android.app.DialogFragment;
import android.widget.*;
import android.view.View;
import android.os.*;
import java.util.Calendar;

/**
 * Created by evahuynh on 11/8/15.
 */

@SuppressLint("ValidFragment")
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    EditText txtDate;

    public DateDialog( View view) {
        txtDate = (EditText) view;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog( getActivity(), this, year, month, day);

    }

    public void onDateSet( DatePicker view, int year, int month, int day){
        String date = (month+1)+"/"+day+"/"+year;
        txtDate.setText(date);
    }
}
