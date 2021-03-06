package com.teamvallartas.autodue;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grokkingandroid.samplesapp.samples.recyclerviewdemo.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class RecyclerViewDemoAdapter
        extends RecyclerView.Adapter
                <RecyclerViewDemoAdapter.ListItemViewHolder> {

    private List<TaskModel> items;
    private SparseBooleanArray selectedItems;
    private PriorityQueue<TaskModel> demoQueue = new PriorityQueue<TaskModel>(10, new Comparator<TaskModel>(){
        public int compare(TaskModel m1, TaskModel m2)
        {
            return (m2.priority>m1.priority)?1:-1; // descending order
        }
    });

    RecyclerViewDemoAdapter(List<TaskModel> modelData) {
        if (modelData == null) {
            throw new IllegalArgumentException("modelData must not be null");
        }
        items = modelData;
        for(int i=0; i<modelData.size(); i++)
            demoQueue.add(modelData.get(i));
        selectedItems = new SparseBooleanArray();
    }

    /**
     * Adds and item into the underlying data set
     * at the position passed into the method.
     *
     * @param newModelData The item to add to the data set.
     * @param position The index of the item to remove.
     */
    public void addData(TaskModel newModelData, int position) {
        items.add(position, newModelData);
        demoQueue.add(newModelData);
        notifyItemInserted(position);
        notifyDataSetChanged();
    }

    public void sort(){
        Collections.sort(items);
        notifyDataSetChanged();
    }

    /**
     * Removes the item that currently is at the passed in position from the
     * underlying data set.
     *
     * @param position The index of the item to remove.
     */
    public void removeData(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void removeDataUsingObject(TaskModel model)
    {
        items.remove(model);
    }

    public TaskModel getItem(int position) {
        return items.get(position);
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_demo_01, viewGroup, false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder viewHolder, int position) {
        TaskModel model = items.get(position);
        viewHolder.label.setText(model.label);

        DateFormat df = new SimpleDateFormat("MMM dd HH:mm");
        String dateAndHours = df.format(model.deadline);

        String dateStr = DateUtils.formatDateTime(
                viewHolder.label.getContext(),
                model.deadline.getTime(),
                DateUtils.FORMAT_ABBREV_ALL);

        long millis = System.currentTimeMillis();
        int hoursLeft = (int) ((model.deadline.getTime() - millis)/(1000*60*60));

        viewHolder.colorIndicator.setImageResource(R.drawable.ic_brightness_1_white_24dp);

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

        viewHolder.colorIndicator.setColorFilter(color);

        viewHolder.dateTime.setText("Due " + dateAndHours + " (" + hoursLeft + " hours left)");
        viewHolder.durationText.setText("Time needed: " + model.duration/3600000 + " hours");
        String prioritySetting;
        switch(model.priority) {
            case 1: prioritySetting = "Low";break;
            case 2: prioritySetting = "Medium";break;
            case 3: prioritySetting = "High";break;
            default: prioritySetting = "Low";break;
        }
        viewHolder.priorityText.setText("Priority: " + prioritySetting);
        viewHolder.itemView.setActivated(selectedItems.get(position, false));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        }
        else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    // For updating file when items are deleted
    public List<TaskModel> getAllItems(){
        return items;
    }

    public int getSize() {
        return items.size();
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        TextView label;
        TextView dateTime;
        TextView priorityText;
        TextView durationText;
        ImageView colorIndicator;

        public ListItemViewHolder(View itemView) {
            super(itemView);

            colorIndicator = (ImageView) itemView.findViewById(R.id.color_indicator);
            label = (TextView) itemView.findViewById(R.id.txt_label_item);
            dateTime = (TextView) itemView.findViewById(R.id.txt_date_time);
            priorityText = (TextView) itemView.findViewById(R.id.txt_priority);
            durationText = (TextView) itemView.findViewById(R.id.txt_duration);

        }
    }
}
