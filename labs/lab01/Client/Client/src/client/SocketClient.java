package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Example of a simple socket client.
 */
public class SocketClient {

    static int Svcport = 80;
    static String SvcIP = "34.175.170.137"; // "localhost"

    /**
     * Entry point of the client.
     *
     * @param args Command line arguments.
     *             arg0: Server public IP
     *             arg1: Server port
     */
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

            List<String> lines = readLines();
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

    /**
     * Reads lines from the console.
     *
     * @return List of lines read from the console.
     * @throws IOException If an I/O error occurs.
     */
    static List<String> readLines() throws IOException {
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

    /**
     * Packs a list of strings into a single string.
     *
     * @param lines List of strings to pack.
     * @return A single string with all the strings in the list.
     */
    static String packToString(List<String> lines) {
        String aux = "";
        for (String s : lines) {
            aux += s + " ";
        }
        return aux;
    }
}
