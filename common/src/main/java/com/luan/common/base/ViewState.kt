package com.luan.common.base

class ViewState<T> private constructor(val status: Status, val data: T? = null, val exception: Throwable? = null) {

    enum class Status {
        LOADED, ERROR, LOADING
    }
    companion object {
        fun <T> loaded(data: T?): ViewState<T> {
            return ViewState(Status.LOADED, data)
        }

        fun <T> error(exception: Throwable?, data: T? = null): ViewState<T> {
            return ViewState(
                Status.ERROR,
                data,
                exception
            )
        }
        fun <T> loading(data: T? = null): ViewState<T> {
            return ViewState(
                Status.LOADING,
                data
            )
        }
    }

    fun <T> transform(data: T? = null) : ViewState<T> {
        return ViewState(status, data, exception)
    }

}
