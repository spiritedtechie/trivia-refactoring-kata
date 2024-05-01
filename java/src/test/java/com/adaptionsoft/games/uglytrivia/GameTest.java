package com.adaptionsoft.games.uglytrivia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameTest {

	private Game game = new Game();
	private final PrintStream standardOut = System.out;
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

	@BeforeEach
	public void addBasePlayers() {
		game.add("Bob");
		game.add("John");
	}

	@BeforeEach
	public void setOutputStreamCaptor() {
		System.setOut(new PrintStream(outputStreamCaptor));
	}

	@AfterEach
	public void tearDown() {
		System.setOut(standardOut);
	}

	@Test
	public void test_goToNextPlayerCannotExceedNumberOfPlayers() throws Exception {
		assertEquals(0, game.currentPlayer);
		game.goToNextPlayer();
		assertEquals(1, game.currentPlayer);
		game.goToNextPlayer();
		assertEquals(0, game.currentPlayer);
	}

	@Test
	public void test_addPlayerCanOnlySupportAtMost5Players() {
		game.add("George");
		game.add("Fred");
		game.add("Jane");

		Exception exception = assertThrows(IllegalStateException.class, () -> {
			game.add("Jane");
		});

		String expectedMessage = "Can only add upto and including 5 players";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void test_updateNextPlaceIsCurrentPlacePlusTheRollValue() {
		int playerNo = 0;
		Player player = game.players.get(playerNo);

		player.updateNextPlace(2);

		assertEquals(2, player.getPlace());

	}

	@Test
	public void test_updateNextPlaceCannotExceed11AndCirclesBackRound() {
		int playerNo = 0;
		Player player = game.players.get(playerNo);

		player.updateNextPlace(12);

		assertEquals(0, player.getPlace());
	}

	@Test
	public void test_updateNextPlaceMaxesAt11() {
		int playerNo = 0;
		Player player = game.players.get(playerNo);

		player.updateNextPlace(11);

		assertEquals(11, player.getPlace());
	}

	@Test
	public void test_getCategoryBasedOnPlayerPlace() {
		assertEquals("Pop", game.getCategory(0));
		assertEquals("Science", game.getCategory(1));
		assertEquals("Sports", game.getCategory(2));
		assertEquals("Rock", game.getCategory(3));
		assertEquals("Pop", game.getCategory(4));
		assertEquals("Science", game.getCategory(5));
		assertEquals("Sports", game.getCategory(6));
		assertEquals("Rock", game.getCategory(7));
		assertEquals("Pop", game.getCategory(8));
		assertEquals("Science", game.getCategory(9));
		assertEquals("Sports", game.getCategory(10));
		assertEquals("Rock", game.getCategory(11));
		assertEquals("Rock", game.getCategory(12));
	}

	@Test
	public void test_getNextQuestionForEachCategory() {
		assertEquals("Pop Question 0", game.getNextQuestion("Pop"));
		assertEquals("Pop Question 1", game.getNextQuestion("Pop"));

		assertEquals("Science Question 0", game.getNextQuestion("Science"));
		assertEquals("Science Question 1", game.getNextQuestion("Science"));

		assertEquals("Sports Question 0", game.getNextQuestion("Sports"));
		assertEquals("Sports Question 1", game.getNextQuestion("Sports"));

		assertEquals("Rock Question 0", game.getNextQuestion("Rock"));
		assertEquals("Rock Question 1", game.getNextQuestion("Rock"));
	}

	@Test
	public void test_getNextQuestionIsEmptyForUnknownCategory() {
		assertNull(game.getNextQuestion("Unknown"));
	}

	@Test
	public void test_askQuestionPrintsTheNextQuestion() {
		game.askQuestion();

		assertEquals("The category is Pop\n" + "Pop Question 0", outputStreamCaptor.toString().trim());

	}

	@Test
	public void test_getNextQuestionErrorsOnceQuestionsForACategoryHaveRanOut() {
		Exception exception = assertThrows(IllegalStateException.class, () -> {
			for (int r = 0; r <= 50; r++) {
				game.getNextQuestion("Pop");
			}
		});

		assertTrue(exception.getMessage().contains("No further questions for category"));
	}

	@Test
	public void test_getPlayerName() {
		assertEquals("Bob", game.getPlayerName(0));
		assertEquals("John", game.getPlayerName(1));
	}

	@Test
	public void test_getPlayerNameThrowWhenPlayerNumberGreaterThanNumberOfPlayers() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			game.getPlayerName(3);
		});

		assertTrue(exception.getMessage().contains("Invalid player number"));

	}

	@Test
	public void test_getPlayerNameThrowWhenPlayerNumberNegative() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			game.getPlayerName(-1);
		});

		assertTrue(exception.getMessage().contains("Invalid player number"));

	}

	@Test
	public void test_numberofPlayers() {
		assertEquals(2, game.numberOfPlayers());
	}

	@Test
	public void test_numberofPlayersWhereNoPlayersAddedYet() {
		game = new Game();

		assertEquals(0, game.numberOfPlayers());
	}

	@Test
	public void test_numberofPlayersOnceMorePlayersAdded() {
		game.add("George");
		game.add("Fred");
		game.add("Jane");

		assertEquals(5, game.numberOfPlayers());
	}

	@Test
	public void test_add1CoinToPurseForPlayer0() {
		int playerNo = 0;
		Player player = game.players.get(playerNo);
		assertEquals(0, player.getPurse());

		player.addToPurse(1);

		assertEquals(1, player.getPurse());
	}

	@Test
	public void test_add2CoinToPurseForPlayer1() {
		int playerNo = 1;
		Player player = game.players.get(playerNo);
		assertEquals(0, player.getPurse());

		player.addToPurse(1);
		player.addToPurse(1);

		assertEquals(2, player.getPurse());
	}

	@Test
	public void test_didPlayerWin_False_whenPurseLessThan6() {
		int playerNo = 1;
		Player player = game.players.get(playerNo);

		addCoinsToPurse(playerNo, 5);

		assertFalse(player.didPlayerWin());
	}

	@Test
	public void test_didPlayerWin_false_whenPurseGreaterThan6() {
		int playerNo = 1;
		Player player = game.players.get(playerNo);

		addCoinsToPurse(playerNo, 7);

		assertFalse(player.didPlayerWin());
	}

	@Test
	public void test_didPlayerWin_true_whenPurseEqualToSix() {
		int playerNo = 1;
		Player player = game.players.get(playerNo);

		addCoinsToPurse(playerNo, 6);

		assertTrue(player.didPlayerWin());
	}

	private void addCoinsToPurse(int playerNo, int coinCount) {
		Player player = game.players.get(playerNo);
		for (int times = 1; times <= coinCount; times++) {
			player.addToPurse(1);
		}
	}

	@Test
	public void test_inPenaltyBox_false() {
		int playerNo = 1;
		Player player = game.players.get(playerNo);

		assertFalse(player.isInPenaltyBox());
	}

	@Test
	public void test_inPenaltyBox_true() {
		int playerNo = 1;
		Player player = game.players.get(playerNo);
		assertFalse(player.isInPenaltyBox());

		player.setInPenaltyBox(true);

		assertTrue(player.isInPenaltyBox());
	}

	@Test
	public void integration_test_roll_wherePlayerNotInPenaltyBox() {

		Player player = game.players.get(0);
		assertEquals(0, player.getPlace());

		game.roll(6);

		assertEquals(6, player.getPlace());
		assertEquals("Bob is the current player\n" + //
				"They have rolled a 6\n" + //
				"Bob's new location is 6\n" + //
				"The category is Sports\n" + //
				"Sports Question 0", outputStreamCaptor.toString().trim());
	}

	@Test
	public void integration_test_roll_wherePlayerInPenaltyBoxAndDidNotRollAnOddNumberSoStaysInPenaltyBox() {

		Player player = game.players.get(0);
		player.setInPenaltyBox(true);
		assertEquals(0, player.getPlace());

		game.roll(6);

		assertEquals(0, player.getPlace());
		assertEquals("Bob is the current player\n" + //
				"They have rolled a 6\n" + //
				"Bob is not getting out of the penalty box", outputStreamCaptor.toString().trim());
		assertTrue(player.isInPenaltyBox());

	}

	@Test
	public void integration_test_roll_wherePlayerInPenaltyBoxAndDidRollAnOddNumberSoGetsOutOfPenaltyBox() {

		Player player = game.players.get(0);
		player.setInPenaltyBox(true);
		assertEquals(0, player.getPlace());

		game.roll(5);

		assertEquals(5, player.getPlace());
		assertEquals("Bob is the current player\n" + //
				"They have rolled a 5\n" + //
				"Bob's new location is 5\n" + //
				"The category is Science\n" + //
				"Science Question 0", outputStreamCaptor.toString().trim());
		assertFalse(player.isInPenaltyBox());
	}

	@Test
	public void integration_test_wasCorrectlyAnswered_wherePlayerNotInPenaltyBox() {

		Player player = game.players.get(0);
		player.setInPenaltyBox(false);
		assertEquals(0, game.currentPlayer);
		assertEquals(0, player.getPurse());

		boolean wasNotWinner = game.wasCorrectlyAnswered();

		assertEquals(1, player.getPurse());
		assertEquals("Answer was correct!!!!\n" + //
				"Bob now has 1 Gold Coins.", outputStreamCaptor.toString().trim());
		assertTrue(wasNotWinner);
		assertEquals(1, game.currentPlayer);
		assertFalse(player.isInPenaltyBox());

	}

	@Test
	public void integration_test_wasCorrectlyAnswered_wherePlayerInPenaltyBox() {

		Player player = game.players.get(0);
		player.setInPenaltyBox(true);
		assertEquals(0, game.currentPlayer);
		assertEquals(0, player.getPurse());

		boolean wasNotWinner = game.wasCorrectlyAnswered();

		assertEquals(0, player.getPurse());
		assertEquals("", outputStreamCaptor.toString().trim());
		assertTrue(wasNotWinner);
		assertEquals(1, game.currentPlayer);
		assertTrue(player.isInPenaltyBox());
	}

	@Test
	public void integration_test_wrongAnswer() {
		Player player = game.players.get(0);
		player.setInPenaltyBox(false);
		assertEquals(0, game.currentPlayer);

		boolean wasNotWinner = game.wrongAnswer();

		assertEquals(0, player.getPurse());
		assertEquals("Question was incorrectly answered\n" + //
				"Bob was sent to the penalty box", outputStreamCaptor.toString().trim());
		assertTrue(wasNotWinner);
		assertEquals(1, game.currentPlayer);
		assertTrue(player.isInPenaltyBox());
	}
}
