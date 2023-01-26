package hangmanserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jaime Sánchez Rodríguez
 */
public class ServerThread extends Thread {

    private DataInputStream in;
    private DataOutputStream out;
    private String clientName;
    private Socket sc;
    private long id = this.getId();
    private final String THREAD_TAG;

    public ServerThread(Socket sc, DataInputStream in, DataOutputStream out, String clientName) {
        this.sc = sc;
        this.in = in;
        this.out = out;
        this.clientName = clientName;
        THREAD_TAG = ServerThread.class.getSimpleName() + id + "(" + clientName + ") | ";
    }

    @Override
    public void run() {
        int clientPick;
        boolean end = false;
        try {
            while (!end) {
                clientPick = in.readInt();
                switch (clientPick) {
                    case 1: //Play Hangman
                        HangmanGame hg = new HangmanGame();
                        hg.play(in, out);
                        break;
                    case 0:
                        //End Session
                        System.out.println(THREAD_TAG + "End Session Picked");
                        out.writeUTF("Ending session...");
                        end = true;
                        break;
                    default:
                        out.writeUTF("Pick an option between 0 and 1.");
                        break;
                }
            }
            
            out.close();
            in.close();
            sc.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println(THREAD_TAG + "Session Closed with client: " + clientName);
    }

}
