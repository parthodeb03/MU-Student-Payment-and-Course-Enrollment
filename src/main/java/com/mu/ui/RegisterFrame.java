package com.mu.ui;

import com.mu.dao.StudentDAO;
import com.mu.factory.DAOFactory;
import com.mu.model.Student;
import com.mu.service.RegistrationService;
import com.mu.ui.theme.UITheme;
import com.mu.util.InputValidator;
import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private JTextField name, batch, studentId, email, phone; private JComboBox<String> department; private JPasswordField password, confirm; private JLabel validation; private final RegistrationService service=new RegistrationService();
    public RegisterFrame(){
        setTitle("Student Registration");setSize(600,650);setLocationRelativeTo(null);setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel content=new JPanel(new BorderLayout(16,16));content.setBackground(UITheme.BACKGROUND_COLOR);content.setBorder(BorderFactory.createEmptyBorder(24,24,24,24));setContentPane(content);
        JPanel header=new JPanel(new GridLayout(2,1,0,6));header.setOpaque(false);header.add(UITheme.createLabel("Create Your Student Account",UITheme.HEADER_FONT,UITheme.PRIMARY_COLOR));header.add(UITheme.createLabel("Complete all required fields to register",UITheme.BODY_FONT,UITheme.TEXT_MUTED));
        JPanel card=UITheme.createCardPanel();card.setLayout(new GridBagLayout());GridBagConstraints g=c();g.gridwidth=2;card.add(UITheme.createLabel("Student Registration",UITheme.SUBHEADER_FONT,UITheme.TEXT_DARK),g);
        name=UITheme.createTextField(20); add(card,g,"Full Name *:",name);
        department=new JComboBox<>();department.setFont(UITheme.BODY_FONT);department.setBackground(UITheme.SURFACE_COLOR);department.addItem("-- Select Department --");for(String d:DAOFactory.createStudentDAO().getAllDepartments())department.addItem(d);add(card,g,"Department *:",department);
        batch=UITheme.createTextField(20);batch.setToolTipText("For example: 57th Batch");add(card,g,"Batch *:",batch);
        studentId=UITheme.createTextField(20);add(card,g,"Student ID *:",studentId);
        email=UITheme.createTextField(20);email.setToolTipText("name@example.com");add(card,g,"Email Address *:",email);
        phone=UITheme.createTextField(20);add(card,g,"Phone Number *:",phone);
        password=UITheme.createPasswordField(20);add(card,g,"Password *:",password);
        confirm=UITheme.createPasswordField(20);add(card,g,"Confirm Password *:",confirm);
        g.gridx=0;g.gridy++;g.gridwidth=2;JCheckBox show=new JCheckBox("Show passwords");show.setOpaque(false);show.setFont(UITheme.SMALL_FONT);show.addActionListener(e->{char x=show.isSelected()?(char)0:'*';password.setEchoChar(x);confirm.setEchoChar(x);});card.add(show,g);
        g.gridy++;validation=UITheme.createLabel("* Required fields. Password must be 6-50 characters.",UITheme.SMALL_FONT,UITheme.TEXT_MUTED);card.add(validation,g);
        g.gridy++;JPanel actions=new JPanel(new GridLayout(1,2,12,0));actions.setOpaque(false);JButton register=UITheme.createPrimaryButton("Register"),back=UITheme.createOutlineButton("Back");actions.add(register);actions.add(back);card.add(actions,g);
        content.add(header,BorderLayout.NORTH);content.add(new JScrollPane(card),BorderLayout.CENTER);register.addActionListener(e->register());back.addActionListener(e->{new LoginFrame();dispose();});setVisible(true);
    }
    private GridBagConstraints c(){GridBagConstraints g=new GridBagConstraints();g.insets=new Insets(8,12,8,12);g.fill=GridBagConstraints.HORIZONTAL;g.anchor=GridBagConstraints.WEST;g.gridx=0;g.gridy=0;return g;}
    private void add(JPanel panel,GridBagConstraints g,String label,JComponent field){g.gridy++;g.gridwidth=1;g.gridx=0;panel.add(UITheme.createLabel(label),g);g.gridx=1;panel.add(field,g);}
    private void register(){try{String p=String.valueOf(password.getPassword());if(name.getText().trim().isEmpty()||department.getSelectedIndex()==0||batch.getText().trim().isEmpty()||studentId.getText().trim().isEmpty()||email.getText().trim().isEmpty()||phone.getText().trim().isEmpty()||p.isEmpty()||confirm.getPassword().length==0){error("All required fields must be completed.");return;}if(!InputValidator.isValidEmail(email.getText().trim())){error("Enter a valid email address, for example name@example.com.");return;}if(!p.equals(String.valueOf(confirm.getPassword()))){error("Password and confirmation do not match.");return;}Student s=new Student();s.setName(name.getText().trim());s.setDepartment(department.getSelectedItem().toString());s.setBatch(batch.getText().trim());s.setStudentId(studentId.getText().trim());s.setEmail(email.getText().trim());s.setPhone(phone.getText().trim());s.setPassword(p);if(service.register(s)){JOptionPane.showMessageDialog(this,"Registration successful! Please login to continue.","Success",JOptionPane.INFORMATION_MESSAGE);new LoginFrame();dispose();}else error("Registration failed. Please try again.");}catch(Exception ex){error(ex.getMessage());}}
    private void error(String m){validation.setForeground(UITheme.DANGER_COLOR);validation.setText(m);}
}
