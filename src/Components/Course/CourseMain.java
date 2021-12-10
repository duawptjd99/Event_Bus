/**
 * Copyright(c) 2021 All rights reserved by Jungho Kim in MyungJi University 
 */
package Components.Course;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.StringTokenizer;

import Components.Student.StudentComponent;
import Framework.Event;
import Framework.EventId;
import Framework.EventQueue;
import Framework.RMIEventBus;

public class CourseMain {
	public static void main(String[] args) throws FileNotFoundException, IOException, NotBoundException {
		RMIEventBus eventBus = (RMIEventBus) Naming.lookup("EventBus");
		long componentId = eventBus.register();
		System.out.println("CourseMain (ID:" + componentId + ") is successfully registered...");

		CourseComponent coursesList = new CourseComponent("Courses.txt");
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
				case Course:
					printLogEvent("Get", event);
					if (event.getType().equals("List")) {
						eventBus.sendEvent(new Event(EventId.ClientOutput, makeCourseList(coursesList)));
					} else if (event.getType().equals("ADD")) {
						eventBus.sendEvent(
								new Event(EventId.ClientOutput, registerCourse(coursesList, event.getMessage())));
					} else if (event.getType().equals("Remove")) {
						eventBus.sendEvent(
								new Event(EventId.ClientOutput, removeCourse(coursesList, event.getMessage())));
					} else if (event.getType().equals("reservation")) {
						eventBus.sendEvent(
								new Event(EventId.Student, CheckInfo(coursesList, event.getMessage()), "reservation"));
					}
					break;
				case QuitTheSystem:
					eventBus.unRegister(componentId);
					done = true;
					break;
				default:
					break;
				}
			}
		}
	}

	private static String CheckInfo(CourseComponent coursesList, String message) {
		StringTokenizer stringTokenizer = new StringTokenizer(message);
		String studentId = stringTokenizer.nextToken();
		String courseId = stringTokenizer.nextToken();
		boolean course = false;

		
		String info = message;
		
		for (int i = 0; i < coursesList.getCourseList().size(); i++) {
			if (coursesList.getCourseList().get(i).getCourseId().equals(courseId)) {
				course = true;
				for (int j = 0; j < coursesList.getCourseList().get(i).getPrerequisiteCoursesList().size(); j++) {
					info = info + " " + coursesList.getCourseList().get(i).getPrerequisiteCoursesList().get(j);
					
				}
			}
		}
		if (course == true) {
			course= false;
			return info;
		} else {
			return "Fail C";
		}
	}

	private static String registerCourse(CourseComponent coursesList, String message) {
		Course course = new Course(message);
		if (!coursesList.isRegisteredCourse(course.courseId)) {
			coursesList.vCourse.add(course);
			return "This course is successfully added.";
		} else
			return "This course is already registered.";
	}

	private static String removeCourse(CourseComponent coursesList, String message) {

		if (coursesList.isRegisteredCourse(message)) {
			coursesList.removeCourse(message);
			return "This course is successfully removed.";
		} else
			return "fail removed";
	}

	private static String makeCourseList(CourseComponent coursesList) {
		String returnString = "";
		for (int j = 0; j < coursesList.vCourse.size(); j++) {
			returnString += coursesList.getCourseList().get(j).getString() + "\n";
		}
		return returnString;
	}

	private static void printLogEvent(String comment, Event event) {
		System.out.println(
				"\n** " + comment + " the event(ID:" + event.getEventId() + ") message: " + event.getMessage());
	}
}
