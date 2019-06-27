package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final int myPort = 9073;
    private boolean serverLive;
    private ServerSocket serverSocket;
    private Socket clientSocket;


    public Server(){

        createServer(myPort);

    }

    private void createServer(int port) {


            serverLive = true;

        ExecutorService fixedPool = Executors.newFixedThreadPool(20);


            try {

                serverSocket = new ServerSocket(myPort);

                while (serverLive) {

                    clientSocket = serverSocket.accept();
                    fixedPool.submit(new ConnectionHandler(serverLive,serverSocket,clientSocket));

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
}
