package vn.tiki.sample.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import java.util.List;
import javax.inject.Inject;
import vn.tiki.daggerhelper.ActivityInjector;
import vn.tiki.daggerhelper.Daggers;
import vn.tiki.sample.R;
import vn.tiki.sample.mvp.MvpActivity;
import vn.tiki.sample.util.NetworkUtil;

public class LoginActivity extends MvpActivity<LoginView, LoginPresenter> implements
    ActivityInjector, LoginView {

  private static final ButterKnife.Setter<View, Boolean> ENABLE =
      new ButterKnife.Setter<View, Boolean>() {
        @Override public void set(@NonNull View view, Boolean value, int index) {
          view.setEnabled(value);
        }
      };

  @BindView(android.R.id.content) View vRoot;
  @BindView(R.id.tilEmail) TextInputLayout tilEmail;
  @BindView(R.id.etEmail) EditText etEmail;
  @BindView(R.id.tilPassword) TextInputLayout tilPassword;
  @BindView(R.id.etPassword) EditText etPassword;
  @BindView(R.id.btLogin) Button btLogin;
  @BindView(R.id.pbLoading) View pbLoading;
  @BindViews({ R.id.etEmail, R.id.etPassword }) List<View> inputViews;
  @BindString(R.string.error_invalid_email) String errorInvalidEmail;
  @BindString(R.string.error_invalid_password) String errorInvalidPassword;
  @BindString(R.string.error_wrong_password) String errorWrongPassword;
  @BindString(R.string.msg_you_are_offline) String msgYouAreOffline;
  @BindString(R.string.msg_successful) String msgSuccessful;
  @BindColor(R.color.colorAccent) int colorAccent;

  @Inject LoginPresenter presenter;
  @NonNull private final BroadcastReceiver networkStatusReceiver = new BroadcastReceiver() {
    @Override public void onReceive(Context context, Intent intent) {
      presenter.onNetworkStatusChanged(NetworkUtil.isConnected(context));
    }
  };
  @Nullable private Snackbar sbNetworkError;

  public static Intent intent(Context context) {
    return new Intent(context, LoginActivity.class);
  }

  @Override public Object activityModule() {
    return new LoginModule();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);
    Daggers.inject(this, this);

    connect(presenter, this);
  }

  @Override protected void onResume() {
    super.onResume();
    registerReceiver(
        networkStatusReceiver,
        new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
  }

  @Override protected void onPause() {
    super.onPause();
    unregisterReceiver(networkStatusReceiver);
  }

  @OnTextChanged(value = R.id.etEmail, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
  public void afterEmailInput(Editable editable) {
    presenter.onInputEmail(editable.toString());
  }

  @OnTextChanged(value = R.id.etPassword, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
  public void afterPasswordInput(Editable editable) {
    presenter.onInputPassword(editable.toString());
  }

  @OnClick(R.id.btLogin) public void onViewClicked() {
    presenter.onClickLogin();
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

  @Override public void showAuthenticationError() {
    tilPassword.setError(errorWrongPassword);
  }

  @Override public void hideAuthenticationError() {
    tilPassword.setError(null);
  }

  @Override public void showNetworkError() {
    if (sbNetworkError != null) {
      return;
    }
    sbNetworkError = Snackbar.make(vRoot, msgYouAreOffline, Snackbar.LENGTH_INDEFINITE);
    sbNetworkError.show();
  }

  @Override public void hideNetworkError() {
    if (sbNetworkError == null) {
      return;
    }
    sbNetworkError.dismiss();
    sbNetworkError = null;
  }

  @Override public void enableSubmit() {
    btLogin.setEnabled(true);
  }

  @Override public void disableSubmit() {
    btLogin.setEnabled(false);
  }

  @Override public void showLoginSuccess() {
    Toast.makeText(this, msgSuccessful, Toast.LENGTH_SHORT).show();
    finish();
  }
}
