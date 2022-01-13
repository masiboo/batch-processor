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
package com.finago.interview.task.controller;

import com.finago.interview.task.service.FileEventNotificationService;
import com.finago.interview.task.service.FileMonitorService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@AllArgsConstructor
@RestController
@RequestMapping("/xml")
public class FileController {

    private final FileMonitorService fileMonitorService;

    private final FileEventNotificationService fileEventNotificationService;


    @SneakyThrows
        @GetMapping("/restart")
        public String restartFileMonitoring(){
            fileMonitorService.startMonitoring();
            return "File monitoring restarted started successfully";
        }

    @GetMapping("/stop")
    public String stopFileMonitoring(){
       fileMonitorService.stopMonitoring();
       return "File monitoring stop successfully";
    }

    @GetMapping("/notification")
    public ResponseEntity<SseEmitter> doNotify() throws InterruptedException, IOException {
        final SseEmitter emitter = new SseEmitter();
        fileEventNotificationService.addEmitter(emitter);
        fileEventNotificationService.doNotify();
        emitter.onCompletion(() -> fileEventNotificationService.removeEmitter(emitter));
        emitter.onTimeout(() -> fileEventNotificationService.removeEmitter(emitter));
        return new ResponseEntity<>(emitter, HttpStatus.OK);
    }

    @GetMapping("/log/{msg}")
    public String getMonitoringLogs(@PathVariable("msg")String msg){
        return msg;
    }
}
