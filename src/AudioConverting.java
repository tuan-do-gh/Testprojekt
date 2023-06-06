import javax.sound.sampled.*;
import java.io.*;

public class AudioConverting {

    private static final int BUFFER_SIZE = 4096;

    public AudioConverting() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("file_example_WAV.wav");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int read;
        byte[] buff = new byte[1024];
        while ((read = inputStream.read(buff)) > 0)
        {
            outputStream.write(buff, 0, read);
        }
        outputStream.flush();
        byte[] audioBytes = outputStream.toByteArray();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioBytes);



        AudioInputStream audioStream = AudioSystem.getAudioInputStream(byteArrayInputStream);
        AudioFormat audioFormat = audioStream.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
        sourceDataLine.open(audioFormat);
        sourceDataLine.start();

        byte[] bufferBytes = new byte[BUFFER_SIZE];
        int readBytes = -1;
        while ((readBytes = audioStream.read(bufferBytes)) != -1) {
            sourceDataLine.write(bufferBytes, 0, readBytes);
        }

        sourceDataLine.drain();
        sourceDataLine.close();
        audioStream.close();
    }

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        AudioConverting audioConverting = new AudioConverting();

        /*InputStream inputStream = getClass().getClassLoader().getResourceAsStream("file_example_WAV.wav");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);*/
    }
}
