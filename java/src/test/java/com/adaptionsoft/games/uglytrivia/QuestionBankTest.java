package com.adaptionsoft.games.uglytrivia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class QuestionBankTest {

	private QuestionBank questionBank = new QuestionBank();

	@Test
	public void test_getNextQuestionForEachCategory() {
		assertEquals("Pop Question 0", questionBank.getNextQuestion(Category.Pop));
		assertEquals("Pop Question 1", questionBank.getNextQuestion(Category.Pop));

		assertEquals("Science Question 0", questionBank.getNextQuestion(Category.Science));
		assertEquals("Science Question 1", questionBank.getNextQuestion(Category.Science));

		assertEquals("Sports Question 0", questionBank.getNextQuestion(Category.Sports));
		assertEquals("Sports Question 1", questionBank.getNextQuestion(Category.Sports));

		assertEquals("Rock Question 0", questionBank.getNextQuestion(Category.Rock));
		assertEquals("Rock Question 1", questionBank.getNextQuestion(Category.Rock));
	}

	@Test
	public void test_getNextQuestionErrorsOnceQuestionsForACategoryHaveRanOut() {
		Exception exception = assertThrows(IllegalStateException.class, () -> {
			for (int r = 0; r <= 50; r++) {
				questionBank.getNextQuestion(Category.Pop);
			}
		});

		assertTrue(exception.getMessage().contains("No further questions for category"));
	}

	@Test
	public void test_getCategoryBasedOnPlayerPlace() {
		assertEquals(Category.Pop, QuestionBank.getCategory(0));
		assertEquals(Category.Science, QuestionBank.getCategory(1));
		assertEquals(Category.Sports, QuestionBank.getCategory(2));
		assertEquals(Category.Rock, QuestionBank.getCategory(3));
		assertEquals(Category.Pop, QuestionBank.getCategory(4));
		assertEquals(Category.Science, QuestionBank.getCategory(5));
		assertEquals(Category.Sports, QuestionBank.getCategory(6));
		assertEquals(Category.Rock, QuestionBank.getCategory(7));
		assertEquals(Category.Pop, QuestionBank.getCategory(8));
		assertEquals(Category.Science, QuestionBank.getCategory(9));
		assertEquals(Category.Sports, QuestionBank.getCategory(10));
		assertEquals(Category.Rock, QuestionBank.getCategory(11));
		assertEquals(Category.Rock, QuestionBank.getCategory(12));
	}
}
