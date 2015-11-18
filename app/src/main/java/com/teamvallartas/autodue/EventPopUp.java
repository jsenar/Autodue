package com.teamvallartas.autodue;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.grokkingandroid.samplesapp.samples.recyclerviewdemo.R;
import android.view.View;

/**
 * Created by evahuynh on 11/15/15.
 */
public class EventPopUp extends Activity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventpopup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*0.95), (int) (height*0.85));
    }

    public void finishPopup(View view) {
        this.finish();
    }
}
