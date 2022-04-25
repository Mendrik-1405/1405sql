package exceptions;

/**
 *
 * 
 */
public class ConnectException extends Exception {

    /**
     * Creates a new instance of <code>TableInexistantException</code> without
     * detail message.
     */
    public ConnectException() {
        
    }

    /**
     * Constructs an instance of <code>TableInexistantException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ConnectException(String msg) {
        super(msg+" deconnexion");
    }
}
