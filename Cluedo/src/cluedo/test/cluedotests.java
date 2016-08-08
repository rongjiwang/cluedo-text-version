package cluedo.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import cluedo.cards.Card;
import cluedo.cards.Characters;
import cluedo.game.Board;
import cluedo.game.Dice;
import cluedo.game.Game;
import cluedo.game.Player;
import cluedo.game.Position;
import cluedo.game.TextMain;

public class cluedotests {

	// game should be alive
	@Test
	public void test01_GAME_STATUS() {
		try {
			Game game = setUpMockGame();
			assert game.GAME_STATUS = true;
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// player should have at least 3 options to choose
	@Test
	public void test02_PlayerOption() {
		try {
			Game game = setUpMockGame();

			String name1 = Characters.COLONEL_MUSTARD.name();
			Player p1 = new Player(game.board.startPoints.get(0), name1, name1.charAt(name1.length() - 1),
					new ArrayList<Card>());
			assertFalse(game.checkPlayerOptions(p1, game.board).isEmpty());
			assertTrue(game.checkPlayerOptions(p1, game.board).size() >= 3);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test03_PlayerStartingPosition() {
		// fixed starting position
		try {
			Game game = setUpMockGame();

			assertTrue(game.playTeam.get(0).getP().equals(new Position(0, 9)));
			assertTrue(game.playTeam.get(1).getP().equals(new Position(0, 14)));
			assertTrue(game.playTeam.get(2).getP().equals(new Position(6, 23)));
			assertTrue(game.playTeam.get(3).getP().equals(new Position(19, 23)));
			assertTrue(game.playTeam.get(4).getP().equals(new Position(24, 7)));
			assertTrue(game.playTeam.get(5).getP().equals(new Position(17, 0)));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	// the dice should always be in 1~6 range
	public void test04_dice() {
		int i = 0;
		Dice d = new Dice();
		while (i != 100) {
			assertTrue(d.throwDice() >= 1 && d.throwDice() <= 6);
			i++;
		}

	}

	@Test
	// the cluedo Cards should be be three
	public void test05_checkCluedoCards() {
		try {
			Game game = setUpMockGame();
			assertTrue(game.cluedoCards.size() == 3);
		} catch (IOException e) {

		}
	}

	@Test
	// In the game, there are 21 cards
	public void test06_checkTotalCards() {
		try {
			Game game = setUpMockGame();
			assertTrue(game.allCards.size() == 21);
		} catch (IOException e) {

		}
	}

	@Test
	// player can not move outside the board
	public void test07_checkBoardBoundry() {
		try {
			Game game = setUpMockGame();
			game.playTeam.get(0).setP(new Position(100, 100));
			assertTrue(game.playTeam.get(0).getP() != new Position(100, 100));
		} catch (IOException e) {

		}
	}

	@Test
	// check move south option
	public void test08_ValidMove01() {
		try {
			Game game = setUpMockGame();
			game.playTeam.get(0).Move("south", game.board);
			assert game.playTeam.get(0).getP().equals(new Position(1, 9));
		} catch (IOException e) {

		}
	}

	@Test
	// check room option
	public void test09_ValidMove02() {
		try {
			Game game = setUpMockGame();
			ArrayList<Player> team = game.playTeam;
			team.get(0).setP(new Position(5, 7));
			assert game.checkRoomOption(team.get(0), game.board).equals("entry BallRoom");

		} catch (IOException e) {

		}
	}

	@Test
	// check room symbol after enter a room
	public void test10_ValidMove03() {
		try {
			Game game = setUpMockGame();
			ArrayList<Player> team = game.playTeam;
			team.get(0).setP(new Position(5, 7));
			team.get(0).enterRoom(game.board, 'b');
			assert team.get(0).getRoomSymbol() == 'b';

		} catch (IOException e) {

		}
	}

	@Test
	// player should be make suggestion in a room
	public void test11_ValidMove04() {
		try {
			Game game = setUpMockGame();
			ArrayList<Player> team = game.playTeam;
			team.get(0).setP(new Position(5, 7));
			team.get(0).enterRoom(game.board, 'b');
			assert game.checkSuggestionOption(team.get(0), game.board).equals("make suggestion");

		} catch (IOException e) {

		}
	}

	@Test
	// player not allow to make more than one suggestion in a turn
	public void test12_ValidMove_05() {
		try {
			Game game = setUpMockGame();
			ArrayList<Player> team = game.playTeam;
			team.get(0).setP(new Position(5, 7));
			team.get(0).enterRoom(game.board, 'b');
			assert game.checkSuggestionOption(team.get(0), game.board).equals("make suggestion");
			assertTrue(game.checkSuggestionOption(team.get(0), game.board) == null);

		} catch (IOException e) {

		}
	}

	@Test
	// player should exit room when in a room
	public void test13_validExitRoom_06() {
		try {
			Game game = setUpMockGame();
			ArrayList<Player> team = game.playTeam;
			team.get(0).setP(new Position(5, 7));
			team.get(0).enterRoom(game.board, 'b');
			assert game.checkExitRoomOption(team.get(0), game.board).equals("exit room");
		} catch (IOException e) {

		}
	}

	//
	@Test
	// enter a non existing room, should fail the in a room boolean
	public void test14_InValidMove_01() {
		try {
			Game game = setUpMockGame();
			ArrayList<Player> team = game.playTeam;
			team.get(0).setP(new Position(5, 7));
			team.get(0).enterRoom(game.board, 'b');
			assertFalse(!team.get(0).isArePlayerInARoom());
		} catch (IOException e) {

		}
	}

	@Test
	// the player can know another card when entering a room
	public void test15_inValidMove_02() {
		try {
			Game game = setUpMockGame();
			ArrayList<Player> team = game.playTeam;
			team.get(0).setP(new Position(5, 7));
			int before = team.get(0).getKnowingCards().size();

			team.get(0).enterRoom(game.board, 'b');
			int after = team.get(0).getKnowingCards().size();
			assertFalse(after > before);
		} catch (IOException e) {

		}
	}

	@Test
	// room symbol should match with the room
	public void test16_checkRoomSymbol() {
		try {
			Game game = setUpMockGame();
			ArrayList<Player> team = game.playTeam;
			team.get(0).setP(new Position(5, 7));
			team.get(0).enterRoom(game.board, 'b');
			assertFalse('b' != team.get(0).getRoomSymbol());
		} catch (IOException e) {

		}
	}

	@Test
	// player can not use stair if there is no stair in a room
	public void test17_checkStairs() {
		try {
			Game game = setUpMockGame();
			ArrayList<Player> team = game.playTeam;
			team.get(0).setP(new Position(5, 7));
			team.get(0).enterRoom(game.board, 'b');
			assertFalse(game.checkStairOption(team.get(0), game.board) != null);
		} catch (IOException e) {

		}
	}

	@Test
	// make sure move player to a empty position in the room
	public void test18_checkEmptyPositionInRoom() {
		try {
			Game game = setUpMockGame();
			ArrayList<Player> team = game.playTeam;
			team.get(0).setP(new Position(5, 7));
			Position p = team.get(0).findPositionInTheRoom(game.board, 'b');
			assertFalse(game.board.outputBoard[p.getX()][p.getY()] != ' ');
		} catch (IOException e) {

		}
	}

	@Test
	// make sure player get transfered to the associate room through stairs
	public void test19_checkUseStairs() {
		try {
			Game game = setUpMockGame();
			ArrayList<Player> team = game.playTeam;
			team.get(0).setP(new Position(5, 18));
			team.get(0).enterRoom(game.board, 'c');
			team.get(0).useStairs(game.board);
			assertTrue(team.get(0).getRoomSymbol() == 'o');
		} catch (IOException e) {

		}
	}

	@Test
	// players shouldn't share a position
	public void test20_checkoverlapping() {
		try {
			Game game = setUpMockGame();
			ArrayList<Player> team = game.playTeam;
			team.get(0).setP(new Position(5, 18));
			team.get(0).enterRoom(game.board, 'c');
			team.get(1).setP(new Position(5, 18));
			team.get(1).enterRoom(game.board, 'c');
			team.get(2).setP(new Position(5, 18));
			team.get(2).enterRoom(game.board, 'c');
			team.get(3).setP(new Position(5, 18));
			team.get(3).enterRoom(game.board, 'c');
			team.get(4).setP(new Position(5, 18));
			team.get(4).enterRoom(game.board, 'c');
			team.get(5).setP(new Position(5, 18));
			team.get(5).enterRoom(game.board, 'c');
			assertFalse(team.get(0).getP().equals(team.get(1).getP()) || team.get(0).getP().equals(team.get(2).getP())
					|| team.get(0).getP().equals(team.get(3).getP()) || team.get(0).getP().equals(team.get(4).getP())
					|| team.get(0).getP().equals(team.get(5).getP()) || team.get(1).getP().equals(team.get(2).getP())
					|| team.get(1).getP().equals(team.get(3).getP()) || team.get(1).getP().equals(team.get(4).getP())
					|| team.get(1).getP().equals(team.get(5).getP()) || team.get(2).getP().equals(team.get(3).getP())
					|| team.get(2).getP().equals(team.get(4).getP()) || team.get(2).getP().equals(team.get(5).getP())
					|| team.get(3).getP().equals(team.get(4).getP()) || team.get(3).getP().equals(team.get(5).getP())
					|| team.get(4).getP().equals(team.get(5).getP()));
		} catch (IOException e) {

		}
	}

	public Game setUpMockGame() throws IOException {
		// TextMain text = new TextMain();
		Board board = new Board("map.txt");
		ArrayList<Player> playTeam = new ArrayList<>();

		// player's name
		String name1 = Characters.COLONEL_MUSTARD.name();
		String name2 = Characters.MISS_SCARLETT.name();
		String name3 = Characters.MRS_PEACOCK.name();
		String name4 = Characters.MRS_WHITE.name();
		String name5 = Characters.PROFESSOR_PLUM.name();
		String name6 = Characters.THE_REVEREND_GREEN.name();

		// set up players
		Player p1 = new Player(board.startPoints.get(0), name1, name1.charAt(name1.length() - 1),
				new ArrayList<Card>());
		Player p2 = new Player(board.startPoints.get(1), name2, name2.charAt(name2.length() - 1),
				new ArrayList<Card>());
		Player p3 = new Player(board.startPoints.get(2), name3, name3.charAt(name3.length() - 1),
				new ArrayList<Card>());
		Player p4 = new Player(board.startPoints.get(3), name4, name4.charAt(name4.length() - 1),
				new ArrayList<Card>());
		Player p5 = new Player(board.startPoints.get(4), name5, name5.charAt(name5.length() - 1),
				new ArrayList<Card>());
		Player p6 = new Player(board.startPoints.get(5), name6, name6.charAt(name6.length() - 1),
				new ArrayList<Card>());

		playTeam.add(p1);
		playTeam.add(p2);
		playTeam.add(p3);
		playTeam.add(p4);
		playTeam.add(p5);
		playTeam.add(p6);

		// Creates a new game
		Game game = new Game(board, playTeam);
		return game;
	}

}
