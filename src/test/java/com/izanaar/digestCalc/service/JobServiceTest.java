package com.izanaar.digestCalc.service;

import com.izanaar.digestCalc.digest.DigestJobFactory;
import com.izanaar.digestCalc.digest.DigestRecursiveAction;
import com.izanaar.digestCalc.exception.JobServiceException;
import com.izanaar.digestCalc.repository.JobRepository;
import com.izanaar.digestCalc.repository.entity.Job;
import com.izanaar.digestCalc.repository.enums.Algo;
import com.izanaar.digestCalc.repository.enums.JobStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URL;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JobServiceTest {
    @Mock
    private UUIDKeeper uuidKeeper;

    @Mock
    private DigestJobFactory factory;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private RecursiveActionExecutionService executionService;

    @InjectMocks
    private JobService jobService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private List<Job> jobList;

    private String uuid;

    private URL url;

    @Before
    public void setUp() throws Exception {
        jobList = Stream
                .iterate(new Job(1L, "afafff-xxqw", Algo.MD5, url), this::taskIterator)
                .limit(1)
                .collect(Collectors.toList());
        url = new URL("file:///opt/file");
    }

    private Job taskIterator(Job previousJob) {
        return null;
    }

    @Test
    public void testGetAllTasks() throws Exception {
        when(jobRepository.findAll()).thenReturn(jobList);

        assertEquals(jobList, jobService.getAll());
    }

    @Test
    public void getById() throws Exception {
        Long id = 2L;
        Job job = new Job(Algo.MD5, url);

        when(jobRepository.findOne(id)).thenReturn(job);

        assertEquals(job, jobService.getById(id));
    }

    @Test
    public void add() throws Exception {
        Job job = mock(Job.class);
        when(job.getId()).thenReturn(5L);
        DigestRecursiveAction recursiveAction = mock(DigestRecursiveAction.class);
        when(factory.getRecursiveAction(job)).thenReturn(recursiveAction);

        assertEquals(job, jobService.add(job));

        verify(factory, times(1)).getRecursiveAction(job);
        verify(executionService).executeAction(recursiveAction, 5L);
        verify(job).setStatus(JobStatus.WAITING);
        verify(jobRepository).save(job);
    }

    @Test
    public void testNormalCancel() throws Exception {
        Job job = new Job(5L, "aggqweq", Algo.SHA256, url);
        job.setStatus(JobStatus.WAITING);

        when(jobRepository.findOne(job.getId())).thenReturn(job);


    }

    @Test
    public void testWrongStatusCancel() throws Exception {

    }

    @Test
    public void testInvalidIdCancel() throws Exception {

    }

    @Test
    public void testNormalDelete() throws Exception {
        Job job = new Job(5L, "aggqweq", Algo.SHA256, url);
        job.setStatus(JobStatus.COMPLETED);

        when(jobRepository.findOne(job.getId())).thenReturn(job);

        jobService.delete(job.getId());

        verify(jobRepository).delete(job.getId());
    }

    @Test
    public void testWrongStatusDelete() throws Exception {
        Job job = new Job(5L, "aggqweq", Algo.SHA256, url);
        job.setStatus(JobStatus.WAITING);
        String message = "Couldn't delete job with status " + JobStatus.WAITING + ".";

        when(jobRepository.findOne(job.getId())).thenReturn(job);

        thrown.expect(JobServiceException.class);
        thrown.expectMessage(message);
        jobService.delete(job.getId());

        verify(jobRepository, times(0)).delete(anyLong());
    }

    @Test
    public void testInvalidIdDelete() throws Exception {
        String message = "Invalid id.";

        thrown.expect(JobServiceException.class);
        thrown.expectMessage(message);
        jobService.delete(5L);

        verify(jobRepository, times(0)).delete(anyLong());
    }

    @Test
    public void notifyStart() throws Exception {

    }

    @Test
    public void notifySuccess() throws Exception {

    }

    @Test
    public void notifyFailure() throws Exception {

    }
}