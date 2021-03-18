package net.etfbl.voicetodb.components;

import org.springframework.stereotype.Component;

@Component
public class JobIdGenerator {

   public String generate() {
      return String.valueOf(System.currentTimeMillis());
   }
}
