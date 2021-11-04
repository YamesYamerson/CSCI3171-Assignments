import java.net.*;
import java.io.*;
public class EchoServer{
	public static void main(String[] args) throws IOException{
		ServerSocket serverSock = null;
		try{
			serverSock = new ServerSocket(50000);
		}
		catch (IOException ie){
			System.out.println("Can't listen on 50000");
			System.exit(1);
		}
		Socket link = null;
		System.out.println("Listening for connection ...");
		try {
			link = serverSock.accept();
		}
		catch (IOException ie){
			System.out.println("Accept failed");
			System.exit(1);
		}
		System.out.println("Connection successful");
		System.out.println("Listening for input ...");
		PrintWriter output = new PrintWriter(link.getOutputStream(), true);
		BufferedReader input = new BufferedReader(new InputStreamReader(link.getInputStream()));
		String inputLine;
		while ((inputLine = input.readLine())!=null){
			System.out.println("Server: " + inputLine);
			output.println(inputLine);
			if(inputLine.equals("Bye")){
				break;
			}
		}
		output.close();
		input.close();
		link.close();
		serverSock.close();
	}
}
