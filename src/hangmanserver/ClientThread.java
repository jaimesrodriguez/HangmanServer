package hangmanserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jaime Sánchez Rodríguez
 */
public class ClientThread extends Thread {

    private DataInputStream in;
    private DataOutputStream out;
    private String name;

    public ClientThread(DataInputStream in, DataOutputStream out, String name) {
        this.in = in;
        this.out = out;
        this.name = name;
    }

    @Override
    public void run() {
        Scanner sn = new Scanner(System.in);

        int pick;
        boolean end = false;
        try {
            while (!end) {
                showMenu();
                pick = sn.nextInt();
                out.writeInt(pick);
                
                showServerMessage();
                switch(pick){
                    case 1: //Play Hangman
                        String serverMessage;
                        do{
                            String letter = sn.next();
                            letter = letter.toLowerCase();
                            while(letter.length()!=1 || (letter.charAt(0)<'a' || letter.charAt(0)>'z')){
                                System.out.println("Please introduce a letter between A-Z");
                                letter = sn.next();
                                letter = letter.toLowerCase();
                            }
                            out.writeUTF(letter);
                            serverMessage = showServerMessage();
                        }while(!serverMessage.contains("You won!" ) && !serverMessage.contains("You lost!" ));
                        break;
                    case 0: //End Session
                        end = true;
                        break;
                    default:
                        break;
                }
            }
            
            out.close();
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Session ended.");
    }

    public static void showMenu() {
        for (int i = 1; i < Utils.MENU_OPTIONS.length; i++) {
            System.out.println(i + ". " + Utils.MENU_OPTIONS[i]);
        }
        System.out.println("0. " + Utils.MENU_OPTIONS[0]); //0. End Session
    }
    
    public String showServerMessage() throws IOException{
        String serverMessage = in.readUTF();
        System.out.println(serverMessage);
        return serverMessage;
    }
}
