package net.etfbl.voicetodb.services;

import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.etfbl.voicetodb.components.AudioStorage;
import net.etfbl.voicetodb.components.JobQueue;
import net.etfbl.voicetodb.models.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * {@code JobRequeueService} works on application startup and puts all unprocessed requests into queue. This allows
 * application restart without losing submitted requests, assuming that working directories persist restart.
 */
@Slf4j
@Service
public class JobRequeueService {

   private final JobQueue jobQueue;
   private final AudioStorage audioStorage;

   @Autowired
   public JobRequeueService(JobQueue jobQueue, AudioStorage audioStorage) {
      this.jobQueue = jobQueue;
      this.audioStorage = audioStorage;
   }

   /**
    * Processes files available at {@link AudioStorage} and puts them in working queue.
    */
   @PostConstruct
   void processAvailableFiles() {
      List<String> jobIds = audioStorage.listAll();

      jobIds.forEach(jobId -> {
         log.info("processing leftover job with id {}", jobId);
         jobQueue.add(new Job(jobId));
      });
   }
}
