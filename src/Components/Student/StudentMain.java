/**
 * Copyright(c) 2021 All rights reserved by Jungho Kim in MyungJi University 
 */

package Components.Student;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import Framework.Event;
import Framework.EventId;
import Framework.EventQueue;
import Framework.RMIEventBus;

public class StudentMain {
	public static void main(String args[]) throws FileNotFoundException, IOException, NotBoundException {
		RMIEventBus eventBus = (RMIEventBus) Naming.lookup("EventBus");
		long componentId = eventBus.register();
		System.out.println("** StudentMain(ID:" + componentId + ") is successfully registered. \n");

		StudentComponent studentsList = new StudentComponent("Students.txt");
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
				case Student:
					if (event.getType().equals("List")) {
						printLogEvent("Get", event);
						eventBus.sendEvent(new Event(EventId.ClientOutput, makeStudentList(studentsList)));
					} else if (event.getType().equals("ADD")) {
						eventBus.sendEvent(
								new Event(EventId.ClientOutput, registerStudent(studentsList, event.getMessage())));
					} else if (event.getType().equals("Remove")) {
						eventBus.sendEvent(
								new Event(EventId.ClientOutput, removeStudent(studentsList, event.getMessage())));
					} else if (event.getType().equals("reservation")) {
						eventBus.sendEvent(
								new Event(EventId.Reservation, CheckInfo(studentsList, event.getMessage()), "Check"));
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

	private static String CheckInfo(StudentComponent studentsList, String message) {

	

		if (!message.equals("Fail C")) {
			boolean student = false;
			Student selectStudent = null;
			StringTokenizer stringTokenizer = new StringTokenizer(message);
			String studentId = stringTokenizer.nextToken();
			String courseId = stringTokenizer.nextToken();
			ArrayList<String> advancedCourse = new ArrayList<String>();

			while (stringTokenizer.hasMoreTokens()) {
				advancedCourse.add(stringTokenizer.nextToken());
			}

			for (int i = 0; i < studentsList.getStudentList().size(); i++) {
				if (studentsList.getStudentList().get(i).getID().equals(studentId)) {
					selectStudent = studentsList.getStudentList().get(i);
					student = true;
				}
			}
			if (student != true) {
				return "Fail S";
			} else {
				System.out.println(studentId);
				System.out.println(courseId);
				System.out.println(advancedCourse);
				if (!advancedCourse.isEmpty()) {
					if (advancedCourse.size() == 1) {
						if (selectStudent.getCompletedCourses().contains(advancedCourse.get(0))) {

							return selectStudent.getID() + " " + selectStudent.getName() + " "
									+ selectStudent.getDepartment() + " " + courseId;

						} else {
							return "Fail A";
						}
					} else if (advancedCourse.size() == 2) {
						if ((selectStudent.getCompletedCourses().contains(advancedCourse.get(0)))
								&& (selectStudent.getCompletedCourses().contains(advancedCourse.get(1)))) {

							return selectStudent.getID() + " " + selectStudent.getName() + " "
									+ selectStudent.getDepartment() + " " + courseId;

						} else {
							return "Fail A";
						}
					}
				} else {
					return selectStudent.getID() + " " + selectStudent.getName() + " " + selectStudent.getDepartment()
							+ " " + courseId;
				}
				student = false;
			}
		
		}
		return "Fail C";
	}

	private static String registerStudent(StudentComponent studentsList, String message) {
		Student student = new Student(message);
		if (!studentsList.isRegisteredStudent(student.studentId)) {
			studentsList.vStudent.add(student);
			return "This student is successfully added.";
		} else
			return "This student is already registered.";
	}

	private static String removeStudent(StudentComponent studentsList, String message) {
		// Student student = new Student(message);
		if (studentsList.isRegisteredStudent(message)) {
			studentsList.removeStudent(message);
			return "This student is successfully removed.";
		} else
			return "fail removed";
	}

	private static String makeStudentList(StudentComponent studentsList) {
		String returnString = "";
		for (int j = 0; j < studentsList.vStudent.size(); j++) {
			returnString += studentsList.getStudentList().get(j).getString() + "\n";
		}
		return returnString;
	}

	private static void printLogEvent(String comment, Event event) {
		System.out.println(
				"\n** " + comment + " the event(ID:" + event.getEventId() + ") message: " + event.getMessage());
	}
}
