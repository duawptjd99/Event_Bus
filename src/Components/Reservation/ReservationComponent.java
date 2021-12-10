/**
 * Copyright(c) 2021 All rights reserved by Jungho Kim in MyungJi University 
 */

package Components.Reservation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReservationComponent {
	protected ArrayList<Reservation> vReservation;

	public ReservationComponent(String sReservationFileName) throws FileNotFoundException, IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(sReservationFileName));
		this.vReservation = new ArrayList<Reservation>();
		while (bufferedReader.ready()) {
			String stuInfo = bufferedReader.readLine();
			if (!stuInfo.equals(""))
				this.vReservation.add(new Reservation(stuInfo));
		}
		bufferedReader.close();
	}

	public ArrayList<Reservation> getReservationList() {
		return vReservation;
	}

	public void setvReservation(ArrayList<Reservation> vRservation) {
		this.vReservation = vRservation;
	}

	public boolean isRegisteredReservation(String rSID) {
		for (int i = 0; i < this.vReservation.size(); i++) {
			if (((Reservation) this.vReservation.get(i)).match(rSID))
				return true;
		}
		return false;
	}

}
