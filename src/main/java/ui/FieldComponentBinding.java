package ui;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.time.LocalDate;
import java.util.Map;

import model.FormField;

public class FieldComponentBinding {
    private final FormField field;
    private final JComponent component;

    public FieldComponentBinding(FormField field, JComponent component) {
        this.field = field;
        this.component = component;
    }

    public FormField getField() {
        return field;
    }

    public JComponent getComponent() {
        return component;
    }

    // Liest den Rohwert direkt aus der Komponente, ohne Konvertierung. Bei Fehlern
    // wird null zurückgegeben.
    public Object getRawValue() {
        if (component instanceof JTextField textField)
            return textField.getText();
        if (component instanceof JTextArea textArea)
            return textArea.getText();

        if (component instanceof JCheckBox checkBox)
            return checkBox.isSelected();

        if (component instanceof JComboBox<?> comboBox)
            return comboBox.getSelectedItem();

        if (component instanceof JSpinner spinner)
            return spinner.getValue();

        if (component instanceof JScrollPane scrollPane
                && scrollPane.getViewport().getView() instanceof JTextArea textArea)
            return textArea.getText();
        return null;
    }

    // Versucht, den Rohwert in den erwarteten Datentyp zu konvertieren. Bei Fehlern
    // wird der Rohwert zurückgegeben, damit die Validierung es abfangen kann.
    public Object getConvertedValue() {
        Object rawValue = getRawValue();
        if (rawValue == null)
            return null;

        String type = field.getDataType() == null ? "string" : field.getDataType().toLowerCase();

        try {
            return switch (type) {
                case "int" ->
                    (rawValue instanceof Number n) ? n.intValue() : Integer.parseInt(String.valueOf(rawValue).trim());
                case "double" -> (rawValue instanceof Number n) ? n.doubleValue()
                        : Double.parseDouble(String.valueOf(rawValue).trim());
                case "boolean" ->
                    (rawValue instanceof Boolean b) ? b : Boolean.parseBoolean(String.valueOf(rawValue).trim());
                case "date" -> LocalDate.parse(String.valueOf(rawValue).trim()).toString();
                default -> rawValue;
            };
        } catch (Exception e) {
            return rawValue; // Bei Fehlern einfach den Rohwert zurückgeben, damit die Validierung es
                             // abfangen kann.
        }
    }

    // Setzt den Wert aus der geladenen Map in die entsprechende Komponente. Bei
    // Fehlern wird der Wert ignoriert.
    public void applyLoadedValue(Map<String, Object> valuesByLabel) {
        Object value = valuesByLabel.get(field.getLabel());
        if (value == null)
            return;

        if (component instanceof JTextField tf) {
            tf.setText(String.valueOf(value));
        } else if (component instanceof JTextArea ta) {
            ta.setText(String.valueOf(value));
        } else if (component instanceof JCheckBox cb) {
            cb.setSelected(value instanceof Boolean b ? b : Boolean.parseBoolean(String.valueOf(value)));
        } else if (component instanceof JComboBox<?> cb) {
            cb.setSelectedItem(String.valueOf(value));
        } else if (component instanceof JSpinner spinner) {
            if (value instanceof Number n) {
                spinner.setValue(n.intValue());
            } else {
                try {
                    spinner.setValue(Integer.parseInt(String.valueOf(value)));
                } catch (Exception ignored) {
                }
            }
        } else if (component instanceof JScrollPane sp && sp.getViewport().getView() instanceof JTextArea ta) {
            ta.setText(String.valueOf(value));
        }
    }
}
