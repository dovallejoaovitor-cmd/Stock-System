
package application;

import javax.swing.SwingUtilities;

import Screen.LoginFrame;
import Screen.MainFrame;
import model.Dao.CategoryDao;
import model.Dao.DaoFactory;
import model.Dao.ProductDao;
import model.Dao.StockMovementDao;
import model.Dao.UserDao;
import model.entites.User;

public class Main {

    public static void main(String[] args) {

    	UserDao userDao = DaoFactory.createUserDao();
    	CategoryDao categoryDao = DaoFactory.createCategoryDao();
    	ProductDao productDao = DaoFactory.createProductDao();
    	StockMovementDao smDao = DaoFactory.createStockMovementDao();
    	User user = new User();
    	
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame(userDao, categoryDao, productDao, smDao);
            MainFrame mainFrame = new MainFrame(categoryDao, productDao, smDao, user);
            login.setVisible(true);
        });
    }
}

