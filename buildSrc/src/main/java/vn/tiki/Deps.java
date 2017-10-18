package vn.tiki;

public interface Deps {

  interface Version {

    int minSdk = 16;
    int compileSdk = 26;
    String kotlin = "1.1.51";
    String support = "26.1.0";
    String dagger = "2.11";
    String butterKnife = "8.8.1";
    String room = "1.0.0-beta1";
    String store = "3.0.0-beta";

  }

  interface Dagger {

    String runtime = "com.google.dagger:dagger:" + Version.dagger;
    String compiler = "com.google.dagger:dagger-compiler:" + Version.dagger;
  }

  interface ButterKnife {

    String runtime = "com.jakewharton:butterknife:" + Version.butterKnife;
    String compiler = "com.jakewharton:butterknife-compiler:" + Version.butterKnife;
  }

  interface AndroidSupport {

    String annotations = "com.android.support:support-annotations:" + Version.support;
    String compat = "com.android.support:support-compat:" + Version.support;
    String appCompat = "com.android.support:appcompat-v7:" + Version.support;
    String design = "com.android.support:design:" + Version.support;
    String recyclerview = "com.android.support:recyclerview-v7:" + Version.support;
    String constraint = "com.android.support.constraint:constraint-layout:1.0.2";
    String testRunner = "com.android.support.test:runner:1.0.0";
  }

  interface Android {

    String runtime = "com.google.android:android:4.1.1.4";
    String gradlePlugin = "com.android.tools.build:gradle:3.0.0-rc1";
  }

  interface Auto {

    String service = "com.google.auto.service:auto-service:1.0-rc3";
    String common = "com.google.auto:auto-common:0.8";
  }

  interface Store {

    String store = "com.nytimes.android:store3:" + Version.store;
    String fileSystem = "com.nytimes.android:filesystem3:" + Version.store;
    String middleware = "com.nytimes.android:middleware3:" + Version.store;
  }

  interface Http {

    String retrofit = "com.squareup.retrofit2:retrofit:2.3.0";
    String gsonConverter = "com.squareup.retrofit2:converter-gson:2.3.0";
    String rxJavaAdapter = "com.squareup.retrofit2:adapter-rxjava2:2.3.0";
    String logging = "com.squareup.okhttp3:logging-interceptor:3.9.0";
    String okhttp = "com.squareup.okhttp3:okhttp:3.9.0";
  }

  interface AutoValue {

    String autoValue = "com.google.auto.value:auto-value:1.5";
    String annotations = "com.jakewharton.auto.value:auto-value-annotations:1.5";
    String parcel = "com.ryanharter.auto.value:auto-value-parcel:0.2.5";
    String gson = "com.ryanharter.auto.value:auto-value-gson:0.6.0";
    String gsonAnnotations = "com.ryanharter.auto.value:auto-value-gson-annotations:0.6.0";
  }

  interface Room {

    String runtime = "android.arch.persistence.room:runtime:" + Version.room;
    String compiler = "android.arch.persistence.room:compiler:" + Version.room;
  }

  interface Glide {

    String version = "4.1.1";
    String runtime = "com.github.bumptech.glide:glide:" + version;
    String compiler = "com.github.bumptech.glide:compiler:" + version;
  }

  interface Test {

    String junit = "junit:junit:4.12 ";
    String truth = "com.google.truth:truth:0.34";
    String mockito = "org.mockito:mockito-core:2.9.0";
    String robolectric = "org.robolectric:robolectric:3.4.2";
    String compileTesting = "com.google.testing.compile:compile-testing:0.11";
  }

  interface ParcelParcel {

    String runtime = "nz.bradcampbell:paperparcel:2.0.4";
    String kotlin = "nz.bradcampbell:paperparcel-kotlin:2.0.4";
    String compiler = "nz.bradcampbell:paperparcel-compiler:2.0.4";
  }

  String kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jre7:" + Version.kotlin;
  String rxJava = "io.reactivex.rxjava2:rxjava:2.1.2";
  String rxAndroid = "io.reactivex.rxjava2:rxandroid:2.0.1";
  String ixJava = "com.github.akarnokd:ixjava:1.0.0-RC5";
  String crashlytics = "com.crashlytics.sdk.android:crashlytics:2.6.8@aar";
  String timber = "com.jakewharton.timber:timber:4.5.1";
  String noadapter = "vn.tiki.noadapter2:noadapter:2.0.2";
  String javapoet = "com.squareup:javapoet:1.9.0";
  String gson = "com.google.code.gson:gson:2.8.0";
}
