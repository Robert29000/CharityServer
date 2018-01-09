import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    static ExecutorService executeIt = Executors.newFixedThreadPool(100);
    public static void main(String[] args) {
            try {
                ServerSocket server = new ServerSocket(3345);
                while (true) {
                    Socket client = server.accept();
                    System.out.println("Ура");
                    executeIt.execute(new MonoThreadClientHandler(client));
                }
            } catch (IOException e) {

            }

    }

}
