package com.teamvallartas.autodue;

import android.util.Log;
import android.widget.TextView;

import java.util.*;
public class Calendar{
	static ArrayList<Event> myCalendar;
	public Calendar(){
		myCalendar = new ArrayList<Event>();
	}
	public static void insert(Event insertion){
		myCalendar.add(insertion);
		sort();
	}
	public static void sort(){
		Collections.sort(myCalendar);
	}
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
	public static Event findTime(long length, Date deadline, String description){
		Date now = new Date();
		Event possibleTime = new Event(now,new Date(now.getTime()+ length), description);
		Event iter;
		for(int i = 0; i<myCalendar.size(); i++){
			if(possibleTime.getEndTime().compareTo(deadline)>=0){
				return null;
			}

			iter = myCalendar.get(i);
			if(iter.getEndTime().before(possibleTime.getStartTime())) {
				continue;
			}
			if(!iter.intersects(possibleTime)){
				int j = i;
				boolean collision = false;
				while(++j<myCalendar.size()&& iter.getStartTime().before(possibleTime.getEndTime())){
					
					iter = myCalendar.get(j);
					if(iter.intersects(possibleTime)){
						collision = true;
					}

				}
				if(!collision){
					return possibleTime;
				}
			}
			iter = myCalendar.get(i);
			possibleTime.setStartingTime(iter.getEndTime().getTime()+1);
			possibleTime.setEndingTime(iter.getEndTime().getTime() + length);
		}
		return possibleTime;
}
	}
	

