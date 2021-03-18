package net.etfbl.voicetodb.controllers;


import net.etfbl.voicetodb.models.FileUploadResponse;
import net.etfbl.voicetodb.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class FileUploadController {

   private final FileUploadService fileUploadService;

   @Autowired
   public FileUploadController(FileUploadService fileUploadService) {
      this.fileUploadService = fileUploadService;
   }

   @PostMapping("/upload")
   public FileUploadResponse uploadFiles(@RequestParam("files") List<MultipartFile> files) {
      return new FileUploadResponse(fileUploadService.save(files));
   }
}
