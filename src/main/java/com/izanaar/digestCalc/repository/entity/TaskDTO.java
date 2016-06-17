package com.izanaar.digestCalc.repository.entity;

import com.izanaar.digestCalc.repository.enums.Algo;
import com.sun.istack.internal.NotNull;

import java.net.URL;
import java.util.Date;

public class TaskDTO {

    @NotNull
    private URL url;

    @NotNull
    private Algo algo;

    private Date startDate;

    private Date endDate;

    public TaskDTO() {
    }

    public TaskDTO(URL url, Algo algo, Date startDate, Date endDate) {
        this.url = url;
        this.algo = algo;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
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
}
