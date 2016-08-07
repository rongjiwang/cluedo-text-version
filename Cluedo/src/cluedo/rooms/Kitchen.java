package cluedo.rooms;

/**
 * setup Kitchen with associate doors
 * 
 * @author rongji wang
 *
 */
public class Kitchen implements Room {
	private Stair s;
	private Door d;

	public Kitchen(Stair s, Door d) {
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
		return "k";
	}

}
