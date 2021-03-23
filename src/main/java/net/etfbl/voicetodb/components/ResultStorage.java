package net.etfbl.voicetodb.components;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class ResultStorage {

   private String resultPath;

   public ResultStorage(@Value("${voice-to-db.result-root-directory}") String resultPath) {
      this.resultPath = resultPath;
   }

   @SneakyThrows
   public void save(String jobId, String result) {
      Files.write(Path.of(resultPath, jobId), result.getBytes());
   }

   @SneakyThrows
   public String get(String jobId) {
      return Files.readString(Path.of(resultPath, jobId));
   }
}
