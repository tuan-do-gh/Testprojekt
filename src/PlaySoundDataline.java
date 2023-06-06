import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class PlaySoundDataline {

    private static final int BUFFER_SIZE = 4096;

    public PlaySoundDataline() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("file_example_WAV.wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(inputStream);
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

    public static void main(String[] args) throws Exception {
        PlaySoundDataline s = new PlaySoundDataline();
    }
}
