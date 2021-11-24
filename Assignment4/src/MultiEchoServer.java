import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class MultiEchoServer{
	//declare the ServerSocket variable and the port number for the server(constant)
	private static ServerSocket serverSock;
	private static final int PORT = 1234;
	private Set<String> usersNames = new HashSet<>();
	private Set<ClientHandler> clientThreads = new HashSet<>();

	//The main method will create the ServerSocket object and listens to inputs
	//from multiple clients

	public static void main(String[] args) throws IOException{
		try{
			serverSock = new ServerSocket(PORT);
		}
		catch(IOException e){
			System.out.println("Can't listen on " + PORT);
			System.exit(1);
		}
		do{
			Socket client = null;
			System.out.println("Listening for connection ...");
			try{
				client = serverSock.accept();
				System.out.println("[SERVER ACCEPTED NEW CLIENT " + client + "]");
				ClientHandler handler = new ClientHandler(client);
				handler.start();
			}
			catch(IOException e){
				System.out.println("Accept failed");
				System.exit(1);
			}
			System.out.println("[CONNECTED ON " + PORT + "]");
			System.out.println("Listening for input ...");
		} while (true);
	}//end main


}//end class MultiEchoServer
