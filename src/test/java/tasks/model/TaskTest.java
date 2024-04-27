package tasks.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Task task;

    @BeforeEach
    void setUp() {
        try {
            task=new Task("new task",Task.getDateFormat().parse("2023-02-12 10:10"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testTaskCreation() throws ParseException {
       assert Objects.equals(task.getTitle(), "new task");
        System.out.println(task.getFormattedDateStart());
        System.out.println(Task.getDateFormat().format(Task.getDateFormat().parse("2023-02-12 10:10")));
       assert task.getFormattedDateStart().equals(Task.getDateFormat().format(Task.getDateFormat().parse("2023-02-12 10:10")));
    }

    @AfterEach
    void tearDown() {
    }

    private static Stream<Arguments> testNextTimeAfter0DataProvider() {
        return Stream.of(
                Arguments.of(
                        new GregorianCalendar(2002, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2002, Calendar.JANUARY, 2).getTime()
                ),
                Arguments.of(
                        new GregorianCalendar(2002, Calendar.FEBRUARY, 1).getTime(),
                        new GregorianCalendar(2002, Calendar.FEBRUARY, 2).getTime()
                ),
                Arguments.of(
                        new GregorianCalendar(2002, Calendar.FEBRUARY, 28).getTime(),
                        new GregorianCalendar(2002, Calendar.MARCH, 1).getTime()
                ),
                Arguments.of(
                        new GregorianCalendar(2004, Calendar.FEBRUARY, 28).getTime(),
                        new GregorianCalendar(2004, Calendar.FEBRUARY, 29).getTime()
                ),
                Arguments.of(
                        new GregorianCalendar(2100, Calendar.FEBRUARY, 28).getTime(),
                        new GregorianCalendar(2100, Calendar.MARCH, 1).getTime()
                ),
                Arguments.of(
                        new GregorianCalendar(2400, Calendar.JANUARY, 29).getTime(),
                        new GregorianCalendar(2400, Calendar.JANUARY, 1).getTime()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("testNextTimeAfter0DataProvider")
    void testNextTimeAfter0(Date currentDate, Date correctNextDate) {
        Date toVerifyNextDate = task.nextTimeAfter(currentDate);

        assertEquals(correctNextDate, toVerifyNextDate);
    }

    @Test
    void testNextTimeAfter1() {
        try {
            //noinspection DataFlowIssue
            task.nextTimeAfter(null);
            Assertions.fail("call did not throw NPE");
        } catch (NullPointerException ignored) {
        }
    }

    @Test
    void testNextTimeAfter2() {
        Date currentDate = new GregorianCalendar(2002, Calendar.JANUARY, 1).getTime();
        Date correctNextDate = new GregorianCalendar(2002, Calendar.JANUARY, 2).getTime();
        Date toVerifyNextDate = task.nextTimeAfter(currentDate);

        assertEquals(correctNextDate, toVerifyNextDate);
    }
}