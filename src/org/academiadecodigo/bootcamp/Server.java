package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int myPort = 9073;
    private boolean serverLive;
    private ServerSocket serverSocket;
    private Socket clientSocket;


    public Server(){

            //Thread thread = new Thread(new ConnectionHandler(myPort));
            //thread.start();

        createServer(myPort);
        while (true) {
            waitingConnection();
        }

    }

    private void createServer(int port) {


            serverLive = true;

            try {
                serverSocket = new ServerSocket(myPort);

                while (serverLive) {

                    clientSocket = serverSocket.accept();
                    Thread thread = new Thread(new ConnectionHandler(serverLive,serverSocket,clientSocket));
                    thread.start();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        /*while (serverLive) {
            try {

                clientSocket = serverSocket.accept();

                request = null;
                request = ConnectionHandler.requestReader(clientSocket);
                parsedRequest = ConnectionHandler.parseRequest(request);

                if ( request != null ) {
                    ConnectionHandler.respond(parsedRequest, parsedRequest, clientSocket);
                }

            } catch (IOException e) {
                e.getMessage();
            }
        }*/

    }

    private void waitingConnection() {

    }


}
