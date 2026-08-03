package com.mu.ui;

import com.mu.model.Admin;
import com.mu.service.AdminService;
import com.mu.ui.theme.UITheme;
import com.mu.util.InputValidator;
import javax.swing.*;
import java.awt.*;

public class AdminLoginFrame extends JFrame {
    private JTextField txtUsername; private JPasswordField txtPassword; private JLabel lblValidation; private final AdminService adminService = new AdminService();
    public AdminLoginFrame() {
        setTitle("Admin Portal"); setSize(560, 440); setLocationRelativeTo(null); setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel content = new JPanel(new BorderLayout(16,16)); content.setBackground(UITheme.BACKGROUND_COLOR); content.setBorder(BorderFactory.createEmptyBorder(24,24,24,24)); setContentPane(content);
        JPanel header = new JPanel(new GridLayout(2,1,0,6)); header.setOpaque(false); header.add(UITheme.createLabel("Administrator Login", UITheme.HEADER_FONT, UITheme.PRIMARY_COLOR)); header.add(UITheme.createLabel("Secure access to university management tools", UITheme.BODY_FONT, UITheme.TEXT_MUTED));
        JPanel card = UITheme.createCardPanel(); card.setLayout(new GridBagLayout()); GridBagConstraints gbc = c(); gbc.gridwidth=2; card.add(UITheme.createLabel("Admin Login", UITheme.SUBHEADER_FONT, UITheme.TEXT_DARK),gbc);
        gbc.gridy++; gbc.gridwidth=1; card.add(UITheme.createLabel("Username *:"),gbc); gbc.gridx=1; txtUsername=UITheme.createTextField(20); card.add(txtUsername,gbc);
        gbc.gridx=0; gbc.gridy++; card.add(UITheme.createLabel("Password *:"),gbc); gbc.gridx=1; txtPassword=UITheme.createPasswordField(20); card.add(txtPassword,gbc);
        gbc.gridx=0; gbc.gridy++; gbc.gridwidth=2; JCheckBox show=new JCheckBox("Show password"); show.setOpaque(false); show.setFont(UITheme.SMALL_FONT); show.addActionListener(e->txtPassword.setEchoChar(show.isSelected()?(char)0:'*')); card.add(show,gbc);
        gbc.gridy++; lblValidation=UITheme.createLabel("New admin? Create an account below.",UITheme.SMALL_FONT,UITheme.TEXT_MUTED); card.add(lblValidation,gbc);
        gbc.gridy++; JPanel buttons=new JPanel(new GridLayout(1,3,12,0)); buttons.setOpaque(false); JButton login=UITheme.createPrimaryButton("Login"), register=UITheme.createOutlineButton("Admin Register"), back=UITheme.createOutlineButton("Back"); buttons.add(login); buttons.add(register); buttons.add(back); card.add(buttons,gbc);
        content.add(header,BorderLayout.NORTH); content.add(card,BorderLayout.CENTER); login.addActionListener(e->login()); register.addActionListener(e->{new AdminRegistrationFrame();dispose();}); back.addActionListener(e->{new LoginFrame();dispose();}); setVisible(true);
    }
    private GridBagConstraints c(){GridBagConstraints g=new GridBagConstraints();g.insets=new Insets(12,12,12,12);g.fill=GridBagConstraints.HORIZONTAL;g.anchor=GridBagConstraints.WEST;g.gridx=0;g.gridy=0;return g;}
    private void login(){try{String username=txtUsername.getText().trim(), password=String.valueOf(txtPassword.getPassword());if(username.isEmpty()||password.isEmpty()){error("Username and password are required.");return;}if(!InputValidator.isValidUsername(username)){error("Username must be 4-30 letters, numbers, or underscores.");return;}Admin admin=adminService.login(username,password);if(admin!=null){new AdminDashboardFrame();dispose();}else JOptionPane.showMessageDialog(this,"Invalid username or password.","Login Failed",JOptionPane.ERROR_MESSAGE);}catch(Exception ex){error(ex.getMessage());}}
    private void error(String message){lblValidation.setForeground(UITheme.DANGER_COLOR);lblValidation.setText(message);}
}
