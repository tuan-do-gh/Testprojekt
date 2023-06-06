
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.*;

public class Recorder {

    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) {
        //AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
        AudioFormat format = new AudioFormat(8000, 16, 2, true, false);
        TargetDataLine microphone;
        SourceDataLine speakers;
        try {
            microphone = AudioSystem.getTargetDataLine(format);

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int numBytesRead;
            int CHUNK_SIZE = 1024;
            byte[] data = new byte[microphone.getBufferSize() / 5];
            microphone.start();

            int bytesRead = 0;
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            speakers.open(format);
            speakers.start();
            while (bytesRead < 100000) {
                numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
                bytesRead += numBytesRead;
                // write the mic data to a stream for use later
                out.write(data, 0, numBytesRead);
                // write mic data to stream for immediate playback
                speakers.write(data, 0, numBytesRead);
            }
            speakers.drain();
            speakers.close();
            microphone.close();


            //try to convert to inputstream
            byte[] audioBytes = out.toByteArray();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioBytes);

            //inputstream für recognizer
            InputStream inputStream = new ByteArrayInputStream(audioBytes);

            AudioInputStream audioInputStream2 = new AudioInputStream(byteArrayInputStream, format, audioBytes.length);

            // Configuration
            Configuration configuration = new Configuration();

            // Load model from the jar
            configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
            configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
            // Grammar
            configuration.setGrammarPath("resource:/grammars");
            configuration.setGrammarName("grammar");
            configuration.setUseGrammar(true);

            StreamSpeechRecognizer streamSpeechRecognizer = new StreamSpeechRecognizer(configuration);
            streamSpeechRecognizer.startRecognition(inputStream);
            SpeechResult result = streamSpeechRecognizer.getResult();
            while ((result = streamSpeechRecognizer.getResult()) != null) {
                System.out.println(result.getHypothesis());
            }
            streamSpeechRecognizer.stopRecognition();

            DataLine.Info info2 = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info2);
            sourceDataLine.open(format);
            sourceDataLine.start();

            byte[] bufferBytes = new byte[BUFFER_SIZE];
            int readBytes = -1;
            while ((readBytes = audioInputStream2.read(bufferBytes)) != -1) {
                sourceDataLine.write(bufferBytes, 0, readBytes);
            }

            sourceDataLine.drain();
            sourceDataLine.close();
            audioInputStream2.close();

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
