package net.etfbl.voicetodb.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PythonRunner {

   private final String scriptPath;
   private final ObjectMapper objectMapper;

   @Autowired
   public PythonRunner(@Value("${voice-to-db.vosk.script-path}") String scriptPath, ObjectMapper objectMapper) {
      this.scriptPath = scriptPath;
      this.objectMapper = objectMapper;
   }

   @SneakyThrows
   public List<String> runAndListenScript(String fileName) {
      ProcessBuilder builder = new ProcessBuilder();
      builder.command("python.exe", "vosk_voice.py", fileName);
      builder.directory(new File(scriptPath));
      Process process = builder.start();

      List<String> result = objectMapper.readValue(process.getInputStream(), RecognitionResult.class);
      result.removeIf(String::isBlank);

      process.waitFor();

      return result;
   }

   static class RecognitionResult extends ArrayList<String> {
   }

}


