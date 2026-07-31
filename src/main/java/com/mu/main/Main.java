package com.mu.main;

import com.mu.ui.LoginFrame;
import com.mu.ui.theme.UITheme;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        UITheme.applySystemLookAndFeel();
        SwingUtilities.invokeLater(LoginFrame::new);
    }

}