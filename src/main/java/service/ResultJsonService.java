package main.java.service;

import java.time.LocalDate;
import java.util.Map;

public class ResultJsonService {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Speichert die übermittelten Werte in einer JSON-Datei. Die Struktur enthält
    // den Formular-Titel, das Übermittlungsdatum und die Werte.
    public void saveResult(Path path, String formTitle, Map<String, Object> values) throws IOException {
        Map<String, Object> root = new LinkedHashMap<>();
        root.put("formTitle", formTitle);
        root.put("submittedAt", LocalDate.now().toString());
        root.put("values", values);

        try (Writer writer = new FileWriter(path.toFile())) {
            gson.toJson(root, writer);
        }
    }

    // Lädt die übermittelten Werte aus einer JSON-Datei. Die Methode gibt eine Map
    // zurück, die die Werte enthält. Es wird sichergestellt, dass die Werte als
    // Map<String, Object> zurückgegeben werden, um die weitere Verarbeitung zu
    // erleichtern.
    @SuppressWarnings("unchecked")
    public Map<String, Object> loadResult(Path path) throws IOException {
        try (Reader reader = new FileReader(path.toFile())) {
            Map<String, Object> root = gson.fromJson(reader, Map.class);
            Object values = root.get("values");

            // Sicherstellen, dass die Werte als Map<String, Object> zurückgegeben werden.
            Map<String, Object> result = new LinkedHashMap<>();
            if (values instanceof Map<?, ?> map) {
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    result.put(String.valueOf(entry.getKey()), entry.getValue());
                }
            }
            return result;
        }
    }
}
