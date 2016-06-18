package com.izanaar.digestCalc.repository.entity;

import com.izanaar.digestCalc.repository.enums.Algo;
import com.izanaar.digestCalc.repository.enums.TaskStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.net.URL;
import java.util.Date;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    private String uuid;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Algo algo;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private Date startDate;

    private Date endDate;

    @NotNull
    private URL srcUrl;

    private String stackTrace;

    public Task() {
    }

    public Task(Long id ,String uuid, Algo algo, URL srcUrl) {
        this.id = id;
        this.uuid = uuid;
        this.algo = algo;
        this.srcUrl = srcUrl;
    }

    public Task(URL srcUrl, Algo algo) {
        this.srcUrl = srcUrl;
        this.algo = algo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (id != null ? !id.equals(task.id) : task.id != null) return false;
        if (uuid != null ? !uuid.equals(task.uuid) : task.uuid != null) return false;
        if (algo != task.algo) return false;
        if (status != task.status) return false;
        if (startDate != null ? !startDate.equals(task.startDate) : task.startDate != null) return false;
        if (endDate != null ? !endDate.equals(task.endDate) : task.endDate != null) return false;
        if (srcUrl != null ? !srcUrl.equals(task.srcUrl) : task.srcUrl != null) return false;
        return stackTrace != null ? stackTrace.equals(task.stackTrace) : task.stackTrace == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + (algo != null ? algo.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (srcUrl != null ? srcUrl.hashCode() : 0);
        result = 31 * result + (stackTrace != null ? stackTrace.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", algo=" + algo +
                ", status=" + status +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", srcUrl=" + srcUrl +
                ", stackTrace='" + stackTrace + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Algo getAlgo() {
        return algo;
    }

    public void setAlgo(Algo algo) {
        this.algo = algo;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public URL getSrcUrl() {
        return srcUrl;
    }

    public void setSrcUrl(URL srcUrl) {
        this.srcUrl = srcUrl;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
