package main.java.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import main.java.factory.FieldComponentFactory;
import main.java.model.FormField;
import main.java.service.FormJsonService;
import main.java.service.ResultJsonService;
import main.java.validation.FieldValidator;

public class DynamicFormFrame extends JFrame {
    private final JLabel titleLabel = new JLabel("Kein Formular geladen", SwingConstants.CENTER);
    private final JPanel formPanel = new JPanel();
    private final List<FieldComponentBinding> bindings = new ArrayList<>();

    private final FormJsonService formJsonService = new FormJsonService();
    private final ResultJsonService resultJsonService = new ResultJsonService();
    private final FieldValidator validator = new FieldValidator.DefaultFieldValidator();
    private final FieldComponentFactory componentFactory = new FieldComponentFactory();

    private FormDefinition currentDefinition;

    public DynamicFormFrame() {
        setTitle("Dynamischer Formular-Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        add(buildTopBar(), BorderLayout.NORTH);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        add(new JScrollPane(formPanel), BorderLayout.CENTER);
    }

    private JPanel buildTopBar() {
        JPanel root = new JPanel(new BorderLayout());
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10,10, 10));
        root.add(titleLabel, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton loadDef = new JButton("Formular laden");
        JButton loadRes = new JButton("Ergebnis laden");
        JButton saveRes = new JButton("Ergebnis speichern");

        loadDef.addActionListener(e -> );
    }

    private void loadDefinition() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
            return;

        try {
            currentDefinition = formJsonService.loadDefenition(chooser.getSelectedFile().toPath());
            renderForm(currentDefinition);
        } catch (Exception e) {
            error(e.getMessage());
        }
    }

    private void renderForm(FormDefinition definition) {
        bindings.clear();
        formPanel.removeAll();

        titleLabel.setText(definition.getFormTitle() != null ? "Dynamisches Formular" : definition.getFormTitle());

        if (definition.getFields() != null) {
            for (FormField field : definition.getFields()) {
                JPanel row = new JPanel(new BorderLayout(8, 8));
                row.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));

                JLabel label new JLabel(field.getLabel() + (field.isRequired() ? "*" : ""));
                label.setPreferredSize(new Dimension(240,28));

                FieldComponentBinding binding = componentFactory.create(field);
                bindings.add(binding);

                row.add(label, BorderLayout.WEST);
                row.add(binding.getComponent(), BorderLayout.CENTER);

                formPanel.add(row);
            }
        }
        formPanel.revalidate();
        formPanel.repaint();
    }

    private void saveResult() {
        if (currentDefinition == null) {
            error("Bitte zuerst eine Formular-Definition laden.");
            return;
        }

        try {
            Map<String, Object> values = new LinkedHashMap<>();

            for (FieldComponentBinding binding : bindings) {
                Object raw = binding.getRawValue();
                String message = validator.validate(binding.getField(), raw);
                if (message != null) {
                    error(message);
                    return;
                }
                values.put(binding.getField().getLabel(), binding.getConvertedValue());
            }

            JFileChooser chooser = new JFileChooser();
            if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
                return;

            Path path = chooser.getSelectedFile().toPath();
            resultJsonService.saveResult(path, currentDefinition.getFormTitle(), values);
            JOptionPane.showMessageDialog(this, "Ergebnis gespeichert.");
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }

    private void loadResult() {
        if (currentDefinition == null) {
            error("Bitte zuerst eine Formular-Definition laden.");
            return;
        }

        try {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
                return;

            Map<String, Object> values = resultJsonService.loadResult(chooser.getSelectedFile().toPath());
            for (FieldComponentBinding binding : bindings) {
                binding.applyLoadedValue(values);
            }

            JOptionPane.showMessageDialog(this, "Ergebnis geladen.");
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Fehler", JOptionPane.ERROR_MESSAGE);
    }
}
