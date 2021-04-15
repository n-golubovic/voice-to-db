package net.etfbl.voicetodb.components;

import org.springframework.stereotype.Component;

/**
 * {@code JobGenerator} provides a singular mean of generating pseudo-unique ids for requests.
 */
@Component
public class JobIdGenerator {

   /**
    * Generates a pseudo-random unique id based on current time.
    *
    * @return random string for identification
    */
   public String generate() {
      return String.valueOf(System.currentTimeMillis());
   }
}
