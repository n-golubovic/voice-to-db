package net.etfbl.voicetodb.controllers;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import net.etfbl.voicetodb.components.ResultStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@code ResultController} allows retrieval of results for processed requests.
 */
@RestController
public class ResultController {

   private final ResultStorage storage;

   @Autowired
   public ResultController(ResultStorage storage) {
      this.storage = storage;
   }

   /**
    * Attempts to retrieve response for previously submitted job. Returns {@code NOT FOUND} if the job is not yet
    * processed or if it doesn't exist.
    *
    * @param response  http response
    * @param requestId request id received through job submit
    * @return processed text
    */
   @CrossOrigin("*")
   @GetMapping(value = "/result", produces = MediaType.TEXT_PLAIN_VALUE)
   public String getResult(HttpServletResponse response,
                           @RequestParam String requestId) {
      try {
         return storage.get(requestId);
      } catch (IOException e) {
         response.setStatus(SC_NOT_FOUND);
         return null;
      }
   }

}
