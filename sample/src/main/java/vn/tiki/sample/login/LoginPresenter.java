package vn.tiki.sample.login;

import android.support.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import vn.tiki.sample.model.UserModel;
import vn.tiki.sample.mvp.ViewAction;
import vn.tiki.sample.mvp.rx.RxBasePresenter;
import vn.tiki.sample.util.EmailValidator;
import vn.tiki.sample.util.PasswordValidator;

/**
 * Created by Giang Nguyen on 8/25/17.
 */

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
        .doOnNext(new Consumer<String>() {
          @Override public void accept(String s) throws Exception {
            email = s;
          }
        })
        .map(new Function<String, Boolean>() {
          @Override public Boolean apply(@io.reactivex.annotations.NonNull String s)
              throws Exception {
            return emailValidator.validate(s);
          }
        });

    final Observable<Boolean> passwordValidations = passwordInputs
        .doOnNext(new Consumer<String>() {
          @Override public void accept(String s) throws Exception {
            password = s;
          }
        })
        .map(new Function<String, Boolean>() {
          @Override public Boolean apply(@io.reactivex.annotations.NonNull String s)
              throws Exception {
            return passwordValidator.validate(s);
          }
        });

    disposeOnDestroy(emailValidations
        .distinctUntilChanged()
        .subscribe(new Consumer<Boolean>() {
          @Override public void accept(Boolean valid) throws Exception {
            if (valid) {
              getViewOrThrow().hideValidationEmailError();
            } else {
              getViewOrThrow().showValidationEmailError();
            }
          }
        }));

    disposeOnDestroy(passwordValidations
        .distinctUntilChanged()
        .subscribe(new Consumer<Boolean>() {
          @Override public void accept(Boolean valid) throws Exception {
            if (valid) {
              getViewOrThrow().hideValidationPasswordError();
            } else {
              getViewOrThrow().showValidationPasswordError();
            }
          }
        }));

    disposeOnDestroy(networkStatusChanges.distinctUntilChanged()
        .subscribe(new Consumer<Boolean>() {
          @Override public void accept(Boolean isConnected) throws Exception {
            if (isConnected) {
              getViewOrThrow().hideNetworkError();
            } else {
              getViewOrThrow().showNetworkError();
            }
          }
        }));

    disposeOnDestroy(Observable.combineLatest(
        emailValidations,
        passwordValidations,
        networkStatusChanges,
        new Function3<Boolean, Boolean, Boolean, Boolean>() {
          @Override public Boolean apply(
              @io.reactivex.annotations.NonNull Boolean validEmail,
              @io.reactivex.annotations.NonNull Boolean validPassword,
              @io.reactivex.annotations.NonNull Boolean isConnected)
              throws Exception {
            return validEmail && validPassword && isConnected;
          }
        })
        .subscribe(new Consumer<Boolean>() {
          @Override public void accept(Boolean validSubmit) throws Exception {
            if (validSubmit) {
              getViewOrThrow().enableSubmit();
            } else {
              getViewOrThrow().disableSubmit();
            }
          }
        }));

    final Observable<LoginResult> authentications =
        loginClicks.switchMap(new Function<Object, ObservableSource<LoginResult>>() {
          @Override
          public ObservableSource<LoginResult> apply(@io.reactivex.annotations.NonNull Object o)
              throws Exception {
            return userModel.login(email, password)
                .map(new Function<Boolean, LoginResult>() {
                  @Override
                  public LoginResult apply(@io.reactivex.annotations.NonNull Boolean aBoolean)
                      throws Exception {
                    if (aBoolean) {
                      return LoginResult.success();
                    } else {
                      throw new Exception("authentication failed");
                    }
                  }
                })
                .onErrorReturn(new Function<Throwable, LoginResult>() {
                  @Override
                  public LoginResult apply(@io.reactivex.annotations.NonNull Throwable throwable)
                      throws Exception {
                    return LoginResult.error(throwable);
                  }
                })
                .subscribeOn(Schedulers.io())
                .startWith(LoginResult.inFlight());
          }
        });

    disposeOnDestroy(authentications
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<LoginResult>() {
          @Override public void accept(LoginResult loginResult) throws Exception {
            final ViewAction<LoginView> viewAction;
            if (loginResult.isInFlight()) {
              viewAction = new ViewAction<LoginView>() {
                @Override public void call(@NonNull LoginView view) {
                  view.disableSubmit();
                  view.hideAuthenticationError();
                  view.hideNetworkError();
                  view.showLoading();
                }
              };
            } else if (loginResult.isError()) {
              viewAction = new ViewAction<LoginView>() {
                @Override public void call(@NonNull LoginView view) {
                  view.hideLoading();
                  view.showAuthenticationError();
                  view.enableSubmit();
                }
              };
            } else {
              viewAction = new ViewAction<LoginView>() {
                @Override public void call(@NonNull LoginView view) {
                  view.showLoginSuccess();
                }
              };
            }
            sendToView(viewAction);
          }
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
