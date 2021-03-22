package net.etfbl.voicetodb.services;

import java.util.List;
import lombok.SneakyThrows;
import net.etfbl.voicetodb.components.JobIdGenerator;
import net.etfbl.voicetodb.components.JobQueue;
import net.etfbl.voicetodb.components.MultipartFileStorage;
import net.etfbl.voicetodb.models.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {

   private final MultipartFileStorage fileStorage;
   private final JobQueue jobQueue;
   private final JobIdGenerator jobIdGenerator;

   @Autowired
   public FileUploadService(MultipartFileStorage fileStorage,
                            JobQueue jobQueue,
                            JobIdGenerator jobIdGenerator) {
      this.fileStorage = fileStorage;
      this.jobQueue = jobQueue;
      this.jobIdGenerator = jobIdGenerator;
   }


   @SneakyThrows
   public String save(List<MultipartFile> files) {
      String id = jobIdGenerator.generate();
      fileStorage.save(id, files);
      jobQueue.submit(new Job(id));
      return id;
   }
}
