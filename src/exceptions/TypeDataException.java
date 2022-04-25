package exceptions;

/**
 *
 * 
 */
public class TypeDataException extends Exception {

    /**
     * Creates a new instance of <code>TableInexistantException</code> without
     * detail message.
     */
    public TypeDataException() {
        
    }

    /**
     * Constructs an instance of <code>TableInexistantException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TypeDataException(String msg) {
        super("type "+msg+" error");
    }
}
