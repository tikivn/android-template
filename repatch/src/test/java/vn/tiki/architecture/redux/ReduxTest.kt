package vn.tiki.architecture.redux

import io.reactivex.Observable
import org.junit.*

sealed class State
object Idle : State()
object Ping : State()
object Pong : State()

@Suppress("IllegalIdentifier")
class ReduxTest {

  lateinit var store: Store<State>

  @Before
  fun setUp() {
    val pingEpic: (Observable<Any>, () -> State) -> Observable<State> = { actions, _ ->
      actions.filter { "PING" == it }
          .map { Ping }
    }
    val pongEpic: (Observable<Any>, () -> State) -> Observable<State> = { actions, _ ->
      actions.filter { "PONG" == it }
          .map { Pong }
    }

    store = createStore(Idle, listOf(pingEpic, pongEpic))
  }

  @After
  fun tearDown() {
    store.destroy()
  }

  @Test
  fun `should be initial state after created`() {
    store.getState()
        .test()
        .assertValue(Idle)
  }

  @Test
  fun `state should be Ping when dispatch PING`() {
    store.dispatch("PING")
    store.getState()
        .test()
        .assertValue(Ping)
  }

  @Test
  fun `state should be Pong when dispatch PONG`() {
    store.dispatch("PONG")
    store.getState()
        .test()
        .assertValue(Pong)
  }

  @Test
  fun `state should be unchanged when dispatch unknown action`() {
    store.dispatch("unknown")
    store.getState()
        .test()
        .assertValue(Idle)
  }
}