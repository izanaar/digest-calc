package com.izanaar.digestCalc.repository.dto;

import com.izanaar.digestCalc.repository.entity.Task;
import com.izanaar.digestCalc.repository.enums.Algo;

import javax.validation.constraints.NotNull;
import java.net.URL;
import java.util.Date;

public class TaskDTO{

    private Long id;

    private String uuid;

    @NotNull
    private Algo algo;

    private Date startDate;

    private Date endDate;

    @NotNull
    private URL srcUrl;

    private String errorMsg;

    @Override
    public String toString() {
        return "TaskDTO{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", algo=" + algo +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", srcUrl=" + srcUrl +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskDTO taskDTO = (TaskDTO) o;

        if (id != null ? !id.equals(taskDTO.id) : taskDTO.id != null) return false;
        if (uuid != null ? !uuid.equals(taskDTO.uuid) : taskDTO.uuid != null) return false;
        if (algo != taskDTO.algo) return false;
        if (startDate != null ? !startDate.equals(taskDTO.startDate) : taskDTO.startDate != null) return false;
        if (endDate != null ? !endDate.equals(taskDTO.endDate) : taskDTO.endDate != null) return false;
        if (srcUrl != null ? !srcUrl.equals(taskDTO.srcUrl) : taskDTO.srcUrl != null) return false;
        return errorMsg != null ? errorMsg.equals(taskDTO.errorMsg) : taskDTO.errorMsg == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + (algo != null ? algo.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (srcUrl != null ? srcUrl.hashCode() : 0);
        result = 31 * result + (errorMsg != null ? errorMsg.hashCode() : 0);
        return result;
    }

    public TaskDTO() {
    }

    public TaskDTO(Long id, String uuid, Algo algo, Date startDate, Date endDate, URL srcUrl, String errorMsg) {
        this.id = id;
        this.uuid = uuid;
        this.algo = algo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.srcUrl = srcUrl;
        this.errorMsg = errorMsg;
    }

    public TaskDTO(Task task){
        this(task.getId(), task.getUuid(), task.getAlgo(), task.getStartDate(), task.getEndDate(), task.getSrcUrl(), null);
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

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
