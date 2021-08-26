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
