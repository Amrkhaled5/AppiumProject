package utils.datamanager;

import org.apache.commons.io.FileUtils;
import logs.LogsManager;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;

public class PropertyReader {

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try {
            String propertiesFolderPath = System.getProperty("user.dir") + File.separator +
                    "src" + File.separator + "main" + File.separator + "resources";
            File dir = new File(propertiesFolderPath);

            if (!dir.exists()) {
                LogsManager.warn("Properties directory does not exist at: " + propertiesFolderPath);
                return;
            }

            Collection<File> propertiesFiles = FileUtils.listFiles(dir, new String[]{"properties"}, true);
            Properties localProperties = new Properties();

            propertiesFiles.forEach(file -> {
                try (InputStream is = FileUtils.openInputStream(file)) {
                    localProperties.load(is);
                    LogsManager.info("Successfully loaded property file: " + file.getName());
                } catch (Exception e) {
                    LogsManager.error("Failed to load properties from file: " + file.getName() + " | Error: " + e.getMessage());
                }
            });
            for (String key : localProperties.stringPropertyNames()) {
                if (System.getProperty(key) == null) {
                    System.setProperty(key, localProperties.getProperty(key));
                }
            }
        } catch (Exception e) {
            LogsManager.fatal("Failed to initialize properties. Error: " + e.getMessage());
        }
    }

    public static String getProperty(String key) {
        String value = System.getProperty(key);
        if (value == null) {
            LogsManager.warn("Property key '" + key + "' was requested but not found in any file or system property.");
        }
        return value;
    }
}