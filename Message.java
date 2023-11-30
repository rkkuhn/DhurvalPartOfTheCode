import java.io.Serializable;

public class Message implements Serializable{

    private static int id;
    private boolean messageToServer;
    private String sender;
    private String messageBody;

    private Message(){
        this.messageToServer = false;
        this.sender = "";
        this.messageBody = "";
    }

    public Message(boolean messageToServer, String sender, String messageBody){
        this.messageToServer = messageToServer;
        this.sender = sender;
        this.messageBody = messageBody;
    }

    public static int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getMessageBody() {
        return messageBody;
    }

}
