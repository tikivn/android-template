package vn.tiki.sample.util

class PasswordValidator {

  /**
   * Validate password
   *
   * @param password password for validation
   * @return true valid password, false invalid password
   */
  fun validate(password: String?): Boolean {
    return password != null && password.length >= 6
  }
}
