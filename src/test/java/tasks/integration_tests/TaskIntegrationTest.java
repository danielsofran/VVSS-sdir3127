package tasks.integration_tests;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.services.TasksService;

import java.util.Date;
import java.util.stream.StreamSupport;

public class TaskIntegrationTest {

    private TasksService service;

    @BeforeEach
    public void setUp() {
        Task task1 = new Task("ma-sa", new Date(Long.MAX_VALUE / 2));
        Task task2 = new Task("ma-sa", new Date(Long.MAX_VALUE / 2 + 1));
        ArrayTaskList repo = new ArrayTaskList();
        repo.add(task1);
        repo.add(task2);
        service = new TasksService(repo);
    }

    @Test
    public void testGetObservableList() {
        ObservableList<?> observableList = service.getObservableList();
        Assertions.assertEquals(2, observableList.size());
    }

    @Test
    public void testFilterTasksEmptyRange() {
        Assertions.assertEquals(
                0,
                StreamSupport.stream(
                        service.filterTasks(new Date(Long.MIN_VALUE), new Date(Long.MIN_VALUE)).spliterator(),
                        false
                ).count()
        );
    }
}
