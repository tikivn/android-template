package vn.tiki.sample.redux

import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_redux_demo.tvValue
import vn.tiki.architecture.redux.Epic
import vn.tiki.architecture.redux.Store
import vn.tiki.architecture.redux.createStore
import vn.tiki.sample.R
import vn.tiki.sample.redux.ReduxDemoActivity.Action.AddAction
import vn.tiki.sample.redux.ReduxDemoActivity.Action.MultiplyAction

class ReduxDemoActivity : AppCompatActivity() {

  sealed class Action {
    data class AddAction(val value: Int) : Action()
    data class MultiplyAction(val value: Int) : Action()
  }

  private lateinit var store: Store<Int>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_redux_demo)

    val addEpic = Epic<Int> { actions, getState ->
      actions.ofType(AddAction::class.java)
          .switchMap { action ->
            Observable.fromCallable {
              SystemClock.sleep(1000)
              getState() + action.value
            }.subscribeOn(Schedulers.io())
          }
    }

    val multiplyEpic = Epic<Int> { actions, getState ->
      actions.ofType(MultiplyAction::class.java)
          .switchMap { action ->
            Observable.fromCallable {
              SystemClock.sleep(2000)
              getState() * action.value
            }.subscribeOn(Schedulers.io())
          }
    }

    store = createStore(5, listOf(addEpic, multiplyEpic), true)

    store.getState()
        .observeOn(AndroidSchedulers.mainThread())
        .map { "$it" }
        .subscribe { tvValue.text = it }

    store.dispatch(MultiplyAction(2))
    store.dispatch(AddAction(3))
  }
}
