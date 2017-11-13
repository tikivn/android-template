package vn.tiki.architecture.redux;

import io.reactivex.Observable;
import kotlin.Function;
import kotlin.jvm.functions.Function0;

public interface Epic<State> extends Function<Observable<State>> {

  Observable<State> involve(Observable<Object> actions, Function0<State> getState);
}
