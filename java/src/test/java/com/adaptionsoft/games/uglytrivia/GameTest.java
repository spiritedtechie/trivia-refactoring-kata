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

		assertEquals(2, game.places[0]);
	}

	@Test
	public void test_thatAPlayersNextPlaceDoesntExceed11() {
		game.updateNextPlace(0, 12);

		assertEquals(0, game.places[0]);
	}

	@Test
	public void test_thatAPlayersNextPlaceMaxesAt11() {
		game.add("Bob");
		game.add("John");

		game.updateNextPlace(0, 11);

		assertEquals(11, game.places[0]);
	}

	@Test
	public void test_calculatesCurrentCategoryForPlayerBasedOnTheirPlace() {
		assertEquals("Pop", game.currentCategory(0));
		assertEquals("Science", game.currentCategory(1));
		assertEquals("Sports", game.currentCategory(2));
		assertEquals("Rock", game.currentCategory(3));
		assertEquals("Pop", game.currentCategory(4));
		assertEquals("Science", game.currentCategory(5));
		assertEquals("Sports", game.currentCategory(6));
		assertEquals("Rock", game.currentCategory(7));
		assertEquals("Pop", game.currentCategory(8));
		assertEquals("Science", game.currentCategory(9));
		assertEquals("Sports", game.currentCategory(10));
		assertEquals("Rock", game.currentCategory(11));
		assertEquals("Rock", game.currentCategory(12));
	}

	@Test
	public void test_fetchesNextQuestionForEachCategory() {
		assertEquals("Pop Question 0", game.getQuestion("Pop"));
		assertEquals("Pop Question 1", game.getQuestion("Pop"));

		assertEquals("Science Question 0", game.getQuestion("Science"));
		assertEquals("Science Question 1", game.getQuestion("Science"));

		assertEquals("Sports Question 0", game.getQuestion("Sports"));
		assertEquals("Sports Question 1", game.getQuestion("Sports"));

		assertEquals("Rock Question 0", game.getQuestion("Rock"));
		assertEquals("Rock Question 1", game.getQuestion("Rock"));
	}

	@Test
	public void test_fetchesEmptyQuestionForUnknownCategory() {
		assertNull(game.getQuestion("Unknown"));
	}

	@Test
	public void test_errorsOnceQuestionsForACategoryHaveRanOut() {
		Exception exception = assertThrows(IllegalStateException.class, () -> {
			for (int r = 0; r <= 50; r++) {
				game.getQuestion("Pop");
			}
		});

		String expectedMessage = "No further questions for category";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}
}
