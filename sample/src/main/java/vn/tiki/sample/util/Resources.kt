package vn.tiki.sample.util

import android.content.Context
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat

fun Context.bindString(@StringRes id: Int) = lazy { getString(id) }
fun Context.bindColor(@ColorRes id: Int) = lazy { ContextCompat.getColor(this, id) }