/*
 * Copyright (c) 2019 by gepardec GmbH.
 * Autor: Philipp Wurm <philipp.wurm@gepardec.com>
 * Create date: 02.05.19 19:16
 */

package connector.common;

import com.google.common.collect.ImmutableMap;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;

public class GlobalTypes {

    public static final ImmutableMap<String, String> CORS_HEADER_MAP = ImmutableMap.<String, String>builder()
            .put("Access-Control-Allow-Origin", "*")
            .put("Access-Control-Allow-Methods", "OPTIONS, HEAD, GET, POST, PUT, DELETE")
            .put("Access-Control-Allow-Credentials", "true")
            .put("Access-Control-Allow-Headers", "x-requested-with, x-auth-token, content-length, origin, content-type, accept, authorization").build();
}
