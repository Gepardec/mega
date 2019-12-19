package com.gepardec.mega.utils;

import com.gepardec.mega.communication.exception.FileReadException;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class FileHelper {

    @Inject
    Logger logger;

    public String readTextOfPath(String pathToRead) {
        String text = null;
        try {
            final Path path = Paths.get(Objects.requireNonNull(FileHelper.class.getClassLoader().getResource(pathToRead)).toURI());
            try (Stream<String> lines = Files.lines(path)) {
                text = lines.collect(Collectors.joining(System.lineSeparator()));
            }
        } catch (URISyntaxException | IOException e) {
            String msg = String.format("Error reading message Text from File %s", pathToRead);
            logger.error(msg);
            throw new FileReadException(msg, e);
        }
        return text;
    }
}
