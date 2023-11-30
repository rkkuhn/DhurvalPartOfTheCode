import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private String username;

    public Client(String username){
        this.username = username;

        try {
            this.socket = new Socket("localhost", 5000);
            this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
            Message usernameObject = new Message(true, this.username, "username");
            this.objectOutputStream.writeObject(usernameObject); 

        } catch (Exception e) {
            //removeClient();
            System.out.println("Exception in Client()");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username for the group chat: ");
        String username = scanner.nextLine();
        Client client = new Client(username);
        client.listenForMessage();
        client.sendMessage();
        scanner.close();
    }

    public void sendMessage(){
        Scanner scanner = new Scanner(System.in);
        
        try {
            while(socket.isConnected()){
                //TODO: Parser
                String messageToSend = scanner.nextLine();
                String encryptedMessage = CaesarCipher.encrypt(messageToSend); // Encrypt the message
                Message messageToServer = new Message(true, this.username, encryptedMessage);
                Message messageToClient = new Message(false, this.username, encryptedMessage);
                objectOutputStream.writeObject(messageToClient);
                
                if(messageToSend.equalsIgnoreCase("quit")){
                    break;
                }
                
                objectOutputStream.flush();
            }
        } catch (Exception e) {
            removeClient();
            System.out.println("Exception in sendMessage()");
        }

        scanner.close();
    }

    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message messageFromGroupChat;

                while(socket.isConnected()){
                    try {
                        messageFromGroupChat = (Message) objectInputStream.readObject();
                        String decryptedMessage = CaesarCipher.decrypt(messageFromGroupChat.getMessageBody()); // Decrypt the message
                        System.out.println(messageFromGroupChat.getSender() + ": " + decryptedMessage);
                    } catch (Exception e) {
                        removeClient();
                        break;
                   }
                }
                
            }
        }).start();
    }

    public void removeClient(){
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
}
