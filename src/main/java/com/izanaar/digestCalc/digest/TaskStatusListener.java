package com.izanaar.digestCalc.digest;

public interface TaskStatusListener {

    void notifySuccess(Long id, String hex);

    void notifyFauilure(Long id, String stackTrace);

}
