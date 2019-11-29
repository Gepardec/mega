package com.gepardec.mega.zep.service;

import de.provantis.zep.ResponseHeaderType;
import org.apache.http.HttpStatus;

public class ZepStatusCodeMapper {

    private ZepStatusCodeMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Integer toHttpResponseCode(ResponseHeaderType zepResponseHeaderType) {
        if (zepResponseHeaderType != null && Integer.parseInt(zepResponseHeaderType.getReturnCode()) == 0) {
            return HttpStatus.SC_OK;
        } else {
            return HttpStatus.SC_INTERNAL_SERVER_ERROR;
        }
    }
}
