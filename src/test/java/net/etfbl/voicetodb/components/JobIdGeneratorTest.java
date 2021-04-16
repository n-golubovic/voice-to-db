package net.etfbl.voicetodb.components;

import org.junit.jupiter.api.Test;

import java.time.Clock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class JobIdGeneratorTest {

   private final Clock clock = mock(Clock.class);
   private final JobIdGenerator jobIdGenerator = new JobIdGenerator(clock);

   @Test
   void generate_calledClockMillisMethod() {
      when(clock.millis()).thenReturn(1234L);

      assertEquals("1234", jobIdGenerator.generate());
      verify(clock, times(1)).millis();
   }
}
