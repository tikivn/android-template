package vn.tiki.sample.util

import com.google.common.truth.Truth.assertThat
import org.junit.*

class EmailValidatorTest {
  private lateinit var tested: EmailValidator

  @Before
  fun setUp() {
    tested = EmailValidator()
  }

  @Test
  fun testValidateInvalidEmail() {
    assertThat(tested.validate("foo")).isFalse()
    assertThat(tested.validate("foo@")).isFalse()
    assertThat(tested.validate("foo@gmail")).isFalse()
  }

  @Test
  fun testValidateValidEmail() {
    assertThat(tested.validate("foo@gmail.com")).isTrue()
  }
}
