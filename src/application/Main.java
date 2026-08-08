package application;

import model.Dao.CategoryDao;
import model.Dao.DaoFactory;
import model.entites.Category;

public class Main {

	public static void main(String[] args) {
	
		CategoryDao catDao = DaoFactory.createCategoryDao();
		Category cat = new Category(null, "Eletrônicos");
		catDao.insert(cat);
		System.out.println(cat.getId() + "Categoria: " + cat.getName());
	}

}
