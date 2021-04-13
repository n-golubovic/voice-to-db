package net.etfbl.voicetodb.services;

import net.etfbl.voicetodb.components.AudioStorage;
import net.etfbl.voicetodb.components.JobQueue;
import net.etfbl.voicetodb.models.Job;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JobRequeueServiceTest {

   private final JobQueue jobQueue = new JobQueue();
   private final AudioStorage audioStorage = mock(AudioStorage.class);

   private final JobRequeueService service = new JobRequeueService(jobQueue, audioStorage);

   @Test
   void processAvailableFiles_shouldSubmitJobsToQueue() {
      when(audioStorage.listAll()).thenReturn(List.of("1", "2"));

      service.processAvailableFiles();

      assertEquals(2, jobQueue.size());
      assertThat(jobQueue, containsInAnyOrder(new Job("1"), new Job("2")));
   }

}
