package model.Dao;

import java.util.List;

import model.entites.Category;
import model.entites.Product;

public interface ProductDao {

	void insert(Product product);
	void update(Product product);
	void deleteById(Integer id);
	Product findById(Integer id);
	List<Product> findByCategory(Category category);
	List<Product> findAll();
}
