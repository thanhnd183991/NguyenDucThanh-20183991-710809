package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import calculateshippingfee.PlaceRushOrderShippingFeeCalculator;
import entity.cart.Cart;
import entity.cart.CartMedia;
import common.exception.InvalidDeliveryInfoException;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import views.screen.popup.PopupScreen;

/**
 * This class controls the flow of place order usecase in our AIMS project
 * 
 * @author nguyen duc thanh
 */
public class PlaceRushOrderController extends PlaceOrderController {

	/**
	 * Just for logging purpose
	 */
	private static Logger LOGGER = utils.Utils.getLogger(PlaceRushOrderController.class.getName());
	/**
	 * This method checks the avalibility of product when user click PlaceRushOrder 
	 * (được extends từ PlaceOrderController)
	 * button
	 * 
	 * @throws SQLException
	 */
	public void placRushOrder() throws SQLException {
		super.placeOrder();
	}

	/**
	 * This method creates the new rush Order based on the Cart
	 * (được extends từ lớp PlaceOrderControler)
	 * Lưu ý trong trường deliveryInfo của entity order có chứa
	 * thêm một trường date cho ngày Rush Order
	 * @return Order
	 * @throws SQLException
	 */
	public Order createRushOrder() throws SQLException {
		return super.createOrder();
	}

	/**
	 * This method creates the new rush Invoice based on order
	 * 
	 * @param order
	 * @return Invoice
	 */
	public Invoice createRushInvoice(Order order) {
		return super.createInvoice(order);
	}

	/**
	 * This method takes responsibility for processing the shipping info from user
	 * được kế thừa từ lớp PlaceOrder và thêm 2 kiểm tra: kiểm tra thành phố , kiểm tra ngày tháng
	 * @param info
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void processRushOrderDeliveryInfo(HashMap info) throws InterruptedException, IOException {
		super.processDeliveryInfo(info);
		validateRushOrderDeliveryInfo(info);
	}

	/**
	 * The method validates the info
	 * kiểm tra thông tin giao hàng nhanh(ngày tháng, thành phố)
	 * @param info
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void validateRushOrderDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException {
		super.validateDeliveryInfo(info);
		if (validateProvince(info.get("province")) == false || validateDate(info.get("date")) == false) {
			throw new IOException("error payload rush order invalid");
		}

	}

	/**
	 * @param province dau vao ten tinh thạnh pho
	 * @return kiem tra xem thanh pho co hop le de giao hang nhanh hay khong
	 */
	public boolean validateProvince(String province) {
		// kiem tra ten tinh thanh co phai la ha noi khong
		if (province.equalsIgnoreCase("Hà Nội"))
			return true;
		else
			return false;
	}

	/**
	 * @param date ngay thang
	 * @return  xac thuc co dung ngay thang hop le hay khong
	 */
	public boolean validateDate(String date) {
		//nguyen duc thanh 20183991
		//date do datePicker sinh ra duoi dang yyyy-mm-dd 
		if(date.matches("\\d{4}-\\d{2}-\\d{2}"))
			return true && date !=null;
		return false;
	}

	/**
	 * This method calculates the shipping fees of order
	 * 
	 * @param order
	 * @return shippingFee
	 */
	public int calculateShippingRushOrderFee(Order order) {
		PlaceRushOrderShippingFeeCalculator calculator = new PlaceRushOrderShippingFeeCalculator();
		return calculator.calculateShippingFee(order);
		
	}
}
