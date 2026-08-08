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
import model.Dao.UserDao;
import model.entites.User;

public class UserDaoJDBC implements UserDao{

	private Connection conn;
	
	
	public UserDaoJDBC(Connection conn) {
		
		this.conn = conn;
	}

	@Override
	public void insert(User user) {
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(
					"INSERT INTO user "
					+ "(userName, email, password) VALUES "
					+ "(?, ?, ?)", Statement.RETURN_GENERATED_KEYS
					);
			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
			
			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					user.setId(id);
				}
				DB.closeResultSet(rs);
			}
		}catch(SQLException e) {
			DB.closeStatement(ps);
		}
		
	}

	@Override
	public void update(User user) {
PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(
					"UPDATE user "+
					"SET userName = ?, email = ?, password = ? " +
					"WHERE id = ?", Statement.RETURN_GENERATED_KEYS
			);
			ps.setString(1, user.getName());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
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
					"DELETE FROM user " + 
					"WHERE id = ?", Statement.RETURN_GENERATED_KEYS
					);
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(ps);
		}
	}

	@Override
	public User findById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(
					"SELECT * FROM user "
					+ "WHERE id = ? "
					);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if(rs.next()) {
				User user = instantiateUser(rs);
				return user;
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
	public List<User> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(
					"SELECT * FROM user"
					);
			rs = ps.executeQuery();
			List<User> list = new ArrayList<>();
			
			while(rs.next()) {
				list.add(instantiateUser(rs));
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
	public User findByEmail(String email) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(
					"SELECT * FROM user "
					+ "WHERE email = ?"
					);
			ps.setString(1, email);
			rs = ps.executeQuery();
			if(rs.next()) {
				System.out.println("ID VINDO DO BANCO: " + rs.getInt("id"));

			    User user = instantiateUser(rs);

			    System.out.println("ID DO USER CRIADO: " + user.getId());
			    return user;
			}
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		return null;
	}
	
	private User instantiateUser(ResultSet rs) {
		
		    try {
		        User user = new User();

		        user.setId(rs.getInt("id"));
		        user.setName(rs.getString("userName"));
		        user.setEmail(rs.getString("email"));
		        user.setPassword(rs.getString("password"));

		        return user;

		    } catch (SQLException e) {
		        throw new DbException(e.getMessage());
		    }
		}
	}

