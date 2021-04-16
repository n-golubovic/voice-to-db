package net.etfbl.voicetodb.components;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ServiceDelegate {

   @Bean
   public Clock clock() {
      return Clock.systemDefaultZone();
   }
}
