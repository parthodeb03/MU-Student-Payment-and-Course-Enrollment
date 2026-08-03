package com.mu.ui;

import com.mu.dao.CourseDAO;
import com.mu.model.Course;
import com.mu.ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class ViewCoursesFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtSearch;
    private JButton btnBack;

    private final CourseDAO courseDAO;

    public ViewCoursesFrame() {
        courseDAO = com.mu.factory.DAOFactory.createCourseDAO();

        setTitle("Available Courses");
        setSize(750, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        JPanel topPanel = UITheme.createCardPanel();
        topPanel.setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = UITheme.createLabel("Available University Courses", UITheme.TITLE_FONT, UITheme.PRIMARY_COLOR);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);

        JLabel lblSearch = UITheme.createLabel("Search Course:", UITheme.BODY_BOLD, UITheme.TEXT_DARK);
        txtSearch = UITheme.createTextField(16);

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);


        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Course ID");
        model.addColumn("Course Name");
        model.addColumn("Credits");

        table = new JTable(model);
        UITheme.styleTable(table);

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Action Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(UITheme.BACKGROUND_COLOR);

        btnBack = UITheme.createOutlineButton("Back to Dashboard");
        bottomPanel.add(btnBack);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        loadCourses();


        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filter(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filter(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filter(); }

            private void filter() {
                String text = txtSearch.getText().trim();
                if (text.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        btnBack.addActionListener(e -> {
            new DashboardFrame();
            dispose();
        });

        setVisible(true);
    }

    private void loadCourses() {
        model.setRowCount(0);
        List<Course> courses = courseDAO.getAllCourses();
        for (Course course : courses) {
            model.addRow(new Object[]{
                    course.getCourseId(),
                    course.getCourseName(),
                    course.getCredit()
            });
        }
    }
}