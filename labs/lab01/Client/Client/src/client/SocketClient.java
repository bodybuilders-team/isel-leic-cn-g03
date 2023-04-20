package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketClient {

    static int Svcport = 80;
    //static String SvcIP="localhost";
    static String SvcIP = "34.175.170.137";

    //arg0: Server public IP
    //arg1: Server port
    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                SvcIP = args[0];
                Svcport = Integer.parseInt(args[1]);
            }
            Socket client = new Socket(SvcIP, Svcport);
            // Stream to write to
            PrintWriter outSock = new PrintWriter(client.getOutputStream(), true);
            // Stream to read from
            BufferedReader inSock = new BufferedReader(new InputStreamReader(client.getInputStream()));
            List<String> lines = LerNlinhas();
            long start = System.currentTimeMillis();
            System.out.println("Start request: " + start);
            System.out.println("Sending request to " + client);
            outSock.println(packToString(lines));
            System.out.println(inSock.readLine());
            long end = System.currentTimeMillis();
            System.out.println("Operation completed in: " + (end - start) + " ms");
            client.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static List<String> LerNlinhas() throws IOException {
        System.out.println("Introduce text lines with separated words and finish with a blank line");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<String> lines = new ArrayList<String>();
        for (; ; ) {
            String line = br.readLine();
            if (line.length() == 0)
                break;
            lines.add(line);
        }
        return lines;
    }

    static String packToString(List<String> lines) {
        String aux = "";
        for (String s : lines) {
            aux += s + " ";
        }
        return aux;
    }
}
