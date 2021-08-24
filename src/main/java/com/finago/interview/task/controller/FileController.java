package com.finago.interview.task.controller;

import com.finago.interview.task.service.FileMonitorService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/xml")
public class FileController {

    @Autowired
    FileMonitorService fileMonitorService;

    @SneakyThrows
    @GetMapping("/restart")
    public String restartFileMonitoring(){
        fileMonitorService.startMonitoring();
        return "File monitoring restarted started successfully";
    }

    @GetMapping("/stop")
    public void stopFileMonitoring(){
       fileMonitorService.stopMonitoring();
    }
}
