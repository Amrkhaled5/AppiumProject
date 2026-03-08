package utils.datamanager; // Standardized to lowercase

import com.jayway.jsonpath.JsonPath;
import logs.LogsManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;

public class JsonReader {

    private final String TEST_DATA_PATH = System.getProperty("user.dir") + File.separator +
            "src" + File.separator + "test" + File.separator + "resources" + File.separator + "testdata" + File.separator;

    private String jsonString;
    private String jsonFileName;

    public JsonReader(String jsonFileName) {
        this.jsonFileName = jsonFileName;
        try {
            JSONObject data = (JSONObject) new JSONParser().parse(new FileReader(TEST_DATA_PATH + jsonFileName));
            this.jsonString = data.toJSONString();
        } catch (Exception e) {
            LogsManager.error("Failed to read JSON file: " + jsonFileName + " | Error: " + e.getMessage());
            this.jsonString = "{}";
        }
    }

    public String getJsonData(String jsonPath) {
        try {
            return JsonPath.read(this.jsonString, jsonPath).toString();
        } catch (Exception e) {
            LogsManager.error("Failed to get JSON data for path: " + jsonPath + " | Error: " + e.getMessage());
            return null;
        }
    }
}