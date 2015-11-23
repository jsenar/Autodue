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
import android.view.Window;
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
    public static DemoModel demo = new DemoModel();
    int hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskpopup);

        /* Each time TaskScreen is brought up, check if the rescheduleModel is null
            If it is non-null, then it's because we just pressed reschedule */
        if(CardViewDemoActivity.rescheduleModel.label!="")
        {
            // Bring over name, description, and duration from cardview
            EditText editName = (EditText) findViewById(R.id.task_name_message);
            editName.setText(CardViewDemoActivity.rescheduleModel.label);

            EditText editDescription = (EditText) findViewById(R.id.description_name_message);
            editDescription.setText(CardViewDemoActivity.rescheduleModel.description);

//            EditText editDuration = (EditText) findViewById(R.id.duration_time);
//            long d = CardViewDemoActivity.rescheduleModel.duration/3600000;
//            editDuration.setText(String.valueOf(d));

            // Reset this so that when you click FAB as usual you won't get previous cancelled values
            CardViewDemoActivity.rescheduleModel = new DemoModel();
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width), (int) (height));


    }

    public void addTask(View view) throws ParseException {
        //Button button = (Button) findViewById(R.id.button1);
        demo = new DemoModel();

        // Check name is filled
        demo.label = "";
        demo.label += ((EditText)findViewById(R.id.task_name_message)).getText().toString();
        if(demo.label == ""){
            Toast.makeText(getApplicationContext(),"Name must be specified!", 1500).show();
            return;
        }

        // Set description and check that it is filled
        demo.description = "";
        demo.description += ((EditText)findViewById(R.id.description_name_message)).getText().toString();
        if(demo.description == ""){
            Toast.makeText(getApplicationContext(),"Description must be specified!", 1500).show();
            return;
        }

        // Set duration and check that it is filled
        String dur = "";
        dur += ((EditText)findViewById(R.id.duration_time)).getText().toString();
        if(dur == ""){
            Toast.makeText(getApplicationContext(),"Duration must be specified!", 1500).show();
            return;
        }
        demo.duration = Long.parseLong(  dur , 10) * 1000 * 60 * 60;

        // Check due date is filled
        String dateString = "";
        dateString += ((EditText)findViewById(R.id.duedate)).getText().toString();
        if(dateString == ""){
            Toast.makeText(getApplicationContext(),"Due Date must be specified!", 1500).show();
            return;
        }

        // Check time is filled
        String timeString = "";
        timeString += ((EditText)findViewById(R.id.duetime_hour)).getText().toString();
        if(timeString == ""){
            Toast.makeText(getApplicationContext(),"Time must be specified!", 1500).show();
            return;
        }

        // Set due dateTime based on due date and time
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String dateAndTime = dateString + " " + timeString;
        System.out.println(dateAndTime);
        Date dDate = df.parse(dateAndTime);
        demo.dateTime = dDate;

        // Check error if duration exceeds difference between current time and end time
        Date currentTime = new Date();
        if(demo.dateTime.getTime() < currentTime.getTime())
        {
            Toast.makeText(getApplicationContext(),"You cannot create events in the past." , 1500).show();
            return;
        }
        else if(currentTime.getTime() + demo.duration > demo.dateTime.getTime())
        {
            Toast.makeText(getApplicationContext(),"There is not enough time between now and the deadline for this task." , 1500).show();
            return;
        }

        // Get priority
        Spinner mySpinner=(Spinner) findViewById(R.id.spinner1);
        String p = mySpinner.getSelectedItem().toString();
        switch(p) {
            case "Low": demo.priority = 1;break;
            case "Medium": demo.priority = 2;break;
            case "High": demo.priority = 3;break;
            default: demo.priority = 1;break;
        }
        // find time for event
        Event e = Calendar.findTime(demo.duration, demo.dateTime, demo.label);
        if(e == null){
            Log.d("", "is null");
            Toast.makeText(getApplicationContext(),"Conflict detected." , 1500).show();
            return;
        }
        else
        {
            demo.begin = e.getStartTime();
            demo.end = e.getEndTime();
        }

        //Log.d("", );
        //Toast.makeText(getApplicationContext(), e.getDescription() + " " + e.getStartTime().toString() + " " + e.getEndTime().toString(), 5000).show();
        //RecyclerViewDemoApp.addItemToList(demo);
        //RecyclerViewDemoActivity.addItemToList(demo);

        //startActivity(new Intent(TaskScreen.this, EventPopUp.class));

        this.finish();
        startActivity(new Intent(TaskScreen.this, EventPopUp.class));
//        ((TextView)findViewById(R.id.EventName)).setText(demo.label);
//        ((TextView)findViewById(R.id.EventDescription)).setText(demo.description);
//        ((TextView)findViewById(R.id.TimeString)).setText(demo.begin.toString() + " to " + demo.end.toString());

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
