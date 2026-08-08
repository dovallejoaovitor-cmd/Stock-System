
package Screen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import model.Dao.CategoryDao;
import model.Dao.ProductDao;
import model.Dao.StockMovementDao;
import model.Dao.UserDao;
import model.entites.User;

public class LoginFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final UserDao userDao;
    private final CategoryDao categoryDao;
    private final ProductDao productDao;
    private final StockMovementDao stockMovementDao;

    private JTextField emailField;
    private JPasswordField passwordField;

   
    public LoginFrame(
            UserDao userDao,
            CategoryDao categoryDao,
            ProductDao productDao,
            StockMovementDao stockMovementDao) {

        this.userDao = userDao;
        this.categoryDao = categoryDao;
        this.productDao = productDao;
        this.stockMovementDao = stockMovementDao;

        setTitle("StockSystem - Login");

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setSize(500, 600);

        setLocationRelativeTo(null);

        setResizable(false);

        buildInterface();
    }

    private void buildInterface() {

        JPanel background =
                new JPanel(new BorderLayout());

        background.setBackground(
                new Color(245, 247, 250)
        );

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        40, 50, 40, 50
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.insets =
                new Insets(8, 0, 8, 0);

        JLabel title =
                new JLabel(
                        "StockSystem",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        30
                )
        );

        title.setForeground(
                new Color(25, 29, 36)
        );

        gbc.gridx = 0;
        gbc.gridy = 0;

        panel.add(title, gbc);

        JLabel subtitle =
                new JLabel(
                        "Inventory Management",
                        SwingConstants.CENTER
                );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(
                new Color(105, 113, 125)
        );

        gbc.gridy = 1;

        panel.add(subtitle, gbc);

        JLabel emailLabel =
                new JLabel("E-mail");

        emailLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        gbc.gridy = 2;

        panel.add(emailLabel, gbc);

        emailField =
                new JTextField();

        emailField.setPreferredSize(
                new Dimension(300, 40)
        );

        gbc.gridy = 3;

        panel.add(emailField, gbc);

        JLabel passwordLabel =
                new JLabel("Senha");

        passwordLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        gbc.gridy = 4;

        panel.add(passwordLabel, gbc);

        passwordField =
                new JPasswordField();

        passwordField.setPreferredSize(
                new Dimension(300, 40)
        );

        gbc.gridy = 5;

        panel.add(passwordField, gbc);

        JButton loginButton =
                new JButton("ENTRAR");

        loginButton.setPreferredSize(
                new Dimension(300, 45)
        );

        loginButton.setBackground(
                new Color(40, 44, 52)
        );

        loginButton.setForeground(
                Color.WHITE
        );

        loginButton.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        loginButton.setFocusPainted(false);

        loginButton.addActionListener(
                e -> login()
        );

        gbc.gridy = 6;

        gbc.insets =
                new Insets(20, 0, 8, 0);

        panel.add(loginButton, gbc);

        JButton registerButton =
                new JButton(
                        "Não possui conta? Criar conta"
                );

        registerButton.setBorderPainted(false);

        registerButton.setContentAreaFilled(false);

        registerButton.setForeground(
                new Color(60, 90, 150)
        );

        registerButton.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        registerButton.addActionListener(
                e -> openRegister()
        );

        gbc.gridy = 7;

        gbc.insets =
                new Insets(5, 0, 5, 0);

        panel.add(registerButton, gbc);

        background.add(
                panel,
                BorderLayout.CENTER
        );

        setContentPane(background);
    }

    private void login() {

        String email =
                emailField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        if (email.isEmpty()
                || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Preencha o e-mail e a senha.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            User user =
                    userDao.findByEmail(email);

            if (user == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "E-mail ou senha incorretos.",
                        "Login inválido",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            if (!user.getPassword().equals(password)) {

                JOptionPane.showMessageDialog(
                        this,
                        "E-mail ou senha incorretos.",
                        "Login inválido",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            System.out.println(
            	    "LOGIN - ID: " + user.getId()
            	);
            
            MainFrame mainFrame =
                    new MainFrame(
                            categoryDao,
                            productDao,
                            stockMovementDao,
                            user
                    );

            mainFrame.setVisible(true);

            dispose();

        } catch (RuntimeException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao realizar login:\n"
                            + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void openRegister() {

        RegisterFrame registerFrame =
                new RegisterFrame(
                        userDao,
                        categoryDao,
                        productDao,
                        stockMovementDao
                );

        registerFrame.setVisible(true);

        dispose();
    }
}

