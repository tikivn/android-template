package vn.tiki.architecture.redux

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.subjects.PublishSubject

interface Store<State> {
  fun getState(): Flowable<State>
  fun dispatch(action: Any)
  fun destroy()
}

internal class StoreImpl<State>(initialState: State, epics: List<Epic<State>>, debug: Boolean) : Store<State> {

  private val actions: PublishSubject<Any> = PublishSubject.create()
  private val states: BehaviorProcessor<State> = BehaviorProcessor.createDefault(initialState)
  private var disposables: CompositeDisposable = CompositeDisposable()

  init {
    val getState = { states.value }

    val stateTransformer: ObservableTransformer<Any, State> = ObservableTransformer {
      it.concatMap { action ->
        Observable.concat(epics.map { epic -> epic.involve(Observable.just(action), getState) })
      }
    }

    disposables.add(actions.compose(stateTransformer)
        .subscribe(
            states::onNext,
            Throwable::printStackTrace))

    if (debug) {
      disposables.add(actions.map { "action = [$it]" }
          .subscribe(::println))
      disposables.add(states.map { "state = [$it]" }
          .subscribe(::println))

    }
  }

  override fun getState(): Flowable<State> {
    return states
  }

  override fun dispatch(action: Any) {
    actions.onNext(action)
  }

  override fun destroy() {
    disposables.clear()
  }
}

fun <State> createStore(initialState: State,
                        epics: List<Epic<State>>,
                        debug: Boolean = false): Store<State> {
  return StoreImpl(initialState, epics, debug)
}
