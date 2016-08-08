package cluedo.game;

import java.util.ArrayList;
import java.util.Collections;

import cluedo.cards.Card;
import cluedo.cards.Characters;
import cluedo.cards.Places;
import cluedo.cards.Weapons;
import cluedo.rooms.BallRoom;
import cluedo.rooms.BilliardRoom;
import cluedo.rooms.Conservatory;
import cluedo.rooms.DiningRoom;
import cluedo.rooms.Door;
import cluedo.rooms.Hall;
import cluedo.rooms.Kitchen;
import cluedo.rooms.Library;
import cluedo.rooms.Lounge;
import cluedo.rooms.Room;
import cluedo.rooms.Study;

/**
 * this class should control all the game logic, rules, game win and game lose
 * 
 * @author rongji wang
 *
 */
public class Game {
	public Board board;
	/**
	 * store all the alive players
	 */
	public ArrayList<Player> playTeam;
	/**
	 * total cards without secret cluedo cards
	 */
	public static ArrayList<Card> allCards;
	/**
	 * while this become false, the game will be stop
	 */
	public static boolean GAME_STATUS;
	public static ArrayList<Card> currentCards;
	/**
	 * 3 secret cards(1 character, 1 weapon, 1 place)
	 */
	public static ArrayList<Card> cluedoCards;
	/**
	 * after evenly dealt cards, this list should store remaining cards
	 */
	public static ArrayList<Card> showLeftCards;

	/**
	 * initialize all the setting above
	 * 
	 * @param board
	 * @param playTeam
	 */
	public Game(Board board, ArrayList<Player> playTeam) {
		this.board = board;
		this.playTeam = playTeam;
		GAME_STATUS = true;
		renewAllCards(); // split reminding cards and cluedo cards
		handOutCards(currentCards);
		displayPlayers(playTeam);
	}

	/**
	 * set up starting position for each player
	 *
	 * @param playTeam2
	 */
	private void displayPlayers(ArrayList<Player> team) {
		for (Player p : team) {
			board.outputBoard[p.getP().getX()][p.getP().getY()] = p.getSymbol();
		}
	}

	/**
	 * store the leftover cards and show to all players(add into their known
	 * cards collection) randomly evenly hand out the remaining cards to each
	 * player
	 *
	 * @param cards
	 */
	private void handOutCards(ArrayList<Card> cards) {
		showLeftCards = new ArrayList<>();
		// store remaining cards, and delete from total cards
		int cardsLeft = cards.size() % playTeam.size();
		if (cardsLeft != 0) {
			for (int i = 0; i < cardsLeft; i++) {
				showLeftCards.add(cards.get(i));
				cards.remove(i);
			}
		}
		// split cards to player
		Collections.shuffle(cards);
		int cardsNumber = cards.size() / playTeam.size();
		for (Player p : playTeam) {
			for (int i = 0; i < cardsNumber; i++) {
				p.getKnowingCards().add(cards.get(0));
				cards.remove(0);
			}
			// make random
			Collections.shuffle(cards);
		}
	}

	/**
	 * randomly choose 3 card to store as cluedo cards(a character, a place and
	 * a weapon) store the rest into collection
	 */
	public void renewAllCards() {
		// choose secret cluedo cards
		currentCards = new ArrayList<>();
		allCards = new ArrayList<>();
		for (Card c : Characters.values()) {
			currentCards.add(c);
			allCards.add(c);
		}
		for (Card c : Places.values()) {
			currentCards.add(c);
			allCards.add(c);

		}
		for (Card c : Weapons.values()) {
			currentCards.add(c);
			allCards.add(c);

		}
		Collections.shuffle(currentCards); // make random
		cluedoCards = new ArrayList<>();
		boolean haveCharacter = false;
		boolean havePlaces = false;
		boolean haveWeapon = false;
		// make sure only pick one of each type from all cards list
		for (int i = 0; i < currentCards.size(); i++) {
			Card c = currentCards.get(i);
			if (c instanceof Characters && !haveCharacter) {
				cluedoCards.add(c);
				currentCards.remove(i);
				haveCharacter = true;
			} else if (c instanceof Places && !havePlaces) {
				cluedoCards.add(c);
				currentCards.remove(i);
				havePlaces = true;
			} else if (c instanceof Weapons && !haveWeapon) {
				cluedoCards.add(c);
				currentCards.remove(i);
				haveWeapon = true;
			}
		}
	}

