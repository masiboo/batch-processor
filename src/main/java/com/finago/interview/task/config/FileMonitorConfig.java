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
package com.finago.interview.task.config;

import com.finago.interview.task.properties.AppProperties;
import com.finago.interview.task.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.*;

@Slf4j
@Configuration
public class FileMonitorConfig {

    @Autowired
    private AppProperties appProperties;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    @Bean
    public WatchService watchService() {
        WatchService watchService = null;
        try {
            Path path = Paths.get(FileUtils.getFileAbsolutePath(appProperties.getDataIn()));
            System.out.println("File to monitor: "+path.toString());
            //log.info("File to monitor: "+path.toString());
            log.info("MONITORING_DIR: {}", FileUtils.getFileAbsolutePath(appProperties.getDataIn()));
            watchService = FileSystems.getDefault().newWatchService();
          // Path path = Paths.get(FileUtils.getFileAbsolutePath(appProperties.getDataIn()));


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
