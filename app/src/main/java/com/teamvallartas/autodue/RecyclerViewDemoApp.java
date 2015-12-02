package com.teamvallartas.autodue;

import android.app.Application;
import android.util.SparseArray;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class RecyclerViewDemoApp extends Application {

    private static List<TaskModel> demoData;
    private static SparseArray<TaskModel> demoMap;
    private static PriorityQueue<TaskModel> demoQueue = new PriorityQueue<TaskModel>(100, new Comparator<TaskModel>(){
        public int compare(TaskModel m1, TaskModel m2)
        {
            if(m1.getDeadline() == m2.getDeadline()) {
                // Sort priority descending if due time is same
                return(m1.priority<m2.getPriority()?1:-1);
            }
            else
                return (m1.getDeadline().before(m2.getDeadline()))?-1:1;
        }
    });

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        demoData = new ArrayList<TaskModel>();
        demoMap = new SparseArray<TaskModel>();
    }

    public static final List<TaskModel> getDemoData() {
        // Create toReturn since demoQueue seems to be unreliable
        ArrayList<TaskModel> toReturn = new ArrayList<TaskModel>();
        int limit = demoQueue.size();
        for(int i=0; i<limit; i++)
        toReturn.add(demoQueue.poll());

        return new ArrayList<TaskModel>(toReturn);
    }

    public static final List<TaskModel> addItemToList(TaskModel model) {
        int position = 0;
        demoData.add(position, model);
        demoMap.put(model.id, model);
        demoQueue.add(model); // NEW
        return new ArrayList<TaskModel>(demoQueue);
    }


    public static TaskModel findById(int id) {
        return demoMap.get(id);
    }

}
