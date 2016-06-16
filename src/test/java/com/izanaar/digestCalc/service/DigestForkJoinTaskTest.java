package com.izanaar.digestCalc.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.net.URL;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class DigestForkJoinTaskTest {

    private ForkJoinPool pool = ForkJoinPool.commonPool();

    @Test
    public void testMD5() throws Exception {
        DigestForkJoinTask task = new DigestForkJoinTask(DigestUtils::sha1Hex, new URL("file:///home/traum/file"));
        pool.awaitTermination(4000, TimeUnit.MILLISECONDS);
        pool.execute(task);
        System.out.println("Cancelled: " + task.isCancelled());
        task.cancel(true);
        System.out.println("Cancelled: " + task.isCancelled());
        System.out.println(task.isDone());
    }
}