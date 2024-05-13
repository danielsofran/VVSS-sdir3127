package tasks.unit_tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.services.TasksService;

import java.util.Date;
import java.util.stream.StreamSupport;

public class TasksServiceTest {

    @SuppressWarnings("FieldCanBeLocal")
    private Task task1;
    @SuppressWarnings("FieldCanBeLocal")
    private ArrayTaskList repo;
    private TasksService service;

    @BeforeEach
    void setUp() {
        task1 = new Task("ma-sa", new Date(Long.MAX_VALUE / 2));
        repo = Mockito.spy(ArrayTaskList.class);
        repo.add(task1);
        service = new TasksService(repo);
    }

    @Test
    void testFilterTasksFullRange() {
        for (Task filterTask : service.filterTasks(new Date(Long.MIN_VALUE), new Date(Long.MAX_VALUE))) {
            System.out.println("here we go");
            System.out.println(filterTask);
        }
        Assertions.assertEquals(
                0,
                StreamSupport.stream(
                        service.filterTasks(new Date(Long.MIN_VALUE), new Date(Long.MAX_VALUE)).spliterator(),
                        false
                ).count()
        );
    }

    @Test
    void testFilterTasksEmptyRange() {
        Assertions.assertEquals(
                0,
                StreamSupport.stream(
                        service.filterTasks(new Date(Long.MIN_VALUE), new Date(Long.MIN_VALUE)).spliterator(),
                        false
                ).count()
        );
    }
}
