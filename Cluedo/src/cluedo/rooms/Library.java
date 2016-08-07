package cluedo.rooms;

import java.util.ArrayList;

/**
 * setup Library with associate doors
 * 
 * @author rongji wang
 *
 */
public class Library implements Room {
	private ArrayList<Door> doors;

	public Library(ArrayList<Door> doors) {
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
		return "l";
	}

}
