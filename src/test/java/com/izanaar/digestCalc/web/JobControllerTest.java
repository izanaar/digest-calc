package com.izanaar.digestCalc.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.izanaar.digestCalc.exception.JobServiceException;
import com.izanaar.digestCalc.repository.entity.Job;
import com.izanaar.digestCalc.repository.enums.Algo;
import com.izanaar.digestCalc.repository.enums.JobStatus;
import com.izanaar.digestCalc.service.JobService;
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
public class JobControllerTest {

    @Mock
    private JobService jobService;

    @InjectMocks
    private JobController jobController;

    private MockMvc mockMvc;

    private URL testUrl;

    private ObjectMapper objectMapper;

    private Long id;

    private String uuid;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(jobController)
                .build();
        testUrl = new URL("file:///opt/somefile");
        objectMapper = new ObjectMapper();
        id = 5L;
        uuid = UUID.randomUUID().toString();
    }

    @Test
    public void testGetAll() throws Exception {
        List<Job> jobs = Collections.singletonList(new Job(testUrl, Algo.MD5));
        ApiResponse<List<Job>> expectedResponse =
                new ApiResponse<>(true, jobs);

        when(jobService.getAll()).thenReturn(jobs);

        mockMvc
                .perform(get("/job/all"))
                .andExpect(contentTypeJSON())
                .andExpect(contentJSON(expectedResponse))
                .andExpect(status().isOk());
    }

    @Test
    public void getById() throws Exception {
        Job expectedJob = new Job(id, uuid, Algo.SHA256, testUrl);
        ApiResponse<Job> expectedResponse = new ApiResponse<>(true, expectedJob);

        when(jobService.getById(id)).thenReturn(expectedJob);

        mockMvc
                .perform(get("/job").param("id", id.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    public void testSuccessfulAddingJob() throws Exception {
        String uuid = UUID.randomUUID().toString();

        Job incomingJob = new Job(Algo.MD5, testUrl),
                outgoingJob = new Job(id, uuid, incomingJob.getAlgo(), incomingJob.getSrcUrl());

        ApiResponse<Job> jobApiResponse = new ApiResponse<>(true, outgoingJob);

        when(jobService.add(incomingJob)).thenReturn(outgoingJob);

        mockMvc
                .perform(post("/job")
                        .param("algo", incomingJob.getAlgo().toString())
                        .param("srcUrl", incomingJob.getSrcUrl().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(objectMapper.writeValueAsString(jobApiResponse)));
    }

    @Test
    public void testFailedAddingJob() throws Exception {
        Job incomingJob = new Job(Algo.MD5, testUrl);
        String message = "Something went wrong.";

        ApiResponse<Job> jobApiResponse = new ApiResponse<>(message, false, incomingJob);

        doThrow(new RuntimeException(message)).when(jobService).add(incomingJob);

        mockMvc
                .perform(post("/job")
                        .param("algo", incomingJob.getAlgo().toString())
                        .param("srcUrl", incomingJob.getSrcUrl().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(objectMapper.writeValueAsString(jobApiResponse)));
    }

    @Test
    public void testJobConstraints() throws Exception {
        Algo algo = Algo.SHA256;
        URL url = new URL("file:///opt/web/file.txt");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        mockMvc
                .perform(post("/job").params(params))
                .andExpect(status().isBadRequest());

        params.add("algo", algo.toString());
        mockMvc
                .perform(post("/job").params(params))
                .andExpect(status().isBadRequest());

        params.add("srcUrl", url.toString());
        mockMvc
                .perform(post("/job").params(params))
                .andExpect(status().isOk());
    }

    @Test
    public void testSuccessfulDelete() throws Exception {
        ApiResponse expectedResponse = new ApiResponse(true);

        mockMvc
                .perform(delete("/job").param("id", id.toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)))
                .andExpect(status().isOk());
    }

    @Test
    public void testFailedDelete() throws Exception {
        String message = "Something went wrong.";
        JobServiceException exception = new JobServiceException(message);
        ApiResponse<Long> response = new ApiResponse<>(message, false, id);

        doThrow(exception).when(jobService).delete(id);

        mockMvc
                .perform(delete("/job").param("id", id.toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(objectMapper.writeValueAsString(response)))
                .andExpect(status().isOk());
    }

    @Test
    public void testSuccessfulCancel() throws Exception {
        Job outgoingJob = new Job(id, uuid, Algo.MD5, testUrl);
        outgoingJob.setStatus(JobStatus.CANCELLED);

        ApiResponse<Job> response = new ApiResponse<>(true, outgoingJob);

        when(jobService.cancel(id)).thenReturn(outgoingJob);

        mockMvc
                .perform(get("/job/cancel").param("id", id.toString()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(objectMapper.writeValueAsString(response)))
                .andExpect(status().isOk());
    }

    @Test
    public void testFailedCancel() throws Exception {
        String message = "Something went wrong.";
        ApiResponse<Long> response = new ApiResponse<>(message, false, id);


        doThrow(new JobServiceException(message)).when(jobService).cancel(id);

        mockMvc
                .perform(get("/job/cancel").param("id", id.toString()))
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