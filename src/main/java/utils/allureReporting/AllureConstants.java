package utils.allureReporting;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AllureConstants {
    public static final Path USER_DIR = Paths.get(System.getProperty("user.dir"));
    public static final Path USER_HOME = Paths.get(System.getProperty("user.home"));

    public static final Path RESULTS_FOLDER = Paths.get(USER_DIR.toString(), "allure-results");
    public static final Path REPORT_PATH = Paths.get(USER_DIR.toString(), "test-results", "reports");
    public static final Path FULL_REPORTS_PATH = Paths.get(USER_DIR.toString(), "test-results", "reports", "full-report");

    public static final String INDEX_HTML = "index.html";
    public static final String REPORT_PREFIX = "AllureReport_";
    public static final String REPORT_EXTENSION = ".html";

    public static final String ALLURE_ZIP_BASE_URL = "https://repo1.maven.org/maven2/io/qameta/allure/allure-commandline/";
    public static final Path EXTRACTION_DIR = Paths.get(USER_HOME.toString(), ".m2", "repository", "allure");
}