package com.teamvallartas.autodue;

import java.util.Date;

public class TaskModel implements Comparable<TaskModel> {
    private static int nextId = 0;
    boolean locked = false;
    String label;
    String description;
    long duration; // in milliseconds
    Date deadline;
    Date begin;
    Date end;
    Long eventID;
    String pathToImage;
    int id = ++nextId;
    int priority;
    public TaskModel() {

    }
    public TaskModel(TaskModel other){
        label = other.getLabel();
        description = other.description;
        locked = other.locked;
        duration = other.duration;
        deadline = other.getDeadline();
        begin = other.begin;
        end = other.end;
        pathToImage = other.pathToImage;
        id = other.id;
        priority = other.getPriority();
        eventID = other.eventID;

    }
    public int compareTo(TaskModel other) {

        if(this.getDeadline().equals(other.getDeadline())) {
            // Sort priority descending if due time is same
            return(this.priority<other.getPriority()?1:-1);
        }
        else
            return (this.getDeadline().before(other.getDeadline()))?-1:1;
    }

    public int getPriority(){
        return priority;
    }
    public Date getDeadline() { return deadline; }
    public String getLabel() {  return label;}
}
