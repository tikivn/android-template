package vn.tiki.sample.login

sealed class LoginState

data class LoginError(val throwable: Throwable) : LoginState()
object LoginSuccess : LoginState()
object LoginInFlight : LoginState()
