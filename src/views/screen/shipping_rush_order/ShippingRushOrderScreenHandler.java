package views.screen.shipping_rush_order;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import controller.PlaceOrderController;
import controller.PlaceRushOrderController;
import common.exception.InvalidDeliveryInfoException;
import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.invoice.InvoiceScreenHandler;
import views.screen.popup.PopupScreen;
import views.screen.shipping.ShippingScreenHandler;

public class ShippingRushOrderScreenHandler extends ShippingScreenHandler implements Initializable {

	/**
	 * cho ch�?n ngày giao hàng nhanh
	 */
	@FXML
	private DatePicker datePicker;

	/**
	 * hiển thị diện tích giao hàng nhanh
	 */
	@FXML
	private VBox areaPlaceRushOrderVBox;

	/**
	 * hiển thị diện tích mật hàng hỗ trợ giao hàng nhanh
	 */
	@FXML
	private VBox itemRushOrderVBox;

	/**
	 * hiển thị diện tích mật hàng không hỗ trợ giao hàng nhanh
	 */
	@FXML
	private VBox itemNoRushOrderVBox;

	/**
	 * hiển thị thông tin tổng giá trị sản phẩm đã tính thuế VAT nhưng chưa tính
	 * thuế vận chuyển
	 */
	@FXML
	private Label subTotalLabel;

	private HashMap<String, String> messages = new HashMap<String, String>();

	public ShippingRushOrderScreenHandler(Stage stage, String screenPath, Order order) throws IOException {
		super(stage, screenPath, order);
	}

	/**
	 * khởi tạo mặc định là diện tích giao hàng nhanh là false và
	 * rushOrderRadioButton là false
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.initialize(arg0, arg1);
		this.areaPlaceRushOrderVBox.setVisible(false);
		this.rushOrderRadioButton.setSelected(false);
	}

	/**
	 * nếu không giao hàng nhanh thì g�?i lại phương thức được kết thừa nếu giao
	 * hàng nhanh thì - Thêm trư�?ng date vào deliveryInfo của order - Xác thực
	 * thông tin giao hàng nhanh - Tính toán chi phí giao hàng nhanh - Tạo ra
	 * invoice cho giao hàng nhanh
	 */
	@FXML
	public void submitDeliveryInfo(MouseEvent event) throws IOException, InterruptedException, SQLException {

		if (rushOrderRadioButton.isSelected() == false) {
			super.submitDeliveryInfo(event);
		} else {

			// process and validate delivery info
			try {
				if (this.rushOrderRadioButton.isSelected()
						&& (datePicker.getValue() == null || datePicker.getValue().equals(""))) {
					PopupScreen.error("Chọn ngày giao hàng nhanh");
					return;
				}
				getBController().processRushOrderDeliveryInfo(messages);

			} catch (InvalidDeliveryInfoException e) {
				PopupScreen.error(e.getMessage());
				throw new InvalidDeliveryInfoException(e.getMessage());
			}

			// calculate shipping fees
			calculateShippingFees();

			// create invoice screen
			createInvoiceScreen();
		}
	}

	public PlaceRushOrderController getBController() {
		return (PlaceRushOrderController) super.getBController();
	}

	/**
	 * xử lý sự kiển khi tỉnh thay đổi
	 * 
	 * @param e
	 * @throws IOException
	 */
	public void changeProvice(ActionEvent e) throws IOException {
		if (!this.province.getValue().equals("Hà Nội") && this.rushOrderRadioButton.isSelected()) {
			this.areaPlaceRushOrderVBox.setVisible(false);
			this.rushOrderRadioButton.setSelected(false);
			PopupScreen.error("Xin lỗi giao hàng nhanh chỉ áp dụng ở Hà Nội");
		}
	}

	/**
	 * Xử lý sự kiện khi ngày giao hàng thay đổi
	 * 
	 * @param e
	 */
	public void getDate(ActionEvent e) {
		if (datePicker != null) {
			LocalDate myDate = datePicker.getValue();
			messages.put("date", myDate.toString());
		}
	}

