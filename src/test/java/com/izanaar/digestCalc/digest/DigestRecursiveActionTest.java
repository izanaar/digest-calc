package com.izanaar.digestCalc.digest;

import org.h2.util.IOUtils;
import org.junit.Test;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DigestRecursiveActionTest {

    private final Function<byte[], String>
            performer,
            delayPerformer;

    private final URL fileUrl;

    public DigestRecursiveActionTest() throws MalformedURLException {
        fileUrl = new URL("file:///home/traum/file");

        delayPerformer = (array) -> {
            try {
                Thread.sleep(200);
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


        JobStatusListener statusListener = getSuccessStatusListener(testContent, testTaskID);

        DigestRecursiveAction task = new DigestRecursiveAction(performer, url, statusListener, testTaskID);
        task.compute();
    }

    private JobStatusListener getSuccessStatusListener(String expectedHex, Long expectedId) {
        return new JobStatusListener() {
            @Override
            public void notifyStart(Long id) {

            }

            @Override
            public void notifySuccess(Long id, String hex) {
                assertEquals(expectedHex, hex);
                assertEquals(expectedId, id);
            }

            @Override
            public void notifyFailure(Long id, String stackTrace) {
                assertTrue(false);
            }
        };
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


        JobStatusListener statusListener = getErrorStatusListener("FileNotFoundException", testTaskId);

        DigestRecursiveAction task = new DigestRecursiveAction(performer, url, statusListener, testTaskId);
        task.invoke();
    }

    private JobStatusListener getErrorStatusListener(String expectedStackTracePart, Long expectedId) {
        return new JobStatusListener() {
            @Override
            public void notifyStart(Long id) {

            }

            @Override
            public void notifySuccess(Long id, String hex) {
                assertTrue(false);
            }

            @Override
            public void notifyFailure(Long id, String stackTrace) {
                assertEquals(expectedId, id);
                assertTrue(stackTrace.contains(expectedStackTracePart));
            }
        };
    }

    @Test
    public void testCancel() throws Exception {
        UnaryOperator<DigestRecursiveAction> streamIterateOperator = this::iterateOperator;

        List<DigestRecursiveAction> tasks = Stream
                .iterate(new DigestRecursiveAction(delayPerformer, fileUrl, getEmptyListener(), 1L), streamIterateOperator)
                .limit(3)
                .collect(Collectors.toList());

        ForkJoinPool singlePool = new ForkJoinPool(1);

        tasks.forEach(singlePool::execute);
        Thread.sleep(100);
        DigestRecursiveAction cancellableTask = tasks.get(0);
        assertFalse(cancellableTask.cancel(true));

        DigestRecursiveAction cancelledTask = tasks.get(1);
        assertTrue(cancelledTask.cancel(true));
        Thread.sleep(700);

        DigestRecursiveAction completedTask = tasks.get(2);
        assertTrue(completedTask.isCompletedNormally() && completedTask.isDone());

        assertTrue(cancellableTask.isDone() && cancellableTask.isCompletedNormally());

        assertTrue(cancelledTask.isDone() && cancelledTask.isCompletedAbnormally());


    }

    private DigestRecursiveAction iterateOperator(DigestRecursiveAction oldTask) {
        Long oldId = (Long) ReflectionTestUtils.getField(oldTask, DigestRecursiveAction.class, "jobId");
        DigestRecursiveAction newTask = new DigestRecursiveAction(delayPerformer, fileUrl, getEmptyListener(), ++oldId);
        return newTask;
    }

    private JobStatusListener getEmptyListener() {
        return new JobStatusListener() {
            @Override
            public void notifyStart(Long id) {

            }

            @Override
            public void notifySuccess(Long id, String hex) {

            }

            @Override
            public void notifyFailure(Long id, String stackTrace) {

            }
        };
    }
}