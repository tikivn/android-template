package vn.tiki.sample.login

import android.text.Editable
import butterknife.OnClick
import butterknife.OnTextChanged
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import vn.tiki.sample.R
import vn.tiki.sample.di.ActivityScope
import vn.tiki.sample.model.UserModel
import vn.tiki.sample.mvp.rx.RxBasePresenter
import vn.tiki.sample.util.EmailValidator
import vn.tiki.sample.util.PasswordValidator
import vn.tiki.sample.util.subscribe
import javax.inject.Inject

@ActivityScope
class LoginPresenter @Inject constructor(userModel: UserModel) : RxBasePresenter<LoginView>() {

  private val emailInputs = PublishSubject.create<String>()
  private val passwordInputs = PublishSubject.create<String>()
  private val loginClicks = PublishSubject.create<Any>()
  private val networkStatusChanges = PublishSubject.create<Boolean>()

  private var email: String? = null
  private var password: String? = null

  init {
    val emailValidator = EmailValidator()
    val passwordValidator = PasswordValidator()

    val emailValidations = emailInputs
        .doOnNext { s -> email = s }
        .map(emailValidator::validate)

    val passwordValidations = passwordInputs
        .doOnNext { s -> password = s }
        .map(passwordValidator::validate)

    emailValidations
        .distinctUntilChanged()
        .subscribe(this) { valid ->
          if (valid!!) {
            viewOrThrow.hideValidationEmailError()
          } else {
            viewOrThrow.showValidationEmailError()
          }
        }

    passwordValidations
        .distinctUntilChanged()
        .subscribe(this) {
          if (it) {
            viewOrThrow.hideValidationPasswordError()
          } else {
            viewOrThrow.showValidationPasswordError()
          }
        }

    Observable.combineLatest<Boolean, Boolean, Boolean, Boolean>(emailValidations,
        passwordValidations,
        networkStatusChanges,
        Function3<Boolean, Boolean, Boolean, Boolean> { validEmail, validPassword, isConnected -> validEmail && validPassword && isConnected })
        .subscribe { validSubmit ->
          if (validSubmit) {
            view?.enableSubmit()
          } else {
            view?.disableSubmit()
          }
        }

    val authentications = loginClicks.switchMap { _ ->
      userModel.login(email!!, password!!)
          .map { LoginSuccess as LoginState }
          .onErrorReturn { LoginError(it) }
          .subscribeOn(Schedulers.io())
          .startWith(LoginInFlight)
    }

    authentications
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(this) { result ->
          when (result) {
            is LoginInFlight -> sendToView { view ->
              view.disableSubmit()
              view.hideAuthenticationError()
              view.showLoading()
            }
            is LoginError -> sendToView { view ->
              view.hideLoading()
              view.showAuthenticationError()
              view.enableSubmit()
            }
            else -> sendToView(LoginView::showLoginSuccess)
          }
        }
  }

  fun onNetworkStatusChanged(connected: Boolean) {
    networkStatusChanges.onNext(connected)
  }

  @OnTextChanged(value = R.id.etEmail, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
  fun onInputEmail(editable: Editable) {
    emailInputs.onNext(editable.toString())
  }

  @OnTextChanged(value = R.id.etPassword, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
  fun onInputPassword(editable: Editable) {
    passwordInputs.onNext(editable.toString())
  }

  @OnClick(R.id.btLogin)
  fun onClickLogin() {
    loginClicks.onNext(Any())
  }
}
