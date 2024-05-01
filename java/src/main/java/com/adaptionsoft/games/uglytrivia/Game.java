package com.adaptionsoft.games.uglytrivia;

import static com.adaptionsoft.games.uglytrivia.QuestionBank.getCategory;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

enum Category {
	Pop, Science, Sports, Rock
}

class Player {
	private static final int WINNING_PURSE_AMOUNT = 6;
	private static final int TOTAL_PLACES = 12;

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

	int getPlace() {
		return place;
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
		this.place = (place + roll) % TOTAL_PLACES;
	}

	boolean didPlayerWin() {
		return getPurse() == WINNING_PURSE_AMOUNT;
	}
}

class QuestionBank {
	private static final int NUMBER_OF_QUESTIONS_PER_CATEGORY = 50;

	private EnumMap<Category, LinkedList<String>> questions = new EnumMap<>(Category.class);

	public QuestionBank() {
		initialiseQuestions();
	}

	private void initialiseQuestions() {
		for (Category category : Category.values()) {
			LinkedList<String> categoryQuestions = new LinkedList<String>();
			for (int i = 0; i < NUMBER_OF_QUESTIONS_PER_CATEGORY; i++) {
				categoryQuestions.addLast(category.name() + " Question " + i);
			}
			questions.put(category, categoryQuestions);
		}
	}

	String getNextQuestion(Category category) {
		LinkedList<String> questionsForCategory = questions.get(category);

		if (questionsForCategory.isEmpty()) {
			throw new IllegalStateException("No further questions for category: " + category);
		}

		return questionsForCategory.removeFirst();
	}

	static Category getCategory(int playerPlace) {
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

}

public class Game {

	private static final int MAXIMUM_NUMBER_OF_PLAYERS_ALLOWED = 5;

	private List<Player> players = new ArrayList<>();

	private QuestionBank questionBank;

	private int currentPlayerIndex = 0;

	public Game() {
		questionBank = new QuestionBank();

	}

	public boolean add(String playerName) {
		if (numberOfPlayers() == MAXIMUM_NUMBER_OF_PLAYERS_ALLOWED) {
			throw new IllegalStateException("Can only add upto and including 5 players");
		}

		players.add(new Player(playerName));

		System.out.println(playerName + " was added");
		System.out.println("They are player number " + numberOfPlayers());
		return true;
	}

	/*
	 * Use rollDice instead
	 */
	@Deprecated
	public void roll(int roll) {
		this.rollDice(roll);
	}

	public void rollDice(int roll) {
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

	/**
	 * 
	 * @return returns whether the play did NOT win. Reluctant to change
	 *         a public interface method of this class. Ideally I would like to
	 *         return isWinner instead, and have the client handle that case.
	 */
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

	/**
	 * 
	 * @return returns a fixed value of True for the play did NOT win. Reluctant to change
	 *         a public interface method of this class. Ideally I would like to
	 *         return isWinner=False instead, and have the client handle that case.
	 */
	public boolean wrongAnswer() {
		Player player = getCurrentPlayer();
		player.setInPenaltyBox(true);
		goToNextPlayer();

		System.out.println("Question was incorrectly answered");
		System.out.println(player.getName() + " was sent to the penalty box");

		return true;
	}

	void askQuestion() {
		Category category = getCategory(getCurrentPlayer().getPlace());
		System.out.println("The category is " + getCategory(getCurrentPlayer().getPlace()));
		String question = (String) questionBank.getNextQuestion(category);
		System.out.println(question);
	}

	int numberOfPlayers() {
		return players.size();
	}

	Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}

	void goToNextPlayer() {
		currentPlayerIndex = (currentPlayerIndex + 1) % numberOfPlayers();
	}
}
