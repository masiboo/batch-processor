package com.finago.interview.task.service;

import com.finago.interview.task.util.FileUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.*;

@Slf4j
@Service
@AllArgsConstructor
public class FileMonitorService {

    private final WatchService watchService;

    @PostConstruct
    @Async
    public void startMonitoring() {
        FileUtils.readFilesForDirectory();
        log.info("START_MONITORING");
        try {
            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    log.info("Event kind: {}; File affected: {}", event.kind(), event.context());
                   if((event.kind() == StandardWatchEventKinds.ENTRY_CREATE ||
                      event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) &&
                      event.context().toString().contains(".xml")){
                       try {
                           FileUtils.readXml(Path.of(FileUtils.getFileAbsolutePath(FileUtils.DATA_PATH_IN),
                                   event.context().toString()));
                       }catch (Exception e){
                           log.error("startMonitoring Exception: "+e.getMessage());
                       }
                   }
                }
                key.reset();
            }
        } catch (InterruptedException e) {
            log.warn("startMonitoring: interrupted exception for monitoring service: "+e.getMessage());
        }
    }

    @PreDestroy
    public void stopMonitoring() {
        log.info("STOP_MONITORING");

        if (watchService != null) {
            try {
                watchService.close();
            } catch (IOException e) {
                log.error("stopMonitoring: "+e.getMessage());
            }
        }
    }
}
