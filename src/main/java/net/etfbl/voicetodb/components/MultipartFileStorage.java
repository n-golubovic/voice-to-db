package net.etfbl.voicetodb.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Component
public class MultipartFileStorage {

   private final String uploadPath;

   public MultipartFileStorage(@Value("${voice-to-db.upload-root-directory}") String uploadPath) {
      this.uploadPath = uploadPath;
   }

   public void save(String directoryName, List<MultipartFile> files) throws IOException {
      Path directoryPath = Path.of(uploadPath + directoryName);
      try {
         Files.createDirectory(directoryPath);

         for (MultipartFile file : files) {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(  directoryPath + "/" + file.getOriginalFilename());
            Files.write(path, bytes);
         }
      } catch (IOException e) {
         FileSystemUtils.deleteRecursively(directoryPath);
         throw e;
      }
   }
}
