package cluedo.game;

import java.io.IOException;
import java.util.*;
import cluedo.cards.*;
import cluedo.rooms.Door;

/**
 * Game engine spark(main program to active the game) this class should support
 * screen output and take users input to execute the decision
 *
 * @author rongjiwang
 *
 */
public class TextMain {
	/**
	 * alive players
	 */
	public static ArrayList<Player> team;
	public static Board board;
	private static Scanner scan;

	/**
	 * control the gaming loop, text output, user input
	 * 
	 * @throws IOException
	 */
	public TextMain() throws IOException {
		int numOfPlayers = 0;
		while (numOfPlayers < 3 || numOfPlayers > 6) {
			System.out.println("Please enter number 3,4,5 or 6 for how many players are going to play. ");
			System.out.println("For exit the game, Please enter x");
			numOfPlayers = inputNumber("How many players?", scan);
		}
		// store all the appropriate name for user to select
		ArrayList<Characters> chooseName = new ArrayList<>();
		team = new ArrayList<>();
		for (Characters c : Characters.values()) {
			chooseName.add(c);
		}
		// construct players
		int count = 0;
		while (count < numOfPlayers) {
			// if player return as null(invalid input), it will rerun setup
			// player method
			Player player = setUpNewPlayer(chooseName);
			if (player != null) {
				count++;
				team.add(player);
				String msg = "Hello User " + (count) + ", you have chosen <" + player.getName()
						+ "> to be your character name\nYou symbol on the map will be [" + player.getSymbol()
						+ "] \nGood Luck with your game!\n";
				msg += "---------------------------------------------------------\n";
				System.out.println(msg);
			}
		}
		// active the game
		Game game = new Game(board, team);
		// loop will stop when a player made a perfect accusation or only 1
		// player left in the game
		while (Game.GAME_STATUS) {
			for (int i = 0; i < team.size(); i++) {
				int steps = new Dice().throwDice();
				System.out.println(team.get(i).getName() + " roll a dice with <" + steps + "> moves\n");
				for (int j = 0; j < steps; j++) { // per every movement chosen
					System.out.println("-----------------------------------------------------------------------------");
					board.displayBoard();
					System.out.println("-----------------------------------------------------------------------------");
					System.out.println("You[" + team.get(i).getName() + "] have " + (steps - j)
							+ " move(s) left\n Take one option below as your next move\n");
					ArrayList<String> playerOptions = game.checkPlayerOptions(team.get(i), board);
					// show a list of options
					String outputOptions = showOptions(playerOptions);
					// pick a integer within the list
					int inputNum = 0;
					while (inputNum < 1 || inputNum > playerOptions.size()) {
						inputNum = inputNumber(outputOptions, scan);
					}
					int num = executeTheChoice(playerOptions.get(inputNum - 1), team.get(i), board, game);
					if (num == 1) // player out of the game
						break; // if accusation failed
					else if (num == 2)
						j--; // execute The Move Not Count As Step

				}
				if (team.get(i) != null)
					team.get(i).setSuggestion(false);
			}
		}

	}

