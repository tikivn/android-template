package vn.tiki.sample.login

import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.view.View
import android.widget.Toast
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_login.btLogin
import kotlinx.android.synthetic.main.activity_login.etEmail
import kotlinx.android.synthetic.main.activity_login.etPassword
import kotlinx.android.synthetic.main.activity_login.pbLoading
import kotlinx.android.synthetic.main.activity_login.tilEmail
import kotlinx.android.synthetic.main.activity_login.tilPassword
import vn.tiki.daggers.Daggers
import vn.tiki.sample.R
import vn.tiki.sample.base.BaseMvpActivity
import vn.tiki.sample.base.NetworkStatusObserver
import vn.tiki.sample.util.bindString
import javax.inject.Inject

class LoginActivity : BaseMvpActivity<LoginView, LoginPresenter>(), NetworkStatusObserver, LoginView {

  @Inject internal lateinit var presenter: LoginPresenter

  private val inputViews: List<View> by lazy { listOf(etEmail, etPassword) }
  val errorInvalidEmail: String by bindString(R.string.login_error_invalid_email)
  private val errorInvalidPassword: String by bindString(R.string.login_error_invalid_password)
  @VisibleForTesting internal val errorWrongPassword: String by bindString(R.string.login_error_wrong_password)
  val msgSuccessful: String by bindString(R.string.login_msg_successful)
  private val enable: (View, Boolean, Int) -> Unit = { view, value, _ -> view.isEnabled = value }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Daggers.inject(this)

    setContentView(R.layout.activity_login)
    ButterKnife.bind(this)
    ButterKnife.bind(presenter, this)

    connect(presenter, this)
  }

  override fun disableSubmit() {
    btLogin.isEnabled = false
  }

  override fun enableSubmit() {
    btLogin.isEnabled = true
  }

  override fun hideAuthenticationError() {
    tilPassword.error = null
  }

  override fun hideLoading() {
    btLogin.visibility = View.VISIBLE
    pbLoading.visibility = View.GONE
    ButterKnife.apply(inputViews, enable, true)
  }

  override fun hideValidationEmailError() {
    tilEmail.error = null
  }

  override fun hideValidationPasswordError() {
    tilPassword.error = null
  }

  override fun onNetworkStatusChanged(isConnected: Boolean) {
    presenter.onNetworkStatusChanged(isConnected)
  }

  override fun showAuthenticationError() {
    tilPassword.error = errorWrongPassword
  }

  override fun showLoading() {
    pbLoading.visibility = View.VISIBLE
    btLogin.visibility = View.GONE
    ButterKnife.apply(inputViews, enable, false)
  }

  override fun showLoginSuccess() {
    Toast.makeText(this, msgSuccessful, Toast.LENGTH_SHORT).show()
    finish()
  }

  override fun showValidationEmailError() {
    tilEmail.error = errorInvalidEmail
  }

  override fun showValidationPasswordError() {
    tilPassword.error = errorInvalidPassword
  }
}
