package vn.tiki.sample.mvp;

/**
 * Created by Giang Nguyen on 9/4/17.
 */

public interface Mvp {

  interface View {
  }

  interface Presenter<V extends View> {

    void attach(V view);

    void detach();

    void destroy();
  }
}
