package net.etfbl.voicetodb.components;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * {@code ResultStorage} allows permanent storage of results.
 */
@Component
public class ResultStorage {

   private final String resultPath;

   public ResultStorage(@Value("${voice-to-db.result-root-directory}") String resultPath) {
      this.resultPath = resultPath;
   }

   /**
    * Saves given result and identifies it by given jobId for later retrieval.
    *
    * @param jobId  identifier to store by
    * @param result result to store
    */
   @SneakyThrows
   public void save(String jobId, String result) {
      Files.write(Path.of(resultPath, jobId), result.getBytes());
   }

   /**
    * Reads result identified by given jobId.
    *
    * @param jobId job id
    * @return Optional<String> result with null value if there is no data for given jobId
    */
   public Optional<String> get(String jobId) {
      try {
         return Optional.of(Files.readString(Path.of(resultPath, jobId)));
      } catch (IOException e) {
         return Optional.empty();
      }
   }
}
