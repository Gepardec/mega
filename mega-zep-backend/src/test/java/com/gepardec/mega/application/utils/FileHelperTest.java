package com.gepardec.mega.application.utils;

import com.gepardec.mega.aplication.utils.FileHelper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class FileHelperTest {

    @InjectMocks
    private FileHelper fileHelper;

    @Mock
    Logger logger;

    @Test
    void readTextOfPath_correctPath_returnsContent() {
        assertTrue(StringUtils.isNoneBlank(fileHelper.readTextOfPath("notifications/5_plProjectControlling.html")));
    }

    @Test
    void readTextOfPath_incorrectPath_returnsNull() {
        assertNull(fileHelper.readTextOfPath("notificatictControlling.html"));
    }

    @Test
    void readTextOfPath_nullPath_returnsNull() {
        assertNull(fileHelper.readTextOfPath("null"));
    }

    @Test
    void readTextOfPath_BlankPath_returnsNull() {
        assertNull(fileHelper.readTextOfPath(" "));
    }

    @Test
    void readTextOfPath_EmptyPath_returnsNull() {
        assertNull(fileHelper.readTextOfPath(""));
    }
}