package calculateshippingfee;

public class CalculateFeesFromProvince {
	public static int calculateFeesHaNoiAndHCM(int maxWeight) {
		/*
		 * - Nếu khách hàng ở nội thành TP. Hà Nội hoặc nội thành TP.HCM, giá khởi điểm
		 * cho 3kg đầu là 22.000VNĐ. Cứ 0.5kg tiếp theo, khách hàng sẽ phải trả thêm
		 * 2.500VNĐ.
		 */
		System.out.println("maxWeight " + maxWeight);
		if (maxWeight <= 3)
			return 22000;
		else {
			int soLan = (int) Math.ceil((double) (maxWeight - 3) / 0.5);
			System.out.println("soLan " + soLan);
			return 22000 + soLan * 2500;
		}
	}

	public static int calculateFeesNoHaNoiAndHCM(int maxWeight) {
		/*
		 * * - Nếu khách hàng ở vị trí khác trong lãnh thổ Việt Nam, giá khởi điểm cho
		 * 0.5kg đầu là 30.000VNĐ. - Cứ 0.5kg tiếp theo, khách hàng sẽ phải trả thêm
		 * 2.500VNĐ.
		 */
		if (maxWeight <= 0.5) {
			return 30000;
		} else {
			int soLan = (int) Math.ceil((double) (maxWeight - 0.5) / 0.5);
			return 30000 + soLan * 2500;
		}
	}
}
