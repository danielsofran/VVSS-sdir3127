package tasks.model;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Task task;
    private Task taskRepeated4TestNextTimeAfter;
    private Task taskNotRepeated4TestNextTimeAfter;

    @BeforeEach
    void setUp() {
        try {
            task=new Task("new task",Task.getDateFormat().parse("2023-02-12 10:10"));
            taskNotRepeated4TestNextTimeAfter = new Task("a task",
                    Task.getDateFormat().parse("2023-02-12 10:00"));
            taskRepeated4TestNextTimeAfter = new Task("a task",
                    Task.getDateFormat().parse("2023-02-12 10:00"),
                    Task.getDateFormat().parse("2023-02-22 10:00"),
                    12);
            taskRepeated4TestNextTimeAfter.setActive(true);
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

    @Test
    public void testNextTimeAfter0() {
        try {
            Date nextTime = taskRepeated4TestNextTimeAfter.nextTimeAfter(null);
            Assertions.fail("NullPointerException not thrown");
        } catch (NullPointerException e) {
            assert true;
        }
    }

    @Test
    public void testNextTimeAfter1() {
        try {
            Date testDate = Task.getDateFormat().parse("2023-02-22 10:00");
            Date nextTime = taskRepeated4TestNextTimeAfter.nextTimeAfter(testDate);
            Assertions.assertNull(nextTime);
        } catch (ParseException e) {
            Assertions.fail("ParseException");
        }
    }

    @Test
    public void testNextTimeAfter2() {
        try {
            taskRepeated4TestNextTimeAfter.setActive(false);

            Date testDate = Task.getDateFormat().parse("2023-02-22 09:00");
            Date nextTime = taskRepeated4TestNextTimeAfter.nextTimeAfter(testDate);
            Assertions.assertNull(nextTime);
        } catch (ParseException e) {
            Assertions.fail("ParseException");
        } finally {
            taskRepeated4TestNextTimeAfter.setActive(true);
        }
    }

    @Test
    public void testNextTimeAfter3() {
        try {
            taskNotRepeated4TestNextTimeAfter.setActive(true);

            Date testDate = Task.getDateFormat().parse("2023-02-12 09:00");
            Date nextTime = taskNotRepeated4TestNextTimeAfter.nextTimeAfter(testDate);
            Assertions.assertEquals("Sun Feb 12 10:00:00 EET 2023", nextTime.toString());
        } catch (ParseException e) {
            Assertions.fail("ParseException");
        } finally {
            taskNotRepeated4TestNextTimeAfter.setActive(false);
        }
    }

    @Test
    public void testNextTimeAfter4() {
        try {
            Date testDate = Task.getDateFormat().parse("2023-02-12 09:00");
            Date nextTime = taskRepeated4TestNextTimeAfter.nextTimeAfter(testDate);
            Assertions.assertEquals("Sun Feb 12 10:00:00 EET 2023", nextTime.toString());
        } catch (ParseException e) {
            Assertions.fail("ParseException");
        }
    }

    @Test
    public void testNextTimeAfter5() {
        try {
            Date testDate = Task.getDateFormat().parse("2023-02-12 10:00:12");
            Date nextTime = taskRepeated4TestNextTimeAfter.nextTimeAfter(testDate);
            Assertions.assertEquals("Sun Feb 12 10:00:12 EET 2023", nextTime.toString());
        } catch (ParseException e) {
            Assertions.fail("ParseException");
        }
    }

    @Test
    public void testNextTimeAfter6() {
        try {
            Date testDate = Task.getDateFormat().parse("2023-02-12 10:01:30");
            Date nextTime = taskRepeated4TestNextTimeAfter.nextTimeAfter(testDate);
            Assertions.assertEquals("Sun Feb 12 10:01:12 EET 2023", nextTime.toString());
        } catch (ParseException e) {
            Assertions.fail("ParseException");
        }
    }

    @Test
    public void testNextTimeAfter7() {
        try {
            Date testDate = Task.getDateFormat().parse("2023-02-22 09:48");
            Date nextTime = taskRepeated4TestNextTimeAfter.nextTimeAfter(testDate);
            Assertions.assertEquals("Wed Feb 22 09:48:12 EET 2023", nextTime.toString());
        } catch (ParseException e) {
            Assertions.fail("ParseException");
        }
    }

    @Test
    public void testNextTimeAfter8() {
        try {
            Date testDate = Task.getDateFormat().parse("2023-02-22 09:59");
            Date nextTime = taskRepeated4TestNextTimeAfter.nextTimeAfter(testDate);
            Assertions.assertEquals("Wed Feb 22 09:59:12 EET 2023", nextTime.toString());
        } catch (ParseException e) {
            Assertions.fail("ParseException");
        }
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
    void testNextTimeAfterNPE() {
        try {
            //noinspection DataFlowIssue
            task.nextTimeAfter(null);
            Assertions.fail("call did not throw NPE");
        } catch (NullPointerException ignored) {
        }
    }

    @Test
    void testNextTimeAfterNormalFlow() {
        Date currentDate = new GregorianCalendar(2002, Calendar.JANUARY, 1).getTime();
        Date correctNextDate = new GregorianCalendar(2002, Calendar.JANUARY, 2).getTime();
        Date toVerifyNextDate = task.nextTimeAfter(currentDate);

        assertEquals(correctNextDate, toVerifyNextDate);
    }
}