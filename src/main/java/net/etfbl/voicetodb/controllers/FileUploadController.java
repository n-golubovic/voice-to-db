package net.etfbl.voicetodb.controllers;


import lombok.extern.slf4j.Slf4j;
import net.etfbl.voicetodb.models.FileUploadResponse;
import net.etfbl.voicetodb.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.*;

@Slf4j
@RestController
public class FileUploadController {

   private final FileUploadService fileUploadService;

   @Autowired
   public FileUploadController(FileUploadService fileUploadService) {
      this.fileUploadService = fileUploadService;
   }

   @CrossOrigin("*")
   @PostMapping("/upload")
   public FileUploadResponse uploadFiles(HttpServletResponse response,
                                         @RequestParam("files") List<MultipartFile> files) {
      files.removeIf(MultipartFile::isEmpty);
      files.removeIf(FileUploadController::notAudioFile);

      if (files.isEmpty()) {
         response.setStatus(SC_BAD_REQUEST);
         return FileUploadResponse.EMPTY;
      }

      try {
         return new FileUploadResponse(fileUploadService.save(files));
      } catch (IOException e) {
         response.setStatus(SC_INTERNAL_SERVER_ERROR);
         log.error("Error occurred while handling a request", e);
      } catch (EncoderException e) {
         response.setStatus(SC_UNSUPPORTED_MEDIA_TYPE);
         log.error("Error occurred while handling a request", e);
      }

      return FileUploadResponse.EMPTY;
   }

   private static boolean notAudioFile(MultipartFile file) {
      return file.getContentType() != null && !file.getContentType().matches("audio/.*");
   }

}
