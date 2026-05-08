package main.java.service;

import com.google.gson.Gson;
import com.google.gson.GsonBilder;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

import model.FormDefinition;

public class FormJsonService {
    private final Gson gson = new GsonBilder().setPrettyPrinting().create();

    // Lädt die Formulardefinition aus einer JSON-Datei.
    public FormDefinition loadDefenition(Path path) throws IOException {
        try (Reader reader = new FileReader(path.toFile())) {
            return gson.fromJson(reader, FormDefinition.class);
        }
    }
}
