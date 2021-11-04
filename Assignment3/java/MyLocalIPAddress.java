import java.net.*;
import java.util.Scanner;
public class MyLocalIPAddress{
	public static void main(String[] args) throws Exception{
		try
		{
			InetAddress address = InetAddress.getLocalHost();
			System.out.println("IP address: " + address);
		}
		catch (UnknownHostException e)
		{
			System.out.println(" Could not find local host");
		}
	}
}
