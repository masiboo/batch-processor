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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class FileControllerTest {
    @Mock
    private FileMonitorService fileMonitorService;

    @InjectMocks
    private FileController fileController;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRestartFileMonitoring(){
        String expected = "File monitoring restarted started successfully";

        doNothing().when(fileMonitorService).startMonitoring();
        fileMonitorService.startMonitoring();
        String actual = fileController.restartFileMonitoring();

        verify(fileMonitorService, times(2)).startMonitoring();
        assertSame(expected, actual);
    }

    @Test
    void testStopFileMonitoring(){
        doNothing().when(fileMonitorService).stopMonitoring();
        fileMonitorService.stopMonitoring();
        verify(fileMonitorService, times(1)).stopMonitoring();
    }

}