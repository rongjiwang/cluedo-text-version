package cluedo.rooms;

import java.util.ArrayList;

/**
 * setup BilliardRoom with associate doors
 * 
 * @author rongji wang
 *
 */
public class BilliardRoom implements Room {
	private ArrayList<Door> doors;

	public BilliardRoom(ArrayList<Door> doors) {
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
		return "i";
	}

}
