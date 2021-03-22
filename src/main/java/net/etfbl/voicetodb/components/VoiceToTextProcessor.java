package net.etfbl.voicetodb.components;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import java.io.InputStream;
import static java.lang.System.currentTimeMillis;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VoiceToTextProcessor {

   private final StreamSpeechRecognizer recognizer;

   @SneakyThrows
   @Autowired
   public VoiceToTextProcessor(@Value("${voice-to-db.sphinx4.dictionary-path}") String dictionaryPath,
                               @Value("${voice-to-db.sphinx4.language-model-path}") String languageModelPath,
                               @Value("${voice-to-db.sphinx4.acoustic-model-path}") String acousticModelPath) {
      long start = currentTimeMillis();
      Configuration configuration = new Configuration();
      configuration.setAcousticModelPath(acousticModelPath);
      configuration.setDictionaryPath(dictionaryPath);
      configuration.setLanguageModelPath(languageModelPath);

      recognizer = new StreamSpeechRecognizer(configuration);
      log.info("loading took {}ms", currentTimeMillis() - start);
   }


   @SneakyThrows
   public String process(InputStream stream) {
      log.info("received request");
      long start = currentTimeMillis();

      recognizer.startRecognition(stream);
      SpeechResult result;
      StringBuilder textResult = new StringBuilder();
      while ((result = recognizer.getResult()) != null) {
         textResult.append(result.getHypothesis()).append(". ");
      }

      recognizer.stopRecognition();

      log.info("processing took {}ms", currentTimeMillis() - start);
      return textResult.toString().trim();
   }

}
