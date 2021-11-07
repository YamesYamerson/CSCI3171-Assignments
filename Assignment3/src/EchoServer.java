import java.net.*;
import java.io.*;
public class EchoServer{


	public static void main(String[] args) throws Exception {

		//Initializes and attempts to connect server
		ServerSocket serverSock = null;
		try{
			serverSock = new ServerSocket(8000);
		}
		catch (IOException ie){
			System.out.println("[SERVER DISCONNECTED!]\nCan't listen on 8000");
			System.exit(1);
		}
		Socket link = null;
		System.out.println("[SERVER CONNECTED!]\nListening for connection ...");
		try {
			link = serverSock.accept();
		}
		catch (IOException ie){
			System.out.println("[SERVER DISCONNECTED!]\nAccept failed");
			System.exit(1);
		}

		System.out.println("[CLIENT CONNECTED!] \nLink Information:" + link);
		System.out.println("Listening for input ...");

		PrintWriter output = new PrintWriter(link.getOutputStream(), true);
		BufferedReader input = new BufferedReader(new InputStreamReader(link.getInputStream()));
		String inputLine;
		while ((inputLine = input.readLine())!=null) {


			System.out.println("Server: " + inputLine);
			output.println(inputLine);
		}

	}
}
