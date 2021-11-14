import java.net.*;
import java.io.*;
import java.util.Scanner;
public class UrlProg{
	public static void main(String[] args) throws Exception{
		Scanner keyboard = new Scanner(System.in);
		System.out.print("Enter the URL from which you need content: ");
		String urlString = keyboard.nextLine();
		URL theURL = new URL(urlString);
		URLConnection theConn= theURL.openConnection();
		int contentLen = theConn.getContentLength();
		int c;
		if (contentLen!=0)
		{
			InputStream urlInput = theConn.getInputStream();
			while (((c=urlInput.read())!=-1))
			{
				System.out.print((char)c);
			}
			urlInput.close();
		}
		else
			System.out.println("Sorry. No Content");
	}
}