	/**
	 * player should execute the string decision by himself/herself
	 * 
	 * @param string
	 * @param player
	 * @param board
	 * @return a special integer to represent a none countable move or
	 *         exceptions(eg. player out of the game and so on)
	 * @throws IOException
	 */
	public static int executeTheChoice(String string, Player player, Board board, Game game) throws IOException {
		int moveNotCountAsStep = 2;
		switch (string) {

		case "move north":

			player.Move("north", board);
			break;
		case "move south":

			player.Move("south", board);
			break;
		case "move east":

			player.Move("east", board);
			break;
		case "move west":

			player.Move("west", board);
			break;
		case "entry BallRoom":
			player.enterRoom(board, 'b');
			break;
		case "entry BilliardRoom":
			player.enterRoom(board, 'i');
			break;
		case "entry Conservatory":
			player.enterRoom(board, 'c');
			break;
		case "entry DiningRoom":
			player.enterRoom(board, 'd');
			break;
		case "entry Hall":
			player.enterRoom(board, 'h');
			break;
		case "entry Kitchen":
			player.enterRoom(board, 'k');
			break;
		case "entry Library":
			player.enterRoom(board, 'l');
			break;
		case "entry Lounge":
			player.enterRoom(board, 'o');
			break;
		case "entry Study":
			player.enterRoom(board, 's');
			break;
		case "go stairs":
			// use the stairs to transfer the player to another room
			player.useStairs(board);
			break;
		case "exit room": // multiple doors
			// find the door and transfer player to the knock door position
			Door d = UserChooseADoor(board, player);
			player.exitRoom(board, d);
			break;
		case "make suggestion":
			// if player name match with suggestion name,move that player to the
			// current room with this player
			ArrayList<Card> suggestionCards = new ArrayList<>();
			Card character1 = chooseUnknowCharacter(player, scan, Game.allCards);
			Card weapon1 = chooseUnknowWeapon(player, scan, Game.allCards);
			Card place1 = findPlayerLocation(player, board);
			suggestionCards.add(character1);
			suggestionCards.add(weapon1);
			suggestionCards.add(place1);
			// drag the the player(if match with alive player) from suggestion
			// to the current player room
			for (Player p : team) {
				if (p.getName().equalsIgnoreCase(character1.toString())) {
					transferThePlayer(player, board, p);
				}
			}
			// clockwise fashion check cards from next alive player
			refuteSuggestion(player, suggestionCards);
			return moveNotCountAsStep;
		case "make accusation":
			// compare the card names with the cludoCards,if match player win
			// ,else remove player from the game
			// show all the cards the other players
			Card character = chooseUnknowCharacter(player, scan, Game.allCards);
			Card place = chooseUnknowPlace(player, scan, Game.allCards);
			Card weapon = chooseUnknowWeapon(player, scan, Game.allCards);
			if (Game.cluedoCards.contains(character) && Game.cluedoCards.contains(place)
					&& Game.cluedoCards.contains(weapon)) {
				game.wonTheGame(player);
			} else {
				return game.lostTheGame(player);
			}

			break;
		case "check cards":
			// display the knowing cards to player
			// also display unknown cards to player
			player.checkCards(Game.allCards);
			return moveNotCountAsStep;
		case "guidelines":
			game.outPutGuidelines();
			return moveNotCountAsStep;
		case "end turn":
			System.out.println("[" + player.getName() + "] made (end turn) move, next player please!");
			return 1;

		default:
			throw new GameError("invaild option " + string);
		}
		return 0;
	}

	/**
	 * output text to inform user to choose a exit door from current room
	 *
	 * @param board2
	 * @param player
	 * @return
	 * @throws IOException
	 */
	private static Door UserChooseADoor(Board board2, Player player) throws IOException {
		ArrayList<Door> doorsOption = board2.getExitDoors(player.getRoomSymbol());
		String output = "Please choose your exit door from the position(s) below.\n";
		for (int i = 0; i < doorsOption.size(); i++) {
			output += (i + 1) + ". " + doorsOption.get(i).getKp() + "\n";
		}
		// only accept a integer input between two numbers
		int inputNum = 0;
		while (inputNum < 1 || inputNum > doorsOption.size()) {
			inputNum = inputNumber(output, scan);
		}

		return doorsOption.get(inputNum - 1);
	}

