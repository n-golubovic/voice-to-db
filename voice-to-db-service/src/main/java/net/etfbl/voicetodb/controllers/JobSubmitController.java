package net.etfbl.voicetodb.controllers;


import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE;
import lombok.extern.slf4j.Slf4j;
import net.etfbl.voicetodb.models.JobSubmitResponse;
import net.etfbl.voicetodb.services.JobSubmitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

/**
 * {@code JobSubmitController} is a rest-based controller that allows submitting audio-to-text requests.
 */
@Slf4j
@RestController
public class JobSubmitController {

   private final JobSubmitService jobSubmitService;

   @Autowired
   public JobSubmitController(JobSubmitService jobSubmitService) {
      this.jobSubmitService = jobSubmitService;
   }

   /**
    * Uploads files for processing. Returns {@code BAD REQUEST} if none of uploaded files are audio files, or if actual
    * audio files cannot be converted to compatible format. Returns {@code INTERNAL SERVER ERROR} if files cannot be
    * successfully stored.
    *
    * @param response http response
    * @param files    audio files to process
    * @return response containing id used to retrieve result once processed
    */
   @CrossOrigin("*")
   @PostMapping("/upload")
   public JobSubmitResponse uploadFiles(HttpServletResponse response,
                                        @RequestParam("files") List<MultipartFile> files) {
      files.removeIf(MultipartFile::isEmpty);
      files.removeIf(JobSubmitController::notAudioFile);

      if (files.isEmpty()) {
         response.setStatus(SC_BAD_REQUEST);
         return JobSubmitResponse.EMPTY;
      }

      try {
         return new JobSubmitResponse(jobSubmitService.save(files));
      } catch (IOException e) {
         response.setStatus(SC_INTERNAL_SERVER_ERROR);
         log.error("Error occurred while handling a request", e);
      } catch (EncoderException e) {
         response.setStatus(SC_UNSUPPORTED_MEDIA_TYPE);
         log.error("Error occurred while handling a request", e);
      }

      return JobSubmitResponse.EMPTY;
   }

   private static boolean notAudioFile(MultipartFile file) {
      return file.getContentType() == null || !file.getContentType().matches("audio/.*");
   }

}
