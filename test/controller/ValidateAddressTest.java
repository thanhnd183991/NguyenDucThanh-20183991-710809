package controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ValidateAddressTest {

	private PlaceOrderController placeOrderController;
	@BeforeEach
	void setUp() throws Exception {
		placeOrderController = new PlaceOrderController();
	}

	@ParameterizedTest
	@CsvSource({
		"ha noi, true",
		"so 15 Hai Ba Trung Ha Noi, true", 
		"$#NguyenDucThanh20183991, false",
		"null, false"
	})
	void test(String address, Boolean expected) {
		Boolean isValid = placeOrderController.validateAddress(address);
		assertEquals(expected, isValid);
	}

}
