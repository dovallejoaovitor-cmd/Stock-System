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
import enums.MovementType;
import model.Dao.StockMovementDao;
import model.entites.Product;
import model.entites.StockMovement;
import model.entites.User;

public class StockMovementDaoJDBC implements StockMovementDao{

	private Connection conn;
	
	public StockMovementDaoJDBC(Connection conn) {

		this.conn = conn;
	}

	@Override
	public void insert(StockMovement sm) {
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(
					"INSERT INTO stockmovement "
					+ "(productId, userId, movementType, quantity, movementDate) "
					+ "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
					);
			ps.setInt(1, sm.getProduct().getId());
			ps.setInt(2, sm.getUser().getId());
			ps.setString(3, sm.getMt().name());
			ps.setInt(4, sm.getQuantity());
			ps.setDate(5, java.sql.Date.valueOf(sm.getDate()));
				int rowsAffected = ps.executeUpdate();
				if(rowsAffected > 0) {
					ResultSet rs = ps.getGeneratedKeys();
					if(rs.next()) {
						int id = rs.getInt(1);
						sm.setId(id);
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
	public StockMovement findById(Integer id) {
	    PreparedStatement ps = null;
	    ResultSet rs = null;

	    try {
	    	ps = conn.prepareStatement(
	    		    "SELECT StockMovement.*, product.nameProduct, user.userName " +
	    		    "FROM StockMovement " +
	    		    "INNER JOIN product ON StockMovement.productId = product.id " +
	    		    "INNER JOIN user ON StockMovement.userId = user.id " +
	    		    "WHERE StockMovement.id = ?"
	    		);

	        ps.setInt(1, id);

	        rs = ps.executeQuery();

	        if (rs.next()) {
	            return instantiateStockMovement(rs);
	        }

	        return null;

	    } catch (SQLException e) {
	        throw new DbException(e.getMessage());

	    } finally {
	        DB.closeResultSet(rs);
	        DB.closeStatement(ps);
	    }
	}

	@Override
	public List<StockMovement> findAll() {
		PreparedStatement ps = null; 
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(
					"SELECT StockMovement.*, product.nameProduct, category.categoryName, user.userName " +
			                "FROM StockMovement " +
			                "INNER JOIN product ON StockMovement.productId = product.id " +
			                "INNER JOIN category ON product.categoryId = category.id " +
			                "INNER JOIN user ON StockMovement.userId = user.id " +
			                "ORDER BY StockMovement.id"
					);
			rs = ps.executeQuery();
			List<StockMovement> list = new ArrayList<>();
			while (rs.next()) {
				list.add(instantiateStockMovement(rs));
			}
			return list;
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		return null;
	}

	@Override
	public List<StockMovement> findByProduct(Product product) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(
					"SELECT StockMovement.*, product.nameProduct " +
							"FROM StockMovement " +
							"INNER JOIN product ON product.id = StockMovement.productId " +
							"WHERE product.id = ? " +
							"ORDER BY StockMovement.id"
					);
			ps.setInt(1, product.getId());
			rs = ps.executeQuery();
			List<StockMovement> list = new ArrayList<>();
			while(rs.next()) {
				list.add(instantiateStockMovement(rs));
			}
			return list;
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);	
		}
		return null;
	}

	@Override
	public List<StockMovement> findByUser(User user) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(
					"SELECT StockMovement.*, user.userName "
					+ "FROM StockMovement JOIN user "
					+ "ON user.id = StockMovement.userId "
					+ "WHERE user.id = ? "
					+ "ORDER BY StockMovement.id"
					);
			ps.setInt(1, user.getId());
			rs = ps.executeQuery();
			List<StockMovement> list = new ArrayList<>();
			while(rs.next()) {
				list.add(instantiateStockMovement(rs));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);	
		}
		return null;
	}

	private StockMovement instantiateStockMovement(ResultSet rs) {
	    try {
	        StockMovement sm = new StockMovement();

	        Product product = new Product();
	        User user = new User();

	        product.setId(rs.getInt("productId"));
	        user.setId(rs.getInt("userId"));

	        sm.setId(rs.getInt("id"));
	        sm.setProduct(product);
	        sm.setUser(user);
	        sm.setMt(MovementType.valueOf(rs.getString("movementType")));
	        sm.setQuantity(rs.getInt("quantity"));
	        sm.setDate(rs.getDate("movementDate").toLocalDate());

	        return sm;

	    } catch (SQLException e) {
	        throw new DbException(e.getMessage());
	    }
	}
}
