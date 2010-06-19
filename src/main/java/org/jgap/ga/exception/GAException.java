/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jgap.ga.exception;

/**
 *
 * @author Tabiul Mahmood
 */
public class GAException extends Exception{
  /**
     * Constructs a new exception with the specified detail message.
     * @param message the exception message
     */
    public GAException(String message)
    {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * @param message the exception message
     * @param cause the exception cause
     */
    public GAException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    /**
     * Constructs a new exception with the specified cause and a detail 
     * message of (cause==null ? null : cause.toString()) (which typically 
     * contains the class and detail message of cause).
     * @param cause the exception cause
     */
    public GAException(Throwable cause)
    {
        super(cause);
    }
}
