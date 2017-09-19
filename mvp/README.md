MVP
===

![Logo](../logo.png)

Simple implementation of MVP architecture.

 * Implement Presenter, provides some elegant methods to interact with view.
 * Provides base classes: `MvpActivity`, `MvpFragment`, `MvpBinding` that will
 automatically attach/detach views base on appropriate lifecycle callback.

Usage
-----

for Activity

 * Extends from MvpActivity.
 * call connect(Presenter, View) on `onCreate()` callback.

~~~java
public class ExampleActivity extends MvpActivity<ExampleView, ExamplePresenter>
implements ExampleView {
  @Inject ExamplePresenter presenter;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Dependencies injection

    connect(presenter, this);
  }
}
~~~

for Fragment

 * Extends from MvpFragment.
 * call connect(Presenter, View) before `onViewCreated()` callback.

~~~java
public class ExampleFragment extends MvpFragment<ExampleView, ExamplePresenter>
implements ExampleView {
  @Inject ExamplePresenter presenter;

  @Nullable @Override
  public View onCreateView(
      LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    final View view = // view inflation
    // Dependencies injection
    connect(presenter, this);
    return view;
  }
}
~~~

for custom View

 * call addOnAttachStateChangeListener(MvpBinding).

~~~java
public class ExampleView extends FrameLayout implements ExampleView {
  @Inject ExamplePresenter presenter;

  public ExampleView(
      @NonNull Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
    // dependencies injection
    addOnAttachStateChangeListener(new MvpBinding<>(presenter, this));
  }
}
~~~

to handle `onActivityResult()` from your Presenter, just implement `ActivityResultDelegate`

```java
class ExamplePresenter extends BasePresenter<ExampleView> 
implement ActivityResultDelegate {

  void onActivityResult(@NonNull ActivityResult activityResult) {
    if (activityResult.isCancel()) {
      // TODO handle cancelled.
      return;
    } 
    if (activityResult.isRequestCode(RQ_LOGIN)) {
      // TODO handle login success.
    }
  }
}

```

Good practices
--------------

 * Presenter handles user input then name of its public methods (which will be called by View) should be event descriptive and started by prefix `on`. (e.g. onEmailInput, onUserSubmit, etc.).
 * View handle output then name of its public methods (which will be called by Presenter) should be display descriptive. (e.g. showError, showLogoutButton, etc.).

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
