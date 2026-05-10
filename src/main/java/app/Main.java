package app;

import javax.swing.SwingUtilities;

import ui.DynamicFormFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DynamicFormFrame().setVisible(true));
    }
}
