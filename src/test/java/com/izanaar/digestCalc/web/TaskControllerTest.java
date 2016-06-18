package com.izanaar.digestCalc.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.izanaar.digestCalc.exception.TaskServiceException;
import com.izanaar.digestCalc.repository.entity.Task;
import com.izanaar.digestCalc.repository.enums.Algo;
import com.izanaar.digestCalc.repository.enums.TaskStatus;
import com.izanaar.digestCalc.service.TaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;

    private URL testUrl;

    private ObjectMapper objectMapper;

    private Long id;

    private String uuid;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(taskController)
                .build();
        testUrl = new URL("file:///opt/somefile");
        objectMapper = new ObjectMapper();
        id = 5L;
        uuid = UUID.randomUUID().toString();
    }

    @Test
    public void testGetAll() throws Exception {
        List<Task> tasks = Collections.singletonList(new Task(testUrl, Algo.MD5));
        ApiResponse<List<Task>> expectedResponse =
                new ApiResponse<>(true, tasks);

        when(taskService.getAll()).thenReturn(tasks);

        mockMvc
                .perform(get("/task/all"))
                .andExpect(contentTypeJSON())
                .andExpect(contentJSON(expectedResponse))
                .andExpect(status().isOk());
    }

    @Test
    public void getById() throws Exception {
        Task expectedTask = new Task(id, uuid, Algo.SHA256, testUrl);
        ApiResponse<Task> expectedResponse = new ApiResponse<>(true, expectedTask);

        when(taskService.getById(id)).thenReturn(expectedTask);

        mockMvc
                .perform(get("/task").param("id", id.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    public void testSuccessfulAddingTask() throws Exception {
        String uuid = UUID.randomUUID().toString();

        Task incomingTask = new Task(Algo.MD5, testUrl),
                outgoingTask = new Task(id, uuid, incomingTask.getAlgo(), incomingTask.getSrcUrl());

        ApiResponse<Task> taskApiResponse = new ApiResponse<>(true, outgoingTask);

        when(taskService.add(incomingTask)).thenReturn(outgoingTask);

        mockMvc
                .perform(post("/task")
                        .param("algo", incomingTask.getAlgo().toString())
                        .param("srcUrl", incomingTask.getSrcUrl().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(objectMapper.writeValueAsString(taskApiResponse)));
    }

    @Test
    public void testFailedAddingTask() throws Exception {
        Task incomingTask = new Task(Algo.MD5, testUrl);
        String message = "Something went wrong.";

        ApiResponse<Task> taskApiResponse = new ApiResponse<>(message, false, incomingTask);

        doThrow(new RuntimeException(message)).when(taskService).add(incomingTask);

        mockMvc
                .perform(post("/task")
                        .param("algo", incomingTask.getAlgo().toString())
                        .param("srcUrl", incomingTask.getSrcUrl().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(objectMapper.writeValueAsString(taskApiResponse)));
    }

    @Test
    public void testTaskConstraints() throws Exception {
        Algo algo = Algo.SHA256;
        URL url = new URL("file:///opt/web/file.txt");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        mockMvc
                .perform(post("/task").params(params))
                .andExpect(status().isBadRequest());

        params.add("algo", algo.toString());
        mockMvc
                .perform(post("/task").params(params))
                .andExpect(status().isBadRequest());

        params.add("srcUrl", url.toString());
        mockMvc
                .perform(post("/task").params(params))
                .andExpect(status().isOk());
    }

    @Test
    public void testSuccessfulDelete() throws Exception {
        ApiResponse expectedResponse = new ApiResponse(true);

        mockMvc
                .perform(delete("/task").param("id", id.toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
                .andExpect(status().isOk());
    }

    @Test
    public void testFailedDelete() throws Exception {
        String message = "Something went wrong.";
        TaskServiceException exception = new TaskServiceException(message);
        ApiResponse<Long> response = new ApiResponse<>(message, false, id);

        doThrow(exception).when(taskService).delete(id);

        mockMvc
                .perform(delete("/task").param("id", id.toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(objectMapper.writeValueAsString(response)))
                .andExpect(status().isOk());
    }

    @Test
    public void testSuccessfulCancel() throws Exception {
        Task outgoingTask = new Task(id, uuid, Algo.MD5, testUrl);
        outgoingTask.setStatus(TaskStatus.CANCELLED);

        ApiResponse<Task> response = new ApiResponse<>(true, outgoingTask);

        when(taskService.cancel(id)).thenReturn(outgoingTask);

        mockMvc
                .perform(get("/task/cancel").param("id", id.toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(objectMapper.writeValueAsString(response)))
                .andExpect(status().isOk());
    }

    @Test
    public void testFailedCancel() throws Exception {
        String message = "Something went wrong.";
        ApiResponse<Long> response = new ApiResponse<>(message, false, id);


        doThrow(new TaskServiceException(message)).when(taskService).cancel(id);

        mockMvc
                .perform(get("/task/cancel").param("id", id.toString()))
                .andExpect(contentTypeJSON())
                .andExpect(contentJSON(response))
                .andExpect(status().isOk());
    }


    private ResultMatcher contentTypeJSON() {
        return content().contentType(MediaType.APPLICATION_JSON_UTF8);
    }

    private ResultMatcher contentJSON(Object object) throws JsonProcessingException {
        return content().string(objectMapper.writeValueAsString(object));
    }
}