package hangmanserver;

import java.io.File;

/**
 *
 * @author Jaime Sánchez Rodríguez
 */
public class Utils {
    public static final int COMMUNICATION_PORT = 5112;
    public static final String SERVER_IP = "127.0.0.1";
    
    public static final File WORDS_FILE = new File("hangman_words.txt");
    
    //Options for the user to pick
    public static final String [] MENU_OPTIONS = {
        "End Session",
        "Play Hangman"
    };
    
    public static final int MAX_ERRORS = 5;
}
