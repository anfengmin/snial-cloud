package com.snail.common.log.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 日志ID生成器
 *
 * 规则：
 * yyyyMMddHHmmssSSS + 2位序号
 * 示例：2026040716351299901
 *
 * @author Levi.
 * @since 1.0
 */
public final class LogIdGenerator {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    private static final ZoneId ZONE_ID = ZoneId.systemDefault();
    private static final int MAX_SEQUENCE = 99;

    private static long lastMillis = -1L;
    private static int sequence = 0;

    private LogIdGenerator() {
    }

    public static synchronized long nextId() {
        long currentMillis = System.currentTimeMillis();
        if (currentMillis == lastMillis) {
            sequence++;
            if (sequence > MAX_SEQUENCE) {
                currentMillis = waitNextMillis(currentMillis);
                sequence = 0;
            }
        } else {
            sequence = 0;
        }
        lastMillis = currentMillis;

        String timePart = LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(currentMillis), ZONE_ID)
                .format(DATE_TIME_FORMATTER);
        String seqPart = String.format("%02d", sequence);
        return Long.parseLong(timePart + seqPart);
    }

    private static long waitNextMillis(long millis) {
        long now = System.currentTimeMillis();
        while (now <= millis) {
            now = System.currentTimeMillis();
        }
        return now;
    }
}