	/**
	 * Xử lý sự kiện khi rushOrderRadioButton thay đổi
	 * 
	 * @param e
	 * @throws IOException
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public void isCheckPlaceRushOrder(ActionEvent e) throws IOException, SQLException, InterruptedException {
		if (this.rushOrderRadioButton.isSelected() && this.province != null && this.province.getValue() != null) {
			if (this.province.getValue().equals("Hà Nội")) {
				this.areaPlaceRushOrderVBox.setVisible(true);
				subTotalLabel.setText(Utils.getCurrencyFormat(order.getAmount()));

				messages = addInfoToMessages();
				if (datePicker.getValue() != null)
					messages.put("date", datePicker.getValue().toString());
				order.setDeliveryInfo(messages);
				displayAreaPlaceRushOrder();
			} else {
				this.rushOrderRadioButton.setSelected(false);
				this.areaPlaceRushOrderVBox.setVisible(false);
				PopupScreen.error("Xin lỗi giao hàng nhanh chỉ áp dụng ở Hà Nội");
			}
		} else {
			this.areaPlaceRushOrderVBox.setVisible(false);
		}

	}

	/**
	 * hiển thị giao diện giao nhanh
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	public void displayAreaPlaceRushOrder() throws IOException, SQLException {
		displaySupportMediaPlaceRushOrder();
		calculateShippingFees();
	}

	/**
	 * tính toán chi phí giao nhanh
	 */
	public void calculateShippingFees() {
		// calculate shipping fees

		int shippingFees = getBController().calculateShippingRushOrderFee(order);
		order.setShippingFees(shippingFees);
	}

	/**
	 * tính toán các mặt hàng giao nhanh nếu không có in ra thông báo
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	public void displaySupportMediaPlaceRushOrder() throws IOException, SQLException {
//	 ArrayList<CartMedia> mediaItems = this.getBController().getSupportMedias();
		itemRushOrderVBox.getChildren().clear();
		itemNoRushOrderVBox.getChildren().clear();
		Label drawLabel = new Label();
		int totalItemRushOrder = 0;
		int totalItemNoRushOrder = 0;
		drawLabel.setFont(new Font("System", 16));
		drawLabel.setStyle("-fx-font-weight: bold");

		drawLabel.setText("Hỗ trợ(Phí: +10.000đ/1sp)");
		itemRushOrderVBox.getChildren().add(drawLabel);
		Label drawLabel2 = new Label();
		drawLabel2.setFont(new Font("System", 16));
		drawLabel2.setStyle("-fx-font-weight: bold");

		drawLabel2.setText("Không hỗ trợ");
		itemNoRushOrderVBox.getChildren().add(drawLabel2);
		for (int i = 0; i < order.getlstOrderMedia().size(); i++) {
			OrderMedia orderMedia = (OrderMedia) order.getlstOrderMedia().get(i);
			String append = orderMedia.getMedia().getTitle() + "(" + orderMedia.getQuantity() + "):  "
					+ Utils.getCurrencyFormat(orderMedia.getMedia().getPrice() * orderMedia.getQuantity());

			Label tmp = new Label(append);
			tmp.setFont(new Font("System", 16));
			if (orderMedia.getMedia().getIsSupport()) {
				totalItemRushOrder += orderMedia.getQuantity();
				itemRushOrderVBox.getChildren().add(tmp);
			} else {
				totalItemNoRushOrder += orderMedia.getQuantity();
				itemNoRushOrderVBox.getChildren().add(tmp);
			}
		}
		if (totalItemRushOrder == 0) {
			this.areaPlaceRushOrderVBox.setVisible(false);
			this.rushOrderRadioButton.setSelected(false);
			PopupScreen.error("Xin lỗi không có sản phẩm nào giao nhanh");
		} else {
			Label totalItemRushOrderLabel = new Label("Tổng: " + String.valueOf(totalItemRushOrder));
			totalItemRushOrderLabel.setFont(new Font("System", 16));
			totalItemRushOrderLabel.setStyle("-fx-font-weight: bold");
			itemRushOrderVBox.getChildren().add(totalItemRushOrderLabel);

			Label totalItemNoRushOrderLabel = new Label("Tổng: " + String.valueOf(totalItemNoRushOrder));
			totalItemNoRushOrderLabel.setFont(new Font("System", 16));
			totalItemNoRushOrderLabel.setStyle("-fx-font-weight: bold");
			itemNoRushOrderVBox.getChildren().add(totalItemNoRushOrderLabel);
		}
	}

}
