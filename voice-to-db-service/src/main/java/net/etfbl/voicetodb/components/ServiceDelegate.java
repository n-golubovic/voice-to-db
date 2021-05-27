package net.etfbl.voicetodb.components;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceDelegate {

   @Bean
   public Clock clock() {
      return Clock.systemDefaultZone();
   }
}
