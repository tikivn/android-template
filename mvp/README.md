MVP
===

![Logo](../logo.png)


```java

interface ExampleView extends Mvp.View {
  void showLoading();

  void showUser(User user);

  void showError();
}

class ExamplePresenter extends BasePresenter<ExampleView> {

  private UserModel userModel;

  @Override public void attach(ExampleView view) {
    super.attach(view);
  }

  void onLoad() {
    getViewOrThrow().showLoading();
    userModel.loadUser()
        .subscribe(
            user -> sendToView(view -> view.showUser(user)),
            throwable -> sendToView(view -> view.showError()))
  }
}

public class ExampleActivity extends MvpActivity<ExampleView, ExamplePresenter> implements ActivityInjector{
  @Inject ExamplePresenter presenter;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Daggers.inject(this, this);

    connect(presenter, new ExampleView() {
      @Override public void showLoading() {

      }

      @Override public void showUser(User user) {

      }

      @Override public void showError() {

      }
    });
    presenter.onLoad();
  }
}

```

Download
--------

```groovy
allprojects {
  repositories {
	 ...
    maven { url 'https://jitpack.io' }
  }
}

dependencies {
  compile 'com.github.tikivn.android-template:mvp:-SNAPSHOT'
}
```

License
-------

    Copyright 2017 Tiki Corp

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
