package validation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import model.FormField;

public interface FieldValidator {
    String validate(FormField field, Object value);
    // Standard-Implementierung. Kann bei Bedarf durch spezifischere Validatoren ersetzt werden.
    class DefaultFieldValidator implements FieldValidator {
        @Override
        public String validate(FormField field, Object value) {
            if (field.isRequired()) {
                if (value == null) {
                    return field.getLabel() + " ist ein Pflichtfeld.";
                }
                if (value instanceof String s && s.trim().isEmpty()) {
                    return field.getLabel() + " ist ein Pflichtfeld.";
                }
            }

            if (value == null) {
                return null;
            }

            String type = field.getDataType() == null ? "string" : field.getDataType().toLowerCase();

            try {
                return switch (type) {
                    case "string" -> null;
                    case "int" -> {
                        Integer.parseInt(String.valueOf(value).trim());
                        yield null;
                    }
                    case "double" -> {
                        Double.parseDouble(String.valueOf(value).trim());
                        yield null;
                    }
                    case "boolean" -> {
                        String v = String.valueOf(value).trim().toLowerCase();
                        yield (v.equals("true") || v.equals("false")) ? null
                                : field.getLabel() + " muss true oder false sein.";
                    }
                    case "date" -> {
                        LocalDate.parse(String.valueOf(value).trim());
                        yield null;
                    }
                    case "enum" -> {
                        yield field.getOptions() != null && field.getOptions().contains(String.valueOf(value))
                                ? null
                                : field.getLabel() + " enthält einen ungültigen Wert.";
                    }
                    default -> null;
                };
            } catch (NumberFormatException e) {
                return field.getLabel() + " hat einen ungültigen Zahlenwert.";
            } catch (DateTimeParseException e) {
                return field.getLabel() + " hat ein ungültiges Datum. Format: YYYY-MM-DD.";
            }
        }
    }
}
