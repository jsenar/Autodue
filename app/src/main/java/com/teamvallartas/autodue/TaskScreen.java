package com.teamvallartas.autodue;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.grokkingandroid.samplesapp.samples.recyclerviewdemo.R;


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

    public void hidePopup(View view){
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
