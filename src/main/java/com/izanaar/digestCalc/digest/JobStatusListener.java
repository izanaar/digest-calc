package com.izanaar.digestCalc.digest;

public interface JobStatusListener {

    void notifyStart(Long id);

    void notifySuccess(Long id, String hex);

    void notifyFailure(Long id, String stackTrace);

}
