package net.etfbl.voicetodb.components;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ReportStorage {

   private final Map<String, String> jobReports = new HashMap<>();

   public void save(String jobId, String result) {
      jobReports.put(jobId, result);
   }

   public String get(String jobId) {
      return jobReports.get(jobId);
   }

}
