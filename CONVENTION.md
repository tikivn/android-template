# Convention for contributing to Tiki's Android Template

Most importantly, be consistent with existing code.  Look around the codebase and match the style.

## Architecture
We use MVP for architecture. Checkout our simple [MVP](https://github.com/tikivn/android-template/tree/master/mvp) implement for usage and good practices

## Naming
1. id -> [prefix]Name

 * `tv` -> **TextView** (e.g. tvEmail)
 * `et` -> **EditText** (e.g. etPassword)
 * `bt` -> **Button** (e.g. btSubmit)
 * `iv` -> **ImageView** (e.g. ivThumb)
 * ...

2. strings.xml -> [Screen|Module]_name. e.g.

 * Uses prefix `login` for `Login` module (e.g. login_error_invalid_email, login_message_welcome, etc.)
 * Uses prefix `main` for `Main` module (e.g. main_message_welcome, etc.).

3. classes

 * `Activity`: [Name]Activity (e.g. LoginActivity, ProfileActivitym etc.)
 * `Fragment`: [Name]Fragment (e.g. LoginFragment, ProfileFragment, etc.)
 * `Utility`:
	 * Fore a specific type: we add suffix `s` to that type (e.g. [Context]s for Context, [TextView]s for TextView, etc.).
	 * Other: put everything else in the `Util` class.

## Dependencies Injection
1. [ButterKnife](http://jakewharton.github.io/butterknife/) for views, resources and events injection.

  Butter knife can resolve most of references from the *R* (e.g. id, color, string. etc)

  ~~~java
  // View
  @BindView(R.id.btLogin) Button btLogin;
  @BindViews({ R.id.etEmail, R.id.etPassword }) List<View> inputViews;
  ...

  // String
  @BindString(R.string.msg_successful) String msgSuccessful;
  ...

  // Color
  @BindColor(R.color.colorAccent) int colorAccent;
  ...

  // Events
  @OnTextChanged(value = R.id.etEmail, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
  public void afterEmailInput(Editable editable) { ... }

  @OnClick(R.id.btLogin) public void onLoginClicked() { ... }
  ~~~

2. [Intents](https://github.com/tikivn/android-template/tree/master/intents) for extra injection. Checkout [repository](https://github.com/tikivn/android-template/tree/master/intents) for convention

3. [Dagger2](https://github.com/google/dagger) and [Daggers](https://github.com/tikivn/android-template/tree/master/daggers) helper for dependencies injection. Checkout [repository](https://github.com/tikivn/android-template/tree/master/daggers) for convention


License
-------

Copyright (C) 2017 Tiki Corp

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
