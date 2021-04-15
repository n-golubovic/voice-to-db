package net.etfbl.voicetodb.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * {@code PythonRunner} is a wrapper for process runner that is designed to run and process python scripts. In this
 * case, it is implemented to run {@code vosk_voice.py} script, and additional generalization isn't needed. Python
 * executable is defined through the configuration.
 */
@Component
public class PythonRunner {

   private final String pythonExecutable;
   private final String scriptPath;
   private final ObjectMapper objectMapper;

   @Autowired
   public PythonRunner(@Value("${voice-to-db.python-executable}") String pythonExecutable,
                       @Value("${voice-to-db.vosk.script-path}") String scriptPath,
                       ObjectMapper objectMapper) {
      this.pythonExecutable = pythonExecutable;
      this.scriptPath = scriptPath;
      this.objectMapper = objectMapper;
   }

   /**
    * Runs Python implementation of Vosk and retrieves the result.
    *
    * @param fileName audio file to read and process
    * @return list of sentences decoded from the audio file
    */
   @SneakyThrows
   public List<String> runAndListenScript(String fileName) {
      ProcessBuilder builder = new ProcessBuilder();
      builder.command(pythonExecutable, "vosk_voice.py", fileName);
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


