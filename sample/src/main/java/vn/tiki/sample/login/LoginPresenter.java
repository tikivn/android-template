package vn.tiki.sample.login;

import android.support.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import vn.tiki.architecture.mvp.ViewAction;
import vn.tiki.sample.model.UserModel;
import vn.tiki.sample.mvp.rx.RxBasePresenter;
import vn.tiki.sample.util.EmailValidator;
import vn.tiki.sample.util.PasswordValidator;

class LoginPresenter extends RxBasePresenter<LoginView> {

  @NonNull private final PublishSubject<String> emailInputs = PublishSubject.create();
  @NonNull private final PublishSubject<String> passwordInputs = PublishSubject.create();
  @NonNull private final PublishSubject<Object> loginClicks = PublishSubject.create();
  @NonNull private final PublishSubject<Boolean> networkStatusChanges = PublishSubject.create();

  private String email;
  private String password;

  LoginPresenter(@NonNull final UserModel userModel) {
    final EmailValidator emailValidator = new EmailValidator();
    final PasswordValidator passwordValidator = new PasswordValidator();

    final Observable<Boolean> emailValidations = emailInputs
        .doOnNext(s -> email = s)
        .map(emailValidator::validate);

    final Observable<Boolean> passwordValidations = passwordInputs
        .doOnNext(s -> password = s)
        .map(passwordValidator::validate);

    disposeOnDestroy(emailValidations
        .distinctUntilChanged()
        .subscribe(valid -> {
          if (valid) {
            getViewOrThrow().hideValidationEmailError();
          } else {
            getViewOrThrow().showValidationEmailError();
          }
        }));

    disposeOnDestroy(passwordValidations
        .distinctUntilChanged()
        .subscribe(valid -> {
          if (valid) {
            getViewOrThrow().hideValidationPasswordError();
          } else {
            getViewOrThrow().showValidationPasswordError();
          }
        }));

    disposeOnDestroy(networkStatusChanges.distinctUntilChanged()
        .subscribe(isConnected -> {
          if (isConnected) {
            getViewOrThrow().hideNetworkError();
          } else {
            getViewOrThrow().showNetworkError();
          }
        }));

    disposeOnDestroy(Observable.combineLatest(
        emailValidations,
        passwordValidations,
        networkStatusChanges,
        (validEmail, validPassword, isConnected) -> validEmail && validPassword && isConnected)
        .subscribe(validSubmit -> {
          if (validSubmit) {
            getViewOrThrow().enableSubmit();
          } else {
            getViewOrThrow().disableSubmit();
          }
        }));

    final Observable<LoginResult> authentications =
        loginClicks.switchMap(__ ->
            userModel.login(email, password)
                .map(aBoolean -> {
                  if (aBoolean) {
                    return LoginResult.success();
                  } else {
                    throw new Exception("authentication failed");
                  }
                })
                .onErrorReturn(LoginResult::error)
                .subscribeOn(Schedulers.io())
                .startWith(LoginResult.inFlight()));

    disposeOnDestroy(authentications
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(loginResult -> {
          final ViewAction<LoginView> viewAction;
          if (loginResult.isInFlight()) {
            viewAction = view -> {
              view.disableSubmit();
              view.hideAuthenticationError();
              view.hideNetworkError();
              view.showLoading();
            };
          } else if (loginResult.isError()) {
            viewAction = view -> {
              view.hideLoading();
              view.showAuthenticationError();
              view.enableSubmit();
            };
          } else {
            viewAction = LoginView::showLoginSuccess;
          }
          sendToView(viewAction);
        }));
  }

  void onNetworkStatusChanged(boolean connected) {
    networkStatusChanges.onNext(connected);
  }

  void onInputEmail(String email) {
    emailInputs.onNext(email);
  }

  void onInputPassword(String password) {
    passwordInputs.onNext(password);
  }

  void onClickLogin() {
    loginClicks.onNext(new Object());
  }
}
