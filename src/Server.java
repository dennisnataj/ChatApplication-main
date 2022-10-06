import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {

        //Final keyword on a class/variable/method makes it so its value cannot be modified and cannot be overridden by any subclasses
        //ServerSocket is used by the server to listen to connection requests from client

        final ServerSocket serverSocket; // Declaring an object which is an object of the class ServerSocket
        final Socket clientSocket; //Declaring an object which is an object of the class Socket
        final BufferedReader in; //Declaring an object which is used to read data from the ClientSocket
        final PrintWriter out; //Declaring an object which is used to write data into the ClientSocket

        Scanner scanner = new Scanner(System.in); // Able to read data from that the user types in from keyboard

        try { //Try is a block of code that will check the code for errors while being executed
            System.out.println("Enter Your Message: ");
            serverSocket = new ServerSocket(5000); //Port number that the server will use to listen to clients requests
            clientSocket = serverSocket.accept(); // Accept() = Accepts the request coming from the server
            out = new PrintWriter(clientSocket.getOutputStream()); //Instantiate out so that it is ready to write data into ClientSocket
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //Instantiate in so that it will read from ClientSocket

            Thread sender = new Thread(new Runnable() {
                String msg; //This variable will contain the message from the user

                @Override //annotation to override the run method
                public void run() {
                    while (true) {
                        msg = scanner.nextLine(); //reads data that the user inputs
                        out.println(msg);//write data stored in msg in the clientSocket
                        out.flush(); //forces the sending of the data
                    }
                }
            });
            sender.start();

            Thread receive = new Thread(new Runnable() {
                String msg;

                @Override
                public void run() {
                    try {
                        msg = in.readLine(); //Read data from clientSocket using "in"
                        //While the client is still connected to the server
                        while (msg != null) {
                            System.out.println("Client: " + msg); //print the message to the screen sent by client
                            msg = in.readLine(); //read data from the clientSocket using "in"
                        }
                        //if msg == null that means the client is not connected anymore
                        System.out.println("Client disconnected");
                        //Closing the sockets and streams
                        out.close();
                        clientSocket.close();
                        serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            receive.start();
        } catch (IOException e) { //Catches the error in the if it occurs
            e.printStackTrace();
        }

    }
}
