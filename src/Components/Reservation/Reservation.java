package Components.Reservation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Reservation implements Serializable {

	protected String studentId;
	protected String name;
	protected String department;
	protected ArrayList<String> completedCoursesList;

	public Reservation(String inputString) {
		StringTokenizer stringTokenizer = new StringTokenizer(inputString);
		this.studentId = stringTokenizer.nextToken();
		this.name = stringTokenizer.nextToken();
		this.name = this.name + " " + stringTokenizer.nextToken();
		this.department = stringTokenizer.nextToken();
		this.completedCoursesList = new ArrayList<String>();

		while (stringTokenizer.hasMoreTokens()) {
			this.completedCoursesList.add(stringTokenizer.nextToken());
		}

	}

	public boolean match(String studentId) {
		return this.studentId.equals(studentId);
	}

	public String getString() {
		String stringReturn = this.studentId + " " + this.name + " " + this.department;
		for (int i = 0; i < this.completedCoursesList.size(); i++) {
			stringReturn = stringReturn + " " + this.completedCoursesList.get(i).toString();
		}
		return stringReturn;
	}
	
	public boolean addCourseList(String course) {
		this.completedCoursesList.add(course);
		return true;
	}

	public String getName() {
		return this.name;
	}

	public String getID() {
		// TODO Auto-generated method stub
		return this.studentId;
	}

	public String getDepartment() {
		// TODO Auto-generated method stub
		return this.department;
	}

	public ArrayList<String> getCourseList() {
		// TODO Auto-generated method stub
		return this.completedCoursesList;
	}

}
