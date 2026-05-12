import service.ResultJsonService;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResultJsonServiceTest {
    @Test
    void saveAndLoadResult() throws Exception {
        ResultJsonService service = new ResultJsonService();
        Path tmp = Files.createTempFile("result", ".json");

        Map<String, Object> values = new LinkedHashMap<>();
        values.put("Name", "Max");
        values.put("Alter", 25);

        service.saveResult(tmp, "Testform", values);
        Map<String, Object> loaded = service.loadResult(tmp);

        assertEquals("Max", loaded.get("Name"));
        assertEquals(25.0, loaded.get("Alter"));
    }
}