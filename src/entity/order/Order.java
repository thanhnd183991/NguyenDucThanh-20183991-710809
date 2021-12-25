package entity.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.Configs;

/**
 * @author ADMIN
 *
 */
public class Order {

	private int shippingFees;
	private List lstOrderMedia;
	/**
	 * đối với placeOrder: map sẽ không có thuộc tính date đối với placeRusherOrder:
	 * map sẽ có thêm thuộc tính date để hiển thị ngày giao nhanh
	 */
	private HashMap<String, String> deliveryInfo;

	private Order(OrderBuilder builder) {
		this.shippingFees = builder.shippingFees;
		this.lstOrderMedia = builder.lstOrderMedia;
		this.deliveryInfo = builder.deliveryInfo;
	}

	public void addOrderMedia(OrderMedia om) {
		this.lstOrderMedia.add(om);
	}

	public void removeOrderMedia(OrderMedia om) {
		this.lstOrderMedia.remove(om);
	}

	public List getlstOrderMedia() {
		return this.lstOrderMedia;
	}

	public void setlstOrderMedia(List lstOrderMedia) {
		this.lstOrderMedia = lstOrderMedia;
	}

	public void setShippingFees(int shippingFees) {
		this.shippingFees = shippingFees;
	}

	public int getShippingFees() {
		return shippingFees;
	}

	public String getAddressDeliveryInfo() {
		return deliveryInfo.get("address");
	}

	public String getPhoneDeliveryInfo(String phone) {
		return deliveryInfo.get("phone");
	}

	public String getInstructionsDeliveryInfo(String instructions) {
		return deliveryInfo.get("instructions");
	}

	public String getProvinceDeliveryInfo() {
		return deliveryInfo.get("province");
	}

	public String getDateDeliveryInfo(String date) {
		return deliveryInfo.get("date");
	}

	public HashMap getDeliveryInfo() {
		HashMap rs = new HashMap();
		rs = this.deliveryInfo;
		return rs;
	}
	public void setDeliveryInfo(HashMap messages) {
		this.deliveryInfo = messages;
	}

	/**
	 * @return tổng giá trị sản phầm được order
	 */
	public int getAmount() {
		double amount = 0;
		for (Object object : lstOrderMedia) {
			OrderMedia om = (OrderMedia) object;
			amount += om.getPrice() * om.getQuantity();
		}
		return (int) (amount + (Configs.PERCENT_VAT / 100) * amount);
	}

	@Override
	public String toString() {
		return "Order [shippingFees=" + shippingFees + ", lstOrderMedia=" + lstOrderMedia.get(0).toString()
				+ ", deliveryInfo=" + deliveryInfo + "]";
	}

	public static class OrderBuilder {

		private int shippingFees;
		private List lstOrderMedia;
		/**
		 * đối với placeOrder: map sẽ không có thuộc tính date đối với placeRusherOrder:
		 * map sẽ có thêm thuộc tính date để hiển thị ngày giao nhanh
		 */
		private HashMap<String, String> deliveryInfo;

		public OrderBuilder() {
			this.lstOrderMedia = new ArrayList<>();
		}

		public OrderBuilder(List lstOrderMedia) {
			this.lstOrderMedia = lstOrderMedia;
		}

		public OrderBuilder setAddressDeliveryInfo(String address) {
			deliveryInfo.put("address", address);
			return this;
		}

		public OrderBuilder setPhoneDeliveryInfo(String phone) {
			deliveryInfo.put("phone", phone);
			return this;
		}

		public OrderBuilder setInstructionsDeliveryInfo(String instructions) {
			deliveryInfo.put("instructions", instructions);
			return this;
		}

		public OrderBuilder setProvinceDeliveryInfo(String province) {
			deliveryInfo.put("province", province);
			return this;
		}

		public OrderBuilder setDateDeliveryInfo(String date) {
			deliveryInfo.put("date", date);
			return this;
		}

		public Order build() {
			return new Order(this);
		}

	}
}
