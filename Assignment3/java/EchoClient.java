import java.io.*;
import java.net.*;
public class EchoClient{
	public static void main(String[] args) throws IOException{
		Socket link = null;
		PrintWriter output = null;
		BufferedReader input = null;

		try{
			link = new Socket("127.0.0.1", 50000);
			output = new PrintWriter(link.getOutputStream(), true);
			input = new BufferedReader(new InputStreamReader(link.getInputStream()));
		}
		catch(UnknownHostException e)
		{
			System.out.println("Unknown Host");
			System.exit(1);
		}
		catch (IOException e){
			System.out.println("Cannot connect to host");
			System.exit(1);
		}
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String usrInput;
		while ((usrInput = stdIn.readLine())!=null){
			output.println(usrInput);
			System.out.println("echo: " + input.readLine());
		}
		output.close();
		input.close();
		stdIn.close();
		link.close();
	}
}
