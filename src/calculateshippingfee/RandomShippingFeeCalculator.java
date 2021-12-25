package calculateshippingfee;

import java.util.Random;

import entity.order.Order;

public class RandomShippingFeeCalculator implements ShippingFeeCalculator{

	@Override
	public int calculateShippingFee(Order order) {
		Random rand = new Random();
        // neu don hang co gia tren 100.000đ thi free shipping
        if(order.getAmount() > 100000)
        	return 0;

        // fake fees
        int fees = (int)( ( (rand.nextFloat()*10)/100 ) * order.getAmount() ) + 1;
        return fees;
		
	}

}
