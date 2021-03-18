package net.etfbl.voicetodb.controllers;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import java.io.InputStream;
import static java.lang.System.currentTimeMillis;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import service.TranscriberDemo;

@Slf4j
// @RestController
public class VoiceToTextController {

   private static final String DICTIONARY_PATH = "file:C:\\Users\\Chardash\\Desktop\\cmudict-en-us.dict";
   private static final String LANGUAGE_MODEL_PATH = "file:C:\\Users\\Chardash\\Desktop\\en-70k-0.2-pruned.lm";
   private static final String ACOUSTIC_MODEL_PATH = "file:C:\\Users\\Chardash\\Desktop\\cmusphinx-en-us-ptm-8khz-5.2";

   private final StreamSpeechRecognizer recognizer;

   @SneakyThrows
   @Autowired
   public VoiceToTextController() {
      long start = currentTimeMillis();
      Configuration configuration = new Configuration();
      configuration.setAcousticModelPath(ACOUSTIC_MODEL_PATH);
      configuration.setDictionaryPath(DICTIONARY_PATH);
      configuration.setLanguageModelPath(LANGUAGE_MODEL_PATH);

      recognizer = new StreamSpeechRecognizer(configuration);
      log.info("loading took {}ms", currentTimeMillis() - start);
   }

   @SneakyThrows
   @PostMapping("/eh")
   public void eh() {
      log.info("received request");
      long start = currentTimeMillis();

      InputStream stream = TranscriberDemo.class.getResourceAsStream("/eh.wav");
      recognizer.startRecognition(stream);
      SpeechResult result;
      while ((result = recognizer.getResult()) != null) {
         log.info("Hypothesis: {}", result.getHypothesis());
      }
      recognizer.stopRecognition();

      log.info("loading took {}ms", currentTimeMillis() - start);
   }

}
