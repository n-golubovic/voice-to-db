package net.etfbl.voicetodb.controllers;


import net.etfbl.voicetodb.models.FileUploadResponse;
import net.etfbl.voicetodb.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

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

      return new FileUploadResponse(fileUploadService.save(files));
   }

   private static boolean notAudioFile(MultipartFile file) {
      return file.getContentType() != null && !file.getContentType().matches("audio/.*");
   }

}
