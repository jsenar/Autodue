package com.teamvallartas.autodue;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.format.DateUtils;
import android.transition.ChangeTransform;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.grokkingandroid.samplesapp.samples.recyclerviewdemo.R;

public class CardViewDemoActivity extends Activity {

    CardView cardView;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setSharedElementEnterTransition(new ChangeTransform());

        Bundle extras = getIntent().getExtras();
        int id = extras.getInt(Constants.KEY_ID);
        DemoModel model = RecyclerViewDemoApp.findById(id);

        setContentView(R.layout.activity_cardview_demo);
        cardView = (CardView) findViewById(R.id.card_detail);
        View innerContainer = cardView.findViewById(R.id.container_inner_item);
        innerContainer.setTransitionName(Constants.NAME_INNER_CONTAINER);

        // Contents
        TextView label = (TextView) cardView.findViewById(R.id.txt_label_item);
        TextView dateTime = (TextView) cardView.findViewById(R.id.txt_date_time);
        TextView labelDuration = (TextView) cardView.findViewById(R.id.txt_duration);
        TextView labelPriority = (TextView) cardView.findViewById(R.id.txt_priority);

        label.setText(model.label);
        String dateStr = DateUtils.formatDateTime(
                this,
                model.dateTime.getTime(),
                DateUtils.FORMAT_ABBREV_ALL);

        long millis = System.currentTimeMillis();
        int hoursLeft = (int) ((model.dateTime.getTime() - millis)/(1000*60*60));

        dateTime.setText("Due " + dateStr + " (" + hoursLeft + " hours left)");
        labelDuration.setText("Time needed: " + model.duration/3600000 + " hours");
        String prioritySetting;
        switch(model.priority) {
            case 1: prioritySetting = "Low";break;
            case 2: prioritySetting = "Medium";break;
            case 3: prioritySetting = "High";break;
            default: prioritySetting = "Low";break;
        }
        labelPriority.setText("Priority: " + prioritySetting );
    }
}
