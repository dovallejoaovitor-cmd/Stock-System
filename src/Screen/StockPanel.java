
package Screen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import model.Dao.ProductDao;
import model.Dao.StockMovementDao;
import model.entites.Category;
import model.entites.Product;
import model.entites.StockMovement;
import model.entites.User;
import enums.MovementType;

public class StockPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final ProductDao productDao;
    private final StockMovementDao stockMovementDao;
    private final User currentUser;
    private final Runnable backAction;

    private Category selectedCategory;

    private final DefaultListModel<Product> model =
            new DefaultListModel<>();

    private final JList<Product> productList =
            new JList<>(model);

    private final JLabel categoryTitle =
            new JLabel("Estoque");

    public StockPanel(
            ProductDao productDao,
            StockMovementDao stockMovementDao,
            User currentUser,
            Runnable backAction) {

        this.productDao = productDao;
        this.stockMovementDao = stockMovementDao;
        this.currentUser = currentUser;
        this.backAction = backAction;

        setLayout(
                new BorderLayout(0, 18)
        );

        setBackground(
                new Color(245, 247, 250)
        );

        setBorder(
                new EmptyBorder(
                        32, 35, 28, 35
                )
        );

        buildInterface();
    }

    private void buildInterface() {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setOpaque(false);

        JButton back =
                secondaryButton(
                        "←  Categorias"
                );

        back.addActionListener(
                e -> backAction.run()
        );

        JPanel titles =
                new JPanel();

        titles.setOpaque(false);

        titles.setLayout(
                new javax.swing.BoxLayout(
                        titles,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        categoryTitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        28
                )
        );

        categoryTitle.setForeground(
                new Color(25, 29, 36)
        );

        JLabel subtitle =
                new JLabel(
                        "Gerencie os produtos e movimentações."
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

        titles.add(categoryTitle);

        titles.add(
                javax.swing.Box.createVerticalStrut(5)
        );

        titles.add(subtitle);

        JButton addProduct =
                primaryButton(
                        "+  Adicionar produto"
                );

        addProduct.addActionListener(
                e -> addProduct()
        );

        header.add(
                back,
                BorderLayout.WEST
        );

        header.add(
                titles,
                BorderLayout.CENTER
        );

        header.add(
                addProduct,
                BorderLayout.EAST
        );

        productList.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        productList.setFixedCellHeight(78);

        productList.setBackground(
                new Color(245, 247, 250)
        );

        productList.setBorder(
                new EmptyBorder(
                        8, 0, 8, 0
                )
        );

        productList.setCellRenderer(
                new ProductRenderer()
        );

        JScrollPane scroll =
                new JScrollPane(productList);

        scroll.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scroll.getViewport().setBackground(
                new Color(245, 247, 250)
        );

        JPanel actions =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                5
                        )
                );

        actions.setOpaque(false);

        JButton entry =
                primaryButton(
                        "+  Entrada"
                );

        JButton sale =
                secondaryButton(
                        "−  Venda / Saída"
                );

        entry.addActionListener(
                e -> changeQuantity(true)
        );

        sale.addActionListener(
                e -> changeQuantity(false)
        );

        actions.add(entry);
        actions.add(sale);

        add(
                header,
                BorderLayout.NORTH
        );

        add(
                scroll,
                BorderLayout.CENTER
        );

        add(
                actions,
                BorderLayout.SOUTH
        );
    }

    public void setCategory(
            Category category) {

        this.selectedCategory =
                category;

        if (category != null) {

            categoryTitle.setText(
                    "Estoque — "
                            + category.getName()
            );

        } else {

            categoryTitle.setText(
                    "Estoque"
            );
        }
    }

    public void reload() {

        model.clear();

        if (selectedCategory == null) {
            return;
        }

        try {

            List<Product> products =
                    productDao.findByCategory(
                            selectedCategory,
                            currentUser
                    );

            for (Product product : products) {

                model.addElement(
                        product
                );
            }

        } catch (RuntimeException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar produtos:\n"
                            + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void addProduct() {

        if (selectedCategory == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione uma categoria primeiro."
            );

            return;
        }

        javax.swing.JTextField name =
                new javax.swing.JTextField();

        javax.swing.JTextField price =
                new javax.swing.JTextField();

        javax.swing.JTextField quantity =
                new javax.swing.JTextField();

        JPanel panel =
                new JPanel(
                        new java.awt.GridLayout(
                                0,
                                1,
                                5,
                                5
                        )
                );

        panel.add(
                new JLabel(
                        "Nome do produto:"
                )
        );

        panel.add(name);

        panel.add(
                new JLabel(
                        "Preço:"
                )
        );

        panel.add(price);

        panel.add(
                new JLabel(
                        "Quantidade inicial:"
                )
        );

        panel.add(quantity);

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Novo produto",
                        JOptionPane.OK_CANCEL_OPTION
                );

        if (result !=
                JOptionPane.OK_OPTION) {

            return;
        }

        try {

            String productName =
                    name.getText().trim();

            double productPrice =
                    Double.parseDouble(
                            price.getText()
                                    .replace(",", ".")
                    );

            int productQuantity =
                    Integer.parseInt(
                            quantity.getText()
                    );

            if (productName.isEmpty()
                    || productPrice < 0
                    || productQuantity < 0) {

                throw new NumberFormatException();
            }

            Product product =
                    new Product();

            product.setName(
                    productName
            );

            product.setCategory(
                    selectedCategory
            );

            product.setPrice(
                    productPrice
            );
            
            product.setQuantity(
                    productQuantity
            );

            product.setUser(
                    currentUser
            );
            
            productDao.insert(
                    product
            );

            reload();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Preencha os dados corretamente.",
                    "Dados inválidos",
                    JOptionPane.WARNING_MESSAGE
            );

        } catch (RuntimeException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao cadastrar produto:\n"
                            + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void changeQuantity(
            boolean entry) {

        Product product =
                productList.getSelectedValue();

        if (product == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um produto."
            );

            return;
        }

        String value =
                JOptionPane.showInputDialog(
                        this,
                        entry
                                ? "Quantidade de entrada:"
                                : "Quantidade da venda:"
                );

        if (value == null) {
            return;
        }

        try {

            int amount =
                    Integer.parseInt(
                            value
                    );

            if (amount <= 0) {

                throw new NumberFormatException();
            }

            if (!entry &&
                    amount > product.getQuantity()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Não há quantidade suficiente em estoque.",
                        "Estoque insuficiente",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int newQuantity =
                    entry
                            ? product.getQuantity()
                                    + amount
                            : product.getQuantity()
                                    - amount;

            product.setQuantity(
                    newQuantity
            );

            productDao.update(
                    product
            );

            if (currentUser != null) {

                StockMovement movement =
                        new StockMovement();

                movement.setProduct(
                        product
                );

                movement.setUser(
                        currentUser
                );

                movement.setQuantity(
                        amount
                );

                movement.setDate(
                        LocalDate.now()
                );

                movement.setMt(
                        entry
                                ? MovementType.ENTRADA
                                : MovementType.SAÍDA
                );

                stockMovementDao.insert(
                        movement
                );
            }

            reload();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Digite uma quantidade inteira válida.",
                    "Quantidade inválida",
                    JOptionPane.WARNING_MESSAGE
            );

        } catch (RuntimeException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao atualizar estoque:\n"
                            + e.getMessage(),
                    "Erro",
                            JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private JButton primaryButton(
            String text) {

        JButton button =
                new JButton(text);

        button.setBackground(
                new Color(40, 44, 52)
        );

        button.setForeground(
                Color.WHITE
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
                        12, 17, 12, 17
                )
        );

        return button;
    }

    private JButton secondaryButton(
            String text) {

        JButton button =
                new JButton(text);

        button.setBackground(
                Color.WHITE
        );

        button.setForeground(
                new Color(40, 44, 52)
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
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 223, 228)
                        ),
                        BorderFactory.createEmptyBorder(
                                11, 16, 11, 16
                        )
                )
        );

        return button;
    }

    private static class ProductRenderer
            extends JPanel
            implements ListCellRenderer<Product> {

        private final JLabel name =
                new JLabel();

        private final JLabel details =
                new JLabel();

        public ProductRenderer() {

            setLayout(
                    new BorderLayout()
            );

            setOpaque(true);

            setBorder(
                    new EmptyBorder(
                            12, 18, 12, 18
                    )
            );

            JPanel text =
                    new JPanel();

            text.setOpaque(false);

            text.setLayout(
                    new javax.swing.BoxLayout(
                            text,
                            javax.swing.BoxLayout.Y_AXIS
                    )
            );

            name.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            15
                    )
            );

            details.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            12
                    )
            );

            details.setForeground(
                    new Color(105, 113, 125)
            );

            text.add(name);

            text.add(
                    javax.swing.Box.createVerticalStrut(
                            5
                    )
            );

            text.add(details);

            add(
                    text,
                    BorderLayout.CENTER
            );
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends Product> list,
                Product value,
                int index,
                boolean selected,
                boolean focus) {

            name.setText(
                    value.getName()
            );

            details.setText(
                    String.format(
                            "R$ %.2f   •   Estoque: %d",
                            value.getPrice(),
                            value.getQuantity()
                    )
            );

            if (selected) {

                setBackground(
                        new Color(229, 232, 238)
                );

            } else {

                setBackground(
                        Color.WHITE
                );
            }

            return this;
        }
    }
}

