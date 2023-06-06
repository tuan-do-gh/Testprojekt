import model.TextToSpeech;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class ByteArrayAudioInputStream {
    private SourceDataLine sLine;
    private AudioFormat audioFormat;
    private AudioInputStream audioInputStream;
    private AudioInputStream audioInputStream2;
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ;
    private File file = new File("src/file_example_WAV.wav");


    ByteArrayAudioInputStream() {

        //AudioInputStream von Datei bekommen
        try {
            audioInputStream = AudioSystem.getAudioInputStream(file);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //statt Datei Audio von TTS benutzen
        TextToSpeech tts = new TextToSpeech();
        audioInputStream = tts.getAudioInputStream("Hello World");

        AudioFileFormat.Type targetType = AudioFileFormat.Type.WAVE;

        //ByteArray aus AudioInputStream erstellen
        try {
            AudioSystem.write(audioInputStream, targetType, byteArrayOutputStream);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Finally block");
            try {
                audioInputStream.close();
                System.out.println("Stream closed.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //Aus ByteArray ein AudioInputStream erstellen
        System.out.println("Größe des outputStreams : " + byteArrayOutputStream.size());
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        System.out.println("Größe des arrays : " + byteArray.length);
        ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);

        audioFormat = new AudioFormat(8000, 16, 2, true, false);
        //AudioFormat audioFormat = audioInputStream.getFormat();

        System.out.println("sampleRate: " + audioFormat.getSampleRate());
        System.out.println("sampleSizeInBits: " + audioFormat.getSampleSizeInBits());
        audioInputStream2 = new AudioInputStream(bis, audioFormat, byteArray.length);

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            //System.out.println(info);
            sLine = (SourceDataLine) AudioSystem.getLine(info);
            System.out.println(sLine.getLineInfo());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            sLine.open(audioFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sLine.start();
        System.out.println("Line Started");


        //Audio abspielen
        try {
            byte bytes[] = new byte[1024];
            int bytesRead = 0;
            int loop = 0;
            while ((bytesRead = audioInputStream2.read(bytes, 0, bytes.length)) != -1) {

                try {
                    sLine.write(bytes, 0, bytesRead);
                    System.out.println(loop);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                loop += 1;
            }
            System.out.println("Keine Bytes");

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Line stopped");
    }

    public static void main(String[] args) throws Exception {
        ByteArrayAudioInputStream s = new ByteArrayAudioInputStream();
    }
}