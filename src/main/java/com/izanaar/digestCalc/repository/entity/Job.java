package com.izanaar.digestCalc.repository.entity;

import com.izanaar.digestCalc.repository.enums.Algo;
import com.izanaar.digestCalc.repository.enums.JobStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.net.URL;
import java.util.Date;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String uuid;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Algo algo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    private Date startDate;

    private Date endDate;

    private String hex;

    @NotNull
    @Column(nullable = false)
    private URL srcUrl;

    @Lob
    @Column(length=1024)
    private String stackTrace;

    public Job() {
    }

    public Job(Long id , String uuid, Algo algo, URL srcUrl) {
        this.id = id;
        this.uuid = uuid;
        this.algo = algo;
        this.srcUrl = srcUrl;
    }

    public Job(URL srcUrl, Algo algo) {
        this.srcUrl = srcUrl;
        this.algo = algo;
    }

    public Job(Algo algo, URL srcUrl) {
        this.algo = algo;
        this.srcUrl = srcUrl;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", algo=" + algo +
                ", status=" + status +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", hex='" + hex + '\'' +
                ", srcUrl=" + srcUrl +
                ", stackTrace='" + stackTrace + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Job job = (Job) o;

        if (id != null ? !id.equals(job.id) : job.id != null) return false;
        if (uuid != null ? !uuid.equals(job.uuid) : job.uuid != null) return false;
        if (algo != job.algo) return false;
        if (status != job.status) return false;
        if (startDate != null ? !startDate.equals(job.startDate) : job.startDate != null) return false;
        if (endDate != null ? !endDate.equals(job.endDate) : job.endDate != null) return false;
        if (hex != null ? !hex.equals(job.hex) : job.hex != null) return false;
        if (srcUrl != null ? !srcUrl.equals(job.srcUrl) : job.srcUrl != null) return false;
        return stackTrace != null ? stackTrace.equals(job.stackTrace) : job.stackTrace == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + (algo != null ? algo.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (hex != null ? hex.hashCode() : 0);
        result = 31 * result + (srcUrl != null ? srcUrl.hashCode() : 0);
        result = 31 * result + (stackTrace != null ? stackTrace.hashCode() : 0);
        return result;
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

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }
}
