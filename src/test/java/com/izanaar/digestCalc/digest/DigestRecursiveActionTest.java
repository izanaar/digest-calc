package com.izanaar.digestCalc.digest;

import org.h2.util.IOUtils;
import org.junit.Test;
import org.mockito.Matchers;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class DigestRecursiveActionTest {

    private final Function<byte[], String>
            performer,
            delayPerformer;

    private final URL fileUrl;

    public DigestRecursiveActionTest() throws MalformedURLException {
        fileUrl = new URL("file:///home/traum/file");

        delayPerformer = (array) -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new String(array);
        };

        performer = String::new;

    }

    @Test
    public void testSuccess() throws Exception {
        String testContent = "Tempest keep was merely a setback";
        Long testTaskID = 55L;

        URL url = createTestUrl(testContent);


        JobStatusListener statusListener = mock(JobStatusListener.class);

        DigestRecursiveAction task = new DigestRecursiveAction(performer, url, statusListener, testTaskID);
        task.compute();
        verify(statusListener).notifySuccess(testTaskID, testContent);
    }

    private URL createTestUrl(String testContent) throws IOException {
        InputStream stream = IOUtils.getInputStreamFromString(testContent);
        final HttpURLConnection mockCon = mock(HttpURLConnection.class);
        when(mockCon.getInputStream()).thenReturn(stream);

        URLStreamHandler stubURLStreamHandler = new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL u) throws IOException {
                return mockCon;
            }
        };

        return new URL(null, "file:///file.txt", stubURLStreamHandler);
    }

    @Test
    public void testFailure() throws Exception {
        URL url = new URL("file:///opt/nofile");
        Long testTaskId = 55L;

        Function<byte[], String> performer = String::new;


        JobStatusListener statusListener = mock(JobStatusListener.class);

        DigestRecursiveAction task = new DigestRecursiveAction(performer, url, statusListener, testTaskId);
        task.invoke();
        verify(statusListener).notifyFailure(eq(testTaskId), Matchers.contains("FileNotFoundException"));
    }


    @Test
    public void testCancel() throws Exception {
        UnaryOperator<DigestRecursiveAction> streamIterateOperator = this::iterateOperator;

        List<DigestRecursiveAction> tasks = Stream
                .iterate(new DigestRecursiveAction(delayPerformer, fileUrl, mock(JobStatusListener.class), 1L), streamIterateOperator)
                .limit(3)
                .collect(Collectors.toList());

        ForkJoinPool singlePool = new ForkJoinPool(1);

        tasks.forEach(singlePool::execute);
        Thread.sleep(100);
        DigestRecursiveAction cancellableTask = tasks.get(0);
        assertFalse(cancellableTask.cancel(true));

        DigestRecursiveAction cancelledTask = tasks.get(1);
        assertTrue(cancelledTask.cancel(true));
        Thread.sleep(800);

        DigestRecursiveAction completedTask = tasks.get(2);

        assertTrue(cancellableTask.isDone() && cancellableTask.isCompletedNormally());

        assertTrue(cancelledTask.isDone() && cancelledTask.isCompletedAbnormally());

        assertTrue(completedTask.isCompletedNormally() && completedTask.isDone());

    }

    private DigestRecursiveAction iterateOperator(DigestRecursiveAction oldTask) {
        Long oldId = (Long) ReflectionTestUtils.getField(oldTask, DigestRecursiveAction.class, "jobId");
        return new DigestRecursiveAction(delayPerformer, fileUrl, mock(JobStatusListener.class), ++oldId);
    }

}