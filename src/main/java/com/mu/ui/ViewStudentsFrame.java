package com.mu.ui;

import com.mu.model.Student;
import com.mu.service.AdminService;
import com.mu.ui.theme.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class ViewStudentsFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtSearch;
    private JLabel lblTotalCount;
    private JButton btnBack;

    private final AdminService adminService;

    public ViewStudentsFrame() {
        adminService = new AdminService();

        setTitle("Registered Students");
        setSize(750, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        // Top Header
        JPanel topPanel = UITheme.createCardPanel();
        topPanel.setLayout(new BorderLayout(10, 10));

        JLabel lblTitle = new JLabel("Registered Student Directory");
        lblTitle.setFont(UITheme.TITLE_FONT);
        lblTitle.setForeground(UITheme.PRIMARY_COLOR);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);

        JLabel lblSearch = new JLabel("Search Student: ");
        lblSearch.setFont(UITheme.BODY_BOLD);
        txtSearch = UITheme.createTextField(16);

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Student ID");
        model.addColumn("Full Name");
        model.addColumn("Email Address");

        table = new JTable(model);
        UITheme.styleTable(table);

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(UITheme.BACKGROUND_COLOR);

        lblTotalCount = new JLabel("Total Registered Students: 0");
        lblTotalCount.setFont(UITheme.BODY_BOLD);
        lblTotalCount.setForeground(UITheme.TEXT_MUTED);

        btnBack = UITheme.createOutlineButton("Back to Dashboard");

        footerPanel.add(lblTotalCount, BorderLayout.WEST);
        footerPanel.add(btnBack, BorderLayout.EAST);

        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        loadStudents();

        // Search Listener
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
            new AdminDashboardFrame();
            dispose();
        });

        setVisible(true);
    }

    private void loadStudents() {
        model.setRowCount(0);
        List<Student> students = adminService.getAllStudents();
        for (Student student : students) {
            model.addRow(new Object[]{
                    student.getStudentId(),
                    student.getName(),
                    student.getEmail()
            });
        }
        lblTotalCount.setText("Total Registered Students: " + students.size());
    }
}