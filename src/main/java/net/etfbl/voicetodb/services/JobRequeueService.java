package net.etfbl.voicetodb.services;

import net.etfbl.voicetodb.components.JobQueue;
import net.etfbl.voicetodb.components.MultipartFileStorage;
import net.etfbl.voicetodb.models.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class JobRequeueService {

   private final JobQueue jobQueue;
   private final MultipartFileStorage multipartFileStorage;

   @Autowired
   public JobRequeueService(JobQueue jobQueue, MultipartFileStorage multipartFileStorage) {
      this.jobQueue = jobQueue;
      this.multipartFileStorage = multipartFileStorage;
   }

   @PostConstruct
   void processAvailableFiles() {
      System.out.println("Post startup");
      List<String> jobIds = multipartFileStorage.listAll();

      jobIds.forEach(jobId -> jobQueue.submit(new Job(jobId)));
   }
}
