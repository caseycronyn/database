package edu.uob;

import java.io.Serial;

public class UnbalancedParenthesesException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;

  public UnbalancedParenthesesException(String message) {
        super(message);
    }
}
