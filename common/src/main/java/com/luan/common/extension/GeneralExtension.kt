package com.luan.common.extension

import android.view.View
import androidx.fragment.app.Fragment
import com.luan.common.base.ViewState
import retrofit2.Response

fun Fragment.handleLoading(status: ViewState.Status, view:View){
    if (status == ViewState.Status.LOADING) view.show()
    else view.hide()
}

fun <T> Response<T>.isNotFound():Boolean = this.code() == 404

infix fun <T> Boolean.then(param: T): T? = if (this) param else null
