package model.entites;

import java.time.LocalDate;

import enums.MovementType;

public class StockMovement {

	private Integer id;
	private Product product;
	private User user;
	private MovementType mt;
	private Integer quantity;
	private LocalDate date;
	
	public StockMovement() {
		
	}
	public StockMovement(Integer id, Product product, User user, MovementType mt, Integer quantity, LocalDate date) {

		this.id = id;
		this.product = product;
		this.user = user;
		this.mt = mt;
		this.quantity = quantity;
		this.date = date;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public MovementType getMt() {
		return mt;
	}
	public void setMt(MovementType mt) {
		this.mt = mt;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	
}
