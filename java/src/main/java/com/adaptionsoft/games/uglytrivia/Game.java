package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class Player {
	private String name;
	private int place = 0;
	private int purse = 0;
	private boolean inPenaltyBox = false;

	public Player(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	public int getPurse() {
		return purse;
	}

	public void addToPurse(int coins) {
		this.purse += coins;
	}

	public boolean isInPenaltyBox() {
		return inPenaltyBox;
	}

	public void setInPenaltyBox(boolean inPenaltyBox) {
		this.inPenaltyBox = inPenaltyBox;
	}

	void updateNextPlace(int roll) {
		Integer newPlace = getPlace() + roll;
		if (newPlace > 11)
			newPlace = newPlace - 12;
		setPlace(newPlace);
	}

	boolean didPlayerWin() {
		return getPurse() == 6;
	}

}

public class Game {

	private List<Player> players = new ArrayList<>();

	private Map<String, LinkedList<String>> questions = new HashMap<>();

	private int currentPlayer = 0;

	public Game() {
		LinkedList<String> popQuestions = new LinkedList<String>();
		LinkedList<String> scienceQuestions = new LinkedList<String>();
		LinkedList<String> sportsQuestions = new LinkedList<String>();
		LinkedList<String> rockQuestions = new LinkedList<String>();

		for (int i = 0; i < 50; i++) {
			popQuestions.addLast("Pop Question " + i);
			scienceQuestions.addLast(("Science Question " + i));
			sportsQuestions.addLast(("Sports Question " + i));
			rockQuestions.addLast("Rock Question " + i);
		}

		questions.put("Pop", popQuestions);
		questions.put("Science", scienceQuestions);
		questions.put("Sports", sportsQuestions);
		questions.put("Rock", rockQuestions);
	}

	public boolean add(String playerName) {
		if (numberOfPlayers() == 5) {
			throw new IllegalStateException("Can only add upto and including 5 players");
		}

		Player player = new Player(playerName);
		players.add(player);

		System.out.println(playerName + " was added");
		System.out.println("They are player number " + numberOfPlayers());
		return true;
	}

	int numberOfPlayers() {
		return players.size();
	}

	Player getCurrentPlayer() {
		return players.get(currentPlayer);
	}

	public void roll(int roll) {
		Player player = getCurrentPlayer();

		System.out.println(player.getName() + " is the current player");
		System.out.println("They have rolled a " + roll);

		boolean isOddRoll = roll % 2 != 0;

		if (player.isInPenaltyBox() && !isOddRoll) {
			System.out.println(player.getName() + " is not getting out of the penalty box");
		} else {
			player.setInPenaltyBox(false);
			player.updateNextPlace(roll);
			System.out.println(player.getName() + "'s new location is " + player.getPlace());
			askQuestion();
		}
	}

	void askQuestion() {
		String category = getCategory(getCurrentPlayer().getPlace());
		System.out.println("The category is " + getCategory(getCurrentPlayer().getPlace()));
		String question = (String) getNextQuestion(category);
		System.out.println(question);
	}

	Object getNextQuestion(String category) {
		LinkedList<String> questionsForCategory = questions.get(category);

		if (questionsForCategory == null) {
			return null;
		}

		if (questionsForCategory.isEmpty()) {
			throw new IllegalStateException("No further questions for category: " + category);
		}

		return questionsForCategory.removeFirst();
	}

	String getCategory(int playerPlace) {
		switch (playerPlace) {
			case 0:
			case 4:
			case 8:
				return "Pop";
			case 1:
			case 5:
			case 9:
				return "Science";
			case 2:
			case 6:
			case 10:
				return "Sports";
			default:
				return "Rock";
		}
	}

	public boolean wasCorrectlyAnswered() {
		Player player = getCurrentPlayer();
		if (player.isInPenaltyBox()) {
			goToNextPlayer();
			return true;
		} else {
			player.addToPurse(1);
			boolean notWinner = !player.didPlayerWin();
			goToNextPlayer();

			System.out.println("Answer was correct!!!!");
			System.out.println(player.getName() + " now has " + player.getPurse() + " Gold Coins.");

			return notWinner;
		}
	}

	void goToNextPlayer() {
		currentPlayer++;
		if (currentPlayer == numberOfPlayers())
			currentPlayer = 0;
	}

	public boolean wrongAnswer() {
		Player player = getCurrentPlayer();
		player.setInPenaltyBox(true);
		goToNextPlayer();

		System.out.println("Question was incorrectly answered");
		System.out.println(player.getName() + " was sent to the penalty box");

		return true;
	}
}
