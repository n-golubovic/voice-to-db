package net.etfbl.voicetodb.components;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ResultStorage {

   private final String resultPath;

   public ResultStorage(@Value("${voice-to-db.result-root-directory}") String resultPath) {
      this.resultPath = resultPath;
   }

   @SneakyThrows
   public void save(String jobId, String result) {
      Files.write(Path.of(resultPath, jobId), result.getBytes());
   }

   public String get(String jobId) throws IOException {
      return Files.readString(Path.of(resultPath, jobId));
   }
}
