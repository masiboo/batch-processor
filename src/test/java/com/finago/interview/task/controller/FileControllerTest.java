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