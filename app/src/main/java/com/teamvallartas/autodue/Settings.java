package com.teamvallartas.autodue;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.Window;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import com.grokkingandroid.samplesapp.samples.recyclerviewdemo.R;

/**
 * Created by evahuynh on 11/23/15.
 */
public class Settings extends Activity {
    static final int dialog_id1= 1;
    static final int dialog_id2= 2;
    int starthr, startmin, endhr, endmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_screen);

    }

    public void closePopUp(View view){
        this.finish();
    }

    @SuppressWarnings("deprecation")
    public void showTimePicker1(View view){
        showDialog(dialog_id1);

    }

    @SuppressWarnings("deprecation")
    public void showTimePicker2(View view){
        showDialog(dialog_id2);

    }

    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id){
        switch(id){
            case dialog_id1: return new TimePickerDialog(this, mTimeSetListener1, starthr, startmin, false);
            case dialog_id2: return new TimePickerDialog(this, mTimeSetListener2, endhr, endmin, false);
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener mTimeSetListener1 = new TimePickerDialog.OnTimeSetListener (){
        @Override
        public void onTimeSet (TimePicker view, int hourOfDay, int hour_minute){
            starthr = hourOfDay;
            startmin = hour_minute;
            String str_hour = ""+starthr;
            String str_min = ""+startmin;
            if( starthr < 10 )
                str_hour = "0" + starthr;
            if( startmin < 10 )
                str_min = "0" + startmin;

            String settledTime = str_hour + ":" + str_min;
            EditText txt = (EditText) findViewById(R.id.starttime);
            txt.setText(settledTime);
            com.teamvallartas.autodue.Calendar.setOffLimitsStart(starthr,startmin);

        }

    };

    private TimePickerDialog.OnTimeSetListener mTimeSetListener2 = new TimePickerDialog.OnTimeSetListener (){
        @Override
        public void onTimeSet (TimePicker view, int hourOfDay, int hour_minute){
            endhr = hourOfDay;
            endmin = hour_minute;
            String str_hour = ""+endhr;
            String str_min = ""+endmin;
            if( endhr< 10 )
                str_hour = "0" + endhr;
            if( endmin< 10 )
                str_min = "0" + endmin;

            String settledTime = str_hour + ":" + str_min;
            EditText txt = (EditText) findViewById(R.id.endtime);
            txt.setText(settledTime);
            com.teamvallartas.autodue.Calendar.setOffLimitsEnd(endhr, endmin);
        }

    };

}
