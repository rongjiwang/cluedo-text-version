package cluedo.game;

import java.util.ArrayList;

import cluedo.cards.Card;
import cluedo.rooms.Door;

/**
 * this class should show all the abilities that a player can do
 *
 * @author rongjiwang
 *
 */
public class Player {
	/**
	 * player current position
	 */
	private Position p;
	/**
	 * one of the six characters name
	 */
	private String name;
	/**
	 * cards that player know are not secret cluedo cards
	 */
	private ArrayList<Card> knowingCards;
	/**
	 * the symbol show at the board
	 */
	private char symbol;
	/**
	 * true = in the room, false = outside a room
	 */
	private boolean arePlayerInARoom;
	/**
	 * room type symbol, eg, 's' for Study
	 */
	private char roomSymbol;
	/**
	 * in the room,should active this boolean, and only allow once per round per
	 * player
	 */
	private boolean suggestion;

	/**
	 * initialize a player
	 * 
	 * @param p
	 * @param name
	 * @param symbol
	 * @param knowingCards
	 */
	public Player(Position p, String name, char symbol, ArrayList<Card> knowingCards) {
		this.p = p;
		this.name = name;
		this.knowingCards = knowingCards;
		this.symbol = symbol;
	}

	/**
	 * player move a position on the board
	 *
	 * @param where
	 */
	public void Move(String where, Board board) {
		if (where.equals("north")) {
			// erase symbol
			board.outputBoard[this.p.getX()][this.p.getY()] = ' ';
			this.p = new Position(p.getX() - 1, p.getY());
			// add the symbol to new position
			board.outputBoard[this.p.getX()][this.p.getY()] = this.symbol;

		} else if (where.equals("south")) {
			board.outputBoard[this.p.getX()][this.p.getY()] = ' ';
			this.p = new Position(p.getX() + 1, p.getY());
			board.outputBoard[this.p.getX()][this.p.getY()] = this.symbol;
		}

		else if (where.equals("west")) {
			board.outputBoard[this.p.getX()][this.p.getY()] = ' ';
			this.p = new Position(p.getX(), p.getY() - 1);
			board.outputBoard[this.p.getX()][this.p.getY()] = this.symbol;
		} else if (where.equals("east")) {
			board.outputBoard[this.p.getX()][this.p.getY()] = ' ';
			this.p = new Position(p.getX(), p.getY() + 1);
			board.outputBoard[this.p.getX()][this.p.getY()] = this.symbol;
		}

	}

	/**
	 * move the player position to a random position inside of a given room
	 *
	 * @param board
	 * @param c
	 */
	public void enterRoom(Board board, char c) {
		switch (c) {
		case 'b':
			// move to a empty position in a room associate with type char c
			movePostion(board, findPositionInTheRoom(board, c));
			this.setArePlayerInARoom(true);
			this.setRoomSymbol(c);
			;
			// get out to the knocking room position
			break;
		case 'i':
			movePostion(board, findPositionInTheRoom(board, c));
			this.setArePlayerInARoom(true);
			this.setRoomSymbol(c);
			;
			// get out to the knocking room position
			break;
		case 'c':
			movePostion(board, findPositionInTheRoom(board, c));
			this.setArePlayerInARoom(true);
			this.setRoomSymbol(c);
			;
			// get out to the knocking room position
			break;
		case 'd':
			movePostion(board, findPositionInTheRoom(board, c));
			this.setArePlayerInARoom(true);
			this.setRoomSymbol(c);
			;
			// get out to the knocking room position
			break;
		case 'h':
			movePostion(board, findPositionInTheRoom(board, c));
			this.setArePlayerInARoom(true);
			this.setRoomSymbol(c);
			;
			// get out to the knocking room position
			break;
		case 'k':
			movePostion(board, findPositionInTheRoom(board, c));
			this.setArePlayerInARoom(true);
			this.setRoomSymbol(c);
			;
			// get out to the knocking room position
			break;
		case 'l':
			movePostion(board, findPositionInTheRoom(board, c));
			this.setArePlayerInARoom(true);
			this.setRoomSymbol(c);
			;
			// get out to the knocking room position
			break;
		case 'o':
			movePostion(board, findPositionInTheRoom(board, c));
			this.setArePlayerInARoom(true);
			this.setRoomSymbol(c);
			;
			// get out to the knocking room position
			break;
		case 's':
			movePostion(board, findPositionInTheRoom(board, c));
			this.setArePlayerInARoom(true);
			this.setRoomSymbol(c);
			;
			// get out to the knocking room position
			break;
		default:
			throw new GameError("Wrong room entering");
		}
	}

	/**
	 * move the symbol to a new position and update the current position
	 * 
	 * @param board
	 * @param newPosition
	 */
	public void movePostion(Board board, Position newPosition) {
		board.outputBoard[newPosition.getX()][newPosition.getY()] = this.symbol;
		board.outputBoard[this.p.getX()][this.p.getY()] = ' ';
		this.p = newPosition;
	}

