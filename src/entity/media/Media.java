package entity.media;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import entity.db.AIMSDB;
import utils.Utils;

/**
 * The general media class, for another media it can be done by inheriting this
 * class
 * 
 * @author nguyenlm
 */
public class Media {

	private static Logger LOGGER = Utils.getLogger(Media.class.getName());

	protected Statement stm;
	protected int id;
	protected String title;
	protected String category;
	protected int value; // the real price of product (eg: 450)
	protected int price; // the price which will be displayed on browser (eg: 500)
	protected int quantity;
	protected String type;
	/**
	 * nguyen duc thanh 20183991
	 * 	chiều dài của hàng hóa
	 */
	protected int length;
	/**
	 * 	chiều cao của hàng hóa
	 */
	protected int height;
	/**
	 *  chiều rộng của hàng hóa
	 */
	protected int width;

	protected String imageURL;
	/**
	 * thuộc tính bổ trợ giúp phân biệt hàng hóa có được đặt hàng nhanh hay không
	 * (thêm code không tạo mới thuộc tính ở phần db --> không thay đổi file .sql)
	 */
	protected Boolean isSupport;

	public Media() throws SQLException {
		stm = AIMSDB.getConnection().createStatement();
	}

	public Media(int id, String title, String category, int price, int quantity, String type) throws SQLException {
		this.id = id;
		this.title = title;
		this.category = category;
		this.price = price;
		this.quantity = quantity;
		this.type = type;

		// stm = AIMSDB.getConnection().createStatement();
	}

	public Media(int id, String title, String category, int price, int quantity, String type, Boolean isSupport)
			throws SQLException {
		this.id = id;
		this.title = title;
		this.category = category;
		this.price = price;
		this.quantity = quantity;
		this.type = type;
		this.isSupport = isSupport;
		// stm = AIMSDB.getConnection().createStatement();
	}

	public int getQuantity() throws SQLException {
		int updated_quantity = getMediaById(id).quantity;
		this.quantity = updated_quantity;
		return updated_quantity;
	}

	public Media getMediaById(int id) throws SQLException {
		String sql = "SELECT * FROM Media ;";
		Statement stm = AIMSDB.getConnection().createStatement();
		ResultSet res = stm.executeQuery(sql);
		if (res.next()) {

			return new Media().setId(res.getInt("id")).setTitle(res.getString("title"))
					.setQuantity(res.getInt("quantity")).setCategory(res.getString("category"))
					.setMediaURL(res.getString("imageUrl")).setPrice(res.getInt("price")).setType(res.getString("type"))
					.setIsSupport(this.getRandomBoolean()).setHeight(getRandomSize()).setWidth(getRandomSize())
					.setLength(getRandomSize());
		}
		return new Media();
	}

	/**
	 * @return danh sách các sản phẩm có trong db
	 * @throws SQLException
	 */
	public List getAllMedia() throws SQLException {
		Statement stm = AIMSDB.getConnection().createStatement();
		ResultSet res = stm.executeQuery("select * from Media");
		ArrayList medium = new ArrayList<>();
		while (res.next()) {
			Media media = new Media().setId(res.getInt("id")).setTitle(res.getString("title"))
					.setQuantity(res.getInt("quantity")).setCategory(res.getString("category"))
					.setMediaURL(res.getString("imageUrl")).setPrice(res.getInt("price")).setType(res.getString("type"))
					.setIsSupport(getRandomBoolean()).setHeight(getRandomSize()).setWidth(getRandomSize())
					.setLength(getRandomSize());
			medium.add(media);
		}
		return medium;
	}

	public void updateMediaFieldById(String tbname, int id, String field, Object value) throws SQLException {
		Statement stm = AIMSDB.getConnection().createStatement();
		if (value instanceof String) {
			value = "\"" + value + "\"";
		}
		stm.executeUpdate(" update " + tbname + " set" + " " + field + "=" + value + " " + "where id=" + id + ";");
	}

	/**
	 * các thông số về chiều rộng chiều cao chiều dài của 1 sản phẩm được random
	 * trong khoảng từ 20cm --> 40cm
	 * 
	 * @return
	 */
	private int getRandomSize() {
		Random random = new Random();
		return random.nextInt((40 - 20) + 1) + 20;
	}

	/**
	 * Tạo mới giao hàng nhanh cho các mặt hàng
	 * 
	 * @return giá trị isSupport ngẫu nhiên
	 */
	private boolean getRandomBoolean() {
		Random random = new Random();
		return random.nextBoolean();
	}

	// getter and setter
	public int getId() {
		return this.id;
	}

	private Media setId(int id) {
		this.id = id;
		return this;
	}

	public String getTitle() {
		return this.title;
	}

	public Media setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getCategory() {
		return this.category;
	}

	public Media setCategory(String category) {
		this.category = category;
		return this;
	}

	public int getPrice() {
		return this.price;
	}

	public Media setPrice(int price) {
		this.price = price;
		return this;
	}

	public String getImageURL() {
		return this.imageURL;
	}

	public Media setMediaURL(String url) {
		this.imageURL = url;
		return this;
	}

	public Media setQuantity(int quantity) {
		this.quantity = quantity;
		return this;
	}

	public String getType() {
		return this.type;
	}

	public Media setType(String type) {
		this.type = type;
		return this;
	}

	public Boolean getIsSupport() {
		return isSupport;
	}

	public Media setIsSupport(Boolean isSupport) {

		this.isSupport = isSupport;
		return this;
	}

	public int getLength() {
		return length;
	}

	public Media setLength(int length) {
		this.length = length;
		return this;
	}

	public int getHeight() {
		return height;
	}

	public Media setHeight(int height) {
		this.height = height;
		return this;
	}

	public int getWidth() {
		return width;
	}

	public Media setWidth(int width) {
		this.width = width;
		return this;
	}
	
	/**
	 * @return khổi lượng thay thế của sản phẩm
	 */
	public int getAlternativeWeight() {
		return (this.getHeight() * this.getWidth() * this.getLength()) /6000;
	}

	@Override
	public String toString() {
		return "Media [stm=" + stm + ", id=" + id + ", title=" + title + ", category=" + category + ", value=" + value
				+ ", price=" + price + ", quantity=" + quantity + ", type=" + type + ", length=" + length + ", height="
				+ height + ", width=" + width + ", imageURL=" + imageURL + ", isSupport=" + isSupport + "]";
	}


}