	/**
	 * the game should filter through all the options that suitable for current
	 * player to choose
	 * 
	 * @param player
	 *            current player
	 * @return options list
	 */
	public ArrayList<String> checkPlayerOptions(Player player, Board board) {
		ArrayList<String> currentOptions = new ArrayList<>();
		// move options
		ArrayList<String> directions = board.movable(player);
		if (!directions.isEmpty()) {
			for (String s : directions) {
				currentOptions.add(s);
			}
		}
		// enter room option
		String roomEntry = checkRoomOption(player, board);
		if (roomEntry != null) {
			currentOptions.add(roomEntry);
		}
		// stair use option
		String stairOption = checkStairOption(player, board);
		if (stairOption != null) {
			currentOptions.add(stairOption);
		}
		// exit room option
		String exitRoomOption = checkExitRoomOption(player, board);
		if (exitRoomOption != null) {
			currentOptions.add(exitRoomOption);
		}
		// suggestion option
		String makeSuggestion = checkSuggestionOption(player, board);
		if (makeSuggestion != null) {
			currentOptions.add(makeSuggestion);
		}
		// accusation option
		String makeAccusation = "make accusation";
		currentOptions.add(makeAccusation);
		// check knowing cards
		String checkCards = "check cards";
		currentOptions.add(checkCards);
		// add end turn option
		String makeEndTurn = "end turn";
		currentOptions.add(makeEndTurn);
		// add guidelines option
		String guideLine = "guidelines";
		currentOptions.add(guideLine);

		return currentOptions;
	}

	/**
	 * as long as the player in a room, this should return "make suggestion"
	 *
	 * @param player
	 * @param board2
	 * @return
	 */
	public String checkSuggestionOption(Player player, Board board2) {
		if (player.isArePlayerInARoom() && !player.isSuggestion()) {
			player.setSuggestion(true);
			return "make suggestion";
		}
		return null;
	}

	/**
	 * exit room options , could be multiple doors
	 *
	 * @param player
	 * @param board2
	 * @return
	 */
	public String checkExitRoomOption(Player player, Board board2) {
		if (player.isArePlayerInARoom()) {
			return "exit room";
		}
		return null;
	}

	/**
	 * only 4 room should support this stair option,check player in the right
	 * room or not
	 *
	 * @param player
	 * @param board2
	 * @return
	 */
	public String checkStairOption(Player player, Board board2) {
		// if player in the room
		// if player in 1,2,3,4,room,
		// then the stairs can be using
		if (player.isArePlayerInARoom()) {
			if (player.getRoomSymbol() == 'k' || player.getRoomSymbol() == 'c' || player.getRoomSymbol() == 'o'
					|| player.getRoomSymbol() == 's') {
				return "go stairs";
			}
		}
		return null;
	}

	/**
	 * should return enter room if player stand on a knock door position
	 *
	 * @param player
	 * @param board2
	 * @return
	 */
	public String checkRoomOption(Player player, Board board2) {
		// return the correct room name only when player standing at the
		// knocking door position
		for (Room r : board2.allRooms) {
			if (r instanceof BallRoom) {
				BallRoom ballRoom = (BallRoom) r;
				for (Door d : ballRoom.getDoors()) {
					if (player.getP().equals(d.getKp().getP())) {
						return "entry BallRoom";
					}
				}
			} else if (r instanceof BilliardRoom) {
				BilliardRoom billiardRoom = (BilliardRoom) r;
				for (Door d : billiardRoom.getDoors()) {
					if (player.getP().equals(d.getKp().getP())) {
						return "entry BilliardRoom";
					}
				}
			} else if (r instanceof Conservatory) {
				Conservatory conservatory = (Conservatory) r;
				if (player.getP().equals(conservatory.getD().getKp().getP())) {

					return "entry Conservatory";
				}
			} else if (r instanceof DiningRoom) {
				DiningRoom diningRoom = (DiningRoom) r;
				for (Door d : diningRoom.getDoors()) {
					if (player.getP().equals(d.getKp().getP())) {
						return "entry DiningRoom";
					}
				}
			} else if (r instanceof Hall) {
				Hall hall = (Hall) r;
				for (Door d : hall.getDoors()) {
					if (player.getP().equals(d.getKp().getP())) {
						return "entry Hall";
					}
				}
			} else if (r instanceof Kitchen) {
				Kitchen kitchen = (Kitchen) r;
				if (player.getP().equals(kitchen.getD().getKp().getP())) {
					return "entry Kitchen";
				}

			} else if (r instanceof Library) {
				Library library = (Library) r;
				for (Door d : library.getDoors()) {
					if (player.getP().equals(d.getKp().getP())) {
						return "entry Library";
					}
				}
			} else if (r instanceof Lounge) {
				Lounge lounge = (Lounge) r;
				if (player.getP().equals(lounge.getD().getKp().getP())) {
					return "entry Lounge";
				}

			} else if (r instanceof Study) {
				Study study = (Study) r;
				if (player.getP().equals(study.getD().getKp().getP())) {
					return "entry Study";
				}
			}
		}
		return null;
	}

