package net.etfbl.voicetodb.components;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TextProcessor {

   public String process(List<String> process) {
      return String.join(". ", process);
   }

}
