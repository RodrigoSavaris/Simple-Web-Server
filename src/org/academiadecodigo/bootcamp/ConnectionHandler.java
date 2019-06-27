package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class ConnectionHandler implements Runnable{

    private final int myPort = 9073;

    private ServerSocket serverSocket;
    private Socket clientSocket;

    private String request;
    private String parsedRequest;

    private boolean serverLive;

    public ConnectionHandler(boolean serverLive, ServerSocket serverSocket, Socket clientSocket){
        this.serverLive = serverLive;
        this.serverSocket=serverSocket;
        this.clientSocket = clientSocket;
    }




    @Override
    public void run() {

        System.out.println(Thread.currentThread().getName());
        request = null;
        request = ConnectionHandler.requestReader(clientSocket);
        parsedRequest = ConnectionHandler.parseRequest(request);

        if ( request != null ) {
            ConnectionHandler.respond(parsedRequest, parsedRequest, clientSocket);
        }


    }

    public static String requestReader(Socket clientSocket){

        //System.out.println("Checking threads: "+Thread.currentThread().getName());

        try {

            BufferedReader bReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String request = bReader.readLine();
            //System.out.println("Initial request: "+request);
            return request;

        } catch (IOException e) {
            e.getMessage();
        }

        return null;
    }

    public static String parseRequest(String request) {

        String parsedRequest = request.split(" ")[1];

        //System.out.println("Parsed request: "+parsedRequest);

        if (parsedRequest.equals("/")) {
            parsedRequest = "/index.html";
        }
        //System.out.println("The final request form is: "+parsedRequest);

        return parsedRequest;

    }

    public static void respond(String clientRequest, String parsedRequest, Socket clientSocket) {

        File file = new File("www"+clientRequest);

        if (!file.exists()) {
            file = new File("www/NotFound.html");
        }

        //System.out.println("The selected file is "+file.toString());
        String fileExtension  = parsedRequest.split("\\.")[parsedRequest.split("\\.").length-1];
        //System.out.println("The extension is: "+fileExtension);

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

            outputStream.close();

        } catch (FileNotFoundException e){
            e.getMessage();
        } catch (IOException e){
            e.getMessage();
        }




    }


}
