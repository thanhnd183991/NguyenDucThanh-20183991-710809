package controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ValidateProvinceTest {

	private PlaceRushOrderController placeRushOrderController;
	@BeforeEach
	void setUp() throws Exception {
		placeRushOrderController = new PlaceRushOrderController();
	}

	@ParameterizedTest
	@CsvSource({
		"Hà Nội, true",
		"Bắc Cạn, false",
		"Nguyen Duc Thanh 20183991, false"
	})
	void test(String province, boolean expected) {
		boolean isValid = placeRushOrderController.validateProvince(province);

		assertEquals(expected, isValid);
	}

}
