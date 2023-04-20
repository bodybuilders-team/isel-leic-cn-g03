package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SockServer {

    static int port = 0;

    //arg0: flag {s - sequential requests | c - concurrent requests}
    // arg1: port
    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                System.out.println("Usage: Server {s|c} port");
                System.exit(-1);
            }
            ServerSocket svcSocket = null;
            port = Integer.parseInt(args[1]);
            svcSocket = null;
            svcSocket = new ServerSocket(port);
            if (args[0].toLowerCase().compareTo("c") == 0) { // concurrent requests
                System.out.println("Server concurrent on port " + port);
                ExecutorService executor = Executors.newFixedThreadPool(5);
                int sessionId = 0;
                for (; ; ) {
                    System.out.println("Accepting new connections... ");
                    Socket client = svcSocket.accept();
                    System.out.println("New connection with... " + client);
                    sessionId++;
                    Runnable worker = new Session(client, sessionId);
                    executor.execute(worker);
                }
            } else {
                if (args[0].toLowerCase().compareTo("s") == 0) {// sequential requests
                    for (; ; ) {
                        try {
                            System.out.println("Server Serial on port " + port);
                            System.out.println("Accepting new connections... ");
                            //block until receive connection
                            Socket cliSocket = svcSocket.accept();
                            System.out.println("Serial Connection with " + cliSocket);
                            BufferedReader inStream = new BufferedReader(
                                    new InputStreamReader(cliSocket.getInputStream())
                            );
                            PrintWriter outStream = new PrintWriter(cliSocket.getOutputStream(), true);
                            String request = inStream.readLine();  //receive the request
                            String response = ProcessRequest.processar(request);
                            outStream.println("biggest word: " + response + " size = " + response.length()); // send the reply
                            cliSocket.close();
                        } catch (IOException ex) {
                            System.out.println("Server crashed!");
                            System.exit(-1);
                        }
                    }
                } else {
                    System.out.println("option shoud be s for sequential or c for concurrent requests");
                    System.exit(-1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
