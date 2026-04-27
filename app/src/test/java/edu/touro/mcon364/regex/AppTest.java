package edu.touro.mcon364.regex;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;

public class AppTest {

	@Test
	public void properName() {
		assertTrue(App.properName("Al"));
		assertFalse(App.properName("Q"));
		assertTrue(App.properName("Baljeet"));
		assertFalse(App.properName(""));
		assertFalse(App.properName("jemima"));
	}

	@Test
	public void integer() {
		assertTrue(App.integer("12"));
		assertTrue(App.integer("43.23"));
		assertTrue(App.integer("-34.5"));
		assertTrue(App.integer("+98.7"));
		assertTrue(App.integer("0"));
		assertTrue(App.integer("0.0230"));
		assertFalse(App.integer("023"));
		assertFalse(App.integer("Word"));
	}

	@Test
	public void ancestor() {
		assertTrue(App.ancestor("father"));
		assertTrue(App.ancestor("mother"));
		assertTrue(App.ancestor("Mother"));
		assertTrue(App.ancestor("grandfather"));
		assertTrue(App.ancestor("grandmother"));
		assertTrue(App.ancestor("Grandfather"));
		assertTrue(App.ancestor("Grandmother"));
		assertTrue(App.ancestor("great-grandmother"));
		assertTrue(App.ancestor("great-great-grandmother"));
		assertFalse(App.ancestor("son"));
		assertFalse(App.ancestor("great-father"));
		assertFalse(App.ancestor("great-mother"));
	}

	@Test
	public void palindrome() {
		assertTrue(App.palindrome("asdfggfdsa"));
		assertTrue(App.palindrome("ASDFGGFDSA"));
		assertTrue(App.palindrome("StrawWarts"));
		assertTrue(App.palindrome("RegalLager"));
		assertTrue(App.palindrome("SmartTrams"));
		assertFalse(App.palindrome("palindrome"));
		assertFalse(App.palindrome("asdfghfdsa"));
		assertFalse(App.palindrome(""));
	}

	@Test
	public void allGreenIncluded() {
		WordleResponse letter0 = new WordleResponse();
		letter0.c = 'a';
		letter0.index = 0;
		letter0.resp = LetterResponse.CORRECT_LOCATION;
		WordleResponse letter1 = new WordleResponse();
		letter1.c = 'b';
		letter1.index = 1;
		letter1.resp = LetterResponse.CORRECT_LOCATION;
		WordleResponse letter2 = new WordleResponse();
		letter2.c = 'a';
		letter2.index = 2;
		letter2.resp = LetterResponse.CORRECT_LOCATION;
		WordleResponse letter3 = new WordleResponse();
		letter3.c = 'c';
		letter3.index = 3;
		letter3.resp = LetterResponse.CORRECT_LOCATION;
		WordleResponse letter4 = new WordleResponse();
		letter4.c = 'a';
		letter4.index = 4;
		letter4.resp = LetterResponse.CORRECT_LOCATION;
		List<String> matches = App
				.wordleMatches(List.of(List.of(letter0, letter1, letter2, letter3, letter4)));
		assertEquals(1, matches.size());
		assertTrue(matches.contains("abaca"));
	}

	@Test
	public void twoGreenIncluded() {
		WordleResponse letter0 = new WordleResponse();
		letter0.c = 'a';
		letter0.index = 0;
		letter0.resp = LetterResponse.CORRECT_LOCATION;
		WordleResponse letter4 = new WordleResponse();
		letter4.c = 'a';
		letter4.index = 4;
		letter4.resp = LetterResponse.CORRECT_LOCATION;
		List<String> matches = App.wordleMatches(List.of(List.of(letter0, letter4)));
		assertTrue(matches.contains("abaca"));
		assertTrue(matches.contains("abaka"));
		assertTrue(matches.contains("abaya"));
		assertFalse(matches.contains("fling"));
		for (String match : matches) {
			assertTrue(match.charAt(0) == 'a');
			assertTrue(match.charAt(4) == 'a');
		}
	}

	@Test
	public void oneGrayExcluded() {
		WordleResponse letter0 = new WordleResponse();
		letter0.c = 'b';
		letter0.index = 0;
		letter0.resp = LetterResponse.WRONG_LETTER;
		List<String> matches = App.wordleMatches(List.of(List.of(letter0)));
		assertFalse(matches.contains("abuzz"));
		assertTrue(matches.contains("fling"));
	}

	@Test
	public void twoGrayExcluded() {
		WordleResponse letter0 = new WordleResponse();
		letter0.c = 'b';
		letter0.index = 0;
		letter0.resp = LetterResponse.WRONG_LETTER;
		WordleResponse letter1 = new WordleResponse();
		letter1.c = 'z';
		letter1.index = 1;
		letter1.resp = LetterResponse.WRONG_LETTER;
		List<String> matches = App.wordleMatches(List.of(List.of(letter0, letter1)));
		assertFalse(matches.contains("abuzz"));
		assertFalse(matches.contains("adzes"));
		assertTrue(matches.contains("fling"));
		for (String match : matches) {
			assertFalse(match.contains("b"));
			assertFalse(match.contains("z"));
		}
	}

