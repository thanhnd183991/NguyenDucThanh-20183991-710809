package calculateshippingfee;

import entity.order.Order;
import entity.order.OrderMedia;

public class PlaceOrderShippingFeeCalculator implements ShippingFeeCalculator {

	@Override
	public int calculateShippingFee(Order order) {
		// dơn hang co gia tri tren 100.000 vnd mien phi van chuyen
		if (order.getAmount() > 100000)
			return 0;
		/*
		 * tính phí vân chuyển hàng place order - Chỉ tính phí giao hàng với sản phẩm có
		 * khối lượng lớn nhất. - Nếu khách hàng ở nội thành TP. Hà Nội hoặc nội thành
		 * TP.HCM, giá khởi điểm cho 3kg đầu là 22.000VNĐ. - Nếu khách hàng ở vị trí
		 * khác trong lãnh thổ Việt Nam, giá khởi điểm cho 0.5kg đầu là 30.000VNĐ. - Cứ
		 * 0.5kg tiếp theo, khách hàng sẽ phải trả thêm 2.500VNĐ.
		 */

		// tim san pham co khoi luong lon nhat
		// can cu vao alternative weight
		int maxWeight = getMediaMaxWeight(order);
//		System.out.println(order.toString());

		// xem xet noi giao hang
		boolean isProviceHaNoiOrHCM = checkProvinceHaNoiOrHCM(order);

		// tinh tien shipping
		if(isProviceHaNoiOrHCM) {
			return CalculateFeesFromProvince.calculateFeesHaNoiAndHCM(maxWeight);
		}
		else
		return	CalculateFeesFromProvince.calculateFeesNoHaNoiAndHCM(maxWeight);
	}

	private int getMediaMaxWeight(Order order) {
		int maxWeight = 0;
		for (Object object : order.getlstOrderMedia()) {
			OrderMedia om = (OrderMedia) object;
			System.out.println("weight: " + om.getMedia().getAlternativeWeight());
			if (om.getMedia().getAlternativeWeight() > maxWeight) {
				maxWeight = om.getMedia().getAlternativeWeight();
			}
		}
		return maxWeight;
	}

	private boolean checkProvinceHaNoiOrHCM(Order order) {
		boolean isProviceHaNoiOrHCM = false;
		if (order.getProvinceDeliveryInfo() == null)
			return false;
		if (order.getProvinceDeliveryInfo().equals("Hà Nội") || order.getProvinceDeliveryInfo().equals("Hồ Chí Minh"))
			isProviceHaNoiOrHCM = true;
		return isProviceHaNoiOrHCM;
	}

	
}
