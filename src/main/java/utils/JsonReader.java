package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonReader {

    public static Object[][] getTestData(String JSON_PATH) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Map<String, String>> data = mapper.readValue(
                    new File(JSON_PATH),
                    new TypeReference<List<Map<String, String>>>() {}
            );

            Object[][] testData = new Object[data.size()][1];
            for (int i = 0; i < data.size(); i++) {
                testData[i][0] = data.get(i);
            }
            return testData;

        } catch (IOException e) {
            throw new RuntimeException("Error leyendo el archivo JSON: " + JSON_PATH, e);
        }
    }
}
