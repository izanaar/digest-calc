package com.izanaar.digestCalc.digest;

import com.izanaar.digestCalc.repository.entity.Job;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Service
public class DigestJobFactory {

    public DigestRecursiveAction getJob(Job job, JobStatusListener listener){
        DigestRecursiveAction digestRecursiveAction = new DigestRecursiveAction(job.getSrcUrl(), listener, job.getId());

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
