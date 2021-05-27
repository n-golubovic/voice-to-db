package net.etfbl.voicetodb.components;

import java.time.Clock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * {@code JobGenerator} provides a singular mean of generating pseudo-unique ids for requests.
 */
@Component
public class JobIdGenerator {

   private Clock clock;

   @Autowired
   public JobIdGenerator(Clock clock) {
      this.clock = clock;
   }

   /**
    * Generates a pseudo-random unique id based on current time.
    *
    * @return random string for identification
    */
   public String generate() {
      return String.valueOf(clock.millis());
   }
}
