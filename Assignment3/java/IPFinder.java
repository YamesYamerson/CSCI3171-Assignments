import java.net.*;
import java.util.Scanner;
public class IPFinder{
	public static void main(String[] args) throws Exception{
		String host;
		Scanner keyboard = new Scanner(System.in);
		System.out.print("\n\nEnter host name: ");
		host = keyboard.nextLine();
		try
		{
			InetAddress address = InetAddress.getByName(host);
			System.out.println("IP address: " + address + "\n");
		}
		catch (UnknownHostException e)
		{
			System.out.println("  Could not find " + host + "\n");
		}
	}
}
