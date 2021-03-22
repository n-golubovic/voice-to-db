package net.etfbl.voicetodb.services;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import net.etfbl.voicetodb.components.JobQueue;
import net.etfbl.voicetodb.components.MultipartFileStorage;
import net.etfbl.voicetodb.components.ReportStorage;
import net.etfbl.voicetodb.components.TextProcessor;
import net.etfbl.voicetodb.components.VoiceToTextProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class VoiceToTextService {

   private final JobQueue queue;
   private final MultipartFileStorage storage;
   private final ReportStorage reportStorage;
   private final VoiceToTextProcessor processor;
   private final TextProcessor textProcessor;

   @Autowired
   public VoiceToTextService(JobQueue queue,
                             MultipartFileStorage storage,
                             ReportStorage reportStorage,
                             VoiceToTextProcessor voiceToTextProcessor,
                             TextProcessor textProcessor) {
      this.queue = queue;
      this.storage = storage;
      this.reportStorage = reportStorage;
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

         reportStorage.save(jobId, textProcessor.process(texts));
      }
   }

   @SneakyThrows
   private static FileInputStream load(File file) {
      return new FileInputStream(file);
   }

}
