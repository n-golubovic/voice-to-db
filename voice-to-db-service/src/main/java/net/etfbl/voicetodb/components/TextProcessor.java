package net.etfbl.voicetodb.components;

import java.util.List;
import org.springframework.stereotype.Component;

/**
 * {@code TextProcessor} performs and transformation or enrichment of plain text retrieved from audio-to-text
 * processing.
 */
@Component
public class TextProcessor {

   /**
    * Transforms a set of sentences into usable text.
    *
    * @param process list of sentences
    * @return usable text
    */
   public String process(List<String> process) {
      return String.join(". ", process) + (process.isEmpty() ? "" : ".");
   }

}
