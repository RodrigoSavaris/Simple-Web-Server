package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final int myPort = 9073;


    public Server(){


        int howManyThreads = 4;


        for (int i = 0; i<howManyThreads; i++) {

            Thread thread = new Thread(new ConnectionHandler(myPort));
            thread.setName("Server connection handler number: "+(i+1));
            //thread.start();


        }


    }

}
