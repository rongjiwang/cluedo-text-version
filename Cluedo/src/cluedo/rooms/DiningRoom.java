package cluedo.rooms;

import java.util.ArrayList;

/**
 * setup DiningRoom with associate doors
 * 
 * @author rongji wang
 *
 */
public class DiningRoom implements Room {
	private ArrayList<Door> doors;

	public DiningRoom(ArrayList<Door> doors) {
		this.doors = doors;
	}

	public ArrayList<Door> getDoors() {
		return doors;
	}

	public void setDoors(ArrayList<Door> doors) {
		this.doors = doors;
	}

	@Override
	public String toString() {
		return "d";
	}

}
