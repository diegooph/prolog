package br.com.prolog.prologtest.util;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DataUtil {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static String formatLocalDate(LocalDate localDate) {
        return simpleDateFormat.format(Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static String showDuration(LocalDateTime start, LocalDateTime end) {

        Duration diff = Duration.between(start, end);
        String hms = String.format("%d:%02d:%02d",
                diff.toHours(),
                diff.toMinutesPart(),
                diff.toSecondsPart());
        return hms;
    }
}
