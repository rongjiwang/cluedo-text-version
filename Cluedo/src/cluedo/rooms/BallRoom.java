package cluedo.rooms;

import java.util.ArrayList;

/**
 * setup ball room with associate doors
 * 
 * @author rongji wang
 *
 */
public class BallRoom implements Room {
	private ArrayList<Door> doors;

	public BallRoom(ArrayList<Door> doors) {
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
		return "b";
	}

}
