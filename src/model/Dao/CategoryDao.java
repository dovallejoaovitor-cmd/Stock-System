package model.Dao;

import java.util.List;

import model.entites.Category;
import model.entites.User;

public interface CategoryDao {

	void insert (Category category, User user);
	void update (Category category);
	void deleteById (Integer id);
	Category findById(Integer id);
	List <Category> findAll(User user);
}
