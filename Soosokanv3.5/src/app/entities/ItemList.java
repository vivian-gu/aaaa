package app.entities;

public class ItemList {

	private String sellerId;
	private String itemId;
	private String name;
	private float price;
	private int discount;
	private String picture;
	
	public int getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getDisscount() {
		return discount;
	}
	public void setDisscount(int disscount) {
		this.discount = disscount;
	}
	public ItemList(String sellerId, String itemId, String name, float price, int disscount,String picture) {
		super();
		this.sellerId = sellerId;
		this.picture = picture;
		this.itemId = itemId;
		this.name = name;
		this.price = price;
		this.discount = disscount;
	}
	public ItemList() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
