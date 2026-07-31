package com.mu.ui;

import com.mu.dao.CourseDAO;
import com.mu.model.Course;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CourseManagementFrame extends JFrame {

    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtCredit;

    private JTable table;
    private DefaultTableModel model;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnBack;

    private CourseDAO courseDAO;

    public CourseManagementFrame() {

        courseDAO = new CourseDAO();

        setTitle("Course Management");

        setSize(750,500);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(3,2,10,10));

        topPanel.add(new JLabel("Course ID"));
        txtId = new JTextField();
        topPanel.add(txtId);

        topPanel.add(new JLabel("Course Name"));
        txtName = new JTextField();
        topPanel.add(txtName);

        topPanel.add(new JLabel("Credit"));
        txtCredit = new JTextField();
        topPanel.add(txtCredit);

        add(topPanel,BorderLayout.NORTH);

        model = new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("Course");
        model.addColumn("Credit");

        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane,BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();

        btnAdd = new JButton("Add");

        btnUpdate = new JButton("Update");

        btnDelete = new JButton("Delete");

        btnBack = new JButton("Back");

        bottomPanel.add(btnAdd);
        bottomPanel.add(btnUpdate);
        bottomPanel.add(btnDelete);
        bottomPanel.add(btnBack);

        add(bottomPanel,BorderLayout.SOUTH);

        loadCourses();

        table.getSelectionModel().addListSelectionListener(e->{

            int row = table.getSelectedRow();

            if(row!=-1){

                txtId.setText(model.getValueAt(row,0).toString());

                txtName.setText(model.getValueAt(row,1).toString());

                txtCredit.setText(model.getValueAt(row,2).toString());

            }

        });

        btnAdd.addActionListener(e->addCourse());

        btnUpdate.addActionListener(e->updateCourse());

        btnDelete.addActionListener(e->deleteCourse());

        btnBack.addActionListener(e->{

            new AdminDashboardFrame();

            dispose();

        });

        setVisible(true);

    }

    private void loadCourses(){

        model.setRowCount(0);

        List<Course> list = courseDAO.getAllCourses();

        for(Course course:list){

            model.addRow(new Object[]{

                    course.getCourseId(),

                    course.getCourseName(),

                    course.getCredit()

            });

        }

    }

    private void addCourse(){

        try{

            Course course = new Course();

            course.setCourseName(txtName.getText());

            course.setCredit(Integer.parseInt(txtCredit.getText()));

            if(courseDAO.addCourse(course)){

                JOptionPane.showMessageDialog(this,"Course Added");

                clearFields();

                loadCourses();

            }

        }catch(Exception ex){

            JOptionPane.showMessageDialog(this,ex.getMessage());

        }

    }

    private void updateCourse(){

        try{

            Course course = new Course();

            course.setCourseId(Integer.parseInt(txtId.getText()));

            course.setCourseName(txtName.getText());

            course.setCredit(Integer.parseInt(txtCredit.getText()));

            if(courseDAO.updateCourse(course)){

                JOptionPane.showMessageDialog(this,"Course Updated");

                clearFields();

                loadCourses();

            }

        }catch(Exception ex){

            JOptionPane.showMessageDialog(this,ex.getMessage());

        }

    }

    private void deleteCourse(){

        try{

            int id = Integer.parseInt(txtId.getText());

            int option = JOptionPane.showConfirmDialog(

                    this,

                    "Delete this course?",

                    "Confirm",

                    JOptionPane.YES_NO_OPTION

            );

            if(option==JOptionPane.YES_OPTION){

                if(courseDAO.deleteCourse(id)){

                    JOptionPane.showMessageDialog(this,"Course Deleted");

                    clearFields();

                    loadCourses();

                }

            }

        }catch(Exception ex){

            JOptionPane.showMessageDialog(this,ex.getMessage());

        }

    }

    private void clearFields(){

        txtId.setText("");

        txtName.setText("");

        txtCredit.setText("");

    }

}