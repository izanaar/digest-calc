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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DigestForkJoinTaskTest {

    private final Function<byte[], String> performer = String::new;
    final URL fileUrl;

    public DigestForkJoinTaskTest() throws MalformedURLException {
        fileUrl = new URL("file:///home/traum/file");
    }

    @Test
    public void testSuccess() throws Exception {
        String testContent = "Tempest keep was merely a setback";
        Long testTaskID = 55L;

        URL url = createTestUrl(testContent);


        TaskStatusListener statusListener = getSuccessStatusListener(testContent, testTaskID);

        DigestForkJoinTask task = new DigestForkJoinTask(performer, url, statusListener, testTaskID);
        task.compute();
    }

    private TaskStatusListener getSuccessStatusListener(String expectedHex, Long expectedId) {
        return new TaskStatusListener() {
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


        TaskStatusListener statusListener = getErrorStatusListener("FileNotFoundException", testTaskId);

        DigestForkJoinTask task = new DigestForkJoinTask(performer, url, statusListener, testTaskId);
        task.compute();
    }

    private TaskStatusListener getErrorStatusListener(String expectedStackTracePart, Long expectedId) {
        return new TaskStatusListener() {
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
        UnaryOperator<DigestForkJoinTask> taskUnaryOperator = this::createNextTask;
        Function<byte[], String> delayPerformer = (array) -> {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new String(array);
        };

        List<DigestForkJoinTask> tasks = Stream
                .iterate(new DigestForkJoinTask(delayPerformer, fileUrl, getEmptyListener(), 5L), taskUnaryOperator)
                .limit(3)
                .collect(Collectors.toList());

        ForkJoinPool pool = new ForkJoinPool(1);
        tasks.forEach(pool::invoke);
        Thread.sleep(50);
        assertFalse(tasks.get(0).cancel(true));
        assertTrue(tasks.get(1).cancel(true));

        Thread.sleep(400);

        assertFalse(tasks.get(0).isCancelled());
        assertTrue(tasks.get(1).isCancelled());
        assertTrue(tasks.get(0).isCompletedNormally());
        assertTrue(tasks.get(0).isCompletedAbnormally());
    }

    private DigestForkJoinTask createNextTask(DigestForkJoinTask oldTask) {
        Long oldId = oldTask.getTaskId();

        return new DigestForkJoinTask(performer, fileUrl, getEmptyListener(), oldId + 1);
    }

    public TaskStatusListener getEmptyListener() {
        return new TaskStatusListener() {
            @Override
            public void notifySuccess(Long id, String hex) {

            }

            @Override
            public void notifyFailure(Long id, String stackTrace) {

            }
        };
    }
}