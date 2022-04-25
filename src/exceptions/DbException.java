/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author P14A-Mendrik
 */
public class DbException extends Exception {

    /**
     * Creates a new instance of <code>DbException</code> without detail
     * message.
     */
    public DbException() {
    }

    /**
     * Constructs an instance of <code>DbException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public DbException(String msg) {
        super(msg);
    }
}
