package net.etfbl.voicetodb.services;

import net.etfbl.voicetodb.components.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class VoiceToTextService {

   private final JobQueue queue;
   private final MultipartFileStorage storage;
   private final ResultStorage resultStorage;
   private final TextProcessor textProcessor;
   private final PythonRunner pythonRunner;

   @Autowired
   public VoiceToTextService(JobQueue queue,
                             MultipartFileStorage storage,
                             ResultStorage resultStorage,
                             TextProcessor textProcessor,
                             PythonRunner pythonRunner) {
      this.queue = queue;
      this.storage = storage;
      this.resultStorage = resultStorage;
      this.textProcessor = textProcessor;
      this.pythonRunner = pythonRunner;
   }

   @Scheduled(fixedRate = 1000)
   public void checkForNewJobs() {
      if (queue.hasJobs()) {
         String jobId = queue.getJob().getJobId();
         List<String> texts = storage.load(jobId).stream()
               .map(File::getAbsolutePath)
               .map(pythonRunner::runAndListenScript)
               .flatMap(Collection::stream)
               .collect(Collectors.toList());

         resultStorage.save(jobId, textProcessor.process(texts));
         storage.delete(jobId);
      }
   }

}
