import model.TextToSpeech;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.InputStream;


//Audiomanagement für TTS Server
public class Server {
    public Server() throws IOException, UnsupportedAudioFileException {
        ServerSocket ss = new ServerSocket(4999);
        Socket s = ss.accept();
        System.out.println("client connected");

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);

        String str = bf.readLine();
        System.out.println("client: " + str);

        InputStreamReader in2 = new InputStreamReader(s.getInputStream());
        BufferedReader bf2 = new BufferedReader(in);

        String str2 = bf.readLine();
        System.out.println("client: " + str2);

        /*PrintWriter pr = new PrintWriter(s.getOutputStream());
        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine();

        pr.println(response);
        pr.flush();*/

        /*InputStream inputStream = getClass().getClassLoader().getResourceAsStream("file_example_WAV.wav");

        AudioInputStream audioStream = AudioSystem.getAudioInputStream(inputStream);*/

        TextToSpeech tts = new TextToSpeech();
        AudioInputStream audioStream = tts.getAudioInputStream(str2);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        AudioFileFormat.Type targetType = AudioFileFormat.Type.WAVE;
        try {
            AudioSystem.write(audioStream, targetType, outputStream);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Finally block");
            try {
                audioStream.close();
                System.out.println("Stream closed.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        /*int read;
        byte[] buff = new byte[1024];
        while ((read = inputStream.read(buff)) > 0)
        {
            outputStream.write(buff, 0, read);
        }
        outputStream.flush();*/
        byte[] audioBytes = outputStream.toByteArray();

        /*String byteSender = "Hello World";

        byte[] byteArray = byteSender.getBytes();*/
        DataOutputStream dOut = new DataOutputStream(s.getOutputStream());
        dOut.writeInt(audioBytes.length);
        dOut.write(audioBytes);

    }

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {
        Server s = new Server();



        /*String response = "Hello World";
        System.out.println("Vorher: "+ response);
        byte[] byteArray = response.getBytes();

        String string = new String(byteArray);
        System.out.println("Nachher: "+ string);*/

    }
}
