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

    private static List<DemoModel> demoData;
    private static SparseArray<DemoModel> demoMap;
    private static PriorityQueue<DemoModel> demoQueue = new PriorityQueue<DemoModel>(100, new Comparator<DemoModel>(){
        public int compare(DemoModel m1, DemoModel m2)
        {
            if(m1.getDateTime() == m2.getDateTime()) {
                // Sort priority descending if due time is same
                return(m1.priority<m2.getPriority()?1:-1);
            }
            else
                return (m1.getDateTime().before(m2.getDateTime()))?-1:1;
        }
    });

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        Random r = new Random();
        demoData = new ArrayList<DemoModel>();
        demoMap = new SparseArray<DemoModel>();
        for (int i = 0; i < 20; i++) {
            DemoModel model = new DemoModel();
            DateTime dateTime = new DateTime();
            dateTime = dateTime.plusDays(r.nextInt(15)+1);
            model.dateTime = dateTime.toDate();

            String s[] = {"Finish homework", "Work on project", "Do lab exercise", "Type up proposal", "Group meeting", "Study for exams", "Work on coding project"};
            model.label = s[r.nextInt(s.length)];
            model.duration = (r.nextInt(10)+1)*3600000;
            model.priority = r.nextInt(3)+1;

            demoData.add(model);
            demoMap.put(model.id, model);
            demoQueue.add(model); // NEW
        }
    }

    public static final List<DemoModel> getDemoData() {
        // Create toReturn since demoQueue seems to be unreliable
        ArrayList<DemoModel> toReturn = new ArrayList<DemoModel>();
        int limit = demoQueue.size();
        for(int i=0; i<limit; i++)
        toReturn.add(demoQueue.poll());

        return new ArrayList<DemoModel>(toReturn);
    }

    public static final List<DemoModel> addItemToList(DemoModel model) {
        int position = 0;
        demoData.add(position, model);
        demoMap.put(model.id, model);
        demoQueue.add(model); // NEW
        return new ArrayList<DemoModel>(demoQueue);
    }


    //public static final List<DemoModel> removeItemFromList(int position) {
        //demoData.remove(position);
        //demoMap.remove(demoData.get(position).id);
        //return new ArrayList<DemoModel>(demoData);
    //}

    public static DemoModel findById(int id) {
        return demoMap.get(id);
    }

}
