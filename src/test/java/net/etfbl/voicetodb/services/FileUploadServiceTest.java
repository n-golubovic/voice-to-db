package net.etfbl.voicetodb.services;

import lombok.SneakyThrows;
import net.etfbl.voicetodb.components.AudioStorage;
import net.etfbl.voicetodb.components.JobIdGenerator;
import net.etfbl.voicetodb.components.JobQueue;
import net.etfbl.voicetodb.models.Job;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FileUploadServiceTest {

   private final AudioStorage fileStorage = mock(AudioStorage.class);
   private final JobQueue jobQueue = new JobQueue();
   private final JobIdGenerator jobIdGenerator = mock(JobIdGenerator.class);
   private final FileUploadService service = new FileUploadService(fileStorage, jobQueue, jobIdGenerator);

   @SuppressWarnings("unchecked")
   private final List<MultipartFile> files = mock(List.class);

   @SneakyThrows
   @Test
   void save_confirmServicesCalled() {
      when(jobIdGenerator.generate()).thenReturn("jobId");

      String jobId = service.save(files);

      assertEquals("jobId", jobId);
      verify(fileStorage, times(1)).save("jobId", files);
      assertEquals(1, jobQueue.size());
      assertEquals(new Job("jobId"), jobQueue.peek());
   }

}
