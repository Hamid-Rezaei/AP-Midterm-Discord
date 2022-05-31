package server_side;

import server_side.controller.ServerController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(7788);
            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("Sath2");
                new Thread(()->{
                    try {
                        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        while (socket.isConnected()){
                            String input = objectInputStream.readUTF();
                            Object result = process(input);
                            if(result.equals(""))break;
                            objectOutputStream.writeObject(result);
                            objectOutputStream.flush();
                        }
                        objectOutputStream.close();
                        objectInputStream.close();
                        socket.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object process(String input){
        if(input.equals("login")){
            return ServerController.getUser();
        }
        return "";
    }

}
