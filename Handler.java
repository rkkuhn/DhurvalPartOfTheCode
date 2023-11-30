import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Handler implements Runnable{
    
    public static ArrayList<Handler> clientHandlerList = new ArrayList<>();
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private String clientUsername;

    public Handler(Socket socket){

        try {
            this.socket = socket;
            this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
            this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
            
            Message usernameObject = (Message)this.objectInputStream.readObject();
            this.clientUsername = usernameObject.getSender();
            clientHandlerList.add(this);

            String body = "SERVER: " + clientUsername + " has entered the chat";
            System.out.println(body);
            Message messageToClient = new Message(false, clientUsername, body);

            sendMessage(messageToClient);
        } catch (Exception e) {
            removeHandler();
            System.out.println("Exception in Handler()");
        }
    }

    public void sendMessage(Message messageToSend){
        for(Handler handler : clientHandlerList){
            try {
                handler.objectOutputStream.writeObject(messageToSend);
                handler.objectOutputStream.flush();
            } catch (Exception e) {
                removeHandler();
                e.printStackTrace();
            }
        }
    }

    public void removeHandler(){
        clientHandlerList.remove(this);
        String body = "SERVER: " + clientUsername + " has left the chat";
        Message messageToServer = new Message(true, clientUsername, body);
        Message messageToClient = new Message(false, clientUsername, body);

        sendMessage(messageToClient);
        sendMessage(messageToServer);

        try {
            if(objectInputStream != null){
                objectInputStream.close();
            }

            if(objectOutputStream != null){
                objectOutputStream.close();
            }

            if(this.socket != null){
                this.socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Message messageFromClient;

        while(socket.isConnected()){
            try {
                messageFromClient = (Message)objectInputStream.readObject();

                if(messageFromClient.getMessageBody().equalsIgnoreCase("quit")){
                    String message = this.clientUsername + " has left the chat";
                    sendMessage(new Message(true, "SERVER", message));
                    System.out.println("SERVER: " + message);
                    removeHandler();
                    break;
                }

                sendMessage(messageFromClient);
            } catch (Exception e) {
                removeHandler();
                break;
            }
        }
    }

}
