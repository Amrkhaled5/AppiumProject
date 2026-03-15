package utils.allureReporting;

import org.jsoup.Jsoup;
import logs.LogsManager;
import utils.datamanager.PropertyReader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class AllureReportGenerator {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().contains("win");

    public static void generateAndOpenReport() {
        try {
            String version = getLatestAllureVersion();
            Path executable = downloadAndExtractBinary(version);

            if (executable != null && Files.exists(executable)) {
                LogsManager.info("Generating Allure Report...");

                // --- CRITICAL FIX: The Exact Order Allure Requires! ---
                ProcessBuilder generatePb = new ProcessBuilder(
                        executable.toString(),
                        "generate",
                        "--clean",
                        "--single-file",
                        "-o",
                        AllureConstants.FULL_REPORTS_PATH.toString(),
                        AllureConstants.RESULTS_FOLDER.toString()
                );

                generatePb.inheritIO().start().waitFor();
                LogsManager.info("Allure Report Generated Successfully.");

                String executionType = utils.datamanager.PropertyReader.getProperty("executionType");
                if (executionType != null && executionType.equalsIgnoreCase("local")) {
                    LogsManager.info("Opening standalone HTML report...");
                    openReport();
                } else {
                    LogsManager.info("CI/CD Execution detected. Skipping local report auto-open.");
                }
            }
        } catch (Exception e) {
            LogsManager.error("Failed to generate Allure report: " + e.getMessage());
        }
    }

    private static String getLatestAllureVersion() throws Exception {
        String url = Jsoup.connect("https://github.com/allure-framework/allure2/releases/latest")
                .followRedirects(true).execute().url().toString();
        return url.split("/tag/")[1];
    }

    private static Path downloadAndExtractBinary(String version) throws Exception {
        Path extractionDir = Paths.get(AllureConstants.EXTRACTION_DIR.toString(), "allure-" + version);
        Path binaryPath = Paths.get(extractionDir.toString(), "bin", IS_WINDOWS ? "allure.bat" : "allure");

        if (Files.exists(binaryPath)) {
            return binaryPath;
        }

        LogsManager.info("Downloading Allure version: " + version);
        Files.createDirectories(AllureConstants.EXTRACTION_DIR);
        String downloadUrl = AllureConstants.ALLURE_ZIP_BASE_URL + version + "/allure-commandline-" + version + ".zip";
        Path zipPath = Paths.get(AllureConstants.EXTRACTION_DIR.toString(), "allure-" + version + ".zip");

        try (BufferedInputStream in = new BufferedInputStream(new URI(downloadUrl).toURL().openStream());
             OutputStream out = Files.newOutputStream(zipPath)) {
            in.transferTo(out);
        }

        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipPath))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path newPath = Paths.get(AllureConstants.EXTRACTION_DIR.toString(), entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(newPath);
                } else {
                    Files.createDirectories(newPath.getParent());
                    Files.copy(zis, newPath);
                }
            }
        }

        if (!IS_WINDOWS) {
            new ProcessBuilder("chmod", "+x", binaryPath.toString()).start().waitFor();
        }

        Files.deleteIfExists(zipPath);
        return binaryPath;
    }

    private static void openReport() throws Exception {
        Path reportIndex = Paths.get(AllureConstants.FULL_REPORTS_PATH.toString(), "index.html");
        if (IS_WINDOWS) {
            new ProcessBuilder("cmd.exe", "/c", "start", "\"\"", "\"" + reportIndex.toString() + "\"").start();
        } else {
            new ProcessBuilder("open", reportIndex.toString()).start();
        }
    }
}