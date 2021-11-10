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
					System.out.println("[REQUESTING URL FROM CLIENT!]");
					output.println("Enter Website URL:");
					inputLine = input.readLine();
					System.out.println("Server: " + inputLine);

//	TO DO:				if(validator.isValid(inputLine)){
					String ipAddress = ipFinder.find(inputLine);
					System.out.println(ipAddress);
					output.println(ipAddress);

					break;

				case "/numbergame":
					System.out.println("[CLIENT HAS INITIATED GUESS THE NUMBER GAME!]");
					output.println("Would you like to play the number game (Y/N)?");
					inputLine = input.readLine();
					if(inputLine == "Y"){
						System.out.println("[CLIENT REQUESTS TO BEGIN A GAME!]");
						inputLine = input.readLine();

					}else if(inputLine != "Y" || inputLine != "N"){
						int counter = 0;
						while(counter < 3) {
							System.out.println("[CLIENT PROVIDED FAULTY INPUT]");
							output.println("Your input was incorrect, try again!");
						}
						if(counter == 3){
							System.out.println("[GUESS THE NUMBER GAME ABORTING]");
						}
					}
					break;

				case "/killserver":
					System.out.println("[CLIENT REQUESTED SERVER SHUTDOWN!]");
					TimeUnit.SECONDS.sleep(3);
					System.out.println("[SERVER IS SHUTTING DOWN!");
					TimeUnit.SECONDS.sleep(3);
					closeServer(output, input, link, serverSock);
					break;

				case "exit":
				case "quit":
				case "disconnect":
					disconnectClient(output, input, link, serverSock);
					break;
				default:
					output.println(currentMessage);
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