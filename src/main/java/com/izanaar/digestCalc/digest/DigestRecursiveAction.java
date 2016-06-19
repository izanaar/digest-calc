package com.izanaar.digestCalc.digest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class DigestRecursiveAction extends RecursiveAction {

    private Function<byte[], String> performer;
    private URL source;
    private AtomicBoolean operationStarted;
    private JobStatusListener statusListener;
    private Long jobId;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    DigestRecursiveAction(URL source, JobStatusListener statusListener, Long jobId) {
        this.source = source;
        this.statusListener = statusListener;
        this.jobId = jobId;
        operationStarted = new AtomicBoolean(false);
    }

    public DigestRecursiveAction(Function<byte[], String> performer, URL source,
                                 JobStatusListener statusListener, Long jobId) {
        this(source, statusListener, jobId);
        this.performer = performer;
        operationStarted = new AtomicBoolean(false);
    }

    void setPerformer(Function<byte[], String> performer) {
        this.performer = performer;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if(operationStarted.get()){
            return false;
        }else{
            return super.cancel(mayInterruptIfRunning);
        }
    }

    @Override
    protected void compute() {
        operationStarted.set(true);
        statusListener.notifyStart(jobId);
        try {
            InputStream stream = source.openStream();
            byte[] bytes = readStream(stream);
            String hex = performer.apply(bytes);
            statusListener.notifySuccess(jobId, hex);
        } catch (Exception e) {
            statusListener.notifyFailure(jobId, getStackTrace(e));
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

        DigestRecursiveAction that = (DigestRecursiveAction) o;

        if (performer != null ? !performer.equals(that.performer) : that.performer != null) return false;
        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        if (operationStarted != null ? !operationStarted.equals(that.operationStarted) : that.operationStarted != null)
            return false;
        if (statusListener != null ? !statusListener.equals(that.statusListener) : that.statusListener != null)
            return false;
        if (jobId != null ? !jobId.equals(that.jobId) : that.jobId != null) return false;
        return logger != null ? logger.equals(that.logger) : that.logger == null;

    }

    @Override
    public int hashCode() {
        int result = performer != null ? performer.hashCode() : 0;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (operationStarted != null ? operationStarted.hashCode() : 0);
        result = 31 * result + (statusListener != null ? statusListener.hashCode() : 0);
        result = 31 * result + (jobId != null ? jobId.hashCode() : 0);
        result = 31 * result + (logger != null ? logger.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DigestRecursiveAction{" +
                "performer=" + performer +
                ", source=" + source +
                ", operationStarted=" + operationStarted +
                ", statusListener=" + statusListener +
                ", jobId=" + jobId +
                '}';
    }
}
