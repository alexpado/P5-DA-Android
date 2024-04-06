package com.cleanup.todoc;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.database.repository.TaskRepository;
import com.cleanup.todoc.model.TaskWithProject;
import com.cleanup.todoc.utils.TestUtils;
import com.cleanup.todoc.utils.UnitTestDataBank;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

/**
 * Unit tests for tasks
 *
 * @author Gaëtan HERFRAY
 */
public class TaskUnitTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private TaskRepository taskRepository;

    @Before
    public void configureTests() {

        UnitTestDataBank dataBank = new UnitTestDataBank();
        this.taskRepository = Mockito.mock(TaskRepository.class);

        Mockito.when(this.taskRepository.findAll())
               .thenReturn(new MutableLiveData<>(dataBank.getTaskWithProjects()));
    }

    @Test
    public void testProjects() throws InterruptedException {

        List<TaskWithProject> data = TestUtils.liveData(this.taskRepository.findAll());

        // Check if task are linked correctly
        for (TaskWithProject taskWithProject : data) {
            // About this:
            //  In this test, checking IDs between two different object is safe as the data has been
            //  created to match this. You can check in UnitTestDataBank.
            assertEquals(taskWithProject.getProject().getId(), taskWithProject.getTask().getId());
        }
    }

    @Test
    public void testTaskOrderingAZ() throws InterruptedException {

        Long[] expected = {1L, 4L, 3L, 2L};

        TestUtils.assertTaskComparator(
                TestUtils.liveData(this.taskRepository.findAll()),
                new TaskWithProject.TaskAZComparator(),
                expected
        );
    }

    @Test
    public void testTaskOrderingZA() throws InterruptedException {

        Long[] expected = {2L, 3L, 4L, 1L};
        TestUtils.assertTaskComparator(
                TestUtils.liveData(this.taskRepository.findAll()),
                new TaskWithProject.TaskZAComparator(),
                expected
        );
    }

    @Test
    public void testTaskOrderingTimestampDesc() throws InterruptedException {

        Long[] expected = {1L, 2L, 4L, 3L};
        TestUtils.assertTaskComparator(
                TestUtils.liveData(this.taskRepository.findAll()),
                new TaskWithProject.TaskRecentComparator(),
                expected
        );
    }

    @Test
    public void testTaskOrderingTimestampAsc() throws InterruptedException {

        Long[] expected = {3L, 4L, 2L, 1L};
        TestUtils.assertTaskComparator(
                TestUtils.liveData(this.taskRepository.findAll()),
                new TaskWithProject.TaskOldComparator(),
                expected
        );
    }

}