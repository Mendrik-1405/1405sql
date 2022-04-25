package exceptions;

/**
 *
 * 
 */
public class InexistantException extends Exception {

    /**
     * Creates a new instance of <code>TableInexistantException</code> without
     * detail message.
     */
    public InexistantException() {
        
    }

    /**
     * Constructs an instance of <code>TableInexistantException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InexistantException(String msg) {
        super(msg+" inexistant");
    }
}
