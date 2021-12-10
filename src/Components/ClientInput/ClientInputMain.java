/**
 * Copyright(c) 2021 All rights reserved by Jungho Kim in MyungJi University 
 */
package Components.ClientInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import Framework.Event;
import Framework.EventId;
import Framework.RMIEventBus;

public class ClientInputMain {
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		RMIEventBus eventBus = (RMIEventBus) Naming.lookup("EventBus");
		long componentId = eventBus.register();
		System.out.println("** ClientInputMain(ID:" + componentId + ") is successfully registered. \n");

		boolean done = false;
		while (!done) {
			writeMenu();
			try {
				switch (new BufferedReader(new InputStreamReader(System.in)).readLine().trim()) {
				case "1":
					eventBus.sendEvent(new Event(EventId.Student, null, "List"));
					printLogSend(EventId.Student);
					break;
				case "2":
					eventBus.sendEvent(new Event(EventId.Course, null, "List"));
					printLogSend(EventId.Course);
					break;
				case "3":
					eventBus.sendEvent(new Event(EventId.Student, makeStudentInfo(), "ADD"));
					printLogSend(EventId.Student);
					break;
				case "4":
					eventBus.sendEvent(new Event(EventId.Course, makeCourseInfo(), "ADD"));
					printLogSend(EventId.Course);
					break;
				case "5":
					eventBus.sendEvent(new Event(EventId.Student, removestudent(), "Remove"));
					printLogSend(EventId.Student);
					break;
				case "6":
					eventBus.sendEvent(new Event(EventId.Course, removeCourse(), "Remove"));
					printLogSend(EventId.Course);
					break;
				case "7":
					eventBus.sendEvent(new Event(EventId.Reservation, makeReservation(), "ADD"));
					printLogSend(EventId.Reservation);
					break;
				case "8":
					eventBus.sendEvent(new Event(EventId.Reservation, null, "List"));
					printLogSend(EventId.Reservation);
					break;
				case "0":
					eventBus.sendEvent(new Event(EventId.QuitTheSystem, "Quit the system!!!"));
					printLogSend(EventId.QuitTheSystem);
					eventBus.unRegister(componentId);
					done = true;
					break;
				default:
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static String makeStudentInfo() throws IOException {
		String userInput = "";

		userInput = CheckInput("\nEnter student ID and press return (Ex. 20131234)>> ");

		userInput += " " + CheckInput("\nEnter family name and press return (Ex. Hong)>> ");

		userInput += " " + CheckInput("\nEnter first name and press return (Ex. Gildong)>> ");

		userInput += " " + CheckInput("\nEnter department and press return (Ex. CS)>> ");

		userInput += " " + CheckInput(
				"\nEnter a list of IDs (put a space between two different IDs) of the completed courses and press return >> (Ex. 17651 17652 17653 17654) ");

		System.out.println("\n ** Message: " + userInput + "\n");
		return userInput;
	}

	private static String makeCourseInfo() throws IOException {
		String userInput = "";
		userInput = CheckInput("\nEnter course ID and press return (Ex. 12345)>> ");

		userInput += " " + CheckInput("\nEnter the family name of the instructor and press return (Ex. Hong)>> ");

		userInput += " " + CheckInput(
				"\nEnter the name of the course ( substitute a space with ab underbar(_) ) and press return (Ex. C++_Programming)>> ");

		userInput += " " + CheckInput(
				"\nEnter a list of IDs (put a space between two different IDs) of prerequisite courses and press return >> (Ex. 12345 17651)");

		System.out.println("\n ** Message: " + userInput + "\n");
		return userInput;
	}

	private static String removestudent() throws IOException {
		String userInput = "";
		userInput = CheckInput("\nEnter student ID and press return (Ex. 20131234)>> ");
		System.out.println("\n ** Message: " + userInput + "\n");
		return userInput;

	}

	private static String removeCourse() throws IOException {
		String userInput = "";
		userInput = CheckInput("\nEnter course ID and press return (Ex. 12345)>> ");
		System.out.println("\n ** Message: " + userInput + "\n");
		return userInput;

	}

	private static String makeReservation() throws IOException {
		String userInput = "";
		userInput = CheckInput("\nEnter student ID and press return (Ex. 20131234)>> ");
		userInput += " " + CheckInput("\nEnter course ID and press return (Ex. 12345)>> ");
		System.out.println("\n ** Message: " + userInput + "\n");
		return userInput;

	}

	// @SuppressWarnings("unused")
	private static String setStudentId() throws IOException {
		System.out.println("\nEnter student ID and press return (Ex. 20131234)>> ");
		return new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
	}

	// @SuppressWarnings("unused")
	private static String setCourseId() throws IOException {
		System.out.println("\nEnter course ID and press return (Ex. 12345)>> ");
		return new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
	}

	private static void writeMenu() {
		System.out.println("1. List Students");
		System.out.println("2. List Courses");
		System.out.println("3. Register a new Student");
		System.out.println("4. Register a new Course");
		System.out.println("5. Remove a Student");
		System.out.println("6. Remove a Course");
		System.out.println("7. Make a Reservation");
		System.out.println("8. List Reservations");
		System.out.println("0. Quit the system");
		System.out.print("\n Choose No.: ");
	}

	private static String CheckInput(String print) throws IOException {
		boolean test = false;
		String input = "";
		while (!test) {
			System.out.println(print);
			input = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
			if (!input.equals("")) {
				test = true;
			}
		}
		return input;
	}

	private static void printLogSend(EventId eventId) {
		System.out.println("\n** Sending an event(ID:" + eventId + ")\n");
	}
}
