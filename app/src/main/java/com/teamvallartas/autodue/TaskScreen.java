package com.teamvallartas.autodue;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.grokkingandroid.samplesapp.samples.recyclerviewdemo.R;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import android.app.Dialog;
import android.widget.*;
import android.content.Intent;


/**
 * Created by evahuynh on 10/30/15.
 */
public class TaskScreen extends Activity {
    static final int dialog_id= 0;
    int hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskpopup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width), (int) (height));
    }

    public void addTask(View view) throws ParseException {
        //Button button = (Button) findViewById(R.id.button1);
        DemoModel demo = new DemoModel();
        demo.label = ((EditText)findViewById(R.id.task_name_message)).getText().toString();
        demo.description = ((EditText)findViewById(R.id.description_name_message)).getText().toString();
        String dur = ((EditText)findViewById(R.id.duration_time)).getText().toString();
        demo.duration = Long.parseLong(  dur , 10) * 1000 * 60 * 60;

        // Get date
        String dateString = ((EditText)findViewById(R.id.duedate)).getText().toString();
        String timeString = ((EditText)findViewById(R.id.duetime_hour)).getText().toString();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String dateAndTime = dateString + " " + timeString;
        System.out.println(dateAndTime);
        Date dDate = df.parse(dateAndTime);
        demo.dateTime = dDate;

        // Get priority
        Spinner mySpinner=(Spinner) findViewById(R.id.spinner1);
        String p = mySpinner.getSelectedItem().toString();
        switch(p) {
            case "Low": demo.priority = 1;break;
            case "Medium": demo.priority = 2;break;
            case "High": demo.priority = 3;break;
            default: demo.priority = 1;break;
        }

        Event e = Calendar.findTime(demo.duration, new Date(Long.MAX_VALUE), demo.label);
        if(e == null)
            Log.d("", "is null");
        //Log.d("", );
        Toast.makeText(getApplicationContext(), e.getDescription() + " " + e.getStartTime().toString() + " " + e.getEndTime().toString(), 5000).show();
        //RecyclerViewDemoApp.addItemToList(demo);
        RecyclerViewDemoActivity.addItemToList(demo);

        //startActivity(new Intent(TaskScreen.this, EventPopUp.class));

        this.finish();
        startActivity(new Intent(TaskScreen.this, EventPopUp.class));

    }
    public void hidePopup(View view) {
        //Button button = (Button) findViewById(R.id.button1);
        this.finish();
    }


    @SuppressWarnings("deprecation")
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
            EditText txt = (EditText) findViewById(R.id.duetime_hour);
            txt.setText(settledTime);

        }

    };


    public void showCalendar (View view){
        EditText txtDate = (EditText) findViewById(R.id.duedate);
        DateDialog d = new DateDialog(view);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        d.show(ft, "DatePicker");
    }

    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

}
