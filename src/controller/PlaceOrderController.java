package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import calculateshippingfee.PlaceOrderShippingFeeCalculator;
import calculateshippingfee.RandomShippingFeeCalculator;
import calculateshippingfee.ShippingFeeCalculator;
import entity.cart.Cart;
import entity.cart.CartMedia;
import common.exception.InvalidDeliveryInfoException;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import views.screen.popup.PopupScreen;

/**
 * This class controls the flow of place order usecase in our AIMS project
 * @author nguyenlm
 */
public class PlaceOrderController extends BaseController{

    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());
    private ShippingFeeCalculator shippingFeeCalculator;
    /**
     * This method checks the avalibility of product when user click PlaceOrder button
     * @throws SQLException
     */
    public void placeOrder() throws SQLException{
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method creates the new Order based on the Cart
     * @return Order
     * @throws SQLException
     */
    public Order createOrder() throws SQLException{
    	List lstOrderMedia = new ArrayList();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(), 
                                                   cartMedia.getQuantity(), 
                                                   cartMedia.getPrice());    
           lstOrderMedia.add(orderMedia);
        }
        return new Order.OrderBuilder(lstOrderMedia).build();
    }

    /**
     * This method creates the new Invoice based on order
     * @param order
     * @return Invoice
     */
    public Invoice createInvoice(Order order) {
        return new Invoice(order);
    }

    /**
     * This method takes responsibility for processing the shipping info from user
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */
    public void processDeliveryInfo(HashMap info) throws InterruptedException, IOException{
        LOGGER.info("Process Delivery Info");
        LOGGER.info(info.toString());
        validateDeliveryInfo(info);
    }
    
    /**
   * Nguyễn Đức Thành 20183991
   * The method validates the info
   * @param info
   * @throws InterruptedException
   * @throws IOException
   */
    public void validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException{
    	if(validateAddress(info.get("address")) == false || validatePhoneNumber(info.get("phone")) == false || validateName(info.get("name")) == false) {
    		throw new InvalidDeliveryInfoException("payload order invalid");
    	}
    }
    
    public boolean validatePhoneNumber(String phoneNumber) {
    	//check 10 digits
    	if(phoneNumber.length() !=10) return false;
    	//check start with 0
    	if(!phoneNumber.startsWith("0")) return false;
    	try {
    		Integer.parseInt(phoneNumber);
    	}
    	catch(NumberFormatException ex) {
    		return false;
    	}
    	return true;
    }
    
    public boolean validateName(String name) {
    	if(name.equals("null")) 
    		return false;
    	if(!name.matches("[a-zA-Z]+"))
    		return false;
    	return true && name != null;
    }
    
    public boolean validateAddress(String address) {
    	//nguyen duc thanh 20183991
    	if(address.equals("null")) 
    		return false;
    	if(address.matches("[a-zA-Z0-9 ]+"))
    		return true && address != null;
    	return false;
    }
    

    /**
     * This method calculates the shipping fees of order
     * @param order
     * @return shippingFee
     */
    public int calculateShippingFee(Order order){
    	shippingFeeCalculator = new PlaceOrderShippingFeeCalculator();
    	int fees = shippingFeeCalculator.calculateShippingFee(order);
        LOGGER.info("DeliveryInfo: " + order.getDeliveryInfo().toString() + "Order Amount: " + order.getAmount() + " -- Shipping Fees: " + fees);
        return fees;
    }
}
