import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{

    private ServerSocket serverSocket;

    public Server(){
        try {
            this.serverSocket = new ServerSocket(5000);
        } catch (Exception e) {
            
        }
    }

    public void startServer(){
        while(!this.serverSocket.isClosed()){

            try {
                System.out.println("Waiting for new client");
                Socket socket = this.serverSocket.accept();
                System.out.println("New client connected");
                Handler handler = new Handler(socket);

                Thread thread = new Thread(handler);
                thread.start();

            } catch (Exception e) {
                closeServer();
            }
        }
    }

    public void closeServer(){
        try {
            if(this.serverSocket != null){
                this.serverSocket.close();
            }
        } catch (Exception e) {
            
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }

}
