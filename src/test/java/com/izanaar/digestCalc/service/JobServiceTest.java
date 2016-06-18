package com.izanaar.digestCalc.service;

import com.izanaar.digestCalc.digest.DigestJobFactory;
import com.izanaar.digestCalc.repository.JobRepository;
import com.izanaar.digestCalc.repository.entity.Job;
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
public class JobServiceTest {

    @Mock
    private UUIDKeeper uuidKeeper;

    @Mock
    private DigestJobFactory factory;

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService taskService;

    private List<Job> jobList;

    private String uuid;

    @Before
    public void setUp() throws Exception {
        jobList = Stream
                .iterate(new Job(1L, "afafff-xxqw", Algo.MD5, new URL("file:///opt/file")), this::taskIterator)
                .limit(1)
                .collect(Collectors.toList());
    }

    private Job taskIterator(Job previousJob){
        return null;
    }

    @Test
    public void testGetAllTasks() throws Exception {
        when(jobRepository.findAll()).thenReturn(jobList);

        assertEquals(jobList, taskService.getAll());
    }
}