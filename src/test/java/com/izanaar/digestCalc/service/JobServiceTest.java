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
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
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

    private Logger logger;

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
        logger = mock(Logger.class);
        ReflectionTestUtils.setField(jobService, "logger", logger);
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

        when(executionService.tryCancelActionExecutuion(job.getId())).thenReturn(true);

        jobService.cancel(job.getId());

        verify(executionService).tryCancelActionExecutuion(job.getId());
        verify(jobRepository).delete(job);
    }

    @Test
    public void testWrongStatusCancel() throws Exception {
        Job job = mock(Job.class);
        when(job.getStatus()).thenReturn(JobStatus.COMPLETED);

        Long id = 5L;

        when(jobRepository.findOne(id)).thenReturn(job);
        thrown.expect(JobServiceException.class);
        thrown.expectMessage("Couldn't cancel job that has already been done.");
        jobService.cancel(id);
    }

    @Test
    public void testUninterruptibleCancel() throws Exception {
        Job job = mock(Job.class);
        when(job.getStatus()).thenReturn(JobStatus.PENDING);

        Long id = 5L;

        when(jobRepository.findOne(id)).thenReturn(job);
        thrown.expect(JobServiceException.class);
        thrown.expectMessage("Job processing has already been started.");
        jobService.cancel(id);
    }

    @Test
    public void testInvalidIdCancel() throws Exception {
        thrown.expect(JobServiceException.class);
        thrown.expectMessage("Job not found. Invalid id.");
        jobService.cancel(1L);
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
        Job job = mock(Job.class);
        Long jobId = 1L;
        when(job.getId()).thenReturn(jobId);

        when(jobRepository.findOne(jobId)).thenReturn(job);

        jobService.notifyStart(jobId);

        verify(job).setStartDate(any(Date.class));
        verify(job).setStatus(JobStatus.PENDING);
        verify(jobRepository).save(job);
    }

    @Test
    public void testFailedNotifyStart() throws Exception {
        Long id = 5L;
        jobService.notifyStart(id);
        verify(logger).error(anyString(), eq(id));
    }

    @Test
    public void notifySuccess() throws Exception {
        Long jobId = 1L;
        String hex = "Drink, Hellscream. Claim your Destiny. You will all be conquerors.";

        Job job = mock(Job.class);
        when(job.getId()).thenReturn(jobId);

        when(jobRepository.findOne(jobId)).thenReturn(job);

        jobService.notifySuccess(jobId, hex);

        verify(job).setEndDate(any(Date.class));
        verify(job).setStatus(JobStatus.COMPLETED);
        verify(jobRepository).save(job);
    }

    @Test
    public void testFailedNotifySuccess() throws Exception {
        Long id = 5L;
        String hex = "Some hex that does not matter.";
        jobService.notifySuccess(id, hex);
        verify(logger).error(anyString(), eq(id), eq(hex));
    }

    @Test
    public void notifyFailure() throws Exception {
        Long jobId = 1L;
        String hex = "You are not prepared!";

        Job job = mock(Job.class);
        when(job.getId()).thenReturn(jobId);

        when(jobRepository.findOne(jobId)).thenReturn(job);

        jobService.notifyFailure(jobId, hex);

        verify(job).setEndDate(any(Date.class));
        verify(job).setStatus(JobStatus.FAILED);
        verify(jobRepository).save(job);
    }

    @Test
    public void testFailedNotifyFailure() throws Exception {
        Long id = 5L;
        String stackTrace = "Some stack trace that also does not matter.";
        jobService.notifySuccess(id, stackTrace);
        verify(logger).error(anyString(), eq(id), eq(stackTrace));
    }
}