	/**
	 * find a movable position inside the room
	 *
	 * @param board
	 * @param c
	 * @return
	 */
	public Position findPositionInTheRoom(Board board, char c) {
		for (int i = 0; i < board.outputBoard.length; i++) {
			boolean foundRoom = false;
			for (int j = 0; j < board.outputBoard[0].length; j++) {
				// second , get the same room type with right side
				if (board.outputBoard[i][j] == c && foundRoom) {
					int findRightPosition = 1;
					// work out a empty position and return
					while (board.outputBoard[i][j - findRightPosition] != ' ') {
						findRightPosition++;
					}
					return new Position(i, j - findRightPosition);
				}
				// first, search a room type match with left side
				if (board.outputBoard[i][j] == c && j <= 20 && board.outputBoard[i][j + 1] == ' ') {
					foundRoom = true;
				}
			}
		}

		return null;
	}

	/**
	 * transfer player from one corner room to the facing corner room
	 *
	 * @param board
	 */
	public void useStairs(Board board) {
		char c = this.roomSymbol;
		if (c == 'k') {
			Position position = this.findPositionInTheRoom(board, 's');
			this.movePostion(board, position);
			this.setRoomSymbol('s');
		}
		if (c == 'c') {
			Position position = this.findPositionInTheRoom(board, 'o');
			this.movePostion(board, position);
			this.setRoomSymbol('o');
		}
		if (c == 'o') {
			Position position = this.findPositionInTheRoom(board, 'c');
			getClass();
			this.movePostion(board, position);
			this.setRoomSymbol('c');
		}
		if (c == 's') {
			Position position = this.findPositionInTheRoom(board, 'k');
			this.movePostion(board, position);
			this.setRoomSymbol('k');
		}
	}

	/**
	 * take the player outside the room
	 *
	 * @param board
	 * @param d
	 */
	public void exitRoom(Board board, Door d) {
		// swap the position to outside
		if (board.outputBoard[d.getKp().getP().getX()][d.getKp().getP().getY()] == ' ') {
			board.outputBoard[this.p.getX()][this.p.getY()] = ' ';
			board.outputBoard[d.getKp().getP().getX()][d.getKp().getP().getY()] = this.getSymbol();
			this.setP(d.getKp().getP());
			this.setArePlayerInARoom(false);

		} else {
			System.out.println("Sorry, the exit door is blocked.");
		}
	}

	/**
	 * print out knowing cards and unknowing cards
	 * 
	 * @param allCards
	 */
	public void checkCards(ArrayList<Card> allCards) {
		String msg = "Here are your knowing cards\n";
		for (Card c : this.getKnowingCards()) {
			msg += c + "\n";
		}
		msg += "\nHere are your unknowing cards\n";
		for (Card c : allCards) {
			if (!this.getKnowingCards().contains(c)) {
				msg += c + "\n";
			}
		}
		msg += "\n";
		System.out.println(msg);

	}

	/**
	 * get boolean suggestion method
	 * 
	 * @return
	 */
	public boolean isSuggestion() {
		return suggestion;
	}

	/**
	 * set boolean suggestion method
	 * 
	 * @param suggestion
	 */
	public void setSuggestion(boolean suggestion) {
		this.suggestion = suggestion;
	}

	/**
	 * get position method
	 * 
	 * @return
	 */
	public Position getP() {
		return p;
	}

	/**
	 * set position method
	 * 
	 * @param p
	 */
	public void setP(Position p) {
		this.p = p;
	}

	/**
	 * get name method
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * set name method
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * get knowing cards method
	 * 
	 * @return
	 */
	public ArrayList<Card> getKnowingCards() {
		return knowingCards;
	}

	/**
	 * set knowing cards method
	 * 
	 * @param knowingCards
	 */
	public void setKnowingCards(ArrayList<Card> knowingCards) {
		this.knowingCards = knowingCards;
	}

	/**
	 * get board symbol method
	 * 
	 * @return
	 */
	public char getSymbol() {
		return symbol;
	}

	/**
	 * set board symbol method
	 * 
	 * @param symbol
	 */
	public void setSymbol(char symbol) {
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return this.getName();
	}

	/**
	 * get are Player In A Room method
	 * 
	 * @return
	 */
	public boolean isArePlayerInARoom() {
		return arePlayerInARoom;
	}

	/**
	 * set are Player In A Room method
	 * 
	 * @param arePlayerInARoom
	 */
	public void setArePlayerInARoom(boolean arePlayerInARoom) {
		this.arePlayerInARoom = arePlayerInARoom;
	}

	/**
	 * get roomSymbol method
	 * 
	 * @return
	 */
	public char getRoomSymbol() {
		if (isArePlayerInARoom()) {
			return roomSymbol;
		}
		return '0';
	}

	/**
	 * set roomSymbol method
	 * 
	 * @param roomSymbol
	 */
	public void setRoomSymbol(char roomSymbol) {
		this.roomSymbol = roomSymbol;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((p == null) ? 0 : p.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (p == null) {
			if (other.p != null)
				return false;
		} else if (!p.equals(other.p))
			return false;
		return true;
	}

}
