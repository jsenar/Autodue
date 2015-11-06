package com.teamvallartas.autodue;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.grokkingandroid.samplesapp.samples.recyclerviewdemo.R;

import java.util.Date;


/**
 * Created by evahuynh on 10/30/15.
 */
public class TaskScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskpopup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width), (int) (height));

        Spinner dropdown = (Spinner)findViewById(R.id.spinner1);
        String[] items = new String[]{"Very Important", "Kinda Important", "Important", "Little Important", "Not Important"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }

    public void addTask(View view){
        //Button button = (Button) findViewById(R.id.button1);
        DemoModel demo = new DemoModel();
        demo.label = ((EditText)findViewById(R.id.task_name_message)).getText().toString();
        demo.description = ((EditText)findViewById(R.id.description_name_message)).getText().toString();
        String dur = ((EditText)findViewById(R.id.duration_time)).getText().toString();
        demo.duration = Long.parseLong(  dur , 10) * 1000 * 60 * 60;
        Event e = Calendar.findTime(demo.duration, new Date(Long.MAX_VALUE), demo.label);
        if(e == null)
            Log.d("", "is null");
        //Log.d("", );
        Toast.makeText(getApplicationContext(), e.getDescription() + " " + e.getStartTime().toString() + " " + e.getEndTime().toString(), 5000).show();
//        RecyclerViewDemoApp.addItemToList(demo);
        RecyclerViewDemoActivity.addItemToList(demo);
        this.finish();
    }
    public void hidePopup(View view) {
        //Button button = (Button) findViewById(R.id.button1);
        this.finish();
    }
    public void deselectButton2(View view){
        RadioButton b = (RadioButton) findViewById(R.id.radioButton2);
        b.setChecked(false);
    }

    public void deselectButton1(View view){
        RadioButton b = (RadioButton) findViewById(R.id.radioButton1);
        b.setChecked(false);
    }


}
