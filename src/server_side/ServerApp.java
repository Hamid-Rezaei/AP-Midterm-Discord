package server_side;

import database.Database;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp {
    private final ServerSocket serverSocket;

    public ServerApp(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {

            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                System.out.println("A new Application has connected!");

/*                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                int choice = inputStream.readInt();
                if(choice == 1){
                    outputStream.writeObject(Database.retrieveFromDB());
                    outputStream.flush();
                }*/
                ServerController serverController = new ServerController(socket);
                Thread thread = new Thread(serverController);
                thread.start();
            }
        } catch (IOException e) {
            closeServerSocket();
        }
    }


    // Close the server socket gracefully.
    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Run the program.
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9988);
        ServerApp server = new ServerApp(serverSocket);
        server.startServer();
    }
}
