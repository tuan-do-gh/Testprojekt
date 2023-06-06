package testumgebung;
import model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Testserver {
    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(4998);
        Socket s = ss.accept();
        System.out.println("client connected");

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);

        String str = bf.readLine();
        System.out.println("client: " + str);
        TextToSpeech tts = new TextToSpeech();
        /*PrintWriter pr = new PrintWriter(s.getOutputStream());
        Scanner scanner = new Scanner(System.in);
        //System.out.println("Bitte Text eingeben: ");
        String response = scanner.nextLine();

        pr.println(response);
        pr.flush();*/

        tts.speak(str, 1.0f, false, false);


    }
}
