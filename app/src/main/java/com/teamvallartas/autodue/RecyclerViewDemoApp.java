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
        Random r = new Random();
        demoData = new ArrayList<TaskModel>();
        demoMap = new SparseArray<TaskModel>();
        for (int i = 0; i < 20; i++) {
            TaskModel model = new TaskModel();
            DateTime dateTime = new DateTime();
            dateTime = dateTime.plusDays(r.nextInt(15)-1);
            dateTime = dateTime.plusMillis(r.nextInt(36000000));
            model.deadline = dateTime.toDate();

            String s[] = {"Finish homework", "Work on project", "Do lab exercise", "Type up proposal", "Group meeting", "Study for exams", "Work on coding project"};
            model.description = "Work on parts 1 and 2";
            model.label = s[r.nextInt(s.length)];
            model.duration = (r.nextInt(10)+1)*3600000;
            model.priority = r.nextInt(3)+1;

            demoData.add(model);
            demoMap.put(model.id, model);
            demoQueue.add(model); // NEW
        }
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


    //public static final List<TaskModel> removeItemFromList(int position) {
        //demoData.remove(position);
        //demoMap.remove(demoData.get(position).id);
        //return new ArrayList<TaskModel>(demoData);
    //}

    public static TaskModel findById(int id) {
        return demoMap.get(id);
    }

}
