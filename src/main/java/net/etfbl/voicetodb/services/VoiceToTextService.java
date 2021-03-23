package net.etfbl.voicetodb.services;

import lombok.SneakyThrows;
import net.etfbl.voicetodb.components.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class VoiceToTextService {

   private final JobQueue queue;
   private final MultipartFileStorage storage;
   private final ResultStorage resultStorage;
   private final VoiceToTextProcessor processor;
   private final TextProcessor textProcessor;

   @Autowired
   public VoiceToTextService(JobQueue queue,
                             MultipartFileStorage storage,
                             ResultStorage resultStorage,
                             VoiceToTextProcessor voiceToTextProcessor,
                             TextProcessor textProcessor) {
      this.queue = queue;
      this.storage = storage;
      this.resultStorage = resultStorage;
      this.processor = voiceToTextProcessor;
      this.textProcessor = textProcessor;
   }

   @Scheduled(fixedRate = 1000)
   public void checkForNewJobs() {
      if (queue.hasJobs()) {
         String jobId = queue.getJob().getJobId();
         List<String> texts = storage.load(jobId).stream()
               .map(VoiceToTextService::load)
               .map(processor::process)
               .collect(Collectors.toList());

         resultStorage.save(jobId, textProcessor.process(texts));
         storage.delete(jobId);
      }
   }

   @SneakyThrows
   private static FileInputStream load(File file) {
      return new FileInputStream(file);
   }

}
