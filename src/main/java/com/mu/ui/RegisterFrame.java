package com.mu.ui;

import com.mu.model.Student;
import com.mu.service.RegistrationService;
import com.mu.ui.theme.FormSupport;
import com.mu.ui.theme.UITheme;
import com.mu.util.InputValidator;
import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private JTextField txtId, txtName, txtEmail, txtDepartment, txtBatch, txtPhone;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JLabel lblError;
    private final RegistrationService registrationService = new RegistrationService();

    public RegisterFrame() {
        setTitle("Student Registration"); setSize(600, 700); setLocationRelativeTo(null); setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel content = new JPanel(new BorderLayout(16,16)); content.setBackground(UITheme.BACKGROUND_COLOR); content.setBorder(BorderFactory.createEmptyBorder(20,24,20,24)); setContentPane(content);
        JPanel header = new JPanel(new GridLayout(2,1,0,6)); header.setOpaque(false);
        header.add(UITheme.createLabel("Create Your Student Account", UITheme.HEADER_FONT, UITheme.PRIMARY_COLOR));
        header.add(UITheme.createLabel("Fields marked * are required", UITheme.BODY_FONT, UITheme.TEXT_MUTED)); content.add(header, BorderLayout.NORTH);
        JPanel card = UITheme.createCardPanel(); card.setLayout(new GridBagLayout()); GridBagConstraints g = c();
        label(card,g,"Student Registration",UITheme.SUBHEADER_FONT); g.gridy++;
        txtId = field(card,g,"University ID *", "Example: CSE-2024-001");
        txtName = field(card,g,"Full Name *", "Your full name");
        txtEmail = field(card,g,"Email Address *", "name@example.com");
        txtDepartment = field(card,g,"Department *", "Example: CSE");
        txtBatch = field(card,g,"Batch *", "Example: 55th Batch");
        txtPhone = field(card,g,"Phone Number *", "Example: 01XXXXXXXXX");
        label(card,g,"Password *",UITheme.BODY_BOLD); g.gridx=1; txtPassword=UITheme.createPasswordField(20); card.add(txtPassword,g); g.gridx=0; g.gridy++;
        label(card,g,"Confirm Password *",UITheme.BODY_BOLD); g.gridx=1; txtConfirmPassword=UITheme.createPasswordField(20); card.add(txtConfirmPassword,g); g.gridx=0; g.gridy++;
        g.gridwidth=2; card.add(FormSupport.createShowPasswordToggle(txtPassword,txtConfirmPassword),g); g.gridy++;
        lblError=FormSupport.createErrorLabel(); card.add(lblError,g); g.gridy++;
        JPanel actions=new JPanel(new GridLayout(1,2,12,0)); actions.setOpaque(false); JButton register=UITheme.createPrimaryButton("Register"); JButton back=UITheme.createOutlineButton("Back"); actions.add(register);actions.add(back);card.add(actions,g); g.gridy++;
        label(card,g,"Already have an account? Login instead.",UITheme.SMALL_FONT); content.add(card,BorderLayout.CENTER);
        register.addActionListener(e->registerStudent()); back.addActionListener(e->{new LoginFrame();dispose();}); setVisible(true);
    }
    private GridBagConstraints c(){GridBagConstraints g=new GridBagConstraints();g.gridx=0;g.gridy=0;g.gridwidth=2;g.insets=new Insets(5,8,5,8);g.fill=GridBagConstraints.HORIZONTAL;g.anchor=GridBagConstraints.WEST;return g;}
    private void label(JPanel p,GridBagConstraints g,String s,Font f){p.add(UITheme.createLabel(s,f,f==UITheme.SMALL_FONT?UITheme.TEXT_MUTED:UITheme.TEXT_DARK),g);}
    private JTextField field(JPanel p,GridBagConstraints g,String label,String hint){g.gridwidth=1;label(p,g,label,UITheme.BODY_BOLD);g.gridx=1;JTextField field=UITheme.createTextField(20);field.setToolTipText(hint);p.add(field,g);g.gridx=0;g.gridy++;return field;}
    private void registerStudent(){
        FormSupport.clearError(lblError); String password=String.valueOf(txtPassword.getPassword());
        if(txtId.getText().trim().isEmpty()||txtName.getText().trim().isEmpty()||txtDepartment.getText().trim().isEmpty()||txtBatch.getText().trim().isEmpty()||txtPhone.getText().trim().isEmpty()){FormSupport.showError(lblError,"Complete every required field.");return;}
        if(!InputValidator.isValidEmail(txtEmail.getText().trim())){FormSupport.showError(lblError,"Enter a valid email address.");return;}
        if(!InputValidator.isValidPassword(password)){FormSupport.showError(lblError,"Password must contain 6 to 50 characters.");return;}
        if(!password.equals(String.valueOf(txtConfirmPassword.getPassword()))){FormSupport.showError(lblError,"Passwords do not match.");return;}
        try { Student s=new Student();s.setStudentId(txtId.getText().trim());s.setName(txtName.getText().trim());s.setEmail(txtEmail.getText().trim());s.setDepartment(txtDepartment.getText().trim());s.setBatch(txtBatch.getText().trim());s.setPhone(txtPhone.getText().trim());s.setPassword(password);registrationService.register(s);JOptionPane.showMessageDialog(this,"Registration successful. Please log in.","Success",JOptionPane.INFORMATION_MESSAGE);new LoginFrame();dispose(); }
        catch(Exception ex){FormSupport.showError(lblError,ex.getMessage());}
    }
}
