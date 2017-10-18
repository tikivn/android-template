package vn.tiki.sample.login

import com.google.common.truth.Truth.assertThat
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_login.btLogin
import kotlinx.android.synthetic.main.activity_login.etEmail
import kotlinx.android.synthetic.main.activity_login.etPassword
import kotlinx.android.synthetic.main.activity_login.tilEmail
import kotlinx.android.synthetic.main.activity_login.tilPassword
import org.junit.*
import org.junit.runner.*
import org.mockito.*
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast
import vn.tiki.sample.RxSchedulerTestRule
import vn.tiki.sample.TestApplication
import vn.tiki.sample.di.AppModule
import vn.tiki.sample.model.UserModel

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class, packageName = "vn.tiki.sample")
class LoginTest {

  @Rule
  @JvmField
  val rxSchedulerTestRule = RxSchedulerTestRule()

  @Mock internal lateinit var userModel: UserModel

  private lateinit var loginActivity: LoginActivity

  @Before
  @Throws(Exception::class)
  fun setUp() {
    MockitoAnnotations.initMocks(this)
    val testApplication = RuntimeEnvironment.application as TestApplication
    testApplication.setAppModule(object : AppModule(testApplication) {
      override fun provideUserModel(): UserModel {
        return userModel
      }
    })

    loginActivity = Robolectric.buildActivity(LoginActivity::class.java)
        .create()
        .start()
        .resume()
        .get()
  }

  @Test
  @Throws(Exception::class)
  fun testInputInvalidEmail() {
    loginActivity.etEmail.setText("foo@gmail")
    assertThat(loginActivity.tilEmail.error).isEqualTo(loginActivity.errorInvalidEmail)
  }

  @Test
  @Throws(Exception::class)
  fun testInputValidEmail() {
    loginActivity.etEmail.setText("foo@gmail.com")
    assertThat(loginActivity.tilEmail.error).isNull()
  }

  @Test
  @Throws(Exception::class)
  fun testLoginFailure() {
    val email = "foo@gmail.com"
    val password = "123456"
    println(userModel.toString())
    `when`(userModel.login(
        anyString(),
        anyString())).thenReturn(Observable.error(Exception("")))

    loginActivity.etEmail.setText(email)
    loginActivity.etPassword.setText(password)
    loginActivity.btLogin.performClick()

    assertThat(loginActivity.tilPassword.error).isEqualTo(loginActivity.errorWrongPassword)
  }

  @Test
  @Throws(Exception::class)
  fun testLoginSuccess() {
    val email = "foo@gmail.com"
    val password = "123456"
    `when`(userModel.login(
        anyString(),
        anyString())).thenReturn(Observable.just(true))

    loginActivity.etEmail.setText(email)
    loginActivity.etPassword.setText(password)
    loginActivity.btLogin.performClick()

    assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo(loginActivity.msgSuccessful)
  }
}
