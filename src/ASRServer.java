import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

//Audiomanagement für ARS Server
public class ASRServer {

    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) throws IOException, LineUnavailableException {
        ServerSocket ss = new ServerSocket(4997);
        Socket s = ss.accept();
        System.out.println("client connected");

        DataInputStream dIn = new DataInputStream(s.getInputStream());

        int length = dIn.readInt();
        if(length > 0) {

            byte[] receiver = new byte[length];
            dIn.readFully(receiver, 0, receiver.length);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(receiver);

            //inputstream für recognizer
            InputStream inputStream = new ByteArrayInputStream(receiver);
            AudioFormat format = new AudioFormat(8000, 16, 2, true, false);
            AudioInputStream audioInputStream2 = new AudioInputStream(byteArrayInputStream, format, receiver.length);

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
        }
    }
}
