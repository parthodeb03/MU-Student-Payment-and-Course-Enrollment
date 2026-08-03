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

public class CourseManagementFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;

    private JTextField txtCourseName;
    private JTextField txtCredit;
    private JTextField txtSearch;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnBack;

    private final CourseDAO courseDAO;
    private int selectedCourseId = -1;

    public CourseManagementFrame() {
        courseDAO = com.mu.factory.DAOFactory.createCourseDAO();

        setTitle("Admin - Course Management");
        setSize(850, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);


        JPanel topPanel = UITheme.createCardPanel();
        topPanel.setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Course Management Panel");
        lblTitle.setFont(UITheme.TITLE_FONT);
        lblTitle.setForeground(UITheme.PRIMARY_COLOR);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        JLabel lblSearch = new JLabel("Search Course: ");
        lblSearch.setFont(UITheme.BODY_BOLD);
        txtSearch = UITheme.createTextField(14);
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


        JPanel formPanel = UITheme.createCardPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setPreferredSize(new Dimension(300, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblFormHeader = new JLabel("Course Details");
        lblFormHeader.setFont(UITheme.HEADER_FONT);
        lblFormHeader.setForeground(UITheme.PRIMARY_COLOR);
        formPanel.add(lblFormHeader, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JLabel lblName = new JLabel("Course Name:");
        lblName.setFont(UITheme.BODY_BOLD);
        formPanel.add(lblName, gbc);

        gbc.gridy = 2;
        txtCourseName = UITheme.createTextField(14);
        formPanel.add(txtCourseName, gbc);

        gbc.gridy = 3;
        JLabel lblCredit = new JLabel("Credits:");
        lblCredit.setFont(UITheme.BODY_BOLD);
        formPanel.add(lblCredit, gbc);

        gbc.gridy = 4;
        txtCredit = UITheme.createTextField(14);
        formPanel.add(txtCredit, gbc);

        JPanel btnFormPanel = new JPanel(new GridLayout(3, 1, 8, 8));
        btnFormPanel.setOpaque(false);

        btnAdd = UITheme.createPrimaryButton("Add Course");
        btnUpdate = UITheme.createSecondaryButton("Update Course");
        btnDelete = UITheme.createDangerButton("Delete Course");

        btnFormPanel.add(btnAdd);
        btnFormPanel.add(btnUpdate);
        btnFormPanel.add(btnDelete);

        gbc.gridy = 5;
        formPanel.add(btnFormPanel, gbc);


        JPanel centerSplit = new JPanel(new BorderLayout(15, 15));
        centerSplit.setBackground(UITheme.BACKGROUND_COLOR);
        centerSplit.add(formPanel, BorderLayout.WEST);
        centerSplit.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerSplit, BorderLayout.CENTER);


        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(UITheme.BACKGROUND_COLOR);
        btnBack = UITheme.createOutlineButton("Back to Dashboard");
        footerPanel.add(btnBack);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        loadCourses();


        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int modelRow = table.convertRowIndexToModel(selectedRow);
                selectedCourseId = (int) model.getValueAt(modelRow, 0);
                txtCourseName.setText((String) model.getValueAt(modelRow, 1));
                txtCredit.setText(String.valueOf(model.getValueAt(modelRow, 2)));
            }
        });


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

        btnAdd.addActionListener(e -> addCourse());
        btnUpdate.addActionListener(e -> updateCourse());
        btnDelete.addActionListener(e -> deleteCourse());
        btnBack.addActionListener(e -> {
            new AdminDashboardFrame();
            dispose();
        });

        setVisible(true);
    }

    private void loadCourses() {
        model.setRowCount(0);
        List<Course> courses = courseDAO.getAllCourses();
        for (Course c : courses) {
            model.addRow(new Object[]{c.getCourseId(), c.getCourseName(), c.getCredit()});
        }
        selectedCourseId = -1;
        txtCourseName.setText("");
        txtCredit.setText("");
    }

    private void addCourse() {
        try {
            String name = txtCourseName.getText().trim();
            String creditStr = txtCredit.getText().trim();
            if (name.isEmpty() || creditStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int credit = Integer.parseInt(creditStr);
            Course course = new Course(0, name, credit);
            if (courseDAO.addCourse(course)) {
                JOptionPane.showMessageDialog(this, "Course Added Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCourses();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Credit must be a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCourse() {
        if (selectedCourseId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to update.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String name = txtCourseName.getText().trim();
            int credit = Integer.parseInt(txtCredit.getText().trim());
            Course course = new Course(selectedCourseId, name, credit);
            if (courseDAO.updateCourse(course)) {
                JOptionPane.showMessageDialog(this, "Course Updated Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCourses();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Credit must be a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCourse() {
        if (selectedCourseId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this course?", "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            if (courseDAO.deleteCourse(selectedCourseId)) {
                JOptionPane.showMessageDialog(this, "Course Deleted Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCourses();
            }
        }
    }
}