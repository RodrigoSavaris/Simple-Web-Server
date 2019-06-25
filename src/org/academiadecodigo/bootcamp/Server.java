package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class Server {

    private int myPort = 9073;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader bReader;
    private String request;
    private String parsedRequest;
    private File file;
    private OutputStream outputStream;
    private BufferedWriter bufferedWriter;
    private double fileSize;
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;
    private String fileExtension;

    public Server(){

        try {

            serverSocket = new ServerSocket(myPort);
            clientSocket = serverSocket.accept();

            requestReader();
            parseRequest();
            respond(parsedRequest);

        } catch (IOException e){

            e.getMessage();

        }
    }

    public void requestReader() {

        try {

            bReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            request = bReader.readLine();
            System.out.println("Initial request: "+request);

        } catch (IOException e) {
            e.getMessage();
        }
    }

    public void parseRequest(){

        parsedRequest = request.split(" ")[1];

        System.out.println("Parsed request: "+parsedRequest);

        if (parsedRequest.equals("/")) {
            parsedRequest = "/index.html";
        }
        System.out.println("The final request form is: "+parsedRequest);


    }

    private void respond(String clientRequest) {

        file = new File("www"+clientRequest);
        System.out.println("The selected file is "+file.toString());
        fileExtension  = parsedRequest.split("\\.")[parsedRequest.split("\\.").length-1];
        System.out.println(fileExtension);

        fileSize = file.length();

        try {
            fileInputStream = new FileInputStream(file);
            outputStream = clientSocket.getOutputStream();

            outputStream.write("HTTP/1.0 200 Document Follows\r\n".getBytes());
            switch (fileExtension) {

                case "html":
                    outputStream.write("Content-Type: text/html; charset=UTF-8\r\n".getBytes());
                    break;

                case "jpeg":
                    outputStream.write("Content-Type: image/jpeg \\r\\n".getBytes());
                    break;

                case "gif":
                    outputStream.write("Content-Type: image/gif \\r\\n".getBytes());
                    break;
            }

            outputStream.write(("Content-Length: "+fileSize+" \r\n").getBytes());
            outputStream.write("\r\n".getBytes());

            byte[] fileData = Files.readAllBytes(file.toPath());

            outputStream.write(fileData);

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
