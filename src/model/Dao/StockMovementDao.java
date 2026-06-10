package model.Dao;

import java.util.List;

import model.entites.Product;
import model.entites.StockMovement;
import model.entites.User;

public interface StockMovementDao {

	void insert (StockMovement sm);
	StockMovement findById (Integer id);
	List <StockMovement> findAll();
	List <StockMovement> findByProduct(Product product);
	List <StockMovement> findByUser(User user);
}
