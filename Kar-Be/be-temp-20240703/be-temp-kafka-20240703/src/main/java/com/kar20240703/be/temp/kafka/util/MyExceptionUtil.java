package com.kar20240703.be.temp.kafka.util;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

@Slf4j
public class MyExceptionUtil {

    /**
     * 打印异常日志
     */
    public static void printError(Throwable e) {

        printError(e, null);

    }

    /**
     * 打印异常日志
     */
    public static void printError(Throwable e, @Nullable Long userId) {

        log.error("异常日志打印，userId：{}", userId == null ? -1 : userId, e);

    }

}
