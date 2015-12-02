package com.teamvallartas.autodue;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.format.DateUtils;
import android.transition.ChangeTransform;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.grokkingandroid.samplesapp.samples.recyclerviewdemo.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CardViewDemoActivity extends Activity {

    CardView cardView;
    static TaskModel rescheduleModel = new TaskModel();

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setSharedElementEnterTransition(new ChangeTransform());

        Bundle extras = getIntent().getExtras();
        int id = extras.getInt(Constants.KEY_ID);
        TaskModel model = RecyclerViewDemoApp.findById(id);

        setContentView(R.layout.activity_cardview_demo);
        cardView = (CardView) findViewById(R.id.card_detail);
        View innerContainer = cardView.findViewById(R.id.container_inner_item);
        innerContainer.setTransitionName(Constants.NAME_INNER_CONTAINER);

        // Contents
        TextView label = (TextView) cardView.findViewById(R.id.txt_label_item);
        TextView labelDescription = (TextView) cardView.findViewById(R.id.txt_description);
        TextView dateTime = (TextView) cardView.findViewById(R.id.txt_date_time);
        TextView labelDuration = (TextView) cardView.findViewById(R.id.txt_duration);
        TextView labelPriority = (TextView) cardView.findViewById(R.id.txt_priority);
        ImageView colorIndicator = (ImageView) cardView.findViewById(R.id.color_indicator);
        TextView labelEvent = (TextView) cardView.findViewById(R.id.txt_event);

        // Set task title and description
        label.setText(model.label);
        labelDescription.setText("Description: " + model.description);

        // Set date
        DateFormat df = new SimpleDateFormat("MMM dd HH:mm");
        String dateAndHours = df.format(model.deadline);

//        String dateStr = DateUtils.formatDateTime(
//                this,
//                model.deadline.getTime(),
//                DateUtils.FORMAT_ABBREV_ALL);
        String eventStr = "Event: "+ df.format(model.begin)+" - "+
                df.format(model.end);
        labelEvent.setText(eventStr);
        long millis = System.currentTimeMillis();
        int hoursLeft = (int) ((model.deadline.getTime() - millis)/(1000*60*60));

        int color = Color.parseColor("#000000");
        int hoursInDay = 24;

        if (hoursLeft <= 0) {}
        else if(hoursLeft < hoursInDay) {color = Color.parseColor("#f04141");}
        else if (hoursLeft < hoursInDay*2) {color = Color.parseColor("#eb553b");}
        else if (hoursLeft < hoursInDay*3) {color = Color.parseColor("#eb6336");}
        else if (hoursLeft < hoursInDay*4) {color = Color.parseColor("#fc7e3f");}
        else if (hoursLeft < hoursInDay*5) {color = Color.parseColor("#fa9837");}
        else if (hoursLeft < hoursInDay*6) {color = Color.parseColor("#faad32");}
        else {color = Color.parseColor("#ffdb38");}

        colorIndicator.setColorFilter(color);

        dateTime.setText("Due " + dateAndHours + " (" + hoursLeft + " hours left)");
        labelDuration.setText("Time needed: " + model.duration/3600000 + " hours");

        // Set priority
        String prioritySetting;
        switch(model.priority) {
            case 1: prioritySetting = "Low";break;
            case 2: prioritySetting = "Medium";break;
            case 3: prioritySetting = "High";break;
            default: prioritySetting = "Low";break;
        }
        labelPriority.setText("Priority: " + prioritySetting );
    }

    public void removeItem(View view)  {

        Bundle extras = getIntent().getExtras();
        int id = extras.getInt(Constants.KEY_ID);
        TaskModel model = RecyclerViewDemoApp.findById(id);

        RecyclerViewDemoActivity.removeItemFromListUsingObject(model);

        finish();
    }

    public void rescheduleItem(View view)  {

        Bundle extras = getIntent().getExtras();
        int id = extras.getInt(Constants.KEY_ID);
        TaskModel model = RecyclerViewDemoApp.findById(id);

        rescheduleModel = model;

        // To make things simpler, treat rescheduling as deleting item and re-adding it
        RecyclerViewDemoActivity.removeItemFromListUsingObject(model);

        startActivity(new Intent(this, TaskScreen.class));

        finish();
    }


}
