package com.izanaar.digestCalc.web;

public class ApiResponse<T> {

    private boolean status;
    private T content;
    private String message;

    public ApiResponse() {
    }

    public ApiResponse(boolean status) {
        this.status = status;
    }

    public ApiResponse(boolean status, T content) {
        this.status = status;
        this.content = content;
    }

    public ApiResponse(String message, boolean status, T content) {
        this.message = message;
        this.status = status;
        this.content = content;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "status=" + status +
                ", content=" + content +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApiResponse<?> that = (ApiResponse<?>) o;

        if (status != that.status) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        return message != null ? message.equals(that.message) : that.message == null;

    }

    @Override
    public int hashCode() {
        int result = (status ? 1 : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
