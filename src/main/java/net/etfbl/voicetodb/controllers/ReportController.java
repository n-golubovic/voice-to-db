package net.etfbl.voicetodb.controllers;

import net.etfbl.voicetodb.components.ResultStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

@RestController
public class ReportController {

   private final ResultStorage storage;

   @Autowired
   public ReportController(ResultStorage storage) {
      this.storage = storage;
   }

   @CrossOrigin("*")
   @GetMapping(value = "/report", produces = MediaType.TEXT_PLAIN_VALUE)
   public String getReport(HttpServletResponse response,
                           @RequestParam String jobId) {
      try {
         return storage.get(jobId);
      } catch (IOException e) {
         response.setStatus(SC_NOT_FOUND);
         return null;
      }
   }

}
