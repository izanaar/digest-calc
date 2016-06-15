package com.izanaar.digestCalc.repository.entity;

import com.izanaar.digestCalc.repository.enums.Algo;
import com.sun.istack.internal.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uuid;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private Algo algo;

    private Date startDate;

    private Date endDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (id != null ? !id.equals(task.id) : task.id != null) return false;
        if (uuid != null ? !uuid.equals(task.uuid) : task.uuid != null) return false;
        if (algo != task.algo) return false;
        if (startDate != null ? !startDate.equals(task.startDate) : task.startDate != null) return false;
        return endDate != null ? endDate.equals(task.endDate) : task.endDate == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + (algo != null ? algo.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", algo=" + algo +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    public Task() {
    }

    public Task(String uuid, Algo algo, Date startDate, Date endDate) {
        this.uuid = uuid;
        this.algo = algo;
        this.startDate = startDate;
        this.endDate = endDate;
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
}
