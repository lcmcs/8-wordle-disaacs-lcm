package edu.touro.mcon364.regex;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class App {
	private static final Pattern properNamePat = Pattern.compile("^[A-Z]{1}[a-z]+$");
	private static final Pattern integerPat = Pattern.compile("^[+-]?(0|[1-9]+\\d*)(?:\\.\\d+)?$");
	private static final Pattern ancestorPat = Pattern
			.compile("([gG]rand|([gG]reat-)*(grand)+)?([fF]ather|[Mm]other)+$");
	private static final Pattern palindromePat = Pattern.compile("^(\\w)(\\w)(\\w)(\\w)(\\w)\\5\\4\\3\\2\\1$",
			Pattern.CASE_INSENSITIVE);

	public static void main(String[] args) {
	}

	/** Matches a proper name like Bob, Smith, Joey */
	public static boolean properName(String s) {
		return properNamePat.matcher(s).matches();
	}

	/**
	 * Matches a number (integer or decimal, positive or negative) 12, 43,23, -34.5,
	 * +98.7, 0, 0.0230 (but not 023)
	 */
	public static boolean integer(String s) {
		return integerPat.matcher(s).matches();
	}

	/** Matches an ancestor like father, mother, great-great-grandmother. */
	public static boolean ancestor(String s) {
		return ancestorPat.matcher(s).matches();
	}

	/** Matches a 10 letter case insensitive palindrome like "asdfggfdsa" */
	public static boolean palindrome(String s) {
		return palindromePat.matcher(s).matches();
	}

	public static List<String> wordleMatches(List<List<WordleResponse>> responses) {
		// Load dictionary into memory
		List<String> dict = new ArrayList<>();
		try (InputStream is = App.class.getResourceAsStream("/valid-wordle-words.txt");
				BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			dict = br.readAllLines();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String wrongLetters = "";
		ArrayList<String> wrongLocations = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			wrongLocations.add("");
		}
		HashMap<Integer, Character> correctLetters = new HashMap<>();

		for (List<WordleResponse> word : responses) {
			for (WordleResponse letter : word) {
				switch (letter.resp) {
					case WRONG_LETTER:
						wrongLetters += letter.c;
						break;
					case CORRECT_LOCATION:
						correctLetters.put(letter.index, letter.c);
						break;
					case WRONG_LOCATION:
						wrongLocations.set(letter.index,
								wrongLocations.get(letter.index) + letter.c);
						break;
				}
			}
		}

		String solutionRegexStr = "";
		for (int i = 0; i < 5; i++) {
			if (correctLetters.containsKey(i)) {
				solutionRegexStr += correctLetters.get(i);
			} else {
				String exclusions = wrongLetters + wrongLocations.get(i);
				solutionRegexStr += exclusions.isEmpty() ? "[a-z]" : "[^" + exclusions + "]";
			}
		}
		Pattern solutionPat = Pattern.compile(solutionRegexStr);

		ArrayList<String> matches = new ArrayList<>();
		for (String s : dict) {
			if (solutionPat.matcher(s).matches()) {
				matches.add(s);
			}
		}
		return matches;
	}
}

class WordleResponse {
	public char c;
	public int index;
	public LetterResponse resp;
}

enum LetterResponse {
	CORRECT_LOCATION, // Green
	WRONG_LOCATION, // Yellow
	WRONG_LETTER // Gray
}
