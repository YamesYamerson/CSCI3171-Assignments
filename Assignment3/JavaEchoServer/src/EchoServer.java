import java.io.*;
import java.net.*;

public class EchoServer {
    public static void main(String[] args) {
        final int serverPort = 8000;
        //System message indicating server is operational
        System.out.println("[!ECHO SERVER HAS STARTED!]");

        //This block attempts to connect the EchoServer to the Client
        try(ServerSocket echoServer = new ServerSocket(serverPort)){
            Socket clientSocket = echoServer.accept();
            System.out.println("[!SERVER SOCKET CONNECTED TO CLIENT!]");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(),true);
            String line;

            //While Buffered Reader is not empty, system prints output lines
            while((line = bufferedReader.readLine()) != null){
                System.out.println("[!SERVER!] :"+line);
                output.println(line);

            }
        }

        //This catch block returns a stacktrace if the connection is unsuccessful
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
