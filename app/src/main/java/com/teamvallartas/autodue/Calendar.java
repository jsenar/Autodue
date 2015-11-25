package com.teamvallartas.autodue;

import android.widget.TextView;

import java.util.*;
public class Calendar{
	// Internal storage of events
	static ArrayList<Event> myCalendar;
	public Calendar(){
		myCalendar = new ArrayList<Event>();
	}

	// adds an event then sorts it
	public static void insert(Event insertion){
		myCalendar.add(insertion);
		sort();
	}
	// calls sort on internal storage
	public static void sort(){
		Collections.sort(myCalendar);
	}
	public static void clear(){myCalendar = new ArrayList<Event>();}
	//basic in order print
	public void print(TextView m_text_event){

		String l_str = m_text_event.getText().toString();
		//m_text_event.setText(l_str);
		for(Event e:myCalendar){
//			System.out.print("Event: "+e.getDescription()+"\t\t|");
//			System.out.print("Start Time: "+e.getStartTime().getTime()+"\t|");
//			System.out.println("End Time: "+e.getEndTime().getTime());
			l_str+=e.getDescription() + "\t|\t" + e.getStartTime().getTime() + "\t-\t" + e.getEndTime().getTime()+"\n";

		}
		l_str+="\n\n";
		m_text_event.setText(l_str);
		//return l_str;
	}
	// algorithm for finding a time for the event. will return null  if cannot find a time
	public static Event findTime(TaskModel task){
		long length = task.duration;
		Date deadline = task.deadline;
		String description = task.description;
		System.out.println(length);
		System.out.println(deadline.toString());
		System.out.println(description);
		Date now = new Date();
		// initialize possible time slot as now
		Event possibleTime = new Event(now,new Date(now.getTime()+ length), description);
		Event iter;
		for(int i = 0; i < myCalendar.size(); i++){
			// if past deadline uh oh
			if(possibleTime.getEndTime().compareTo(deadline)>=0){
				return null;
			}
			//get each event in the calendar
			iter = myCalendar.get(i);
			// if intersects
			if(iter.getEndTime().before(possibleTime.getStartTime())) {
				// try again
				continue;
			}
			//if doesn't intersect
			if(!iter.intersects(possibleTime)){
				int j = i;
				boolean collision = false;
				// check collisions for next couple until no possibility of intersection
				while(++j<myCalendar.size()&& iter.getStartTime().before(possibleTime.getEndTime())){
					
					iter = myCalendar.get(j);
					if(iter.intersects(possibleTime)){
						collision = true;
					}

				}
				// if no collision its a good time
				if(!collision){
					return possibleTime;
				}
			}
			//set possible time to directly after the next event
			iter = myCalendar.get(i);
			possibleTime.setStartingTime(iter.getEndTime().getTime()+1);
			possibleTime.setEndingTime(iter.getEndTime().getTime() + length);
		}
		return possibleTime;
}
	}