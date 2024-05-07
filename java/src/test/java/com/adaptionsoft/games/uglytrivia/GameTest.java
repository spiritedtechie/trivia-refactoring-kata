package com.adaptionsoft.games.uglytrivia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
	public void test_goToNextPlayer_cannotExceedNumberOfPlayers() throws Exception {
		assertEquals("Bob", game.getCurrentPlayer().getName());
		game.goToNextPlayer();
		assertEquals("John", game.getCurrentPlayer().getName());
		game.goToNextPlayer();
		assertEquals("Bob", game.getCurrentPlayer().getName());
	}

	@Test
	public void test_addPlayer_initialisesPlayersCorrectyl() {
		Player player = game.getCurrentPlayer();

		assertEquals("Bob", player.getName());
		assertEquals(0, player.getPlace());
		assertEquals(0, player.getPurse());
		assertFalse(player.isInPenaltyBox());
	}

	@Test
	public void test_addPlayer_canOnlySupportAtMost5Players() {
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
	public void test_numberofPlayers() {
		assertEquals(2, game.numberOfPlayers());
	}

	@Test
	public void test_numberofPlayers_whereNoPlayersAddedYet() {
		game = new Game();

		assertEquals(0, game.numberOfPlayers());
	}

	@Test
	public void test_numberofPlayers_onceMorePlayersAdded() {
		game.add("George");
		game.add("Fred");
		game.add("Jane");

		assertEquals(5, game.numberOfPlayers());
	}

	@Test
	public void test_askQuestion_printsTheNextQuestion() {
		game.askQuestion();

		assertEquals("The category is Pop\n" + "Pop Question 0", outputStreamCaptor.toString().trim());
	}

	@Test
	public void integration_test_roll_wherePlayerNotInPenaltyBox() {
		Player player = game.getCurrentPlayer();
		assertEquals(0, player.getPlace());

		game.rollDice(5);

		assertEquals(5, player.getPlace());
		assertEquals("Bob is the current player\n" + //
				"They have rolled a 5\n" + //
				"Bob's new location is 5\n" + //
				"The category is Science\n" + //
				"Science Question 0", outputStreamCaptor.toString().trim());
	}

	@Test
	public void integration_test_roll_wherePlayerInPenaltyBoxAndDidNotRollAnOddNumberSoStaysInPenaltyBox() {
		Player player = game.getCurrentPlayer();
		player.setInPenaltyBox(true);
		assertEquals(0, player.getPlace());

		game.rollDice(4);

		assertEquals(0, player.getPlace());
		assertEquals("Bob is the current player\n" + //
				"They have rolled a 4\n" + //
				"Bob is not getting out of the penalty box", outputStreamCaptor.toString().trim());
		assertTrue(player.isInPenaltyBox());
	}

	@Test
	public void integration_test_roll_wherePlayerInPenaltyBoxAndDidRollAnOddNumberSoGetsOutOfPenaltyBox() {
		Player player = game.getCurrentPlayer();
		player.setInPenaltyBox(true);
		assertEquals(0, player.getPlace());

		game.rollDice(5);

		assertEquals(5, player.getPlace());
		assertEquals("Bob is the current player\n" + //
				"They have rolled a 5\n" + //
				"Bob's new location is 5\n" + //
				"The category is Science\n" + //
				"Science Question 0", outputStreamCaptor.toString().trim());
		assertFalse(player.isInPenaltyBox());
	}

	@Test
	public void integration_test_roll_whereRollAmountLessThan1() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			game.rollDice(0);
		});

		String expectedMessage = "Dice roll should be between 1 and 5 inclusive";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void integration_test_roll_whereRollAmountGreaterThan5() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			game.rollDice(6);
		});

		String expectedMessage = "Dice roll should be between 1 and 5 inclusive";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void integration_test_wasCorrectlyAnswered_wherePlayerNotInPenaltyBox() {
		Player player = game.getCurrentPlayer();
		player.setInPenaltyBox(false);
		assertEquals(player, game.getCurrentPlayer());
		assertEquals(0, player.getPurse());

		boolean wasNotWinner = game.wasCorrectlyAnswered();

		assertEquals(1, player.getPurse());
		assertEquals("Answer was correct!!!!\n" + //
				"Bob now has 1 Gold Coins.", outputStreamCaptor.toString().trim());
		assertTrue(wasNotWinner);
		assertEquals("John", game.getCurrentPlayer().getName());
		assertFalse(player.isInPenaltyBox());
	}

	@Test
	public void integration_test_wasCorrectlyAnswered_wherePlayerInPenaltyBox() {
		Player player = game.getCurrentPlayer();
		player.setInPenaltyBox(true);
		assertEquals(player, game.getCurrentPlayer());
		assertEquals(0, player.getPurse());

		boolean wasNotWinner = game.wasCorrectlyAnswered();

		assertEquals(0, player.getPurse());
		assertEquals("", outputStreamCaptor.toString().trim());
		assertTrue(wasNotWinner);
		assertEquals("John", game.getCurrentPlayer().getName());
		assertTrue(player.isInPenaltyBox());
	}

	@Test
	public void integration_test_wrongAnswer() {
		Player player = game.getCurrentPlayer();
		player.setInPenaltyBox(false);
		assertEquals(player, game.getCurrentPlayer());

		boolean wasNotWinner = game.wrongAnswer();

		assertEquals(0, player.getPurse());
		assertEquals("Question was incorrectly answered\n" + //
				"Bob was sent to the penalty box", outputStreamCaptor.toString().trim());
		assertTrue(wasNotWinner);
		assertEquals("John", game.getCurrentPlayer().getName());
		assertTrue(player.isInPenaltyBox());
	}
}
