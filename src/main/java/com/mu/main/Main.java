package com.mu.main;

import com.mu.ui.LoginFrame;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(LoginFrame::new);

    }

}