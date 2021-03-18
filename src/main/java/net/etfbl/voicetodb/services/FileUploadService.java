package net.etfbl.voicetodb.services;

import lombok.SneakyThrows;
import net.etfbl.voicetodb.components.JobIdGenerator;
import net.etfbl.voicetodb.components.MultipartFileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FileUploadService {

   private final MultipartFileStorage fileStorage;
   private final JobIdGenerator jobIdGenerator;

   @Autowired
   public FileUploadService(MultipartFileStorage fileStorage,
                            JobIdGenerator jobIdGenerator) {
      this.fileStorage = fileStorage;
      this.jobIdGenerator = jobIdGenerator;
   }


   @SneakyThrows
   public String save(List<MultipartFile> files) {
      String id = jobIdGenerator.generate();
      fileStorage.save(id, files);
      return id;
   }
}
