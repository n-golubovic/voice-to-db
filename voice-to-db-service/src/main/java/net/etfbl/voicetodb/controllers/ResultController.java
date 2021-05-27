package net.etfbl.voicetodb.controllers;

import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import net.etfbl.voicetodb.components.ResultStorage;
import net.etfbl.voicetodb.models.ResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
   @GetMapping(value = "/result")
   public ResultResponse getResult(HttpServletResponse response,
                           @RequestParam String requestId) {
      Optional<String> result = storage.get(requestId);

      response.setStatus(result.isPresent() ? SC_OK : SC_NOT_FOUND);
      return new ResultResponse(result.orElse(null));
   }

}
