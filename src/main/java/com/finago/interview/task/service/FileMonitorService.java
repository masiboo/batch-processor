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
package com.finago.interview.task.service;

import com.finago.interview.task.properties.AppProperties;
import com.finago.interview.task.utils.FileUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;

@Slf4j
@Service
@AllArgsConstructor
public class FileMonitorService {

    private final AppProperties appProperties;

    private final WatchService watchService;

    private final RestTemplate restTemplate;

    private final FileEventNotificationService fileEventNotificationService;

    //@PostConstruct
    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void startMonitoring() {
        FileUtils.setAppProperties(appProperties);
        FileUtils.setFileEventNotificationService(fileEventNotificationService);
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
                           restTemplateRequest(event.context().toString()+" processing");
                           fileEventNotificationService.setEventNotification(event.context().toString()+" processing");
                           fileEventNotificationService.doNotify();
                           FileUtils.readXml(Path.of(FileUtils.getFileAbsolutePath(appProperties.getDataIn()),
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

    private void restTemplateRequest(String msg) throws URISyntaxException {
        String baseUrl = "http://localhost:8080/xml/log/"+msg;
        URI uri = new URI(baseUrl);
        restTemplate.getForEntity(uri, String.class  );
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
