/**
 * Copyright(c) 2021 All rights reserved by Jungho Kim in MyungJi University 
 */

package Components.Reservation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.StringTokenizer;

import Components.Course.Course;
import Components.Course.CourseComponent;
import Components.Student.Student;
import Components.Student.StudentComponent;
import Framework.Event;
import Framework.EventId;
import Framework.EventQueue;
import Framework.RMIEventBus;

public class ReservationMain {
	public static void main(String args[]) throws FileNotFoundException, IOException, NotBoundException {
		RMIEventBus eventBus = (RMIEventBus) Naming.lookup("EventBus");
		long componentId = eventBus.register();
		System.out.println("** ReservationMain(ID:" + componentId + ") is successfully registered. \n");

		ReservationComponent reservationList = new ReservationComponent("Reservation.txt");

		Event event = null;
		boolean done = false;
		while (!done) {

			/*
			 * try { Thread.sleep(1000); } catch (InterruptedException e) {
			 * e.printStackTrace(); }
			 */

			EventQueue eventQueue = eventBus.getEventQueue(componentId);
			for (int i = 0; i < eventQueue.getSize(); i++) {
				event = eventQueue.getEvent();
				switch (event.getEventId()) {
				case Reservation: // 이거를 얼마나 적게 만드느냐 근데 없으면 안됨
					if (event.getType().equals("List")) {
						printLogEvent("Get", event);
						eventBus.sendEvent(new Event(EventId.ClientOutput, makeReservationList(reservationList)));
					} else if (event.getType().equals("ADD")) {
						printLogEvent("Get", event);
						eventBus.sendEvent(new Event(EventId.Course, event.getMessage(), "reservation"));
					}else if (event.getType().equals("Check")) {
						printLogEvent("Get", event);
						eventBus.sendEvent(new Event(EventId.ClientOutput, makeReservation(reservationList,event.getMessage())));
					}
					break;
				case QuitTheSystem:
					printLogEvent("Get", event);
					eventBus.unRegister(componentId);
					done = true;
					break;
				default:
					break;
				}
			}
		}
	}

	private static String makeReservation(ReservationComponent reservationList, String message) {
		
		if(message.equals("Fail C")) {
			return  "This reservation is failed. (not match courseId)";
		}else if(message.equals("Fail S")) {
			return "This reservation is failed. (not match studentId)";
		}else if(message.equals("Fail A")) {
			return "This reservation is failed. (Advanced Course fail)";
		}else {
			Reservation reservation = new Reservation(message);
			reservationList.vReservation.add(reservation);
			return "This Reservation is successfully added.";
		}
		
		
		
	}



	private static String makeReservationList(ReservationComponent reservationList) {
		String returnString = "";
		for (int j = 0; j < reservationList.vReservation.size(); j++) {
			returnString += reservationList.getReservationList().get(j).getString() + "\n";
		}
		if (!reservationList.vReservation.isEmpty()) {
			return returnString;
		} else {
			return "reservationList is Empty!";
		}
	}


	private static void printLogEvent(String comment, Event event) {
		System.out.println(
				"\n** " + comment + " the event(ID:" + event.getEventId() + ") message: " + event.getMessage());
	}
}
