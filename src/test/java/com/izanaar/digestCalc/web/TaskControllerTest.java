package com.izanaar.digestCalc.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.izanaar.digestCalc.repository.entity.Task;
import com.izanaar.digestCalc.repository.enums.Algo;
import com.izanaar.digestCalc.service.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;

import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(taskController)
                .build();
    }

    @Test
    public void testGetAll() throws Exception {
        final List<Task> tasks = Collections.singletonList(new Task("fafaw-xq", Algo.MD5, new Date(), new Date()));
        when(taskService.getAll()).thenReturn(tasks);

        mockMvc
                .perform(get("/task/all"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(new ObjectMapper().writeValueAsString(tasks)))
                .andExpect(status().isOk());
    }

    @Test
    public void getById() throws Exception {
        final Task expectedTask = new Task("ffafqtttq-x1", Algo.SHA256, new Date(), new Date());
        long id = 55L;

        when(taskService.getById(id)).thenReturn(expectedTask);

        mockMvc
                .perform(get("/task").param("id","55"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedTask)));
    }

    @Test
    public void testSuccessfulAddingTask() throws Exception {
        final Algo algo = Algo.SHA256;
        final URL url = new URL("file:///opt/web/file.txt");

        mockMvc
                .perform(post("/task")
                .param("algo", algo.toString())
                .param("srcUrl", url.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void testTaskConstraints() throws Exception {

    }
}