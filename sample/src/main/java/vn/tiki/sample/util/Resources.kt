package vn.tiki.sample.util

import android.content.Context
import android.support.annotation.StringRes

fun Context.bindString(@StringRes id: Int) = lazy { getString(id) }