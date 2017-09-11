package vn.tiki.sample.login;

import vn.tiki.architecture.mvp.Mvp;

/**
 * Created by Giang Nguyen on 8/25/17.
 */

public interface LoginView extends Mvp.View {

  void showLoading();

  void hideLoading();

  void showValidationEmailError();

  void hideValidationEmailError();

  void showValidationPasswordError();

  void hideValidationPasswordError();

  void enableSubmit();

  void disableSubmit();

  void showAuthenticationError();

  void hideAuthenticationError();

  void showNetworkError();

  void hideNetworkError();

  void showLoginSuccess();
}
