package model.Dao;

import java.util.List;

import model.entites.Category;

public interface CategoryDao {

	void insert (Category category);
	void update (Category category);
	void deleteById (Integer id);
	Category findById(Integer id);
	List <Category> findAll();
}
