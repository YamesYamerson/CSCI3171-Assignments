import java.io.*;
import java.net.*;
import java.util.Scanner;

public class EchoClient {
    public static void main(String[] args) {
        final int serverPort = 8000;

        //Prints out line to system that client has been started
        System.out.println("[!ECHO CLIENT!]: ");

        //Try block checks to see if cient can co nnect to server
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            Socket socket = new Socket(localhost,serverPort);
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //Notifies client that they are connected to server
            System.out.println("[!CLIENT SOCKET CONNECTED TO SERVER!: ]");

            //Takes client system input
            Scanner scanner  = new Scanner(System.in);
            while(true){
                System.out.println(("Enter Your Message Here:"));
                String input = scanner.nextLine();
                if("exit".equalsIgnoreCase(input) || "quit".equalsIgnoreCase(input)){
                    break;
                }
                output.println(input);
                String response = bufferedReader.readLine();
                System.out.println("[!SERVER RESPONSE!]: "+response);
            }


        }

        catch (Exception e){
            e.printStackTrace();
        }
    }
}