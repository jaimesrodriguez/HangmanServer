package hangmanserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jaime Sánchez Rodríguez
 */
public class Client {
    public static void main(String[] args) {
        Scanner sn = new Scanner(System.in);
        sn.useDelimiter("\n");
        
        try {
            Socket sc = new Socket(Utils.SERVER_IP ,Utils.COMMUNICATION_PORT);
            
            DataInputStream in = new DataInputStream(sc.getInputStream());
            DataOutputStream out = new DataOutputStream(sc.getOutputStream());
            //Server message: "Write your name:"
            String message = in.readUTF();
            System.out.println(message);
            String name = sn.next();
            out.writeUTF(name);
            
            ClientThread ct = new ClientThread(in,out,name);
            ct.start();
            ct.join();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
