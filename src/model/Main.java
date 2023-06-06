package model;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Main {

    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        TextToSpeech tts = new TextToSpeech();
        //tts.speak("Hello World!", 1.0f, false, false);

        AudioInputStream inputStream = tts.getAudioInputStream("Hello World");

        //Convert to byte array and back
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

        //AudioInputStream audioStream = AudioSystem.getAudioInputStream(byteArrayInputStream);
        //AudioFormat audioFormat = audioStream.getFormat();
        AudioFormat audioFormat = new AudioFormat(44100, 16, 2, true, false);
        AudioInputStream audioInputStream2=new AudioInputStream(byteArrayInputStream, audioFormat, audioBytes.length);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
        sourceDataLine.open(audioFormat);
        sourceDataLine.start();

        byte[] bufferBytes = new byte[BUFFER_SIZE];
        int readBytes = -1;
        while ((readBytes = inputStream.read(bufferBytes)) != -1) {
            sourceDataLine.write(bufferBytes, 0, readBytes);
        }

        sourceDataLine.drain();
        sourceDataLine.close();
        inputStream.close();
    }
}
