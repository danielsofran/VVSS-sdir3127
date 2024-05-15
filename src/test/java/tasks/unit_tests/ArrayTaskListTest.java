package tasks.unit_tests;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import tasks.model.ArrayTaskList;
import tasks.model.Task;

public class ArrayTaskListTest {

    @Spy
    private ArrayTaskList repo;
    @Mock
    private Task task1;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddTaskNormalFow() {
        int initialSize = repo.size();
        repo.add(task1);
        Assertions.assertEquals(repo.size(), initialSize + 1);
    }

    @Test
    public void testAddTaskNPE() {
        try {
            //noinspection DataFlowIssue
            repo.add(null);
            Assertions.fail("call did not return NPE");
        } catch (NullPointerException ignored) { }
    }
}
