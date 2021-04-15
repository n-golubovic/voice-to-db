package net.etfbl.voicetodb.services;

import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.etfbl.voicetodb.components.AudioStorage;
import net.etfbl.voicetodb.components.JobIdGenerator;
import net.etfbl.voicetodb.components.JobQueue;
import net.etfbl.voicetodb.models.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

@Slf4j
@Service
public class JobSubmitService {

   private final AudioStorage fileStorage;
   private final JobQueue jobQueue;
   private final JobIdGenerator jobIdGenerator;

   @Autowired
   public JobSubmitService(AudioStorage fileStorage,
                           JobQueue jobQueue,
                           JobIdGenerator jobIdGenerator) {
      this.fileStorage = fileStorage;
      this.jobQueue = jobQueue;
      this.jobIdGenerator = jobIdGenerator;
   }


   public String save(List<MultipartFile> files) throws IOException, EncoderException {
      String id = jobIdGenerator.generate();
      fileStorage.save(id, files);
      jobQueue.add(new Job(id));
      log.info("submitted job request with id {}", id);
      return id;
   }
}
