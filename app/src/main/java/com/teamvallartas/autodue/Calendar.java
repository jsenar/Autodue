package com.teamvallartas.autodue;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.TextView;

import com.grokkingandroid.samplesapp.samples.recyclerviewdemo.R;

import java.util.*;
public class Calendar{
	// Internal storage of events
	public static ArrayList<Event> myCalendar;
	private static int startHr, startMin, endHr, endMin;
	public static final String OfflinePREFERENCES = "MyPrefs" ;
	public static final String StartHr = "StartHr";
	public static final String StartMin = "StartMin";
	public static final String EndHr = "EndHour";
	public static final String EndMin = "EndMin";

	public Calendar(Context context){
		myCalendar = new ArrayList<Event>();
		SharedPreferences prefs = context.getSharedPreferences(OfflinePREFERENCES, context.MODE_PRIVATE);
		startHr = prefs.getInt(StartHr, 0);
		startMin = prefs.getInt(StartMin, 0);
		endHr = prefs.getInt(EndHr, 0);
		endMin = prefs.getInt(EndMin, 0);
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

	public static void setOffLimitsStart(int hr, int min){
		startHr = hr;
		startMin = min;
	}

	public static void setOffLimitsEnd(int hr, int min){
		endHr = hr;
		endMin = min;
	}
	public static void delete(TaskModel task){
		Event e;
		for(int i = 0; i < myCalendar.size(); i++){
			e = myCalendar.get(i);
			if(task.description == null||task.begin == null||task.end == null){
				continue;
			}
			if(task.description == e.eventDescription && task.begin.equals(e.startTime) && task.end.equals(e.endTime)){
				myCalendar.remove(e);
				return;
			}
		}
	}
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

	@SuppressWarnings("deprecation")
	// algorithm for finding a time for the event. will return null  if cannot find a time
	public static Event findTime(TaskModel task){
		long length = task.duration;
		Date deadline = task.deadline;
		String description = task.description;
//		System.out.println(length);
//		System.out.println(deadline.toString());
//		System.out.println(description);
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
			possibleTime.setStartingTime(iter.getEndTime().getTime() + 1);
			possibleTime.setEndingTime(iter.getEndTime().getTime() + length);
			//Check that possible time doesn't overlap off-limit times and move it accordingly
			if (checkOverlap(startHr, endHr, possibleTime)){
				possibleTime.getStartTime().setHours(endHr);
				possibleTime.getEndTime().setHours(endHr + possibleTime.getDuration());
			}
		}
		return possibleTime;
	}

	@SuppressWarnings("deprecation")
	private static boolean checkOverlap(int startHr, int endHr, Event time){
		//checks if start time or end time are within the off-limit hours
		if((time.getStartTime().getHours() >= startHr && time.getStartTime().getHours() <= endHr)
			&& (time.getEndTime().getHours() >= startHr && time.getEndTime().getHours() <= endHr)){
			return true;
		}
		return false;
	}
}