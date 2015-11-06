package com.teamvallartas.autodue;

import java.util.Date;

public class DemoModel implements Comparable<DemoModel> {
    private static int nextId = 0;
    String label;
    String description;
    long duration; // in milliseconds
    Date dateTime;
    String pathToImage;
    int id = ++nextId;
    int priority;

    public int compareTo(DemoModel other) {
        if (this.priority < other.getPriority()) {
            return 1;
        }
        else {
            return -1;
        }
    }

    public int getPriority(){
        return priority;
    }
}
