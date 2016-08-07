package cluedo.game;

/**
 * this class should return random numbers from 1-6 as a real-life dice
 *
 * @author rongjiwang
 *
 */
public class Dice {
	/**
	 * return 1-6 integer number
	 * 
	 * @return
	 */
	public int throwDice() {
		return (int) (Math.random() * 6 + 1);
	}

}
