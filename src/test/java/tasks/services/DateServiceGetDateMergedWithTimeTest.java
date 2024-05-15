package tasks.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DateServiceGetDateMergedWithTimeTest {
    static Random random = new Random();
    // Helper method to generate random numbers within a range
    private int getRandomNumberInRange(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    private Date fromLocalDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    @RepeatedTest(10)
    @Tag("happy-flow")
    public void happyFlow() {
        Integer hour = getRandomNumberInRange(0, 23);
        Integer minute = getRandomNumberInRange(0, 59);
        int year = getRandomNumberInRange(1970, 9999);
        int month = getRandomNumberInRange(1, 12);
        int day = getRandomNumberInRange(1, 28);

        String time = String.format("%02d:%02d", hour, minute);
        LocalDate localDate = LocalDate.of(year, month, day);
        Date date = fromLocalDate(localDate);
        Date resultDate = DateService.getDateMergedWithTime(time, date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String expectedDate = dateFormat.format(date).substring(0, 11) + time;
        assertEquals(expectedDate, dateFormat.format(resultDate).substring(0, 16));
    }

    @Test
    @Tag("invalid-param")
    public void splitterNotFount() {
        String time = "23-55";
        Date date = Date.from(Instant.now());
        assertThrows(RuntimeException.class, () -> {
            DateService.getDateMergedWithTime(time, date);
        });
    }

    @Test
    @Tag("invalid-param")
    public void timeContainsLetters() {
        String time = "23:5a";
        Date date = Date.from(Instant.now());
        assertThrows(NumberFormatException.class, () -> {
            DateService.getDateMergedWithTime(time, date);
        });
    }

    @Test
    @Tag("invalid-param")
    public void timeUnitExceedsBounds() {
        String time = "24:00";
        Date date = fromLocalDate(LocalDate.of(1970, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> {
            DateService.getDateMergedWithTime(time, date);
        });
    }

    @Test
    @Tag("invalid-param")
    public void timeUnitExceedsBounds2() {
        String time = "23:60";
        Date date = fromLocalDate(LocalDate.of(1970, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> {
            DateService.getDateMergedWithTime(time, date);
        });
    }

    @ParameterizedTest
    @CsvSource({
        "00:00, 1970-01-01 00:00:00, 1970-01-01 00:00:00",
        "23:59, 1970-01-01 00:00:00, 1970-01-01 23:59:00",
        "00:00, 1970-01-02 00:00:00, 1970-01-02 00:00:00",
        "23:59, 1970-01-02 00:00:00, 1970-01-02 23:59:00",
        "00:00, 9999-12-30 00:00:00, 9999-12-30 00:00:00",
        "23:59, 9999-12-30 00:00:00, 9999-12-30 23:59:00",
        "00:00, 9999-12-31 00:00:00, 9999-12-31 00:00:00",
        "23:59, 9999-12-31 00:00:00, 9999-12-31 23:59:00",
    })
    @DisplayName("Positive test cases")
    @Tag("happy-flow")
    public void edgeCases(String time, @ConvertWith(DateConverter.class) Date noTimeDate, @ConvertWith(DateConverter.class) Date expectedDate) {
        Date resultDate = DateService.getDateMergedWithTime(time, noTimeDate);
        assertEquals(expectedDate, resultDate);
    }

    public static class DateConverter extends org.junit.jupiter.params.converter.SimpleArgumentConverter {

        @Override
        protected Object convert(Object source, Class<?> targetType) {
            if (targetType == Date.class) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    return dateFormat.parse((String) source);
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Failed to parse date: " + source, e);
                }
            } else {
                throw new IllegalArgumentException("Conversion not supported for " + targetType);
            }
        }
    }
}
