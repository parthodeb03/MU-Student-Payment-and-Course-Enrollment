package com.mu.ui.theme;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UITheme {

    // Color Palette
    public static final Color PRIMARY_COLOR = new Color(30, 58, 138);       // Deep Navy #1E3A8A
    public static final Color PRIMARY_HOVER = new Color(30, 64, 175);       // Hover Navy #1E40AF
    public static final Color SECONDARY_COLOR = new Color(37, 99, 235);     // Accent Blue #2563EB
    public static final Color SECONDARY_HOVER = new Color(29, 78, 216);
    public static final Color DANGER_COLOR = new Color(239, 68, 68);        // Crimson #EF4444
    public static final Color DANGER_HOVER = new Color(220, 38, 38);
    public static final Color SUCCESS_COLOR = new Color(16, 185, 129);      // Emerald #10B981
    
    public static final Color BACKGROUND_COLOR = new Color(241, 245, 249);  // Slate Background #F1F5F9
    public static final Color SURFACE_COLOR = new Color(255, 255, 255);     // Card Background #FFFFFF
    public static final Color TEXT_DARK = new Color(15, 23, 42);            // Slate Dark #0F172A
    public static final Color TEXT_MUTED = new Color(100, 116, 139);        // Slate Muted #64748B
    public static final Color BORDER_COLOR = new Color(226, 232, 240);       // Light Border #E2E8F0

    // Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font SUBHEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 11);

    private UITheme() {
    }

    public static JButton createPrimaryButton(String text) {
        return createStyledButton(text, PRIMARY_COLOR, PRIMARY_HOVER, Color.WHITE);
    }

    public static JButton createSecondaryButton(String text) {
        return createStyledButton(text, SECONDARY_COLOR, SECONDARY_HOVER, Color.WHITE);
    }

    public static JButton createDangerButton(String text) {
        return createStyledButton(text, DANGER_COLOR, DANGER_HOVER, Color.WHITE);
    }

    public static JButton createOutlineButton(String text) {
        JButton btn = createStyledButton(text, SURFACE_COLOR, BACKGROUND_COLOR, TEXT_DARK);
        btn.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(8, 16, 8, 16)
        ));
        return btn;
    }

    public static JLabel createLabel(String text, Font font, Color foreground) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(foreground);
        return label;
    }

    public static JLabel createLabel(String text) {
        return createLabel(text, BODY_BOLD, TEXT_DARK);
    }

    private static JButton createStyledButton(String text, Color bg, Color hoverBg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(BODY_BOLD);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 18, 10, 18));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hoverBg);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });

        return btn;
    }

    public static JTextField createTextField(int columns) {
        JTextField tf = new JTextField(columns);
        tf.setFont(BODY_FONT);
        tf.setForeground(TEXT_DARK);
        tf.setBackground(SURFACE_COLOR);
        tf.setCaretColor(TEXT_DARK);
        tf.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        return tf;
    }

    public static JPasswordField createPasswordField(int columns) {
        JPasswordField pf = new JPasswordField(columns);
        pf.setFont(BODY_FONT);
        pf.setForeground(TEXT_DARK);
        pf.setBackground(SURFACE_COLOR);
        pf.setCaretColor(TEXT_DARK);
        pf.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        return pf;
    }

    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(SURFACE_COLOR);
        panel.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(16, 16, 16, 16)
        ));
        return panel;
    }

    public static JPanel createStatCard(String title, String value, Color accentColor) {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout(5, 5));

        JLabel lblTitle = new JLabel(title.toUpperCase());
        lblTitle.setFont(SMALL_FONT);
        lblTitle.setForeground(TEXT_MUTED);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(TITLE_FONT);
        lblValue.setForeground(accentColor);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }

    public static void styleTable(JTable table) {
        table.setFont(BODY_FONT);
        table.setRowHeight(36);
        table.setShowGrid(true);
        table.setGridColor(BORDER_COLOR);
        table.setSelectionBackground(new Color(224, 231, 255));
        table.setSelectionForeground(TEXT_DARK);

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));

        // Custom renderer to enforce header background/foreground colors
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(PRIMARY_COLOR);
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(BODY_BOLD);
        headerRenderer.setHorizontalAlignment(JLabel.LEFT);
        headerRenderer.setBorder(new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, false),
                new EmptyBorder(0, 10, 0, 10)
        ));
        
        header.setDefaultRenderer(headerRenderer);
        // Also set on header directly just in case
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.LEFT);
        centerRenderer.setBorder(new EmptyBorder(0, 10, 0, 10));
        table.setDefaultRenderer(Object.class, centerRenderer);
    }

    public static void applySystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
    }
}
