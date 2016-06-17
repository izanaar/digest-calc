package com.izanaar.digestCalc.digest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class DigestForkJoinTask extends RecursiveAction {

    private Function<byte[], String> performer;
    private URL source;
    private AtomicBoolean operationStarted;
    private TaskStatusListener statusListener;
    private Long taskId;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    DigestForkJoinTask(URL source, TaskStatusListener statusListener, Long taskId) {
        this.source = source;
        this.statusListener = statusListener;
        this.taskId = taskId;
    }

    public DigestForkJoinTask(Function<byte[], String> performer, URL source,
                              TaskStatusListener statusListener, Long taskId) {
        this(source, statusListener, taskId);
        this.performer = performer;
        operationStarted = new AtomicBoolean(false);
    }

    void setPerformer(Function<byte[], String> performer) {
        this.performer = performer;
    }

    public Long getTaskId() {
        return taskId;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return !operationStarted.get() && super.cancel(mayInterruptIfRunning);
    }

    @Override
    protected void compute() {
        logger.trace("Hex calculation for task {} has started.", taskId);
        try {
            InputStream stream = source.openStream();
            byte[] bytes = readStream(stream);
            String hex = performer.apply(bytes);
            logger.trace("Hex calculation for task {} has ended.", taskId);
            statusListener.notifySuccess(taskId, hex);
        } catch (IOException e) {
            logger.error("Hex calculation for task {} has failed.", taskId);
            statusListener.notifyFailure(taskId, getStackTrace(e));
        }
    }

    private byte[] readStream(InputStream stream) throws IOException {
        byte[] bytes = new byte[stream.available()];
        stream.read(bytes);
        return bytes;
    }

    private String getStackTrace(Exception e) {
        Writer eWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(eWriter);
        e.printStackTrace(writer);
        return eWriter.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DigestForkJoinTask that = (DigestForkJoinTask) o;

        if (performer != null ? !performer.equals(that.performer) : that.performer != null) return false;
        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        if (operationStarted != null ? !operationStarted.equals(that.operationStarted) : that.operationStarted != null)
            return false;
        if (statusListener != null ? !statusListener.equals(that.statusListener) : that.statusListener != null)
            return false;
        return taskId != null ? taskId.equals(that.taskId) : that.taskId == null;

    }

    @Override
    public int hashCode() {
        int result = performer != null ? performer.hashCode() : 0;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (operationStarted != null ? operationStarted.hashCode() : 0);
        result = 31 * result + (statusListener != null ? statusListener.hashCode() : 0);
        result = 31 * result + (taskId != null ? taskId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DigestForkJoinTask{" +
                "performer=" + performer +
                ", source=" + source +
                ", operationStarted=" + operationStarted +
                ", statusListener=" + statusListener +
                ", taskId=" + taskId +
                '}';
    }
}
