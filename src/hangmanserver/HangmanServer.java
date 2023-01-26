package hangmanserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jaime Sánchez Rodríguez
 */
public class HangmanServer {
    private static boolean activeServer = true;
    private static final String NAME_TAG = HangmanServer.class.getSimpleName() + " | ";
    
    public static void main(String[] args) {
        ServerSocket server;
        try {
            server = new ServerSocket(Utils.COMMUNICATION_PORT);
            Socket clientSocket;
            System.out.println(NAME_TAG + "Server initialised!"
                    + "\nUsing port: " + Utils.COMMUNICATION_PORT);
            while(activeServer){
                clientSocket = server.accept();
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                
                out.writeUTF("Write your name:");
                String clientName = in.readUTF();
                
                ServerThread st = new ServerThread(clientSocket,in,out,clientName);
                st.start();
                System.out.println(NAME_TAG + "Connection created with client: " + clientName);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(HangmanServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
