package vn.tiki.sample.model

import io.reactivex.Observable
import vn.tiki.sample.entity.AuthenticationException

class UserModel {

  fun login(email: String, password: String): Observable<Boolean> {
    return Observable.fromCallable {
      Thread.sleep(1000)
      if (System.currentTimeMillis() % 2 == 0L) {
        throw Exception("server error")
      } else if ("foo@gmail.com" != email || "123456" != password) {
        throw AuthenticationException()
      } else {
        true
      }
    }
  }
}
