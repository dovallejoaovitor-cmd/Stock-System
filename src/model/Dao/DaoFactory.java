package model.Dao;

import db.DB;
import model.Dao.impl.CategoryDaoJDBC;

public class DaoFactory {

	public static CategoryDao createCategoryDao() {
		return new CategoryDaoJDBC(DB.getConnection());
	}
}
