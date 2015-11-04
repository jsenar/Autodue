package com.teamvallartas.autodue;

import java.util.*;
public class Task implements Comparable<Task>{
	private String name;
	private int priority;
	private String description;
	private Date duedate;
	private Long duration;
	private boolean completed;
	private Event event;
	public Task(String myName, int myPriority, String myDescription, Date myDate, long myDuration, boolean myCompletion, Event myEvent){
		name = myName;
		priority = myPriority;
		description = myDescription;
		duedate = myDate;
		duration = myDuration;
		completed = myCompletion;
		event = myEvent;
	}
	public String getName(){
		return name;
	}
	public int getPriority(){
		return priority;
	}
	public String getDescription(){
		return description;
	}
	public Date getDueDate(){
		return duedate;
	}
	public long getDuration(){
		return duration;
	}
	public boolean getCompletion(){
		return completed;
	}
	public Event getEvent(){
		return event;
	}
	//compare duedate and priority and ??duration??
	public int compareTo(Task other){
		if(duedate.compareTo(other.getDueDate() ) != 0){
			return duedate.compareTo(other.getDueDate() );
		}
		else if(priority != other.getPriority()){
			return (int)(other.getPriority() - priority) ;
		}else{		
			return(int) (other.getDuration() - duration) ;
		}
	}
	
}