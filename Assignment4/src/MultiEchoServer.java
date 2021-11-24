import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class MultiEchoServer{

	//declare the ServerSocket variable and the port number for the server(constant)
	private static int port;
	private static MultiEchoServer chatroomServer = new MultiEchoServer(1234);;
	private Set<String> usersNames = new HashSet<>();
	private Set<ClientHandler> clientThreads = new HashSet<>();


	public MultiEchoServer(int port){
		this.port = port;
	}

	//Method that starts server and accepts clients. Server disconnects under error conditions.
	public void run() throws IOException {

		//Initializes server on designated port 1234
		try (ServerSocket serverSock = new ServerSocket(port)) {
			System.out.println("[SERVER LISTENING ON PORT " + port + "]");

			//Loop that detects clients, connects them, and allows them to send and receive messages
			while (true) {
				Socket socket = serverSock.accept();
				System.out.println("[USER CONNECTED AT: " + socket + " ]");
				ClientHandler newClient = new ClientHandler(socket, chatroomServer);
				clientThreads.add(newClient);
				newClient.run();
				do {
					Socket client = null;
					System.out.println("Listening for connection ...");
					try {
						client = serverSock.accept();
						System.out.println("[SERVER ACCEPTED NEW CLIENT " + client.toString() + "]");
						ClientHandler handler = new ClientHandler(client, chatroomServer);
						handler.start();
					} catch (IOException e) {
						System.out.println("[SERVER FAILED TO ACCEPT CLIENT");
						System.exit(1);
					}
					System.out.println("[CONNECTED ON PORT" + port + "]");
					System.out.println("Listening for input ...");
				} while (true);
			}

		} catch (IOException e) {
			System.out.println("[!SERVER UNABLE TO LISTEN ON PORT " + port + "]");
			System.exit(1);
		}
	}



	//The main method will create the ServerSocket object and listens to inputs
	//from multiple clients
	public static void main(String[] args) throws IOException {
		int port = 1234;
		chatroomServer.run();
	}

	void addUser(String username){
		usersNames.add(username);
	}

	Set<String> getUsersNames(){
		return usersNames;
	}

	void broadcastMessage(String message, ClientHandler activeClient){
		for(ClientHandler client: clientThreads){
			if(client != activeClient){
				client.sendMessage(message);
			}
		}
	}

}//end class MultiEchoServer
