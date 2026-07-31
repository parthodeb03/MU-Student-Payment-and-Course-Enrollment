package com.mu.observer;

import javax.swing.JOptionPane;

public class StudentObserver implements Observer {

    @Override
    public void update(String message) {

        JOptionPane.showMessageDialog(
                null,
                message,
                "Notification",
                JOptionPane.INFORMATION_MESSAGE
        );

    }

}