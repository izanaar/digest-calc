package com.izanaar.digestCalc.digest;

import com.izanaar.digestCalc.repository.entity.Task;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Service
public class DigestTaskFactory {

    public DigestForkJoinTask getTask(Task task, TaskStatusListener listener){
        DigestForkJoinTask digestForkJoinTask = new DigestForkJoinTask(task.getSrcUrl(), listener, task.getId());

        switch (task.getAlgo()) {
            case MD5:
                digestForkJoinTask.setPerformer(DigestUtils::md5Hex);
                break;
            case SHA1:
                digestForkJoinTask.setPerformer(DigestUtils::sha1Hex);
                break;
            case SHA256:
                digestForkJoinTask.setPerformer(DigestUtils::sha256Hex);
                break;
        }

        return digestForkJoinTask;
    }

}
