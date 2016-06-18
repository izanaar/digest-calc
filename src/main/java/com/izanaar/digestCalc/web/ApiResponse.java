package com.izanaar.digestCalc.web;

public class ApiResponse<T> {

    private boolean status;
    private T response;
    private String message;

    public ApiResponse() {
    }

    public ApiResponse(boolean status) {
        this.status = status;
    }

    public ApiResponse(boolean status, T response) {
        this.status = status;
        this.response = response;
    }

    public ApiResponse(String message, boolean status, T response) {
        this.message = message;
        this.status = status;
        this.response = response;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
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
                ", response=" + response +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApiResponse<?> that = (ApiResponse<?>) o;

        if (status != that.status) return false;
        if (response != null ? !response.equals(that.response) : that.response != null) return false;
        return message != null ? message.equals(that.message) : that.message == null;

    }

    @Override
    public int hashCode() {
        int result = (status ? 1 : 0);
        result = 31 * result + (response != null ? response.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
