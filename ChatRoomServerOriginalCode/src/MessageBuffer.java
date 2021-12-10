import java.util.ArrayList;

public class MessageBuffer {
    public static ArrayList<String> messageBuffer = new ArrayList<>();

    public static void addMessage(String messageToBuffer){
        messageBuffer.add(messageToBuffer);
    }
    public static ArrayList<String> getMessagesInBuffer(){
        return messageBuffer;
    }
    public static void clearBuffer(){
        messageBuffer.clear();
    }

}
