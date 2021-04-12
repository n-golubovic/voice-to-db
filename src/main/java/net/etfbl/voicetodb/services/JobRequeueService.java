package net.etfbl.voicetodb.services;

import java.util.List;
import javax.annotation.PostConstruct;
import net.etfbl.voicetodb.components.AudioStorage;
import net.etfbl.voicetodb.components.JobQueue;
import net.etfbl.voicetodb.models.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobRequeueService {

   private final JobQueue jobQueue;
   private final AudioStorage audioStorage;

   @Autowired
   public JobRequeueService(JobQueue jobQueue, AudioStorage audioStorage) {
      this.jobQueue = jobQueue;
      this.audioStorage = audioStorage;
   }

   @PostConstruct
   void processAvailableFiles() {
      List<String> jobIds = audioStorage.listAll();

      jobIds.forEach(jobId -> jobQueue.submit(new Job(jobId)));
   }
}
