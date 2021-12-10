import java.io.IOException;
import java.util.ArrayList;

public class ChatServerRunner {

    public static ArrayList<String> messageBuffer = new ArrayList<>();
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        MultiEchoServer chatroomServer = new MultiEchoServer();
        chatroomServer.run();



    }
}
