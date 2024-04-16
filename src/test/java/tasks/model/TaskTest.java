package tasks.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Date;

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
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testTaskCreation() throws ParseException {
       assert task.getTitle() == "new task";
        System.out.println(task.getFormattedDateStart());
        System.out.println(task.getDateFormat().format(Task.getDateFormat().parse("2023-02-12 10:10")));
       assert task.getFormattedDateStart().equals(task.getDateFormat().format(Task.getDateFormat().parse("2023-02-12 10:10")));
    }

    @AfterEach
    void tearDown() {
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

            Date testDate = Task.getDateFormat().parse("2023-02-22 10:00");
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
            Date testDate = Task.getDateFormat().parse("2023-02-12 09:00");
            Date nextTime = taskNotRepeated4TestNextTimeAfter.nextTimeAfter(testDate);
            Assertions.assertEquals("Wed Feb 22 09:00:00 MSK 2023", nextTime.toString());
        } catch (ParseException e) {
            Assertions.fail("ParseException");
        }
    }

    @Test
    public void testNextTimeAfter4() {
        try {
            Date testDate = Task.getDateFormat().parse("2023-02-12 09:00");
            Date nextTime = taskRepeated4TestNextTimeAfter.nextTimeAfter(testDate);
            Assertions.assertEquals("Wed Feb 22 10:00:00 MSK 2023", nextTime.toString());
        } catch (ParseException e) {
            Assertions.fail("ParseException");
        }
    }

    @Test
    public void testNextTimeAfter5() {
        try {
            Date testDate = Task.getDateFormat().parse("2023-02-12 10:00");
            Date nextTime = taskRepeated4TestNextTimeAfter.nextTimeAfter(testDate);
            Assertions.assertNull(nextTime);
        } catch (ParseException e) {
            Assertions.fail("ParseException");
        }
    }

    @Test
    public void testNextTimeAfter6() {
        try {
            Date testDate = Task.getDateFormat().parse("2023-02-12 10:00:12");
            Date nextTime = taskRepeated4TestNextTimeAfter.nextTimeAfter(testDate);
            Assertions.assertEquals("Wed Feb 22 10:00:24 MSK 2023", nextTime.toString());
        } catch (ParseException e) {
            Assertions.fail("ParseException");
        }
    }
}