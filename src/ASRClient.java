import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

//Audiomanagement für ARS Client
public class ASRClient {

    public static void main(String[] args) throws IOException {

        Socket s = new Socket("localhost", 4997);

        //Audio über Mikrofon aufnehmen
        AudioFormat format = new AudioFormat(8000, 16, 2, true, false);
        TargetDataLine targetDataLine;
        SourceDataLine sourceDataLine;
        try {
            //targetDataLine = AudioSystem.getTargetDataLine(format);

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            targetDataLine.open(format);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int numBytesRead;
            int CHUNK_SIZE = 1024;
            byte[] data = new byte[targetDataLine.getBufferSize() / 5];
            targetDataLine.start();

            int bytesRead = 0;
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(format);
            sourceDataLine.start();
            while (bytesRead < 200000) {
                numBytesRead = targetDataLine.read(data, 0, CHUNK_SIZE);
                bytesRead += numBytesRead;
                //für das Umwandeln
                out.write(data, 0, numBytesRead);
                // für das sofortige abspielen
                sourceDataLine.write(data, 0, numBytesRead);
            }
            sourceDataLine.drain();
            sourceDataLine.close();
            targetDataLine.close();

            byte[] audioBytes = out.toByteArray();
            DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
            dOut.writeInt(audioBytes.length);
            dOut.write(audioBytes);

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}