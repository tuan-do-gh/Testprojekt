import javax.sound.sampled.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

//Audiomanagement für TTS Client
public class Client {

    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        Socket s = new Socket("localhost", 4999);

        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println("is it working?");
        pr.flush();

        /*InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);

        String str = bf.readLine();
        System.out.println("server: " + str);*/

        PrintWriter pr2 = new PrintWriter(s.getOutputStream());
        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine();
        pr.println(response);
        pr.flush();


        DataInputStream dIn = new DataInputStream(s.getInputStream());

        int length = dIn.readInt();
        if(length > 0) {
            /*byte[] receiver = new byte[length];
            dIn.readFully(receiver, 0, receiver.length);
            String string = new String(receiver);
            System.out.println(string);*/

            byte[] receiver = new byte[length];
            dIn.readFully(receiver, 0, receiver.length);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(receiver);



            //AudioInputStream audioStream = AudioSystem.getAudioInputStream(byteArrayInputStream);
            //AudioFormat audioFormat = audioStream.getFormat();
            AudioFormat audioFormat = new AudioFormat(8000, 16, 2, true, false);
            System.out.println("sampleRate: " + audioFormat.getSampleRate());
            System.out.println("sampleSizeInBits: " + audioFormat.getSampleSizeInBits());
            AudioInputStream audioInputStream2 = new AudioInputStream(byteArrayInputStream, audioFormat, receiver.length);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();

            byte[] bufferBytes = new byte[BUFFER_SIZE];
            int readBytes = -1;
            while ((readBytes = audioInputStream2.read(bufferBytes)) != -1) {
                sourceDataLine.write(bufferBytes, 0, readBytes);
            }

            sourceDataLine.drain();
            sourceDataLine.close();
            audioInputStream2.close();
        }

        //Audiotest
        /*AudioInputStream audioStream = AudioSystem.getAudioInputStream(inputStream);
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
        audioStream.close();*/
    }
}
