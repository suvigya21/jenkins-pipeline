package com.roytuts.junit.parameterized;

import java.util.stream.Stream;

public class PalindromChecker {

	public static boolean isPalindrome(final String str) {
		StringBuilder sb = new StringBuilder(str);

		return str.equals(sb.reverse().toString());
	}

	public static Stream<String> values() {
		return Stream.of("racecar", "radar", "able was I ere I saw elba", "madam");
	}
}
