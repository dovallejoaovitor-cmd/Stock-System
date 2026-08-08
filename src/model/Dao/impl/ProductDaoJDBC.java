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
import model.Dao.ProductDao;
import model.entites.Category;
import model.entites.Product;
import model.entites.User;

public class ProductDaoJDBC implements ProductDao{
	private Connection conn;
	
	public ProductDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Product product) {
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(
					"INSERT INTO product " +
					"(nameProduct, categoryId, price, quantity, userId) VALUES " +
					"(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
					);
			ps.setString(1, product.getName());
			ps.setInt(2, product.getCategory().getId());
			ps.setDouble(3, product.getPrice());
			ps.setInt(4, product.getQuantity());
			ps.setInt(5, product.getUser().getId());
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					product.setId(id);
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
	public void update(Product product) {
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(
					"UPDATE product "+
					"SET nameProduct = ?, categoryId = ?, price = ?, quantity = ? " +
					"WHERE id = ?", Statement.RETURN_GENERATED_KEYS
			);
			ps.setString(1, product.getName());
			ps.setInt(2, product.getCategory().getId());
			ps.setDouble(3, product.getPrice());
			ps.setInt(4, product.getQuantity());
			ps.setInt(5, product.getId());
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
						"DELETE FROM product " + 
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
	public Product findById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(
					"SELECT product.*, category.categoryName " +
			        "FROM product " +
			        "INNER JOIN category ON product.categoryId = category.id " +
			        "WHERE product.id = ?"		
					);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				Product p = instantiateProduct(rs);
				return p;
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
	public List<Product> findByCategory(Category category, User user) {

	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {

	        ps = conn.prepareStatement(
	        		"SELECT product.*, category.categoryName " +
	        		"FROM product " +
	        		"JOIN category " +
	        		"ON category.id = product.categoryId " +
	        		"WHERE category.id = ? " +
	        		"AND product.userId = ?"
	        );

	        ps.setInt(1, category.getId());
	        ps.setInt(2, user.getId());

	        rs = ps.executeQuery();

	        List<Product> list = new ArrayList<>();

	        while (rs.next()) {
	            list.add(instantiateProduct(rs));
	        }

	        return list;

	    } catch (SQLException e) {

	        throw new DbException(e.getMessage());

	    } finally {

	        DB.closeStatement(ps);
	        DB.closeResultSet(rs);
	    }
	}


	@Override
	public List<Product> findAll() {
		PreparedStatement ps = null; 
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(
					"SELECT product.*, category.categoryName " +
			                "FROM product " +
			                "JOIN category " +
			                "ON category.id = product.categoryId " +
			                "ORDER BY product.id"
					);
			rs = ps.executeQuery();
			List<Product> list = new ArrayList<>();
			
			while(rs.next()) {
				list.add(instantiateProduct(rs));
			}
			
			return list;
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		
	}
	
	private Product instantiateProduct(ResultSet rs) {
		try {
			Product p = new Product();
			Category cat = new Category();
			User user = new User();
			p.setId(rs.getInt("id"));
			p.setName(rs.getString("nameProduct"));
			cat.setId(rs.getInt("categoryId"));
			p.setCategory(cat);
			p.setPrice(rs.getDouble("price"));
			p.setQuantity(rs.getInt("quantity"));
			user.setId(rs.getInt("userId"));
			p.setUser(user);
			return p;
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		
	}
}