	/**
	 * won the game , print messages and exit the game
	 * 
	 * @param player
	 */
	public void wonTheGame(Player player) {
		String output = "-------------Below three cards are perfect match with the secret result-------------\n";
		for (Card c : Game.cluedoCards) {
			output += c.toString() + "\n";
		}
		output += "These three are the hidden secret cluedo cards\n";
		output += player.getName() + " has just won the game.\n";
		output += "Good luck with next round\n";
		System.out.println(output);
		System.exit(1);
	}

	/**
	 * erase player from the map, send message out, show his cards to the rest
	 * 
	 * @param player
	 * @return
	 */
	public int lostTheGame(Player player) {
		String output = "-------------The accusation doesn't match with the secret result-------------\n";
		output += player.getName() + " has just lost this game, he will face up all his cards.\n";
		System.out.println(output);
		// erase player from the board
		board.outputBoard[player.getP().getX()][player.getP().getY()] = ' ';
		// show up card to other players
		for (Player p : TextMain.team) {
			if (p != player) {
				for (Card c : player.getKnowingCards()) {
					if (!p.getKnowingCards().contains(c)) {
						p.getKnowingCards().add(c);
					}
				}
			}
		}
		// remove player
		TextMain.team.remove(player);
		if (TextMain.team.size() == 1) {
			wonTheGame(TextMain.team.get(0));
		}
		return 1;
	}

	/**
	 * print out a cluedo game and map description
	 */
	public void outPutGuidelines() {
		String output = "*********************Game Guidelines***********************\n";
		output += "symbol [k] is Kitchen room.\n";
		output += "symbol [b] is Ball room.\n";
		output += "symbol [i] is Billiard room.\n";
		output += "symbol [c] is Conservatory room.\n";
		output += "symbol [d] is Dining room.\n";
		output += "symbol [h] is Hall room.\n";
		output += "symbol [l] is Library room.\n";
		output += "symbol [o] is Lounge room.\n";
		output += "symbol [s] is Study room.\n";
		output += "\n";
		output += "symbol [1,2,3,4] are stairs in the rooms,\n"
				+ "stairs only can take players to a corresponding room,\n" + "1 <--> 4 or 2 <--> 3.\n";
		output += "\n";
		output += "symbol [+] means door, player can not stand on the + symbol,\n"
				+ "the entry room option should only appear when the player stand on the knocking door position\n";
		output += "\n";
		output += "Player can only win the game by make perfect accusation or the player is the last player in the game\n";
		output += "\n";
		output += "Player will lose this game by making wrong accusation, and show all your cards to others\n";
		output += "\n";
		output += "When player stand in a room, he should have one chance per round to make suggestion,\n"
				+ "if the suggestion character is still playing in the game, he should get transfered to the corresponding room,\n"
				+ "and then other players should use clockwise fashion to show\n at least one matching card to reject this suggestion.\n"
				+ "but, this card will become the current player's knowing card\n";
		output += "Otherwise this player has just made a perfect suggestion to match cludo secret cards\n";
		output += "\n";
		output += "*********************Enjoy the Cluedo***********************\n";
		System.out.println(output);
	}
}
