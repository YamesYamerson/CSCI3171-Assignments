import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class MultiEchoServer{

	//declare the ServerSocket variable and the port number for the server(constant)
	final private static int PORT = 1234;
	private Set<String> usersNames = new HashSet<>();
	private Set<ClientHandler> clientThreads = new HashSet<>();

	public MultiEchoServer(int PORT){
	}

	//Method that starts server and accepts clients. Server disconnects under error conditions.
	public void run() {

		//Initializes server on designated port 1234
		try (ServerSocket serverSock = new ServerSocket(PORT)) {

			//Prints successful server connection results
			System.out.println("[SERVER LISTENING ON PORT " + PORT + "]");

			//Loop that detects clients, connects them, and allows them to send and receive messages
			while (true) {
				Socket socket = serverSock.accept();
				System.out.println("[NEW USER CONNECTED AT: " + socket.toString() + " ]");

				do {
					//Initializes new ClientHandler thread for new client and adds
					ClientHandler newClient = new ClientHandler(socket, this);
					clientThreads.add(newClient);
					newClient.run();
					addUser(newClient.getName());

					Socket client = null;
					System.out.println("Listening for connection ...");
					try {
						client = serverSock.accept();
						System.out.println("[SERVER ACCEPTED NEW CLIENT " + client.toString() + "]");
						ClientHandler handler = new ClientHandler(client, this);
						handler.start();
					} catch (IOException e) {
						System.out.println("[SERVER FAILED TO ACCEPT CLIENT");
						System.exit(1);
					}
					System.out.println("[CONNECTED ON PORT" + PORT + "]");
					System.out.println("Listening for input ...");

				} while (true);
			}

		} catch (IOException e) {
			System.out.println("[!SERVER UNABLE TO LISTEN ON PORT " + PORT + "]");
			System.exit(1);
		}
	}


	//The main method will create the ServerSocket object and listens to inputs
	//from multiple clients
	public static void main(String[] args) {
		MultiEchoServer chatroomServer = new MultiEchoServer(PORT);
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
