package net.etfbl.voicetodb.components;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

@Slf4j
@Component
public class AudioStorage {

   private final String uploadPath;
   private final AudioConverter audioConverter;

   @Autowired
   public AudioStorage(@Value("${voice-to-db.upload-root-directory}") String uploadPath,
                       AudioConverter audioConverter) {
      this.uploadPath = uploadPath;
      this.audioConverter = audioConverter;
   }

   public void save(String directoryName, List<MultipartFile> files) throws IOException, EncoderException {
      Path directoryPath = Path.of(uploadPath, directoryName);
      try {
         Files.createDirectory(directoryPath);

         for (MultipartFile file : files) {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(directoryPath + "/" + file.getOriginalFilename());
            audioConverter.toVoskSupportedFormat(bytesToTemporaryFile(bytes), path.toFile());
         }
      } catch (IOException | EncoderException e) {
         FileSystemUtils.deleteRecursively(directoryPath);
         throw e;
      }
   }

   public List<File> load(String directoryName) {
      File directory = Path.of(uploadPath, directoryName).toFile();
      if (directory.exists() && directory.isDirectory()) {
         return Arrays.asList(requireNonNull(directory.listFiles()));
      }

      return null;
   }

   @SneakyThrows
   public List<String> listAll() {
      File[] directories = Path.of(uploadPath).toFile().listFiles();

      return directories != null
            ? Arrays.stream(directories).map(File::getName).collect(Collectors.toList())
            : Collections.emptyList();
   }

   @SneakyThrows
   public void delete(String directoryName) {
      FileSystemUtils.deleteRecursively(Path.of(uploadPath, directoryName));
   }

   @SneakyThrows
   private File bytesToTemporaryFile(byte[] bytes) {
      File tempFile = File.createTempFile("convertingFile", null, null);
      FileOutputStream fileOutput = new FileOutputStream(tempFile);
      fileOutput.write(bytes);

      return tempFile;
   }

}