	@Test
	public void yellowExcluded() {
		WordleResponse letter0 = new WordleResponse();
		letter0.c = 'a';
		letter0.index = 0;
		letter0.resp = LetterResponse.WRONG_LOCATION;
		List<String> matches = App.wordleMatches(List.of(List.of(letter0)));
		assertFalse(matches.contains("abuzz"));
		assertFalse(matches.contains("abaca"));
		assertTrue(matches.contains("fling"));
	}

	@Test
	public void twoYellowExcluded() {
		WordleResponse letter0 = new WordleResponse();
		letter0.c = 'a';
		letter0.index = 0;
		letter0.resp = LetterResponse.WRONG_LOCATION;
		WordleResponse letter2 = new WordleResponse();
		letter2.c = 'b';
		letter2.index = 2;
		letter2.resp = LetterResponse.WRONG_LOCATION;
		List<String> matches = App.wordleMatches(List.of(List.of(letter0, letter2)));
		assertFalse(matches.contains("abuzz"));
		assertFalse(matches.contains("abaca"));
		for (String match : matches) {
			assertTrue(match.charAt(0) != 'a');
			assertTrue(match.charAt(2) != 'b');
		}
	}

	@Test
	public void greenAndGrayCombined() {
		WordleResponse letter0 = new WordleResponse();
		letter0.c = 'a';
		letter0.index = 0;
		letter0.resp = LetterResponse.CORRECT_LOCATION;
		WordleResponse letter1 = new WordleResponse();
		letter1.c = 'b';
		letter1.index = 1;
		letter1.resp = LetterResponse.WRONG_LETTER;
		List<String> matches = App.wordleMatches(List.of(List.of(letter0, letter1)));
		assertFalse(matches.contains("abuzz"));
		assertFalse(matches.contains("fling"));
		for (String match : matches) {
			assertTrue(match.charAt(0) == 'a');
			assertFalse(match.contains("b"));
		}
	}

	@Test
	public void yellowAndGrayCombined() {
		WordleResponse letter1 = new WordleResponse();
		letter1.c = 'b';
		letter1.index = 1;
		letter1.resp = LetterResponse.WRONG_LOCATION;
		WordleResponse letter2 = new WordleResponse();
		letter2.c = 'z';
		letter2.index = 2;
		letter2.resp = LetterResponse.WRONG_LETTER;
		List<String> matches = App.wordleMatches(List.of(List.of(letter1, letter2)));
		assertFalse(matches.contains("abuzz"));
		assertFalse(matches.contains("adzes"));
		assertTrue(matches.contains("fling"));
	}

	@Test
	public void twoGuessRounds() {
		WordleResponse r1Letter0 = new WordleResponse();
		r1Letter0.c = 'b';
		r1Letter0.index = 0;
		r1Letter0.resp = LetterResponse.WRONG_LETTER;
		WordleResponse r2Letter0 = new WordleResponse();
		r2Letter0.c = 'a';
		r2Letter0.index = 0;
		r2Letter0.resp = LetterResponse.CORRECT_LOCATION;
		WordleResponse r2Letter1 = new WordleResponse();
		r2Letter1.c = 'z';
		r2Letter1.index = 1;
		r2Letter1.resp = LetterResponse.WRONG_LETTER;
		List<String> matches = App.wordleMatches(List.of(List.of(r1Letter0), List.of(r2Letter0, r2Letter1)));
		assertFalse(matches.contains("abuzz"));
		assertFalse(matches.contains("adzes"));
		for (String match : matches) {
			assertTrue(match.charAt(0) == 'a');
			assertFalse(match.contains("b"));
			assertFalse(match.contains("z"));
		}
	}

	@Test
	public void threeGuessRounds() {
		WordleResponse letter0 = new WordleResponse();
		letter0.c = 'a';
		letter0.index = 0;
		letter0.resp = LetterResponse.CORRECT_LOCATION;
		WordleResponse letter1 = new WordleResponse();
		letter1.c = 'b';
		letter1.index = 1;
		letter1.resp = LetterResponse.CORRECT_LOCATION;
		WordleResponse letter2 = new WordleResponse();
		letter2.c = 'a';
		letter2.index = 2;
		letter2.resp = LetterResponse.CORRECT_LOCATION;
		WordleResponse letter3 = new WordleResponse();
		letter3.c = 'c';
		letter3.index = 3;
		letter3.resp = LetterResponse.CORRECT_LOCATION;
		WordleResponse letter4 = new WordleResponse();
		letter4.c = 'a';
		letter4.index = 4;
		letter4.resp = LetterResponse.CORRECT_LOCATION;
		List<String> matches = App.wordleMatches(
				List.of(List.of(letter0, letter1), List.of(letter2, letter3), List.of(letter4)));
		assertEquals(1, matches.size());
		assertTrue(matches.contains("abaca"));
	}
}
