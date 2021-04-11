package com.luan.common.extension

import android.view.View
import androidx.fragment.app.Fragment
import com.luan.common.base.Resource
import retrofit2.Response

fun Fragment.handleLoading(status:Resource.Status, view:View){
    if (status == Resource.Status.LOADING) view.show()
    else view.hide()
}

fun <T> Response<T>.isNotFound():Boolean = this.code() == 404