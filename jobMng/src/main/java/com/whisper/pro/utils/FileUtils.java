package com.whisper.pro.utils;

import com.whisper.pro.core.enums.GlobalResultEnum;
import com.whisper.pro.core.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {
    protected static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static String readFile(String path) {
        logger.debug("read {} file text....", path);
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(path));
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("read file error,", e);
            throw new BusinessException(GlobalResultEnum.FAILURE);
        }
    }

    public static void createAndWriteFile(String fileName, String content) {
        File file = new File(fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        } catch (IOException e) {
            logger.error("write file error,", e);
            throw new BusinessException(GlobalResultEnum.FAILURE);
        }
    }
}
