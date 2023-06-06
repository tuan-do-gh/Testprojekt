package testumgebung;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientTTS {

    public static void main(String[] args) throws IOException {
        Socket s = new Socket("localhost", 4998);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bitte Text eingeben: ");
        String response = scanner.nextLine();
        PrintWriter pr = new PrintWriter(s.getOutputStream());
        pr.println(response);
        pr.flush();

        /*InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);

        String str = bf.readLine();
        System.out.println("server: " + str);*/
    }
}
