import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {

        //Final keyword on a class/variable/method makes it so its value cannot be modified and cannot be overridden by any subclasses
        //ServerSocket is used by the server to listen to connection requests from client

        final Socket clientSocket; //Declaring an object which is an object of the class ServerSocket
        final BufferedReader in;   //Declaring an object which is an object of the class Socket
        final PrintWriter out;     //Declaring an object which is used to read data from the ClientSocket
        final Scanner scanner = new Scanner(System.in); //Declaring an object which is used to write data into the ClientSocket
        try {
            clientSocket = new Socket("127.0.0.1", 5000);
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Thread sender = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    while (true) {
                        msg = scanner.nextLine();
                        out.println(msg);
                        out.flush();
                    }
                }
            });
            sender.start();
            Thread receiver = new Thread(new Runnable() {
                String msg;

                @Override
                public void run() {
                    try {
                        msg = in.readLine();
                        while (msg != null) {
                            System.out.println("Server : " + msg);
                            msg = in.readLine();
                        }
                        System.out.println("Server out of service");
                        out.close();
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            receiver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}