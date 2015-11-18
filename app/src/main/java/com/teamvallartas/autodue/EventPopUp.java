package com.teamvallartas.autodue;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.DisplayMetrics;

import com.grokkingandroid.samplesapp.samples.recyclerviewdemo.R;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by evahuynh on 11/15/15.
 */
public class EventPopUp extends Activity{
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventpopup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width), (int) (height));
        ((TextView)findViewById(R.id.EventName)).setText(TaskScreen.demo.label);
        ((TextView)findViewById(R.id.EventDescription)).setText(TaskScreen.demo.description);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");

                ((TextView) findViewById(R.id.TimeString)).setText(df.format(TaskScreen.demo.begin) + " - " + df.format(TaskScreen.demo.end));
    }
    public void finishPopup(View view) {
        this.finish();
    }
    public void addPopup(View view){



    }
    public void addEvent(View view){
        RecyclerViewDemoActivity.addItemToList(new DemoModel(TaskScreen.demo));

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Events.DTSTART, TaskScreen.demo.begin.getTime());
        values.put(CalendarContract.Events.TITLE, TaskScreen.demo.label);
        values.put(CalendarContract.Events.DESCRIPTION, TaskScreen.demo.description);

        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());

        // default calendar
        values.put(CalendarContract.Events.CALENDAR_ID, 1);


        values.put(CalendarContract.Events.DTEND, TaskScreen.demo.end.getTime());

        // insert event to calendar
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        finishPopup(view);
    }
}
