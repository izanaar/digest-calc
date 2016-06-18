package com.izanaar.digestCalc.service;

import com.izanaar.digestCalc.digest.DigestTaskFactory;
import com.izanaar.digestCalc.repository.TaskRepository;
import com.izanaar.digestCalc.repository.entity.Task;
import com.izanaar.digestCalc.repository.enums.Algo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {

    @Mock
    private UUIDKeeper uuidKeeper;

    @Mock
    private DigestTaskFactory factory;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private List<Task> taskList;

    private String uuid;

    @Before
    public void setUp() throws Exception {
        taskList = Stream
                .iterate(new Task(1L, "afafff-xxqw", Algo.MD5, new URL("file:///opt/file")), this::taskIterator)
                .limit(1)
                .collect(Collectors.toList());
    }

    private Task taskIterator(Task previousTask){
        return null;
    }

    @Test
    public void testGetAllTasks() throws Exception {
        when(taskRepository.findAll()).thenReturn(taskList);

        assertEquals(taskList, taskService.getAll());
    }
}