	/**
	 * clockwise any other player has one of suggestion card,the card will
	 * become this player's knowing card
	 *
	 * @param player
	 * @param suggestionCards
	 */
	private static void refuteSuggestion(Player player, ArrayList<Card> suggestionCards) {
		int getPlayerIndex = -1;
		int fullRoundClockwiseCount = 1;
		System.out.println("---------------------------------------------------------");
		System.out.println("Checking your suggestion with clockwise players\n");
		for (int i = 0; i < team.size(); i++) { // get the player index
			if (player.equals(team.get(i))) {
				getPlayerIndex = i + 1;
			}
		}
		// clockwise fashion to check next player
		while (fullRoundClockwiseCount < team.size()) {
			if (getPlayerIndex >= team.size()) {
				getPlayerIndex = 0;
			}
			for (Card c : suggestionCards) {
				if (team.get(getPlayerIndex).getKnowingCards().contains(c)) {
					// at least a player refute your suggestion
					player.getKnowingCards().add(c);
					System.out.println(team.get(getPlayerIndex).getName() + " has " + c.toString() + ".");
					System.out.println("Your suggestion has refuted.");
					System.out.println("However [" + c.toString() + "] card will added to your knowing card list.");
					System.out.println("---------------------------------------------------------");
					return;
				} else {
					System.out.println("[" + team.get(getPlayerIndex).getName() + "] doesn't have " + c.toString()
							+ " in his/her bag");
				}
			}
			System.out.println(
					"[" + team.get(getPlayerIndex).getName() + "] has no match card with your suggestion, pass on!");
			getPlayerIndex++;
			fullRoundClockwiseCount++;
		}
		// perfect suggestion
		System.out.println("Congratulation!, you made a perfect suggestion, "
				+ "make your accusation with those cards will help you to win this game!");
		System.out.println("---------------------------------------------------------");
	}

	/**
	 * transfer the player from suggestion to the made suggestion player's room
	 * 
	 * @param player
	 * @param board2
	 * @param p
	 */
	private static void transferThePlayer(Player player, Board board2, Player p) {
		Position position = player.findPositionInTheRoom(board2, player.getRoomSymbol());
		p.movePostion(board2, position);
		p.setArePlayerInARoom(true);
		p.setRoomSymbol(player.getRoomSymbol());
	}

	/**
	 * return current player location as card name
	 *
	 * @param player
	 * @param board2
	 * @return
	 */
	private static Card findPlayerLocation(Player player, Board board2) {
		char c = player.getRoomSymbol();
		switch (c) {
		case 'k':
			return Places.KITCHEN;
		case 'b':
			return Places.BALL_ROOM;
		case 'i':
			return Places.BILLIARD_ROOM;
		case 'c':
			return Places.CONSERVATORY;
		case 'd':
			return Places.DINING_ROOM;
		case 'h':
			return Places.HALL;
		case 'l':
			return Places.LIBRARY;
		case 'o':
			return Places.LOUNGE;
		case 's':
			return Places.STUDY;
		default:
			throw new GameError("Not a game board room");
		}
	}

	/**
	 * player should choose a unknown weapon
	 * 
	 * @param player
	 * @param scan2
	 * @param allCards
	 * @return weapon
	 * @throws IOException
	 */
	private static Card chooseUnknowWeapon(Player player, Scanner scan2, ArrayList<Card> allCards) throws IOException {
		String output = "Choose a weapon for your accusation\n";
		ArrayList<Card> list = new ArrayList<>();
		for (Card c : allCards) {
			if (!player.getKnowingCards().contains(c)) {
				if (c instanceof Weapons) {
					list.add(c);
					output += (list.size()) + ". " + c + "\n";
				}
			}
		}
		// take user input to associate weapon
		if (!list.isEmpty()) {
			int num = 0;
			while (num < 1 || num > list.size()) {
				num = inputNumber(output, scan2);
			}
			return list.get(num - 1);
		}
		return null;
	}

	/**
	 * player should choose a unknown Place
	 * 
	 * @param player
	 * @param scan2
	 * @param allCards
	 * @return place
	 * @throws IOException
	 */
	private static Card chooseUnknowPlace(Player player, Scanner scan2, ArrayList<Card> allCards) throws IOException {
		String output = "Choose a place for your accusation\n";
		ArrayList<Card> list = new ArrayList<>();
		for (Card c : allCards) {
			if (!player.getKnowingCards().contains(c)) {
				if (c instanceof Places) {
					list.add(c);
					output += (list.size()) + ". " + c + "\n";
				}
			}
		}
		if (!list.isEmpty()) {
			int num = 0;
			while (num < 1 || num > list.size()) {
				num = inputNumber(output, scan2);
			}
			return list.get(num - 1);
		}

		return null;
	}

