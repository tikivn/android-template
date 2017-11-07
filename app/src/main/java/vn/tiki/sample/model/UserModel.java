package vn.tiki.sample.model;

import io.reactivex.Observable;

/**
 * Created by Giang Nguyen on 8/25/17.
 */
public class UserModel {

  public Observable<Boolean> login(final String email, final String password) {
    return Observable.fromCallable(() -> {
      Thread.sleep(1000);
      if (System.currentTimeMillis() % 2 == 0) {
        throw new Exception("server error");
      } else {
        return "foo@gmail.com".equals(email) && "123456".equals(password);
      }
    });
  }
}
