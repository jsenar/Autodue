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
    static final int dialog_id= 0;
    int hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_screen);

    }

    public void closePopUp(View view){
        this.finish();
    }

    /*@SuppressWarnings("deprecation")
    public void showTimePicker(View view){
        showDialog(dialog_id);

    }

    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id){
        switch(id){
            case dialog_id: return new TimePickerDialog(this, mTimeSetListener, hour, minute, false);
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener (){
        @Override
        public void onTimeSet (TimePicker view, int hourOfDay, int hour_minute){
            hour = hourOfDay;
            minute = hour_minute;
            String str_hour = ""+hour;
            String str_min = ""+minute;
            if( hour < 10 )
                str_hour = "0" + hour;
            if( minute < 10 )
                str_min = "0" + minute;

            String settledTime = str_hour + ":" + str_min;
            EditText txt = (EditText) findViewById(R.id.starttime);
            txt.setText(settledTime);

        }

    }; */

}
