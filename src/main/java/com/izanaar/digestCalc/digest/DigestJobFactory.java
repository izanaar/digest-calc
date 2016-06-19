package com.izanaar.digestCalc.digest;

import com.izanaar.digestCalc.repository.entity.Job;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.RecursiveAction;

@Service
public class DigestJobFactory {

    @Autowired
    private JobStatusListener jobStatusListener;

    public RecursiveAction getRecursiveAction(Job job){
        DigestRecursiveAction digestRecursiveAction = new DigestRecursiveAction(job.getSrcUrl(), jobStatusListener, job.getId());

        switch (job.getAlgo()) {
            case MD5:
                digestRecursiveAction.setPerformer(DigestUtils::md5Hex);
                break;
            case SHA1:
                digestRecursiveAction.setPerformer(DigestUtils::sha1Hex);
                break;
            case SHA256:
                digestRecursiveAction.setPerformer(DigestUtils::sha256Hex);
                break;
        }

        return digestRecursiveAction;
    }

}
