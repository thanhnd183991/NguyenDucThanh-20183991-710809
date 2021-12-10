package views.screen.invoice;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import entity.order.OrderMedia;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utils.Utils;
import views.screen.FXMLScreenHandler;

public class MediaInvoiceScreenHandler extends FXMLScreenHandler {

	@FXML
	private HBox hboxMedia;

	@FXML
	private VBox imageLogoVbox;

	@FXML
	private ImageView image;

	@FXML
	private VBox description;

	@FXML
	private Label title;

	@FXML
	private Label numOfProd;

	@FXML
	private Label labelOutOfStock;

	@FXML
	private Label price;

	/**
	 * hiển thị thôn tin giao nhanh khi đặt hàng giao nhanh trong invoice screen
	 */
	@FXML
	private Label isSupportLabel;

	private OrderMedia orderMedia;
	private HashMap<String, String> deliveryInfo;

	public MediaInvoiceScreenHandler(String screenPath) throws IOException {
		super(screenPath);
	}

	public void setOrderMedia(OrderMedia orderMedia, HashMap<String, String> deliveryInfo) throws SQLException {
		this.orderMedia = orderMedia;
		this.deliveryInfo = deliveryInfo;
		setMediaInfo();
	}

	public void setMediaInfo() throws SQLException {
		title.setText(orderMedia.getMedia().getTitle());
		// tổng giá trị sản phẩm (số lượng * giá 1 sản phẩm)
		price.setText(Utils.getCurrencyFormat(orderMedia.getPrice()*orderMedia.getQuantity()));
		if (this.deliveryInfo.get("date") != null  && this.deliveryInfo.get("date") != "") {
			System.out.println("test" + this.deliveryInfo.get("date"));
			isSupportLabel.setText(orderMedia.getMedia().getIsSupport() == true ? "Giao nhanh" : "Không hỗ trợ");
			this.isSupportLabel.setVisible(true);
		} else {
			this.isSupportLabel.setVisible(false);
		}
		numOfProd.setText(String.valueOf(orderMedia.getQuantity()));
		setImage(image, orderMedia.getMedia().getImageURL());
		image.setPreserveRatio(false);
		image.setFitHeight(90);
		image.setFitWidth(83);
	}

}
