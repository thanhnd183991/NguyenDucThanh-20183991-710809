package calculateshippingfee;

import entity.order.Order;
import entity.order.OrderMedia;

public class PlaceRushOrderShippingFeeCalculator implements ShippingFeeCalculator {

	@Override
	public int calculateShippingFee(Order order) {
		PlaceOrderShippingFeeCalculator placeOrderShippingFeeCalculator = new PlaceOrderShippingFeeCalculator();
		int totalNoRushOrder = placeOrderShippingFeeCalculator.calculateShippingFee(order);
		if(totalNoRushOrder == 0)
			return 0;
		
		// tính tổng các mặt hàng được hỗ trợ giao nhanh
		// mỗi mặt hàng gio nhanh tính thêm 10.000đ
		int totalNumberOfItems = 0;
		for(int i =0;i<order.getlstOrderMedia().size();i++) {
			OrderMedia cartMedia = (OrderMedia)order.getlstOrderMedia().get(i);
			if(cartMedia.getMedia().getIsSupport())
				totalNumberOfItems += ((OrderMedia)order.getlstOrderMedia().get(i)).getQuantity();
		}
		return totalNoRushOrder + totalNumberOfItems * 10000;
	}

}
