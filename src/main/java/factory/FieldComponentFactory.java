package factory;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import model.FormField;
import ui.FieldComponentBinding;

public class FieldComponentFactory {
    public FieldComponentBinding create (FormField field) {
        String type = field.getControlType() == null ? "textfield" : field.getControlType().toLowerCase();
        JComponent component;

        // Je nach Typ des Formularfelds wird die entsprechende Swing-Komponente erstellt
        switch (type) {
            case "textarea" -> {
                JTextArea tArea = new JTextArea(4, 24);
                tArea.setLineWrap(true);
                tArea.setWrapStyleWord(true);
                component = new JScrollPane(tArea);
            }
            case "dropdown" -> {
                JComboBox<String> comboBox = new JComboBox<>();
                if (field.getOptions() != null) {
                    for (String option : field.getOptions()) {
                        comboBox.addItem(option);
                    }
                }
                component = comboBox;
            }
            case "checkbox" -> component = new JCheckBox();
            case "spinner" -> component = new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE,1));
            case "datefield" ->{
                JTextField tf = new JTextField();
                tf.setToolTipText("YYYY-MM-DD");
                component = tf;
            }
            default -> component = new JTextField();
        }
        return new FieldComponentBinding(field, component);
    }
}
