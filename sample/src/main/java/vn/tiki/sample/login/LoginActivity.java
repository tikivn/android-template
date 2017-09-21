package vn.tiki.sample.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import java.util.List;
import javax.inject.Inject;
import vn.tiki.daggers.Daggers;
import vn.tiki.sample.R;
import vn.tiki.sample.base.BaseMvpActivity;
import vn.tiki.sample.base.NetworkStatusObserver;

public class LoginActivity extends BaseMvpActivity<LoginView, LoginPresenter> implements
    NetworkStatusObserver, LoginView {

  private static final ButterKnife.Setter<View, Boolean> ENABLE =
      (view, value, index) -> view.setEnabled(value);

  @BindView(R.id.tilEmail) TextInputLayout tilEmail;
  @BindView(R.id.etEmail) EditText etEmail;
  @BindView(R.id.tilPassword) TextInputLayout tilPassword;
  @BindView(R.id.etPassword) EditText etPassword;
  @BindView(R.id.btLogin) Button btLogin;
  @BindView(R.id.pbLoading) View pbLoading;
  @BindViews({ R.id.etEmail, R.id.etPassword }) List<View> inputViews;

  @BindString(R.string.login_error_invalid_email) String errorInvalidEmail;
  @BindString(R.string.login_error_invalid_password) String errorInvalidPassword;
  @BindString(R.string.login_error_wrong_password) String errorWrongPassword;
  @BindString(R.string.login_msg_successful) String msgSuccessful;
  @BindColor(R.color.colorAccent) int colorAccent;

  @Inject LoginPresenter presenter;

  public static Intent intent(Context context) {
    return new Intent(context, LoginActivity.class);
  }

  @Override public void onNetworkStatusChanged(boolean isConnected) {
    presenter.onNetworkStatusChanged(isConnected);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Daggers.inject(this, this);

    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);
    ButterKnife.bind(presenter, this);

    connect(presenter, this);
  }

  @Override public void showLoading() {
    pbLoading.setVisibility(View.VISIBLE);
    btLogin.setVisibility(View.GONE);
    ButterKnife.apply(inputViews, ENABLE, false);
  }

  @Override public void hideLoading() {
    btLogin.setVisibility(View.VISIBLE);
    pbLoading.setVisibility(View.GONE);
    ButterKnife.apply(inputViews, ENABLE, true);
  }

  @Override public void showValidationEmailError() {
    tilEmail.setError(errorInvalidEmail);
  }

  @Override public void hideValidationEmailError() {
    tilEmail.setError(null);
  }

  @Override public void showValidationPasswordError() {
    tilPassword.setError(errorInvalidPassword);
  }

  @Override public void hideValidationPasswordError() {
    tilPassword.setError(null);
  }

  @Override public void enableSubmit() {
    btLogin.setEnabled(true);
  }

  @Override public void disableSubmit() {
    btLogin.setEnabled(false);
  }

  @Override public void showAuthenticationError() {
    tilPassword.setError(errorWrongPassword);
  }

  @Override public void hideAuthenticationError() {
    tilPassword.setError(null);
  }

  @Override public void showLoginSuccess() {
    Toast.makeText(this, msgSuccessful, Toast.LENGTH_SHORT).show();
    finish();
  }
}
