package com.gepardec.mega.domain.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static java.lang.String.format;

@ApplicationScoped
public class FileHelper {

    @Inject
    Logger logger;

    public String readTextOfPath(String pathToRead) {
        String text = null;
        if (StringUtils.isBlank(pathToRead)) {
            logger.error("pathToRead is empty");
            return null;
        }
        try {
            final ClassLoader classLoader = FileHelper.class.getClassLoader();
            final InputStream is = classLoader.getResourceAsStream(pathToRead);
            if (is != null) {
                text = String.join(System.lineSeparator(), IOUtils.readLines(is, StandardCharsets.UTF_8));
            } else {
                logger.error(format("File with path %s not found", pathToRead));
            }
        } catch (IOException e) {
            logger.error(format("Error reading message Text from File %s", pathToRead));
        }
        return text;
    }
}
