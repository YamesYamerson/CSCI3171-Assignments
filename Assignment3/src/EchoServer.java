import com.sun.security.ntlm.Server;

import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

public class EchoServer{

	public static void main(String[] args) throws Exception {

		//Initializes and attempts to connect server
		ServerSocket serverSock = null;
		try{
			serverSock = new ServerSocket(8000);
		}
		catch (IOException ie){
			System.out.println("[SERVER DISCONNECTED!] Can't listen on 8000");
			System.exit(1);
		}
		Socket link = null;
		System.out.println("[SERVER CONNECTED!] Listening for connection ...");
		try {
			link = serverSock.accept();
		}
		catch (IOException ie){
			System.out.println("[SERVER DISCONNECTED!] Accept failed");
			System.exit(1);
		}

		//Connects client if handshake is successful
		System.out.println("[CLIENT CONNECTED!] Link Information:" + link);
		System.out.println("Listening for input ...");

		//Initializes input and output streams
		PrintWriter output = new PrintWriter(link.getOutputStream(), true);
		BufferedReader input = new BufferedReader(new InputStreamReader(link.getInputStream()));
		String inputLine;
		while ((inputLine = input.readLine())!=null) {
            String currentMessage = inputLine;
			System.out.println("Server: " + currentMessage);
			output.println(inputLine);

			switch(currentMessage){
				case "/myip":
					MyLocalIPAddress myIP = new MyLocalIPAddress();
					String ip = myIP.run();
					output.println(ip);
					System.out.println(ip);
					break;

				case "/iplookup":
					IPFinder ipFinder = new IPFinder();
					UrlValidator validator = new UrlValidator();
					output.println("Enter Website URL:\n");
					String websiteInput = input.readLine();
					if(validator.isValid(websiteInput)){
						String ipAddress = ipFinder.find(websiteInput);
						output.println(ipAddress);
						System.out.println(ipAddress);
					}

				case "/killserver":
					System.out.println("[CLIENT REQUESTED SERVER SHUTDOWN!]");
					TimeUnit.SECONDS.sleep(5);
					System.out.println("[SERVER IS SHUTTING DOWN!");
					TimeUnit.SECONDS.sleep(3);
					closeServer(output, input, link, serverSock);
					break;

				case "exit":

				case "quit":

				case "disconnect":
					disconnectClient(output, input, link, serverSock);
					break;
			}
		}
	}
	public static void closeServer(PrintWriter output, BufferedReader input, Socket link, ServerSocket serverSock) throws IOException {
		output.close();
		input.close();
		link.close();
		serverSock.close();
	}
	public static void disconnectClient(PrintWriter output, BufferedReader input, Socket link, ServerSocket serverSock) throws IOException, InterruptedException {
		output.println("[CLIENT REQUESTED TO CLOSE CONNECTION!");
		TimeUnit.SECONDS.sleep(5);
		output.println("[DISCONNECTING!");
		output.close();
		input.close();
		link.close();
	}



}
