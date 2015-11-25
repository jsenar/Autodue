package com.teamvallartas.autodue;

import java.util.*;
public class Event implements Comparable<Event>{
	Date startTime;
	Date endTime;
	String eventDescription;
	public Event(Date start, Date end, String description){
		startTime = start;
		endTime = end;
		eventDescription = description;
	}
	public Event(Event a){
		startTime = a.startTime;
		endTime = a.endTime;
		eventDescription = a.eventDescription;
	}
	public Date getStartTime(){
		return startTime;
	}
	public Date getEndTime(){
		return endTime;
	}
	public String getDescription(){
		return eventDescription;	
	}
	public void setStartingTime(long start){
		startTime.setTime(start);
	}
	public void setEndingTime(long end){
		endTime.setTime(end);
	}

	public int compareTo(Event other){
		if(startTime.compareTo(other.getStartTime()) == 0){
			return endTime.compareTo(other.getEndTime());		
		}else{
			return startTime.compareTo(other.getStartTime());		
		}	
	}
	public boolean intersects(Event placement){
		boolean before = startTime.compareTo(placement.getStartTime())<0 && endTime.compareTo(placement.getStartTime())<0;
		boolean after = startTime.compareTo(placement.getEndTime()) > 0 && endTime.compareTo(placement.getEndTime())>0;
		return !(before || after);
			
	}
}
