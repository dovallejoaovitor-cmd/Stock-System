
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

public class RegisterFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final UserDao userDao;
    private final CategoryDao categoryDao;
    private final ProductDao productDao;
    private final StockMovementDao stockMovementDao;

    private JTextField userNameField;
    private JTextField emailField;

    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public RegisterFrame(
            UserDao userDao,
            CategoryDao categoryDao,
            ProductDao productDao,
            StockMovementDao stockMovementDao) {

        this.userDao = userDao;
        this.categoryDao = categoryDao;
        this.productDao = productDao;
        this.stockMovementDao = stockMovementDao;

        setTitle(
                "StockSystem - Criar conta"
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setSize(500, 650);

        setLocationRelativeTo(null);

        setResizable(false);

        buildInterface();
    }

    private void buildInterface() {

        JPanel background =
                new JPanel(
                        new BorderLayout()
                );

        background.setBackground(
                new Color(245, 247, 250)
        );

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setBackground(
                Color.WHITE
        );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        35, 50, 35, 50
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.insets =
                new Insets(
                        7, 0, 7, 0
                );

        JLabel title =
                new JLabel(
                        "Criar conta",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        28
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
                        "Crie seu acesso ao StockSystem",
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

        JLabel userNameLabel =
                new JLabel(
                        "Nome de usuário"
                );

        userNameLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        gbc.gridy = 2;

        panel.add(
                userNameLabel,
                gbc
        );

        userNameField =
                new JTextField();

        userNameField.setPreferredSize(
                new Dimension(300, 40)
        );

        gbc.gridy = 3;

        panel.add(
                userNameField,
                gbc
        );

        JLabel emailLabel =
                new JLabel("E-mail");

        emailLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        gbc.gridy = 4;

        panel.add(
                emailLabel,
                gbc
        );

        emailField =
                new JTextField();

        emailField.setPreferredSize(
                new Dimension(300, 40)
        );

        gbc.gridy = 5;

        panel.add(
                emailField,
                gbc
        );

        JLabel passwordLabel =
                new JLabel("Senha");

        passwordLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        gbc.gridy = 6;

        panel.add(
                passwordLabel,
                gbc
        );

        passwordField =
                new JPasswordField();

        passwordField.setPreferredSize(
                new Dimension(300, 40)
        );

        gbc.gridy = 7;

        panel.add(
                passwordField,
                gbc
        );

        JLabel confirmLabel =
                new JLabel(
                        "Confirmar senha"
                );

        confirmLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        gbc.gridy = 8;

        panel.add(
                confirmLabel,
                gbc
        );

        confirmPasswordField =
                new JPasswordField();

        confirmPasswordField.setPreferredSize(
                new Dimension(300, 40)
        );

        gbc.gridy = 9;

        panel.add(
                confirmPasswordField,
                gbc
        );

        JButton registerButton =
                new JButton(
                        "CRIAR CONTA"
                );

        registerButton.setPreferredSize(
                new Dimension(300, 45)
        );

        registerButton.setBackground(
                new Color(40, 44, 52)
        );

        registerButton.setForeground(
                Color.WHITE
        );

        registerButton.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        14
                )
        );

        registerButton.setFocusPainted(false);

        registerButton.addActionListener(
                e -> register()
        );

        gbc.gridy = 10;

        gbc.insets =
                new Insets(
                        18, 0, 7, 0
                );

        panel.add(
                registerButton,
                gbc
        );

        JButton backButton =
                new JButton(
                        "Já possui conta? Voltar para login"
                );

        backButton.setBorderPainted(false);

        backButton.setContentAreaFilled(false);

        backButton.setForeground(
                new Color(60, 90, 150)
        );

        backButton.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        13
                )
        );

        backButton.addActionListener(
                e -> backToLogin()
        );

        gbc.gridy = 11;

        gbc.insets =
                new Insets(
                        5, 0, 5, 0
                );

        panel.add(
                backButton,
                gbc
        );

        background.add(
                panel,
                BorderLayout.CENTER
        );

        setContentPane(
                background
        );
    }

    private void register() {

        String userName =
                userNameField
                        .getText()
                        .trim();

        String email =
                emailField
                        .getText()
                        .trim();

        String password =
                new String(
                        passwordField
                                .getPassword()
                );

        String confirmPassword =
                new String(
                        confirmPasswordField
                                .getPassword()
                );

        if (userName.isEmpty()
                || email.isEmpty()
                || password.isEmpty()
                || confirmPassword.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Preencha todos os campos.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (!password.equals(
                confirmPassword)) {

            JOptionPane.showMessageDialog(
                    this,
                    "As senhas não são iguais.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            User user =
                    new User();

            user.setName(
                    userName
            );

            user.setEmail(
                    email
            );

            user.setPassword(
                    password
            );

            userDao.insert(
                    user
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Conta criada com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            backToLogin();

        } catch (RuntimeException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao criar conta:\n"
                            + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void backToLogin() {

        LoginFrame loginFrame =
                new LoginFrame(
                        userDao,
                        categoryDao,
                        productDao,
                        stockMovementDao
                );

        loginFrame.setVisible(true);

        dispose();
    }
}

