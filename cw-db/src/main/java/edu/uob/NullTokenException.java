package edu.uob;

import java.io.Serial;

public class NullTokenException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;

  public NullTokenException(String message) {
    super(message);
  }
}
