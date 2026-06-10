package model.Dao;

import java.util.List;

import model.entites.User;

public interface UserDao {

	void insert (User user);
	void update (User user);
	void deleteById (Integer id);
	void findById (Integer id);
	List <User> findAll();
	User findByEmail(String email);
}
