package cluedo.rooms;

import java.util.ArrayList;

/**
 * setup Hall with associate doors
 * 
 * @author rongji wang
 *
 */
public class Hall implements Room {
	private ArrayList<Door> doors;

	public Hall(ArrayList<Door> doors) {
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
		return "h";
	}

}
