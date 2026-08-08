package model.Dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.Dao.CategoryDao;
import model.entites.Category;
import model.entites.User;

public class CategoryDaoJDBC implements CategoryDao{

	private Connection conn;
	
	public CategoryDaoJDBC(Connection conn) {
		super();
		this.conn = conn;
	}

	@Override
	public void insert(Category category, User user) {
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(
					 "INSERT INTO category (categoryName, userId) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS
					);
			ps.setString(1, category.getName());
			ps.setInt(2, user.getId());
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					category.setId(id);
				}
				DB.closeResultSet(rs);
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			DB.closeStatement(ps);
		}
	}

	@Override
	public void update(Category category) {
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(
					"UPDATE category "+
					"SET categoryName = ? " +
					"WHERE id = ?", Statement.RETURN_GENERATED_KEYS
			);
			ps.setString(1, category.getName());
			ps.setInt(2, category.getId());
			
			ps.executeUpdate();
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(ps);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(
					"DELETE FROM category " + 
					"WHERE id = ?", Statement.RETURN_GENERATED_KEYS
					);
			ps.setInt(1, id);
			ps.execute();
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(ps);
		}
		
	}

	@Override
	public Category findById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(
					"SELECT id, categoryName, userId " +
		                    "FROM category " +
		                    "WHERE id = ?"			
					);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				Category cat = instantiateCategory(rs);
				return cat;
			}
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		return null;
	}

	@Override
	public List<Category> findAll(User user) {

	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {

	        ps = conn.prepareStatement(
	                "SELECT id, categoryName, userId " +
	                "FROM category " +
	                "WHERE userId = ? " +
	                "ORDER BY id"
	        );

	        ps.setInt(1, user.getId());

	        rs = ps.executeQuery();

	        List<Category> list = new ArrayList<>();

	        while (rs.next()) {
	            list.add(instantiateCategory(rs)
	            );
	        }
	        return list;
	    } catch (SQLException e) {

	        throw new DbException(e.getMessage()
	        );

	    }finally{
	        DB.closeStatement(ps);
	        DB.closeResultSet(rs);
	    }
	}
	
	private Category instantiateCategory(ResultSet rs) {
		try {
			Category cat = new Category();
			User user = new User();
			cat.setId(rs.getInt("id"));
			cat.setName(rs.getString("categoryName"));
			user.setId(rs.getInt("userId"));
			cat.setUser(user);
			return cat;
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		
	}

}
