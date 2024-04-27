package com.adaptionsoft.games.uglytrivia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameTest {

	private Game game = new Game();

	@BeforeEach
	public void addBasePlayers() {
		game.add("Bob");
		game.add("John");
	}

	@Test
	public void test_thatCurrentPlayerPositionCannotExceedNumberOfPlayers() throws Exception {
		assertEquals(0, game.currentPlayer);
		game.goToNextPlayer();
		assertEquals(1, game.currentPlayer);
		game.goToNextPlayer();
		assertEquals(0, game.currentPlayer);
	}

	@Test
	public void test_thatThereCanBeAtMost5Players() {
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
	public void test_thatAPlayersNextPlaceIsCurrentPlacePlusTheRollValue() {
		game.updateNextPlace(0, 2);

		assertEquals(2, game.players_typed.get(0).getPlace());

	}

	@Test
	public void test_thatAPlayersNextPlaceDoesntExceed11() {
		game.updateNextPlace(0, 12);

		assertEquals(0, game.players_typed.get(0).getPlace());
	}

	@Test
	public void test_thatAPlayersNextPlaceMaxesAt11() {
		game.updateNextPlace(0, 11);

		assertEquals(11, game.players_typed.get(0).getPlace());
	}

	@Test
	public void test_getCategoryBasedOnPlace() {
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
	public void test_fetchesNextQuestionForEachCategory() {
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
	public void test_fetchesEmptyQuestionForUnknownCategory() {
		assertNull(game.getNextQuestion("Unknown"));
	}

	@Test
	public void test_errorsOnceQuestionsForACategoryHaveRanOut() {
		Exception exception = assertThrows(IllegalStateException.class, () -> {
			for (int r = 0; r <= 50; r++) {
				game.getNextQuestion("Pop");
			}
		});

		assertTrue(exception.getMessage().contains("No further questions for category"));
	}

	@Test
	public void test_retrievesPlayerName() {
		assertEquals("Bob", game.getPlayerName(0));
		assertEquals("John", game.getPlayerName(1));
	}

	@Test
	public void test_retrievesPlayerNameThrowWhenPlayerNumberGreaterThanNumberOfPlayers() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			game.getPlayerName(3);
		});

		assertTrue(exception.getMessage().contains("Invalid player number"));

	}

	@Test
	public void test_retrievesPlayerNameThrowWhenPlayerNumberNegative() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			game.getPlayerName(-1);
		});

		assertTrue(exception.getMessage().contains("Invalid player number"));

	}

	@Test
	public void test_howManyPlayers() {
		assertEquals(2, game.howManyPlayers());
	}

	@Test
	public void test_howManyPlayersWhereNoPlayersAddedYet() {
		game = new Game();

		assertEquals(0, game.howManyPlayers());
	}

	@Test
	public void test_howManyPlayersOnceMorePlayerAdded() {
		game.add("George");
		game.add("Fred");
		game.add("Jane");

		assertEquals(5, game.howManyPlayers());
	}
}
