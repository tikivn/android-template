package vn.tiki.architecture.redux

import io.reactivex.Observable
import org.junit.*
import vn.tiki.architecture.redux.ReduxTodoTest.Action.AddTodo
import vn.tiki.architecture.redux.ReduxTodoTest.Action.ToggleTodo

@Suppress("IllegalIdentifier")
class ReduxTodoTest {

  data class Todo(val id: Int, val task: String, val completed: Boolean)
  data class TodoState(val todoList: List<Todo> = emptyList())
  internal sealed class Action {
    data class AddTodo(val task: String) : Action()
    data class ToggleTodo(val id: Int) : Action()
  }

  lateinit var store: Store<TodoState>

  @Before
  fun setUp() {
    val addEpic: (Observable<Any>, () -> TodoState) -> Observable<TodoState> = { actions, getState ->
      actions.ofType(AddTodo::class.java)
          .map { action ->
            val state = getState()
            val todoList = state.todoList
            val todo = Todo(todoList.size + 1, action.task, false)
            TodoState(todoList + todo)
          }
    }
    val toggleEpic: (Observable<Any>, () -> TodoState) -> Observable<TodoState> = { actions, getState ->
      actions.ofType(ToggleTodo::class.java)
          .map { action ->
            val state = getState()
            val todoList = state.todoList
            val newTodoList = todoList.map { todo ->
              if (todo.id == action.id) {
                todo.copy(completed = !todo.completed)
              } else {
                todo
              }
            }
            TodoState(newTodoList)
          }
    }
    store = createStore(TodoState(listOf(Todo(1, "init", false))), listOf(addEpic, toggleEpic))
  }

  @Test
  fun `should add Todo to list`() {
    store.dispatch(AddTodo("test add"))
    store.getState()
        .test()
        .assertValue(TodoState(listOf(
            Todo(1, "init", false),
            Todo(2, "test add", false))))
  }

  @Test
  fun `should toggle selected`() {
    store.dispatch(ToggleTodo(1))
    store.getState()
        .test()
        .assertValue(TodoState(listOf(Todo(1, "init", true))))

    store.dispatch(ToggleTodo(1))
    store.getState()
        .test()
        .assertValue(TodoState(listOf(Todo(1, "init", false))))
  }
}