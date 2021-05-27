package net.etfbl.voicetodb.components;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class TextProcessorTest {

   private final TextProcessor textProcessor = new TextProcessor();

   @Test
   void process_validInput_returnsJoinedString() {
      List<String> input = Arrays.asList("sentence 1", "sentence 2");

      String result = textProcessor.process(input);

      assertEquals("sentence 1. sentence 2.", result);
   }

   @Test
   void process_forEmptyList_returnsEmptyString() {
      List<String> input = Collections.emptyList();

      String result = textProcessor.process(input);

      assertEquals("", result);
   }

}
