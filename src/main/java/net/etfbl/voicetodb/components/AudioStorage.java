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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

/**
 * {@code AudioStorage} allows conversion to Vosk-supported audio format and storage of converted audio. All received
 * data is stored in root directory defined in configuration, and grouped into directories where each directory matches
 * a batch received through a single processing request.
 */
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

   /**
    * Saves given files into given directory. Assumed that directory denoted by given directoryName parameter doesn't
    * exists.
    *
    * @param directoryName name of parent directory
    * @param files         files to save
    * @throws IOException      file exists, or files can't be saved
    * @throws EncoderException files shouldn't be saved as they aren't Vosk-compatible
    */
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

   /**
    * Loads files inside the given directoryName. Returns null if directory doesn't exist.
    *
    * @param directoryName directory name
    * @return list of files
    */
   public List<File> load(String directoryName) {
      File directory = Path.of(uploadPath, directoryName).toFile();
      if (directory.exists() && directory.isDirectory()) {
         return Arrays.asList(requireNonNull(directory.listFiles()));
      }

      return null;
   }

   /**
    * Returns a list of all existing directories.
    *
    * @return list of names of existing directories
    */
   @SneakyThrows
   public List<String> listAll() {
      File[] directories = Path.of(uploadPath).toFile().listFiles();

      return directories != null
            ? Arrays.stream(directories).map(File::getName).collect(Collectors.toList())
            : Collections.emptyList();
   }

   /**
    * Deletes the directory denoted by the given directoryName and all files in it.
    *
    * @param directoryName directory to delete
    */
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
