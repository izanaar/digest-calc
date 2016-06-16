package com.izanaar.digestCalc.digest;

import org.h2.util.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DigestForkJoinTaskTest {

    @Test
    public void testSuccess() throws Exception {
        final HttpURLConnection mockCon = mock(HttpURLConnection. class);
        URLStreamHandler stubURLStreamHandler = new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL u ) throws IOException {
                return mockCon ;
            }
        };

        String testContent = "Tempest keep was merely a setback";

        URL url = new URL(null,"file:///file.txt", stubURLStreamHandler);
        Function<byte[], String> performer = String::new;
        InputStream stream = IOUtils.getInputStreamFromString(testContent);

        when(mockCon.getInputStream()).thenReturn(stream);

        TaskStatusListener statusListener = new TaskStatusListener() {
            @Override
            public void notifySuccess(Long id, String hex) {
                int k = 2;
            }

            @Override
            public void notifyFauilure(Long id, String stackTrace) {
                int k = 2;
            }
        };
        DigestForkJoinTask task = new DigestForkJoinTask(performer, url, statusListener, 55L);
        ForkJoinPool.commonPool().execute(task);
        Thread.sleep(40000);
    }
}