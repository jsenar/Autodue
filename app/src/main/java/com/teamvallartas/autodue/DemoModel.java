package com.teamvallartas.autodue;

import java.util.Date;

public class DemoModel implements Comparable<DemoModel> {
    private static int nextId = 0;
    String label;
    String description;
    long duration; // in milliseconds
    Date dateTime;
    Date begin;
    Date end;
    String pathToImage;
    int id = ++nextId;
    int priority;
    public DemoModel() {

    }
    public DemoModel(DemoModel other){
        label = other.getLabel();
        description = other.description;
        duration = other.duration;
        dateTime = other.getDateTime();
        begin = other.begin;
        end = other.end;
        pathToImage = other.pathToImage;
        id = other.id;
        priority = other.getPriority();

    }
    public int compareTo(DemoModel other) {

        if(this.getDateTime().equals(other.getDateTime())) {
            // Sort priority descending if due time is same
            return(this.priority<other.getPriority()?1:-1);
        }
        else
            return (this.getDateTime().before(other.getDateTime()))?-1:1;
    }

    public int getPriority(){
        return priority;
    }
    public Date getDateTime() { return dateTime; }
    public String getLabel() {  return label;}
}
