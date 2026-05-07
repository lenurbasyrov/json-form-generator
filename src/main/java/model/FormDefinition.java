public package main.java.model;

class FormDefinition {
    private String formTitle;
    private List<FormField> fields;

    public String getFormTitle() {
        return formTitle;
    }
    public void setFormTitle(String formTitle) {
        this.formTitle = formTitle;
    }

    public List<FormField> getFields() {
        return fields;
    }
    public void setFields(List<FormField> fields) {
        this.fields = fields;
    }
}