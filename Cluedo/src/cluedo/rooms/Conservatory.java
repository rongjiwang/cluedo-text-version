package cluedo.rooms;

/**
 * setup Conservatory with associate doors
 * 
 * @author rongji wang
 *
 */
public class Conservatory implements Room {
	private Stair s;
	private Door d;

	public Conservatory(Stair s, Door d) {
		this.s = s;
		this.d = d;
	}

	public Stair getS() {
		return s;
	}

	public void setS(Stair s) {
		this.s = s;
	}

	public Door getD() {
		return d;
	}

	public void setD(Door d) {
		this.d = d;
	}

	@Override
	public String toString() {
		return "c";
	}

}
