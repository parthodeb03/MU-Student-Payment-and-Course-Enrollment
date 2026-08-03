package com.mu.ui;

import com.mu.model.Admin;
import com.mu.service.AdminService;
import com.mu.ui.theme.FormSupport;
import com.mu.ui.theme.UITheme;
import com.mu.util.InputValidator;
import javax.swing.*;
import java.awt.*;

public class AdminLoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblError;
    private final AdminService adminService = new AdminService();
    public AdminLoginFrame() {
        setTitle("Administrator Portal"); setSize(540,470); setLocationRelativeTo(null); setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel content=new JPanel(new BorderLayout(16,16));content.setBackground(UITheme.BACKGROUND_COLOR);content.setBorder(BorderFactory.createEmptyBorder(24,24,24,24));setContentPane(content);
        JPanel header=new JPanel(new GridLayout(2,1,0,6));header.setOpaque(false);header.add(UITheme.createLabel("Administrator Portal",UITheme.HEADER_FONT,UITheme.SECONDARY_COLOR));header.add(UITheme.createLabel("Secure access to university management tools",UITheme.BODY_FONT,UITheme.TEXT_MUTED));content.add(header,BorderLayout.NORTH);
        JPanel card=UITheme.createCardPanel();card.setLayout(new GridBagLayout());GridBagConstraints g=c();label(card,g,"Admin Login",UITheme.SUBHEADER_FONT);g.gridy++;
        g.gridwidth=1;label(card,g,"Username *",UITheme.BODY_BOLD);g.gridx=1;txtUsername=UITheme.createTextField(20);txtUsername.setToolTipText("4-30 letters, numbers, or underscores");card.add(txtUsername,g);g.gridx=0;g.gridy++;
        label(card,g,"Password *",UITheme.BODY_BOLD);g.gridx=1;txtPassword=UITheme.createPasswordField(20);card.add(txtPassword,g);g.gridx=0;g.gridy++;
        g.gridwidth=2;card.add(FormSupport.createShowPasswordToggle(txtPassword),g);g.gridy++;lblError=FormSupport.createErrorLabel();card.add(lblError,g);g.gridy++;
        JPanel buttons=new JPanel(new GridLayout(1,3,10,0));buttons.setOpaque(false);JButton login=UITheme.createPrimaryButton("Login"), register=UITheme.createSecondaryButton("Register"), back=UITheme.createOutlineButton("Back");buttons.add(login);buttons.add(register);buttons.add(back);card.add(buttons,g);g.gridy++;
        label(card,g,"New admin? Create an account using Register.",UITheme.SMALL_FONT);content.add(card,BorderLayout.CENTER);
        login.addActionListener(e->login());register.addActionListener(e->{new AdminRegistrationFrame();dispose();});back.addActionListener(e->{new LoginFrame();dispose();});setVisible(true);
    }
    private GridBagConstraints c(){GridBagConstraints g=new GridBagConstraints();g.gridx=0;g.gridy=0;g.gridwidth=2;g.insets=new Insets(8,8,8,8);g.fill=GridBagConstraints.HORIZONTAL;g.anchor=GridBagConstraints.WEST;return g;}
    private void label(JPanel p,GridBagConstraints g,String s,Font f){p.add(UITheme.createLabel(s,f,f==UITheme.SMALL_FONT?UITheme.TEXT_MUTED:UITheme.TEXT_DARK),g);}
    private void login(){FormSupport.clearError(lblError);String username=txtUsername.getText().trim(),password=String.valueOf(txtPassword.getPassword());if(!InputValidator.isValidUsername(username)){FormSupport.showError(lblError,"Enter a valid username.");return;}if(password.isEmpty()){FormSupport.showError(lblError,"Password is required.");return;}try{Admin admin=adminService.login(username,password);if(admin!=null){new AdminDashboardFrame();dispose();}else FormSupport.showError(lblError,"Invalid username or password.");}catch(Exception ex){FormSupport.showError(lblError,ex.getMessage());}}
}
