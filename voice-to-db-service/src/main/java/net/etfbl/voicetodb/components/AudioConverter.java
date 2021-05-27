package net.etfbl.voicetodb.components;

import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

/**
 * {@code AudioConvertor} utilizes FFMPEG to convert many types of audio files into Vosk-compatible format. This class
 * attempts to convert any audio file into wav file with sampling rate of 16k and mono channel, as Vosk requires.
 */
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

   /**
    * Encodes audio file denoted by given input parameter, and stores it into file denoted by given output parameter.
    * Input and output cannot point to the same file.
    *
    * @param input  input audio file
    * @param output output file
    * @throws EncoderException if input file is not supported audio file
    */
   public void toVoskSupportedFormat(File input, File output) throws EncoderException {
      encoder.encode(new MultimediaObject(input), output, encodingAttributes);
   }

}
