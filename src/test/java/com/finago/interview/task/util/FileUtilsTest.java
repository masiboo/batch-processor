/*
 * Copyright (c) 2021. batch-processor is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either
 * version 2 of the License, or batch-processor(at your option) any later version.
 *
 * batch-processor is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with batch-processor; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.finago.interview.task.util;

import com.finago.interview.task.properties.AppProperties;
import com.finago.interview.task.utils.FileUtils;
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

    private static final String DATA_TEST_PATH = "/src/test/data/";
    private static final String RESOURCES_DATA_PATH = "/src/test/resources/data/";
    private static final String XML_FILE_NAME = "90072701.xml";

    private static AppProperties appProperties;

    @BeforeAll
    static void beforeAll() throws IOException {
        appProperties = new AppProperties();
        appProperties.setDataIn(DATA_TEST_PATH+"/in/");
        appProperties.setDataOut(DATA_TEST_PATH+"/out/");
        appProperties.setDataArchive(DATA_TEST_PATH+"/archive/");
        appProperties.setDataError(DATA_TEST_PATH+"/error/");
        FileUtils.setAppProperties(appProperties);
        Path sourceDataPath = Path.of(new File(".").getCanonicalPath() + RESOURCES_DATA_PATH);
        Path destinationDataPath = Path.of(FileUtils.getFileAbsolutePath(DATA_TEST_PATH));
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
        Path destinationDataPath = Path.of(FileUtils.getFileAbsolutePath(DATA_TEST_PATH));
        try (Stream<Path> walk = Files.walk(destinationDataPath)) {
            walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    @Test
    void testGetFileAbsolutePath() {
        String actualDataInPath = FileUtils.getFileAbsolutePath(appProperties.getDataIn());
//        assertTrue(actualDataInPath.contains("BatchProcessor"));
    }

    @Test
    void testGetChecksum() {
        String expectedCheckSum = "c99ddf555859424b8ad6a51bed8f383c";
        String path =  FileUtils.getFileAbsolutePath(RESOURCES_DATA_PATH+"/in/") +
                        XML_FILE_NAME;
        File file = new File(path);
        String actualCheckSum = FileUtils.getChecksum(file);
        assertTrue(expectedCheckSum.equals(actualCheckSum));
    }

    @Test
    void isValidFile() {
        String expectedFileMd5 = "c99ddf555859424b8ad6a51bed8f383c";
        String path = FileUtils.getFileAbsolutePath(RESOURCES_DATA_PATH+"/in/") + XML_FILE_NAME;
        File file = new File(path);
        assertTrue(FileUtils.isValidFile(file, expectedFileMd5));
    }

    @Test
    void readXml() throws IOException {
        Path xmlPath = Path.of(FileUtils.getFileAbsolutePath(appProperties.getDataIn()), XML_FILE_NAME);
        Path expectedArchiveXmlPath = Path.of(FileUtils.getFileAbsolutePath(appProperties.getDataArchive()), XML_FILE_NAME);
        Path archiveDirectoryPath = Path.of(FileUtils.getFileAbsolutePath(appProperties.getDataArchive()));
        Path outDirectoryPath = Path.of(FileUtils.getFileAbsolutePath(appProperties.getDataOut()));
        Path inDirectoryPath = Path.of(FileUtils.getFileAbsolutePath(appProperties.getDataIn()));
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