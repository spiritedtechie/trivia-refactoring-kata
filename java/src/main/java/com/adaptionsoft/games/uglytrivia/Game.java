package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

enum Category {
	Pop, Science, Sports, Rock
}

class Player {
	private String name;
	private int place = 0;
	private int purse = 0;
	private boolean inPenaltyBox = false;

	public Player(String name) {
		this.name = name;
	}

	String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	int getPlace() {
		return place;
	}

	void setPlace(int place) {
		this.place = place;
	}

	int getPurse() {
		return purse;
	}

	void addToPurse(int coins) {
		this.purse += coins;
	}

	boolean isInPenaltyBox() {
		return inPenaltyBox;
	}

	void setInPenaltyBox(boolean inPenaltyBox) {
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

	private Map<Category, LinkedList<String>> questions = new HashMap<>();

	private int currentPlayerIndex = 0;

	public Game() {
		initialiseQuestions();
	}

	private void initialiseQuestions() {
		for (Category category : Category.values()) {
			LinkedList<String> categoryQuestions = new LinkedList<String>();
			for (int i = 0; i < 50; i++) {
				categoryQuestions.addLast(category.name() + " Question " + i);
			}
			questions.put(category, categoryQuestions);
		}
	}

	public boolean add(String playerName) {
		if (numberOfPlayers() == 5) {
			throw new IllegalStateException("Can only add upto and including 5 players");
		}

		players.add(new Player(playerName));

		System.out.println(playerName + " was added");
		System.out.println("They are player number " + numberOfPlayers());
		return true;
	}

	int numberOfPlayers() {
		return players.size();
	}

	Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}

	public void roll(int roll) {
		Player player = getCurrentPlayer();

		System.out.println(player.getName() + " is the current player");
		System.out.println("They have rolled a " + roll);

		boolean isEvenRoll = roll % 2 == 0;

		if (player.isInPenaltyBox() && isEvenRoll) {
			System.out.println(player.getName() + " is not getting out of the penalty box");
		} else {
			player.setInPenaltyBox(false);
			player.updateNextPlace(roll);
			System.out.println(player.getName() + "'s new location is " + player.getPlace());
			askQuestion();
		}
	}

	void askQuestion() {
		Category category = getCategory(getCurrentPlayer().getPlace());
		System.out.println("The category is " + getCategory(getCurrentPlayer().getPlace()));
		String question = (String) getNextQuestion(category);
		System.out.println(question);
	}

	String getNextQuestion(Category category) {
		LinkedList<String> questionsForCategory = questions.get(category);

		if (questionsForCategory == null) {
			return null;
		}

		if (questionsForCategory.isEmpty()) {
			throw new IllegalStateException("No further questions for category: " + category);
		}

		return questionsForCategory.removeFirst();
	}

	Category getCategory(int playerPlace) {
		switch (playerPlace) {
			case 0:
			case 4:
			case 8:
				return Category.Pop;
			case 1:
			case 5:
			case 9:
				return Category.Science;
			case 2:
			case 6:
			case 10:
				return Category.Sports;
			default:
				return Category.Rock;
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
		currentPlayerIndex++;
		if (currentPlayerIndex == numberOfPlayers())
			currentPlayerIndex = 0;
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
