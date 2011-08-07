/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.esbhive.demoapp;

/**
 *
 * @author pubudu
 */
public class InvalidDataException extends Exception{

  public InvalidDataException(Throwable cause) {
    super(cause);
  }

  public InvalidDataException(String message, Throwable cause) {
    super(message,cause);
  }

  public InvalidDataException(String message) {
    super(message);
  }

  public InvalidDataException() {
    super();
  }

}
