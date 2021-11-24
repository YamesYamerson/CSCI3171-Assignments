//This is a support class that extends Thread, runs the client thread
//and sends and receives messages

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ClientHandler extends Thread{
	private Socket client;
	private BufferedReader in;
	private PrintWriter out;



	public ClientHandler(Socket socket){
		client = socket;
		try{
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public void run(){
		try{
			String received;
			do{
				received = in.readLine();
				out.println("ECHO: " + received);
				System.out.println("TESTLINE1: " + received);
			} while (!received.equals("BYE"));
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			try{
				if(client!=null){
					System.out.println("Closing connection");
					client.close();
				}
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}//end run

	void sendMessage(String message){
		out.println(message);
	}
}//end ClientHandler class
