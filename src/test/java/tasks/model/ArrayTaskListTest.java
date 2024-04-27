package tasks.model;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class ArrayTaskListTest {

    @Spy
    private ArrayTaskList repo;
    @Mock
    private Task task1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddTaskNormalFow() {
        int initialSize = repo.size();
        repo.add(task1);
        Assertions.assertEquals(repo.size(), initialSize + 1);
    }

    @Test
    void testAddTaskNPE() {
        try {
            //noinspection DataFlowIssue
            repo.add(null);
            Assertions.fail("call did not return NPE");
        } catch (NullPointerException ignored) { }
    }
}
