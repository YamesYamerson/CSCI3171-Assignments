//This is a support class that extends Thread, runs the client thread
//and sends and receives messages
import java.io.*;
import java.net.*;
public class ClientHandler extends Thread{

	private Socket clientSocket;
	private BufferedReader in;
	private static PrintWriter out;
	private static String userName;

	public ClientHandler(Socket socket){
		clientSocket = socket;
		try{
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(), true);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public void run(){
		userName = null;
		try {
			userName = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.println(userName);
		System.out.println("[" + userName + " CONNECTED TO SERVER]");

		try{
			String received;
			do{
				received = in.readLine();
				sendMessage(received);
				System.out.println(received);
				out.println(received);

			} while (!received.equals("BYE"));
		}
		catch(IOException e){
			System.out.println("[USER HAS REQUESTED TO DISCONNECT FROM SERVER]");

		}
		finally{
			try{
				if(clientSocket!=null){
					System.out.println("[CLOSING CONNECTION TO " + userName + "]");
					clientSocket.close();
				}
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}//end run
	public static void sendMessage(String message){
		out.println(message);
	}

	public static String getUserName(){
		return userName;
	}
}//end ClientHandler class
