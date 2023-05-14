package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A session is a thread that handles a client connection.
 */
public class Session implements Runnable {
    Socket cliSocket;
    int id;
    BufferedReader inStream;
    PrintWriter outStream;

    public Session(Socket cliSocket, int id) {
        this.cliSocket = cliSocket;
        this.id = id;
        try {
            inStream = new BufferedReader(new InputStreamReader(cliSocket.getInputStream()));
            outStream = new PrintWriter(cliSocket.getOutputStream(), true);
        } catch (IOException e) {e.printStackTrace();}
    }

    @Override
    public void run() {
        try {
            String request = inStream.readLine();
            String response = ProcessRequest.processar(request);
            outStream.println("biggest word: " + response + " size = " + response.length());
            cliSocket.close();
            System.out.println("Session " + id + " terminates.");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Server session " + id + " crashed!");
        }
    }
}
