import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class MultiEchoServer{
	//declare the ServerSocket variable and the port number for the server(constant)
	private static int port;
	private Set<String> usersNames = new HashSet<>();
	private Set<ClientHandler> clientThreads = new HashSet<>();

	public MultiEchoServer(int port){
		this.port = port;
	}

	public void run() throws IOException {
		try (ServerSocket serverSock = new ServerSocket(port)) {
			System.out.println("[SERVER LISTENING ON PORT: " + port + "]");

			while (true) {
				Socket socket = serverSock.accept();
				System.out.println("[USER CONNECTED AT: " + socket + "]");
				ClientHandler newClient = new ClientHandler(socket);
				clientThreads.add(newClient);
				newClient.run();
				do {
					Socket client = null;
					System.out.println("Listening for connection ...");
					try {
						client = serverSock.accept();
						System.out.println("[SERVER ACCEPTED NEW CLIENT " + client + "]");
						ClientHandler handler = new ClientHandler(client);
						handler.start();
					} catch (IOException e) {
						System.out.println("Accept failed");
						System.exit(1);
					}
					System.out.println("[CONNECTED ON " + port + "]");
					System.out.println("Listening for input ...");
				} while (true);
			}

		} catch (IOException e) {
			System.out.println("Can't listen on " + port);
			System.exit(1);
		}

	}
	//end main


	//The main method will create the ServerSocket object and listens to inputs
	//from multiple clients
	public static void main(String[] args) throws IOException {
		int port = 1234;
		MultiEchoServer chatroomServer = new MultiEchoServer(1234);
		chatroomServer.run();

	}

}//end class MultiEchoServer
