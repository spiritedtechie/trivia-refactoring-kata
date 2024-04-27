package com.adaptionsoft.games.uglytrivia;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
	public void test_thatAPlayersNextPlaceInTurnsIsCurrentPlacePlusTheRollValue() {
		game.updateNextPlace(0, 2);

		assertEquals(2, game.places[0]);
	}

	@Test
	public void test_thatAPlayersNextPlaceInTurnsDoesntExceed11() {
		game.updateNextPlace(0, 12);

		assertEquals(0, game.places[0]);
	}

	@Test
	public void test_thatAPlayersNextPlaceInTurnsMaxesAt11() {
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
}
