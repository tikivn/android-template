package vn.tiki.sample.util;

public class PasswordValidator {

  /**
   * Validate password
   *
   * @param password password for validation
   * @return true valid password, false invalid password
   */
  public boolean validate(final String password) {
    return password != null && password.length() >= 6;
  }
}
