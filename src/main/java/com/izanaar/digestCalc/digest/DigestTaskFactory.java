package com.izanaar.digestCalc.digest;

import com.izanaar.digestCalc.repository.entity.Task;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Service
public class DigestTaskFactory {

    public DigestRecursiveAction getTask(Task task, TaskStatusListener listener){
        DigestRecursiveAction digestRecursiveAction = new DigestRecursiveAction(task.getSrcUrl(), listener, task.getId());

        switch (task.getAlgo()) {
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
