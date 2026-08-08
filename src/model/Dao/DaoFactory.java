package model.Dao;

import db.DB;
import model.Dao.impl.CategoryDaoJDBC;
import model.Dao.impl.ProductDaoJDBC;
import model.Dao.impl.StockMovementDaoJDBC;
import model.Dao.impl.UserDaoJDBC;

public class DaoFactory {

	public static CategoryDao createCategoryDao() {
		return new CategoryDaoJDBC(DB.getConnection());
	}
	
	public static ProductDao createProductDao() {
		return new ProductDaoJDBC(DB.getConnection());
	}
	
	public static StockMovementDao createStockMovementDao() {
		return new StockMovementDaoJDBC(DB.getConnection());
	}
	
	public static UserDao createUserDao() {
		return new UserDaoJDBC(DB.getConnection());
	}
}
