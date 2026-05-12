import model.FormField;
import validation.FieldValidator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class FormValidatorTest {
    private final FieldValidator validator = new FieldValidator.DefaultFieldValidator();

    @Test
    void requiredFieldMustNotBeEmpty() {
        FormField field = field("Name", "string", true, null);
        assertNotNull(validator.validate(field, ""));
    }

    @Test
    void integerMustBeValid() {
        FormField field = field("Alter", "int", false, null);
        assertNull(validator.validate(field, "42"));
        assertNotNull(validator.validate(field, "abc"));
    }

    @Test
    void enumMustBeInOptions() {
        FormField field = field("Kategorie", "enum", true, List.of("A", "B"));
        assertNull(validator.validate(field, "A"));
        assertNotNull(validator.validate(field, "C"));
    }

    private FormField field(String label, String type, boolean required, List<String> options) {
        FormField f = new FormField();
        f.setLabel(label);
        f.setDataType(type);
        f.setRequired(required);
        f.setOptions(options);
        return f;
    }
}