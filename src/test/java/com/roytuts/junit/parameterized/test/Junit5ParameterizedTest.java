package com.roytuts.junit.parameterized.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.roytuts.junit.parameterized.CustomArgumentsProvider;
import com.roytuts.junit.parameterized.Gender;
import com.roytuts.junit.parameterized.PalindromChecker;
import com.roytuts.junit.parameterized.Person;
import com.roytuts.junit.parameterized.StringChecker;

public class Junit5ParameterizedTest {

	@ParameterizedTest
	@ValueSource(strings = { "racecar", "radar", "able was I ere I saw elba" })
	void palindromesValueSource(String candidate) {
		assertTrue(PalindromChecker.isPalindrome(candidate));
	}

	public static String[] values() {
		return new String[] { "racecar", "radar", "able was I ere I saw elba", "madam" };
	}

	@ParameterizedTest
	@MethodSource("values")
	void palindromesMethodSource(String candidate) {
		assertTrue(PalindromChecker.isPalindrome(candidate));
	}

	public static Stream<String> palindromeStrings() {
		return Stream.of("racecar", "radar", "able was I ere I saw elba", "madam");
	}

	@ParameterizedTest
	@MethodSource("palindromeStrings")
	void palindromesMethodSourceStream(String candidate) {
		assertTrue(PalindromChecker.isPalindrome(candidate));
	}

	@ParameterizedTest
	@MethodSource("com.roytuts.junit.parameterized.PalindromChecker#values")
	void palindromesMethodSourceExternal(String candidate) {
		assertTrue(PalindromChecker.isPalindrome(candidate));
	}

	public static Object[] strings() {
		return new Object[][] { //
				{ "str", true }, //
				{ "str", true }, //
				{ "str", false }, //
				{ "exactly 5 objects", false }, //
				{ "at least 5 objects", false }, //
				{ "\"more than\" 5 objects", false },//
		};
	}

	@ParameterizedTest
	@MethodSource("strings")
	void stringsMethodSource(String str, boolean trueFalse) {
		assertEquals(trueFalse, StringChecker.check(str, trueFalse));
	}

	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { " ", "", "\t", "\n" })
	void stringNullSource(String str) {
		assertTrue(str == null || str.trim().isEmpty());
	}

	@ParameterizedTest
	@EmptySource
	@ValueSource(strings = { " ", "", "\t", "\n" })
	void stringEmptySource(String str) {
		assertTrue(str == null || str.trim().isEmpty());
	}

	@ParameterizedTest
	// @NullSource
	// @EmptySource
	@NullAndEmptySource
	@ValueSource(strings = { " ", "", "\t", "\n" })
	void stringNullAndEmptySource(String str) {
		assertTrue(str == null || str.trim().isEmpty());
	}

	@ParameterizedTest
	@EnumSource(names = { "DAYS", "HOURS" }, value = ChronoUnit.class)
	void enumSource(ChronoUnit unit) {
		assertTrue(EnumSet.of(ChronoUnit.DAYS, ChronoUnit.HOURS).contains(unit));
	}

	@ParameterizedTest
	@EnumSource(mode = Mode.EXCLUDE, names = { "ERAS", "FOREVER" }, value = ChronoUnit.class)
	void enumSourceExclude(ChronoUnit unit) {
		assertFalse(EnumSet.of(ChronoUnit.ERAS, ChronoUnit.FOREVER).contains(unit));
	}

	@ParameterizedTest
	@EnumSource(mode = Mode.MATCH_ALL, names = "^.*DAYS$", value = ChronoUnit.class)
	void enumSourceRegex(ChronoUnit unit) {
		assertTrue(unit.name().endsWith("DAYS"));
	}

	@ParameterizedTest
	@ArgumentsSource(CustomArgumentsProvider.class)
	void argumentsSource(String argument) {
		assertNotNull(argument);
	}

	@ParameterizedTest
	@CsvSource({ "Jane, Doe, F, 1990-05-20", "John, Doe, M, 1990-10-22" })
	void testWithArgumentsAccessor(ArgumentsAccessor arguments) {
		Person person = new Person(arguments.getString(0), arguments.getString(1), arguments.get(2, Gender.class),
				arguments.get(3, LocalDate.class));

		if (person.getFirstName().equals("Jane")) {
			assertEquals(Gender.F, person.getGender());
		} else {
			assertEquals(Gender.M, person.getGender());
		}
		assertEquals("Doe", person.getLastName());
		assertEquals(1990, person.getDateOfBirth().getYear());
	}

}
