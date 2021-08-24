package com.finago.interview.task.config;

import com.finago.interview.task.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.*;

@Slf4j
@Configuration
public class FileMonitorConfig {

    @Bean
    public WatchService watchService() {
        WatchService watchService = null;
        try {
            log.info("MONITORING_DIR: {}", FileUtils.getFileAbsolutePath(FileUtils.DATA_PATH_IN));
            watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(FileUtils.getFileAbsolutePath(FileUtils.DATA_PATH_IN));

            if (!Files.isDirectory(path)) {
                throw new RuntimeException("incorrect monitoring folder: " + path);
            }
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        } catch (IOException e) {
            log.error("exception for watch service creation:", e);
        }
        return watchService;
    }
}
