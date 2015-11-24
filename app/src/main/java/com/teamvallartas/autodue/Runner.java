//package com.teamvallartas.autodue;
//
//import android.widget.TextView;
//
//import java.util.*;
//public class Runner{
//	public void run(TextView m_text_event){
//		Event myEvent = new Event(new Date(0),new Date(10), "First");
//		Event otherEvent = new Event(new Date(16),new Date(20), "Second");
//		Event yetAnotherEvent = new Event(new Date(31),new Date(40), "Third");
//
//		Calendar myCalendar = new Calendar();
//		myCalendar.insert(myEvent);
//		myCalendar.insert(otherEvent);
//		myCalendar.insert(yetAnotherEvent);
//		myCalendar.sort();
//		myCalendar.print(m_text_event);
//
//		// Event filler = myCalendar.findTime(5, new Date(90000000), "Insertion One");
//		// filler = myCalendar.findTime(10, new Date(910000000), "Insertion Two");
//		// filler = myCalendar.findTime(40, new Date(910000000), "Insertion Three");
//		// Event f = myCalendar.findTime(40, new Date(30), "Insertion Four");
//		// if(f == null)
//			// System.out.println("welp");
//		// else myCalendar.insert(f);
//		Queue<Task> eventPlacer = new PriorityQueue<Task>();
//		eventPlacer.add(new Task(null, 1, "Insertion One", new Date(90000000), 5, false, null));
//		eventPlacer.add(new Task(null, 1, "Insertion Two", new Date(90000000), 10, false, null));
//		eventPlacer.add(new Task(null, 1, "Insertion Three", new Date(90000000), 40, false, null));
//		eventPlacer.add(new Task(null, 1, "Insertion Four", new Date(30), 40, false, null));
//
//		TaskModel current = null;
//		while(!eventPlacer.isEmpty()){
//			current = eventPlacer.poll();
//			Event e = myCalendar.findTime(current);
//			if(e != null){
//				myCalendar.insert(e);
//			}
//
//			myCalendar.sort();
//			myCalendar.print(m_text_event);
//		}
//
//
//
//
//
//		//myEvent = new Event(new Date(40),new Date(60), "First");
//		//otherEvent = new Event(new Date(0),new Date(5), "Second");
//		//System.out.println("Unit Test");
//		//for(int i = 0; i< 50; i++){
//		//	System.out.print(5*i +","+(5*(i+1))+": ");
//		//	otherEvent = new Event(new Date(5*i),new Date(5*(i+1)), "Second");
//		//	if(myEvent.intersects(otherEvent))System.out.println("Intersects");
//		//	else System.out.println("Does Not intersect");
//		//}
//
//
//	}
//}
