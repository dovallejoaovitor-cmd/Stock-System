
package Screen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
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

import model.Dao.CategoryDao;
import model.entites.Category;

public class CategoryPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final CategoryDao categoryDao;
    private final CategoryListener listener;

    private final DefaultListModel<Category> model =
            new DefaultListModel<>();

    private final JList<Category> categoryList =
            new JList<>(model);

    public CategoryPanel(
            CategoryDao categoryDao,
            CategoryListener listener) {

        this.categoryDao = categoryDao;
        this.listener = listener;

        setLayout(new BorderLayout(0, 18));
        setBackground(new Color(245, 247, 250));

        setBorder(
                new EmptyBorder(
                        32, 35, 28, 35
                )
        );

        buildInterface();
        reload();
    }

    private void buildInterface() {

        JPanel header = new JPanel(
                new BorderLayout()
        );

        header.setOpaque(false);

        JPanel titles = new JPanel();

        titles.setOpaque(false);

        titles.setLayout(
                new javax.swing.BoxLayout(
                        titles,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        JLabel title = new JLabel(
                "Categorias"
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

        JLabel subtitle = new JLabel(
                "Escolha uma categoria para visualizar o estoque."
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

        titles.add(title);
        titles.add(
                javax.swing.Box.createVerticalStrut(5)
        );
        titles.add(subtitle);

        JButton add = new JButton(
                "+  Adicionar categoria"
        );

        add.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        add.setBackground(
                new Color(40, 44, 52)
        );

        add.setForeground(Color.WHITE);
        add.setFocusPainted(false);

        add.setBorder(
                BorderFactory.createEmptyBorder(
                        12, 17, 12, 17
                )
        );

        add.addActionListener(
                e -> addCategory()
        );

        header.add(
                titles,
                BorderLayout.WEST
        );

        header.add(
                add,
                BorderLayout.EAST
        );

        categoryList.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        categoryList.setFixedCellHeight(70);

        categoryList.setBackground(
                new Color(245, 247, 250)
        );

        categoryList.setBorder(
                new EmptyBorder(
                        8, 0, 8, 0
                )
        );

        categoryList.setCellRenderer(
                new CategoryRenderer()
        );

        categoryList.addListSelectionListener(
                e -> {

                    if (!e.getValueIsAdjusting()) {

                        Category selected =
                                categoryList.getSelectedValue();

                        if (selected != null) {

                            listener.categorySelected(
                                    selected
                            );
                        }
                    }
                }
        );

        JScrollPane scroll =
                new JScrollPane(categoryList);

        scroll.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scroll.getViewport().setBackground(
                new Color(245, 247, 250)
        );

        add(
                header,
                BorderLayout.NORTH
        );

        add(
                scroll,
                BorderLayout.CENTER
        );
    }

    public void reload() {

        model.clear();

        try {

            List<Category> categories =
                    categoryDao.findAll();

            for (Category category : categories) {

                model.addElement(category);
            }

        } catch (RuntimeException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar categorias:\n"
                            + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void addCategory() {

        String name =
                JOptionPane.showInputDialog(
                        this,
                        "Nome da categoria:"
                );

        if (name == null ||
                name.trim().isEmpty()) {

            return;
        }

        try {

            Category category =
                    new Category();

            category.setName(
                    name.trim()
            );

            categoryDao.insert(
                    category
            );

            reload();

        } catch (RuntimeException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao adicionar categoria:\n"
                            + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public interface CategoryListener {

        void categorySelected(
                Category category
        );
    }

    private static class CategoryRenderer
            extends JPanel
            implements ListCellRenderer<Category> {

        private final JLabel name =
                new JLabel();

        private final JLabel arrow =
                new JLabel("›");

        public CategoryRenderer() {

            setLayout(
                    new BorderLayout()
            );

            setOpaque(true);

            setBorder(
                    new EmptyBorder(
                            10, 18, 10, 18
                    )
            );

            name.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            15
                    )
            );

            arrow.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            24
                    )
            );

            add(
                    name,
                    BorderLayout.CENTER
            );

            add(
                    arrow,
                    BorderLayout.EAST
            );
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends Category> list,
                Category value,
                int index,
                boolean selected,
                boolean focus) {

            name.setText(
                    value.getName()
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

            arrow.setForeground(
                    new Color(100, 107, 118)
            );

            return this;
        }
    }
}

