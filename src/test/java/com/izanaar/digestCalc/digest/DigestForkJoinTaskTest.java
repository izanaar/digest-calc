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

public class DigestForkJoinTaskTest {

    private final Function<byte[], String>
            performer,
            delayPerformer;

    private final URL fileUrl;

    public DigestForkJoinTaskTest() throws MalformedURLException {
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
        UnaryOperator<DigestForkJoinTask> streamIterateOperator = this::iterateOperator;

        List<DigestForkJoinTask> tasks = Stream
                .iterate(new DigestForkJoinTask(delayPerformer, fileUrl, getEmptyListener(), 1L), streamIterateOperator)
                .limit(3)
                .collect(Collectors.toList());

        ForkJoinPool singlePool = new ForkJoinPool(1);

        tasks.forEach(singlePool::execute);
        Thread.sleep(100);
        DigestForkJoinTask cancellableTask = tasks.get(0);
        //assertFalse(cancellableTask.cancel(true));
        System.out.println(cancellableTask.cancel(true));

        DigestForkJoinTask cancelledTask = tasks.get(1);
        assertTrue(cancelledTask.cancel(true));
        Thread.sleep(700);
        System.out.println();
    }

    private DigestForkJoinTask iterateOperator(DigestForkJoinTask oldTask) {
        Long oldId = (Long) ReflectionTestUtils.getField(oldTask, DigestForkJoinTask.class, "taskId");
        DigestForkJoinTask newTask = new DigestForkJoinTask(delayPerformer, fileUrl, getEmptyListener(), ++oldId);
        return newTask;
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