	/**
	 * player should choose a unknown Character
	 * 
	 * @param player
	 * @param scan2
	 * @param allCards
	 * @return Character
	 * @throws IOException
	 */
	private static Card chooseUnknowCharacter(Player player, Scanner scan2, ArrayList<Card> allCards)
			throws IOException {
		String output = "Choose a character for your accusation\n";
		ArrayList<Card> list = new ArrayList<>();
		for (Card c : allCards) {
			if (!player.getKnowingCards().contains(c)) {
				if (c instanceof Characters) {
					list.add(c);
					output += (list.size()) + ". " + c + "\n";
				}
			}
		}
		if (!list.isEmpty()) {
			int num = 0;
			while (num < 1 || num > list.size()) {
				num = inputNumber(output, scan2);
			}
			return list.get(num - 1);
		}
		return null;
	}

	/**
	 * list all current player options as string for display on screen
	 *
	 * @param playerOptions
	 * @return
	 */
	private static String showOptions(ArrayList<String> playerOptions) {
		String output = "";
		for (int i = 0; i < playerOptions.size(); i++) {
			output += (i + 1) + ". " + playerOptions.get(i) + "\n";
		}
		return output;
	}

	/**
	 * setup a player with start position, choose name by user, empty card list
	 *
	 * @param chooseName
	 * @return
	 * @throws IOException
	 */
	private static Player setUpNewPlayer(ArrayList<Characters> chooseName) throws IOException {
		String nameList = "*Please enter a number from 1 to " + chooseName.size() + " to choose your name\n";

		for (int i = 0; i < chooseName.size(); i++) {
			nameList += (i + 1) + "." + chooseName.get(i) + "\n";
		}
		nameList += "*The last Letter in your name will represent you in the map\n";

		// take the name choose by user
		int nameNumber = inputNumber(nameList, scan);
		// if it's a valid input
		if (nameNumber > 0 && nameNumber <= chooseName.size()) {
			ArrayList<Card> cardList = new ArrayList<>();
			// store the name
			String name = chooseName.get(nameNumber - 1).name();
			// remove from all names list
			chooseName.remove(nameNumber - 1);
			// user last letter as personal symbol show on the board
			char symbol = name.charAt((name.length()) - 1);
			// clockwise fashion for starting the game on board
			Position start = Board.startPoints.get(0);
			System.out.println("* " + start.toString() + " is your starting position!");
			Board.startPoints.remove(0);
			// complete the player
			Player player = new Player(start, name, symbol, cardList);
			return player;
		}
		// message will be shown if game receive invalid input
		System.out.println("Warning! only require number from 1 to " + chooseName.size() + " \nPlease try again!\n");
		return null;
	}

	/**
	 * take user's input, must be number as given, also support to exit the game
	 *
	 * @param msg
	 * @param scan
	 * @return integer for decision
	 * @throws IOException
	 */
	private static int inputNumber(String msg, Scanner scan) throws IOException {
		System.out.println(msg);
		if (scan.hasNextInt()) {
			int num = scan.nextInt();
			return num;
		} else {
			if (scan.next().equalsIgnoreCase("x")) {
				System.exit(1);
			}
			return -1;
		}
	}

	/**
	 * read valid map file and construct TextMain
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// message will be shown if the game could not find the appropriate
		// map.txt file
		if (args.length != 1) {
			System.out.println("Can not setup board with out map.txt file.\n"
					+ "Please drag the map.txt file outside your scr folder,\n"
					+ "or include the map.txt inside your package if run by terminal\n");
			System.exit(0);
		}
		// set up board from board class
		board = new Board(args[0]);
		// install scanner for user input
		scan = new Scanner(System.in);
		new TextMain();
	}

}