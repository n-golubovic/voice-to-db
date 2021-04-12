package net.etfbl.voicetodb.components;

import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

@Component
public class AudioConverter {

   private final Encoder encoder;
   private final EncodingAttributes encodingAttributes;

   @Autowired
   public AudioConverter() {
      encoder = new Encoder();
      encodingAttributes = new EncodingAttributes();
      encodingAttributes.setOutputFormat("wav");
      encodingAttributes.setAudioAttributes(new AudioAttributes()
            .setCodec("pcm_s16le")
            .setBitRate(128000)
            .setChannels(1)
            .setSamplingRate(16000)
      );
   }

   public void toVoskSupportedFormat(File input, File output) throws EncoderException {
      encoder.encode(new MultimediaObject(input), output, encodingAttributes);
   }

}
