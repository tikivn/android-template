package vn.tiki.sample.login

import vn.tiki.architecture.mvp.Mvp

interface LoginView : Mvp.View {

  fun showLoading()

  fun hideLoading()

  fun showValidationEmailError()

  fun hideValidationEmailError()

  fun showValidationPasswordError()

  fun hideValidationPasswordError()

  fun enableSubmit()

  fun disableSubmit()

  fun showAuthenticationError()

  fun hideAuthenticationError()

  fun showLoginSuccess()
}
