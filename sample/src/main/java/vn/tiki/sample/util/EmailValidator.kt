package vn.tiki.sample.util

import java.util.regex.Pattern

class EmailValidator {
  private val pattern: Pattern

  init {
    pattern = Pattern.compile(EMAIL_PATTERN)
  }

  /**
   * Validate hex with regular expression
   *
   * @param hex hex for validation
   * @return true valid hex, false invalid hex
   */
  fun validate(hex: String): Boolean {
    return pattern.matcher(hex).matches()
  }

  companion object {
    private val EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
  }
}
