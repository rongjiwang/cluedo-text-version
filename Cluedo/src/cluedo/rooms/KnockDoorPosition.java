package cluedo.rooms;

import cluedo.game.Position;

/**
 * the only position can get into the associate door
 * 
 * @author rongji wang
 *
 */
public class KnockDoorPosition {
	private Position p;

	public KnockDoorPosition(Position p) {
		this.p = p;
	}

	public Position getP() {
		return p;
	}

	public void setP(Position p) {
		this.p = p;
	}

	@Override
	public String toString() {
		return "KnockDoorPosition [p=" + p + "]";
	}

}
