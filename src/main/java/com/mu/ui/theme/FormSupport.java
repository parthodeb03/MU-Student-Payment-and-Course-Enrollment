package com.mu.ui.theme;

import javax.swing.*;

/** Shared small UX helpers for login and registration forms. */
public final class FormSupport {
    private FormSupport() { }

    public static JLabel createErrorLabel() {
        JLabel label = UITheme.createLabel(" ", UITheme.SMALL_FONT, UITheme.DANGER_COLOR);
        label.setVisible(false);
        return label;
    }

    public static void showError(JLabel label, String message) {
        label.setText(message);
        label.setVisible(true);
    }

    public static void clearError(JLabel label) {
        label.setText(" ");
        label.setVisible(false);
    }

    public static JCheckBox createShowPasswordToggle(JPasswordField... fields) {
        char hiddenEcho = fields[0].getEchoChar();
        JCheckBox toggle = new JCheckBox("Show password");
        toggle.setOpaque(false);
        toggle.setFont(UITheme.SMALL_FONT);
        toggle.setForeground(UITheme.TEXT_MUTED);
        toggle.addActionListener(e -> {
            char echo = toggle.isSelected() ? (char) 0 : hiddenEcho;
            for (JPasswordField field : fields) field.setEchoChar(echo);
        });
        return toggle;
    }
}
