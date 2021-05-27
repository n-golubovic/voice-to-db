package net.etfbl.voicetodb.services;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.io.File;
import java.util.List;
import net.etfbl.voicetodb.components.AudioStorage;
import net.etfbl.voicetodb.components.JobQueue;
import net.etfbl.voicetodb.components.PythonRunner;
import net.etfbl.voicetodb.components.ResultStorage;
import net.etfbl.voicetodb.components.TextProcessor;
import net.etfbl.voicetodb.models.Job;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.slf4j.LoggerFactory;

class VoiceToTextServiceTest {

   private final JobQueue queue = new JobQueue();
   private final AudioStorage storage = mock(AudioStorage.class);
   private final ResultStorage resultStorage = mock(ResultStorage.class);
   private final TextProcessor textProcessor = mock(TextProcessor.class);
   private final PythonRunner pythonRunner = mock(PythonRunner.class);

   private final VoiceToTextService service = new VoiceToTextService(
         queue,
         storage,
         resultStorage,
         textProcessor,
         pythonRunner
   );

   private ListAppender<ILoggingEvent> listAppender;

   @BeforeEach
   void beforeEach() {
      Logger logger = (Logger) LoggerFactory.getLogger(VoiceToTextService.class);
      listAppender = new ListAppender<>();
      listAppender.start();
      logger.addAppender(listAppender);
   }

   @Test
   void checkForNewJobs_processAvailableJobs() {
      Job job = new Job("1");
      queue.add(job);

      File file1 = mock(File.class);
      File file2 = mock(File.class);

      when(file1.getAbsolutePath()).thenReturn("abs1");
      when(file2.getAbsolutePath()).thenReturn("abs2");
      when(storage.load(any())).thenReturn(List.of(file1, file2));
      when(pythonRunner.runAndListenScript("abs1")).thenReturn(List.of("result1"));
      when(pythonRunner.runAndListenScript("abs2")).thenReturn(List.of("result2", "result3"));
      when(textProcessor.process(List.of("result1", "result2", "result3"))).thenReturn("final result");

      service.checkForNewJobs();

      verify(resultStorage, times(1)).save("1", "final result");
      verify(storage, times(1)).delete("1");
      List<ILoggingEvent> logs = listAppender.list;
      assertEquals(1, logs.size());
      assertEquals("processed job with id 1", logs.get(0).getFormattedMessage());
      assertTrue(queue.isEmpty());
   }

   @Test
   void checkForNewJobs_noJobsInQueue_confirmNothingDone() {
      service.checkForNewJobs();

      verify(resultStorage, never()).save(any(), any());
   }

}
