package controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ValidateDateTest {

	private PlaceRushOrderController placeRushOrderController;
	@BeforeEach
	void setUp() throws Exception {
		placeRushOrderController = new PlaceRushOrderController();
	}

	@ParameterizedTest
	@CsvSource({
		"2020-11-11, true",
		"1111111-11-11, false",
		"Bắc Cạn, false",
		"Nguyen Duc Thanh 20183991, false", 
		"null, false"
	})
	void test(String date, boolean expected) {
		boolean isValid = placeRushOrderController.validateDate(date);

		assertEquals(expected, isValid);
	}

}
