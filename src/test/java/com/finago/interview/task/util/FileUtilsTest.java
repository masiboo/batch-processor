package com.finago.interview.task.util;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FileUtilsTest {

    private static final String DATA_PATH = "/data/";
    private static final String RESOURCES_DATA_PATH = "/src/test/resources/data";
    private static final String XML_FILE_NAME = "90072701.xml";


    @BeforeAll
    static void beforeAll() throws IOException {
        Path sourceDataPath = Path.of(new File(".").getCanonicalPath() + RESOURCES_DATA_PATH);
        Path destinationDataPath = Path.of(FileUtils.getFileAbsolutePath(DATA_PATH));
        if (!Files.exists(destinationDataPath)) {
            Files.walk(sourceDataPath)
                    .forEach(sourcePath -> {
                        try {
                            Path targetPath = destinationDataPath.resolve(sourceDataPath.relativize(sourcePath));
                            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException ex) {
                            System.out.format("I/O error: %s%n", ex);
                        }
                    });
        }
    }

    @AfterAll
    static void afterAll() throws IOException {
        Path destinationDataPath = Path.of(FileUtils.getFileAbsolutePath(DATA_PATH));
        try (Stream<Path> walk = Files.walk(destinationDataPath)) {
            walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .peek(System.out::println)
                    .forEach(File::delete);
        }
    }

    @Test
    void testGetFileAbsolutePath() {
        String actualDataInPath = FileUtils.getFileAbsolutePath(FileUtils.DATA_PATH_IN);

        assertTrue(actualDataInPath.contains("/BatchProcessor/data/in"));
    }

    @Test
    void testGetChecksum() throws IOException {
        String expectedCheckSum = "3ec56d15c9366a5045f5de688867f74a";
        String path =  FileUtils.getFileAbsolutePath(RESOURCES_DATA_PATH+"/in/") +
                        XML_FILE_NAME;
        File file = new File(path);
        String actualCheckSum = FileUtils.getChecksum(file);
        assertTrue(expectedCheckSum.equals(actualCheckSum));
    }

    @Test
    void isValidFile() throws IOException {
        String expectedFileMd5 = "3ec56d15c9366a5045f5de688867f74a";
        String path = new File(".").getCanonicalPath() + FileUtils.getFileAbsolutePath(RESOURCES_DATA_PATH) +
                        XML_FILE_NAME;
        File file = new File(path);

        assertTrue(FileUtils.isValidFile(file, expectedFileMd5));
    }

    @Test
    void readXml() throws IOException {
        Path xmlPath = Path.of(FileUtils.getFileAbsolutePath(FileUtils.DATA_PATH_IN), XML_FILE_NAME);
        Path expectedArchiveXmlPath = Path.of(FileUtils.getFileAbsolutePath(FileUtils.DATA_PATH_ARCHIVE), XML_FILE_NAME);
        Path archiveDirectoryPath = Path.of(FileUtils.getFileAbsolutePath(FileUtils.DATA_PATH_ARCHIVE));
        Path outDirectoryPath = Path.of(FileUtils.getFileAbsolutePath(FileUtils.DATA_PATH_OUT));
        Path inDirectoryPath = Path.of(FileUtils.getFileAbsolutePath(FileUtils.DATA_PATH_IN));
        var archiveDirectory = Files.list(archiveDirectoryPath);
        var outDirectory = Files.list(outDirectoryPath);
        var inDirectory = Files.list(inDirectoryPath);

        FileUtils.readXml(xmlPath);

        assertTrue(archiveDirectory.count() != 0);
        assertTrue(outDirectory.count() != 0);
        assertTrue(inDirectory.count() == 0);
        assertTrue(Files.exists(expectedArchiveXmlPath));
    }

}