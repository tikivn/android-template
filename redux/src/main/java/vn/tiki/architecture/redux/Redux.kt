package vn.tiki.architecture.redux

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.subjects.PublishSubject

interface Store<State> {
  fun getState(): Flowable<State>
  fun dispatch(action: Any)
  fun destroy()
}

internal class StoreImpl<State>(initialState: State, epics: List<(Observable<Any>, () -> State) -> Observable<State>>) : Store<State> {

  private val actions: PublishSubject<Any> = PublishSubject.create()
  private val states: BehaviorProcessor<State> = BehaviorProcessor.createDefault(initialState)
  private var disposable: Disposable? = null

  init {
    disposable = Observable.fromIterable(epics)
        .flatMap { epic -> epic(actions, { states.value }) }
        .subscribe({ nextState -> states.onNext(nextState) }, { t -> t.printStackTrace() })
  }

  override fun getState(): Flowable<State> {
    return states
  }

  override fun dispatch(action: Any) {
    actions.onNext(action)
  }

  override fun destroy() {
    disposable?.dispose()
  }
}

fun <State> createStore(initialState: State, epics: List<(Observable<Any>, () -> State) -> Observable<State>>): Store<State> {
  return StoreImpl(initialState, epics)
}
