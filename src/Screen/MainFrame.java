
package Screen;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import model.Dao.CategoryDao;
import model.Dao.ProductDao;
import model.Dao.StockMovementDao;
import model.entites.Category;
import model.entites.User;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel content = new JPanel(cardLayout);

    private final CategoryPanel categoryPanel;
    private final StockPanel stockPanel;

    public MainFrame(
            CategoryDao categoryDao,
            ProductDao productDao,
            StockMovementDao stockMovementDao,
            User currentUser) {
    	
    	 System.out.println(
    		        "MAIN FRAME - ID DO USER: " + currentUser.getId()
    		    );

        setTitle("StockSystem");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setMinimumSize(new Dimension(1100, 700));
        setSize(1200, 750);
        setLocationRelativeTo(null);

        stockPanel = new StockPanel(
                productDao,
                stockMovementDao,
                currentUser,
                () -> cardLayout.show(content, "categories")
        );
        
        categoryPanel = new CategoryPanel(
                categoryDao,
                category -> openCategory(category),
                currentUser
        );

        buildInterface();
    }

    private void buildInterface() {

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(245, 247, 250));

        root.add(createSidebar(), BorderLayout.WEST);
        root.add(content, BorderLayout.CENTER);

        content.setBackground(new Color(245, 247, 250));

        content.add(categoryPanel, "categories");
        content.add(stockPanel, "stock");

        setContentPane(root);

        cardLayout.show(content, "categories");
    }

    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();

        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(new Color(25, 29, 36));
        sidebar.setBorder(new EmptyBorder(28, 18, 20, 18));

        sidebar.setLayout(
                new BoxLayout(
                        sidebar,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel logo = new JLabel("StockSystem");

        logo.setForeground(Color.WHITE);
        logo.setFont(
                new Font("SansSerif", Font.BOLD, 23)
        );

        logo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Inventory Manager");

        subtitle.setForeground(
                new Color(160, 168, 180)
        );

        subtitle.setFont(
                new Font("SansSerif", Font.PLAIN, 12)
        );

        subtitle.setAlignmentX(LEFT_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(subtitle);
        sidebar.add(Box.createVerticalStrut(35));

        JButton categories = navButton("Categorias");

        categories.addActionListener(e -> {

            categoryPanel.reload();

            cardLayout.show(
                    content,
                    "categories"
            );
        });

        JButton stock = navButton("Estoque");

        stock.addActionListener(e -> {

            stockPanel.reload();

            cardLayout.show(
                    content,
                    "stock"
            );
        });

        sidebar.add(categories);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(stock);

        return sidebar;
    }

    private JButton navButton(String text) {

        JButton button = new JButton(text);

        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        44
                )
        );

        button.setPreferredSize(
                new Dimension(180, 44)
        );

        button.setBackground(
                new Color(25, 29, 36)
        );

        button.setForeground(
                new Color(220, 224, 231)
        );

        button.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        12,
                        10,
                        12
                )
        );

        return button;
    }

    private void openCategory(Category category) {

        stockPanel.setCategory(category);

        stockPanel.reload();

        cardLayout.show(
                content,
                "stock"
        );
    }
}

