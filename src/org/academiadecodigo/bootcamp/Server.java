package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class Server {

    private final int myPort = 9073;

    private ServerSocket serverSocket;
    private Socket clientSocket;

    private String request;
    private String parsedRequest;

    private boolean serverLive;

    public Server(){

        serverLive = true;

        try {
            serverSocket = new ServerSocket(myPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (serverLive) {
            try {

                clientSocket = serverSocket.accept();

                serverProcess();

            } catch (IOException e) {

                e.getMessage();

            }
        }
    }

    public void serverProcess(){

        //I tried a while loop here.

        /*try {

            //serverSocket = new ServerSocket(myPort);
            clientSocket = serverSocket.accept();

        } catch (IOException e){
            e.getMessage();
        }*/

        request = null;
        requestReader();
        parseRequest();
        if ( request != null) {
            respond(parsedRequest);
        }

    }

    public void requestReader() {

        try {

            BufferedReader bReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            request = bReader.readLine();
            //close(bReader);
            System.out.println("Initial request: "+request);

        } catch (IOException e) {
            e.getMessage();
        }
    }

    public void parseRequest(){
        //array low
        parsedRequest = request.split(" ")[1];

        System.out.println("Parsed request: "+parsedRequest);

        if (parsedRequest.equals("/")) {
            parsedRequest = "/index.html";
        }
        System.out.println("The final request form is: "+parsedRequest);


    }

    private void respond(String clientRequest) {

        File file = new File("www"+clientRequest);

        if (!file.exists()) {
            file = new File("www/NotFound.html");
        }

        System.out.println("The selected file is "+file.toString());
        String fileExtension  = parsedRequest.split("\\.")[parsedRequest.split("\\.").length-1];
        System.out.println("The extension is: "+fileExtension);

        double fileSize = file.length();

        try {
            OutputStream outputStream = clientSocket.getOutputStream();

            outputStream.write("HTTP/1.0 200 Document Follows\r\n".getBytes());
            switch (fileExtension) {

                case "html":
                    outputStream.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
                    break;

                case "jpeg":
                    outputStream.write("Content-Type: image/jpeg \r\n".getBytes());
                    break;

                case "jpg":
                    outputStream.write("Content-Type: image/jpg \r\n".getBytes());
                    break;

                case "png":
                    outputStream.write("Content-Type: image/png \r\n".getBytes());
                    break;

                case "gif":
                    outputStream.write("Content-Type: image/gif \r\n".getBytes());
                    break;
            }

            outputStream.write(("Content-Length: "+fileSize+" \r\n").getBytes());
            outputStream.write("\r\n".getBytes());

            byte[] fileData = Files.readAllBytes(file.toPath());

            outputStream.write(fileData);

            close(outputStream);

        } catch (FileNotFoundException e){
            e.getMessage();
        } catch (IOException e){
            e.getMessage();
        }




    }


    public void close( Closeable whatToClose ){

        try {

            whatToClose.close();

        } catch (IOException e){

            e.getMessage();

        }

    }

}
