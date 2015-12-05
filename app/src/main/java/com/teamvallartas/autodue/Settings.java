package com.teamvallartas.autodue;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import com.grokkingandroid.samplesapp.samples.recyclerviewdemo.R;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by evahuynh on 11/23/15.
 */
public class Settings extends Activity {
    static final int dialog_id1= 1;
    static final int dialog_id2= 2;
    static int starthr, startmin, endhr, endmin;
    public static final String OfflinePREFERENCES = "MyPrefs" ;
    public static final String StartHr = "StartHr";
    public static final String StartMin = "StartMin";
    public static final String EndHr = "EndHour";
    public static final String EndMin = "EndMin";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_screen);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width), (int) (height));
        getOffLimit();
    }

    public void closePopUp(View view){
        this.finish();
    }

    public void setOffLimit(View view){
        sharedpreferences = getSharedPreferences(OfflinePREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(EndHr, endhr);
        editor.putInt(EndMin, endmin);
        editor.putInt(StartHr, starthr);
        editor.putInt(StartMin, startmin);
        editor.commit();

        Toast.makeText(getApplicationContext(), "Off-limit time set to: "+fixTimeString(starthr, startmin)+" - "+fixTimeString(endhr,endmin), Toast.LENGTH_SHORT).show();
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

            String settledTime = fixTimeString(starthr, startmin);
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

            String settledTime = fixTimeString(endhr, endmin);

            EditText txt = (EditText) findViewById(R.id.endtime);
            txt.setText(settledTime);
            com.teamvallartas.autodue.Calendar.setOffLimitsEnd(endhr, endmin);
        }

    };

    private void getOffLimit(){
        EditText txt = (EditText) findViewById (R.id.current_offlimit_time);

        SharedPreferences prefs = getSharedPreferences(OfflinePREFERENCES, MODE_PRIVATE);
        starthr = prefs.getInt(StartHr, 0);
        startmin = prefs.getInt(StartMin, 0);
        endhr = prefs.getInt(EndHr, 0);
        endmin = prefs.getInt(EndMin, 0);

        if (starthr==endhr&&startmin==endmin)
            txt.setText(" [None]");
        else
            txt.setText(" "+fixTimeString(starthr, startmin)+" - "+fixTimeString(endhr,endmin));
    }

    private String fixTimeString( int hr, int min ){
        String str_hour = ""+hr;
        String str_min = ""+min;
        if( hr < 10 )
            str_hour = "0" + hr;
        if( min < 10 )
            str_min = "0" + min;
        return str_hour+":"+str_min;
    }

    public void setOffLimitTimeForTesting(int sh, int sm, int eh, int em) {
        starthr = sh;
        startmin = sm;
        endhr = eh;
        endmin = em;
    }

}
